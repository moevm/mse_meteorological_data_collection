//
//  ForecastListController.swift
//  WeatherApp
//
//  Created by Михаил Дементьев on 21.12.2019.
//  Copyright © 2019 Михаил Дементьев. All rights reserved.
//

import UIKit
import Charts

class ForecastListController: UIViewController {
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var lineChart: LineChartView!
    
    weak var axisFormatDelegate: IAxisValueFormatter?
    
    var tableData: [String: [DataWeather]] = [:]
    var titles: [String] = []
    var xData: [String] = []
    var currentChartIndex = 0

    override func viewDidLoad() {
        super.viewDidLoad()
        if #available(iOS 13.0, *) {
            let statusBar = UIView(frame: UIApplication.shared.keyWindow?.windowScene?.statusBarManager?.statusBarFrame ?? CGRect.zero)
            statusBar.backgroundColor = #colorLiteral(red: 0.6862745098, green: 0.3215686275, blue: 0.8705882353, alpha: 0.5)
            UIApplication.shared.keyWindow?.addSubview(statusBar)
        }
        axisFormatDelegate = self
        loadData()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        if !tableData.isEmpty {
            updateData()
            tableView.reloadData()
        }
    }
    
    func loadData() {
        updateData()
        if !tableData.isEmpty {
            setChartValues(title: titles[currentChartIndex])
        }
    }
    
    func updateData() {
        tableData.removeAll()
        titles.removeAll()
        xData.removeAll()
        let userDefaults = UserDefaults.standard
        let decodedData = userDefaults.data(forKey: "historyWeather")
        let decodedHistoryWeather = NSKeyedUnarchiver.unarchiveObject(with: decodedData!) as! [DataWeatherForList]
        if !decodedHistoryWeather.isEmpty {
            var currentTitle = decodedHistoryWeather[0].titleName
            var dataWeather: [DataWeather] = []
            
            for item in decodedHistoryWeather {
                if item.titleName == currentTitle {
                    dataWeather.append(DataWeather(date: item.date, temperature: item.temperature, pressure: item.pressure, overcast: item.overcast, phenomenon: item.phenomenon, wind: item.wind, directionWind: item.directionWind))
                } else {
                    tableData[currentTitle] = dataWeather
                    titles.append(currentTitle)
                    dataWeather.removeAll()
                    currentTitle = item.titleName
                    dataWeather.append(DataWeather(date: item.date, temperature: item.temperature, pressure: item.pressure, overcast: item.overcast, phenomenon: item.phenomenon, wind: item.wind, directionWind: item.directionWind))
                }
            }
            tableData[currentTitle] = dataWeather
            titles.append(currentTitle)
            for item in self.tableData[titles[currentChartIndex]]! {
                self.xData.append(item.date!)
            }
        }
    }
}

extension ForecastListController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return titles.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "DataCell", for: indexPath)
        cell.textLabel?.text = titles[indexPath.row]
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let actionSheet = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
        let cancel = UIAlertAction(title: "Отмена", style: .cancel, handler: nil)
        
        let showGraph = UIAlertAction(title: "Показать график", style: .default) { action in
            let titleText = tableView.cellForRow(at: indexPath)?.textLabel!.text!
            self.xData.removeAll()
            for item in self.tableData[titleText!]! {
                self.xData.append(item.date!)
            }
            self.currentChartIndex = indexPath.row
            self.setChartValues(title: (titleText!))
        }
        
        let showStat = UIAlertAction(title: "Показать статистику", style: .default) { action in
            let statView = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "statView") as! StatViewController
            
            var temperatures: [Double] = []
            var pressures: [Double] = []
            var winds: [Double] = []
            
            let titleText = tableView.cellForRow(at: indexPath)?.textLabel!.text!
            for item in self.tableData[titleText!]! {
                if let temperature = item.temperature, let pressure = item.pressure, let wind = item.wind, let temperatureDouble = Double(temperature), let pressureDouble = Double(pressure), let windDouble = Double(wind) {
                    temperatures.append(temperatureDouble)
                    pressures.append(pressureDouble)
                    winds.append(windDouble)
                }
            }
            
            statView.temperatures = temperatures
            statView.pressures = pressures
            statView.winds = winds
            
            self.addChild(statView)
            statView.view.frame = self.view.frame
            self.view.addSubview(statView.view)
            statView.didMove(toParent: self)
        }
        
        let delete = UIAlertAction(title: "Удалить", style: .default) { action in
            let titleText = tableView.cellForRow(at: indexPath)?.textLabel!.text!
            let userDefaults = UserDefaults.standard
            let decodedData = userDefaults.data(forKey: "historyWeather")
            var decodedHistoryWeather = NSKeyedUnarchiver.unarchiveObject(with: decodedData!) as! [DataWeatherForList]
            for item in decodedHistoryWeather {
                if titleText == item.titleName {
                    decodedHistoryWeather.remove(at: decodedHistoryWeather.firstIndex(of: item)!)
                }
            }
            let encodedData: Data = NSKeyedArchiver.archivedData(withRootObject: decodedHistoryWeather)
            userDefaults.set(encodedData, forKey: "historyWeather")
            userDefaults.synchronize()
            self.updateData()
            tableView.reloadData()
        }
        
        actionSheet.addAction(cancel)
        actionSheet.addAction(showGraph)
        actionSheet.addAction(showStat)
        actionSheet.addAction(delete)
        
        present(actionSheet, animated: true, completion: nil)
    }
    
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        let selectedView = UIView()
        selectedView.backgroundColor = #colorLiteral(red: 0.682451948, green: 0.3138622232, blue: 0.8718026135, alpha: 0.2340448944)
        cell.selectedBackgroundView = selectedView
    }
}

extension ForecastListController {
    func setChartValues(title: String) {
        let rangeCount = tableData[title]!.count
        var dataEntries: [ChartDataEntry] = []
        for i in 0..<rangeCount {
            let yVal = tableData[title]?[i].temperature
            if let temperature = Double(yVal!) {
                let dataEntry = ChartDataEntry(x: Double(i), y: temperature, data: xData as AnyObject?)
                dataEntries.append(dataEntry)
            }
        }
        let chartDataSet = LineChartDataSet(entries: dataEntries, label: "Температура")
        let chartData = LineChartData(dataSet: chartDataSet)
        print(xData.count)
        self.lineChart.data = chartData
        let xAxisValue = lineChart.xAxis
        xAxisValue.valueFormatter = axisFormatDelegate
    }
}

extension ForecastListController: IAxisValueFormatter {

    func stringForValue(_ value: Double, axis: AxisBase?) -> String {
        if xData.count == 8 {
            return String(xData[Int(value)].prefix(6))
        } else {
            return xData[Int(value)]
        }
    }
}
