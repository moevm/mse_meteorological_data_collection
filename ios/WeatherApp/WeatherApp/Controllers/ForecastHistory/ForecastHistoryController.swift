//
//  ForecastHistoryController.swift
//  WeatherApp
//
//  Created by Михаил Дементьев on 28.11.2019.
//  Copyright © 2019 Михаил Дементьев. All rights reserved.
//

import FirebaseFirestore
import Foundation
import UIKit

class ForecastHistoryController: UIViewController {
    
    @IBOutlet weak var countryPicker: DropDownList!
    @IBOutlet weak var regionPicker: DropDownList!
    @IBOutlet weak var cityPicker: DropDownList!
    @IBOutlet weak var intervalPicker: DropDownList!
    @IBOutlet weak var dateStartPicker: DropDownList!
    @IBOutlet weak var dateEndPicker: DropDownList!
    @IBOutlet weak var countryLoader: UIActivityIndicatorView!
    @IBOutlet weak var regionLoader: UIActivityIndicatorView!
    @IBOutlet weak var cityLoader: UIActivityIndicatorView!
    @IBOutlet weak var searchLoader: UIActivityIndicatorView!
    @IBOutlet weak var searchButton: UIButton!
    
    var dropDownLists: [DropDownList] = []
    var dropDownElements: [[String: Any]] = []
    
    var countryArray: [String?] = []
    var regionArray: [String?] = []
    var cityArray: [String?] = []
    var intervalArray: [String?] = []
    
    var db: Firestore?
    var searchData: SearchData?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        initPickers()
        
        addHistoryInUserDefault()
        
        db = Firestore.firestore()
        searchData = SearchData()
        
        intervalArray = ["День", "Месяц", "Сезон"]
        
        db?.collection("countries").getDocuments(completion: { (querySnapshot, err) in
            if let docs = querySnapshot?.documents{
                self.countryLoader.stopAnimating()
                for docSnapshot in docs {
                    self.countryArray.append(docSnapshot.documentID)
                }
                self.initDropDownLists()
            }
        })
    }
    
    @IBAction func getWeatherStatistic(_ sender: Any) {
        searchData?.country = countryPicker.text
        searchData?.region = regionPicker.text
        searchData?.interval = intervalPicker.text
        searchData?.cityName = cityPicker.text
        
        if let cityCode = searchData?.cityIndex, let startYear = searchData?.startYear, let endYear = searchData?.endYear, let interval = searchData?.interval {
            searchLoader.startAnimating()
            searchButton.isEnabled = false
            switch interval {
            case "День":
                DispatchQueue.global().async {
                    self.saveInCSV(dataHistory: self.fetchDailyHistoryWeather(cityCode: cityCode, startYear: startYear, endYear: endYear))
                }
            case "Месяц":
                DispatchQueue.global().async {
                    self.saveInCSV(dataHistory: self.fetchMonthlyHistoryWeather(cityCode: cityCode, startYear: startYear, endYear: endYear))
                }
            case "Сезон":
                DispatchQueue.global().async {
                    self.saveInCSV(dataHistory: self.fetchQuarterHistoryWeather(cityCode: cityCode, startYear: startYear, endYear: endYear))
                }
            default:
                searchLoader.stopAnimating()
                searchButton.isEnabled = true
                print("Error")
            }
        }
    }
    
    func addHistoryInUserDefault(historyWeather: [DataWeatherForList]? = nil) {
        let userDefaults = UserDefaults.standard
        if let decodedData = userDefaults.data(forKey: "historyWeather") {
            if let weatherData = historyWeather {
                var decodedHistoryWeather = NSKeyedUnarchiver.unarchiveObject(with: decodedData) as! [DataWeatherForList]
                decodedHistoryWeather.append(contentsOf: weatherData)
                let encodedData: Data = NSKeyedArchiver.archivedData(withRootObject: decodedHistoryWeather)
                userDefaults.set(encodedData, forKey: "historyWeather")
                userDefaults.synchronize()
            }
        } else {
            let historyWeatherList: [DataWeatherForList] = []
            let encodedData: Data = NSKeyedArchiver.archivedData(withRootObject: historyWeatherList)
            userDefaults.set(encodedData, forKey: "historyWeather")
            userDefaults.synchronize()
        }
    }
    
    func saveInCSV(dataHistory: HistoryWeather) {
        let fileName = "\(searchData?.cityName ?? "-")-\(searchData?.startYear ?? "-")-\(searchData?.endYear ?? "-").csv"
        let path = NSURL(fileURLWithPath: NSTemporaryDirectory()).appendingPathComponent(fileName)
        
        var csvText = "Дата,Температура,Давление,Облачность,Явления,Ветер,Направление ветра\n"
        
        var dataWeatherForList: [DataWeatherForList] = []
        
        for item in dataHistory.historyWeather {
            let newLine = "\(item.date),\(item.data.temperature ?? "-"),\(item.data.pressure ?? "-"),\(item.data.overcast ?? "-"),\(item.data.phenomenon ?? "-"),\(item.data.wind ?? "-"),\(item.data.directionWind ?? "-")\n"
            csvText += newLine
            dataWeatherForList.append(DataWeatherForList(titleName: fileName, date: item.date, temperature: item.data.temperature ?? "-", pressure: item.data.pressure ?? "-", overcast: item.data.overcast ?? "-", phenomenon: item.data.phenomenon ?? "-", wind: item.data.wind ?? "-", directionWind: item.data.directionWind ?? "-"))
        }
        addHistoryInUserDefault(historyWeather: dataWeatherForList)
        
        //Получение данных
        let userDefaults = UserDefaults.standard
        let decodedData = userDefaults.data(forKey: "historyWeather")
        let decodedHistoryWeather = NSKeyedUnarchiver.unarchiveObject(with: decodedData!) as! [DataWeatherForList]

        do {
            try csvText.write(to: path!, atomically: true, encoding: String.Encoding.utf8)
                                
                let vc = UIActivityViewController(activityItems: [path], applicationActivities: [])
                vc.excludedActivityTypes = [
                    UIActivity.ActivityType.assignToContact,
                    UIActivity.ActivityType.saveToCameraRoll,
                    UIActivity.ActivityType.postToFlickr,
                    UIActivity.ActivityType.postToVimeo,
                    UIActivity.ActivityType.postToTencentWeibo,
                    UIActivity.ActivityType.postToTwitter,
                    UIActivity.ActivityType.postToFacebook,
                    UIActivity.ActivityType.openInIBooks
                ]
            DispatchQueue.main.async {
                self.searchLoader.stopAnimating()
                self.searchButton.isEnabled = true
                self.present(vc, animated: true, completion: nil)
            }

            } catch {
                print("Failed to create file")
                print("\(error)")
            }
    }
}
