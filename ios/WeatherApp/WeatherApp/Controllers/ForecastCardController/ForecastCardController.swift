//
//  ForecastCardView.swift
//  WeatherApp
//
//  Created by Михаил Дементьев on 24/09/2019.
//  Copyright © 2019 Михаил Дементьев. All rights reserved.
//

import UIKit
import Alamofire

class ForecastCardController: UIViewController {
    
    
    @IBOutlet weak var tableViewHeight: NSLayoutConstraint!
    @IBOutlet weak var handleArea: UIView!
    @IBOutlet weak var tableView: UITableView!
    
    var forecastArray = [ForecastWeather]()
    
    override func viewDidAppear(_ animated: Bool) {
        tableView.delegate = self
        tableView.dataSource = self
        downloadForecastWeather(upiUrl: FORECAST_API_URL_COORDINATES) {
            print("Forecast data downloaded")
        }
        tableView.backgroundColor = #colorLiteral(red: 0.9372549057, green: 0.3490196168, blue: 0.1921568662, alpha: 1)
    }
    
    func downloadForecastWeather(upiUrl: String, completed: @escaping DownloadComplete) {
        print(upiUrl)
        Alamofire.request(upiUrl).responseJSON { (response) in
            let result = response.result
            if let dictionary = result.value as? Dictionary<String, AnyObject>{
                self.forecastArray.removeAll()
                if let list = dictionary["list"] as? [Dictionary<String, AnyObject>] {
                    for item in 0...39 {
                        let forecast = ForecastWeather(weatherDict: list[item])
                        self.forecastArray.append(forecast)
                    }
                    self.tableView.reloadData()
                }
            }
            completed()
        }
    }
    
    
}

extension ForecastCardController: UITableViewDelegate, UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let identifier = "ForecastCell"
        tableView.register(UINib(nibName: "ForecastCell", bundle: nil), forCellReuseIdentifier: identifier)
        let cell = tableView.dequeueReusableCell(withIdentifier: "ForecastCell") as! ForecastCell
        cell.configureCell(forecastData: forecastArray[indexPath.row])
        return cell
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return forecastArray.count
    }
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        cell.backgroundColor = .clear
        cell.selectionStyle = .none
    }
}
