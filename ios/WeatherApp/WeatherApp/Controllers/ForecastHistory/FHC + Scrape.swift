//
//  FHC + Scrap.swift
//  WeatherApp
//
//  Created by Михаил Дементьев on 30.11.2019.
//  Copyright © 2019 Михаил Дементьев. All rights reserved.
//

import UIKit
import SwiftSoup

extension ForecastHistoryController {
    
    func loadGismeteoWebsite(indexCity: String, year: String, month: String) -> String? {
        let urlString = "https://www.gismeteo.ru/diary/\(indexCity)/\(year)/\(month)"
        guard let url = URL(string: urlString) else { return nil }
        do {
            let HTMLString = try String(contentsOf: url, encoding: .utf8)
            return HTMLString
        } catch {
            print("Error: \(error)")
        }
        return ""
    }
    
    func fetchWeatherForMonth(cityCode: String, year: String, month: String) -> [(date: String, data: DataWeather)]? {
        
        let htmlContent = loadGismeteoWebsite(indexCity: cityCode, year: year, month: month)
        
        do {
            if let content = htmlContent {
                let doc = try SwiftSoup.parse(content)
                do {
                    let tbodies = try doc.select("table").select("tbody")
                    if !tbodies.isEmpty() {
                        var dataArray: [(date: String, data: DataWeather)] = []
                        try tbodies[1].select("tr").forEach {
                            let tds = try $0.select("td")
                            dataArray.append((date: year + "-" + month + "-" + (try! tds[0].text()), data: DataWeather(temperature: getTemperature(tds[1]), pressure: getPressure(tds[2]), overcast: getOvercast(tds[3]), phenomenon: getPhenomenon(tds[4]), wind: getWind(tds[5]), directionWind: getDirectionWind(tds[5]))))
                        }
                        return dataArray
                    } else {
                        return nil
                    }
                } catch { return nil }
            } else { return nil }
        } catch { return nil }
    }
    
    private func averageDataWeather(list: [DataWeather]?) -> DataWeather? {
        
        let directionWind = list?.filter { $0.directionWind != nil }.groupBy { $0.directionWind }.max { a, b in a.value.count < b.value.count }?.key
        
        var wind = ""
        
        if directionWind == "Ш" {
            wind = "0"
        } else {
            wind = String(Int((list?.filter { $0.wind != nil }.map { Int($0.wind!)! }.average)!))
        }
        
        let temperature = String(Int((list?.filter { $0.temperature != nil }.map { Int($0.temperature!)! }.average)!))
        
        let pressure = String(Int((list?.filter { $0.pressure != nil }.map { Int($0.pressure!)! }.average)!))
        
        let overcast = list?.filter { $0.overcast != nil }.groupBy { $0.overcast }.max { a, b in a.value.count < b.value.count }?.key
        
        let phenomenon = list?.filter { $0.phenomenon != nil }.groupBy { $0.phenomenon }.max { a, b in a.value.count < b.value.count }?.key
        
        let dataWeather = DataWeather(temperature: temperature , pressure: pressure, overcast: overcast, phenomenon: phenomenon, wind: wind, directionWind: directionWind)
        
        return dataWeather
    }
    
    private func getTemperature(_ element: Element) -> String? {
        if try! !element.text().isEmpty {
            return try! element.text()
        } else {
            return nil
        }
    }
    
    private func getPressure(_ element: Element) ->  String? {
        if try! !element.text().isEmpty {
            return try! element.text()
        } else {
            return nil
        }
    }
    
    private func getOvercast(_ element: Element) -> String? {
        let imgs = try! element.select("img")
        
        if !imgs.isEmpty {
            switch try! imgs[0].attr("src").split(separator: "/").last {
            case "sun.png":
                return "Ясно"
            case "sunc.png":
                return "Малооблачно"
            case "suncl.png":
                return "Облачно"
            case "dull.png":
                return "Пасмурно"
            default:
                return nil
            }
        } else {
            return nil
        }
    }
    
    private func getPhenomenon(_ element: Element) -> String? {
        let imgs = try! element.select("img")
        
        if !imgs.isEmpty() {
            if let phenomenomName = try! imgs[0].attr("src").split(separator: "/").last {
                switch phenomenomName {
                case "rain.png":
                    return "Дождь"
                case "snow.png":
                    return "Снег"
                case "storm.png":
                    return "Гроза"
                default:
                    return nil
                }
            } else { return nil }
        } else { return nil }
    }
    
    private func getWind(_ element: Element) -> String? {
        let spans = try! element.select("span")
        
        if !spans.isEmpty() {
            do {
                let sbSpeed = StringBuilder()
                let speedArray = try! spans[0].text().split(separator: " ")
                if speedArray.count > 1 {
                    let speed = try! spans[0].text().split(separator: " ")[1]
                    for ch in speed {
                        if (ch.isNumber) {
                            sbSpeed.append(ch)
                        }
                    }
                    return sbSpeed.toString()
                } else {
                    return "0"
                }
            } catch { return nil }
        } else {
            return nil
        }
    }
    
    
    private func getDirectionWind(_ element: Element) -> String? {
        let spans = try! element.select("span")
        
        if !spans.isEmpty() {
            return try! String(spans[0].text().split(separator: " ")[0])
        } else {
            return nil
        }
    }
    
    func fetchDailyHistoryWeather(cityCode: String, startYear: String, endYear: String) -> HistoryWeather {
        
        var dataArray: [(date: String, data: DataWeather)] = []
        
        for i in 0...(Int(endYear)! - Int(startYear)!) {
            for month in 1...12 {
                if let weatherData = fetchWeatherForMonth(cityCode: cityCode, year: String(Int(startYear)! + i), month: String(month)) {
                    dataArray.append(contentsOf: (weatherData))
                } else {
                    DispatchQueue.main.async {
                        self.lostConnectionAller()
                    }
                    break
                }
            }
        }
        
        let historyWeather = HistoryWeather(historyWeather: dataArray)
        print(historyWeather)
        return historyWeather
    }
    
    
    func fetchMonthlyHistoryWeather(cityCode: String, startYear: String, endYear: String) -> HistoryWeather {
        
        var dataArray: [(date: String, data: DataWeather)] = []
        
        for i in 0...(Int(endYear)! - Int(startYear)!) {
            for month in 1...12 {
                let year = String(Int(startYear)! + i)
                if let weatherForMonth =
                    fetchWeatherForMonth(cityCode: cityCode, year: year, month: String(month)) {
                    let weatherData = weatherForMonth.map { $0.data }
                    for data in weatherData {
                        dataArray.append(("\(year)-\(month)", data))
                    }
                } else {
                    DispatchQueue.main.async {
                        self.lostConnectionAller()
                    }
                    break
                }
            }
        }
        
        let historyWeather = HistoryWeather(historyWeather: dataArray)
        print(historyWeather)
        return historyWeather
    }
    
    func fetchQuarterHistoryWeather(cityCode: String, startYear: String, endYear: String) -> HistoryWeather {
        func averageForQuarter(year: String, range: ClosedRange<Int>) -> DataWeather? {
            var quarter: [DataWeather] = []
            for month in range {
                if let weatherForMonth = fetchWeatherForMonth(cityCode: cityCode, year: year, month: String(month)) {
                    let average = averageDataWeather(list: weatherForMonth.map { $0.data })!
                    quarter.append(average)
                } else {
                    DispatchQueue.main.async {
                        self.lostConnectionAller()
                    }
                    break
                }
            }
            if !quarter.isEmpty {
                return averageDataWeather(list: quarter)
            } else {
                return nil
            }
        }
        var dataArray: [(date: String, data: DataWeather)] = []
        
        for i in 0...(Int(endYear)! - Int(startYear)!) {
            let year = String((Int(startYear)! + i))
            if let firstQuarter = averageForQuarter(year: year, range: 1...3), let secondQuarter = averageForQuarter(year: year, range: 4...6), let thirdQuarter = averageForQuarter(year: year, range: 7...9), let fourthQuarter = averageForQuarter(year: year, range: 10...12) {
                
                dataArray.append((date: "\(year)-1 квартал", data: firstQuarter))
                dataArray.append((date: "\(year)-2 квартал", data: secondQuarter))
                dataArray.append((date: "\(year)-3 квартал", data: thirdQuarter))
                dataArray.append((date: "\(year)-4 квартал", data: fourthQuarter))
            }
        }
        
        let historyWeather = HistoryWeather(historyWeather: dataArray)
        print(historyWeather)
        return historyWeather
    }
    
    func lostConnectionAller() {
        let alert = UIAlertController(title: "Ошибка", message: "Отсутствует интернет-соединение", preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Ок", style: .default, handler: nil))
        self.present(alert, animated: true)
        searchButton.isEnabled = true
        searchLoader.stopAnimating()
    }
}
