//
//  ForecastWeather.swift
//  WeatherApp
//
//  Created by Михаил Дементьев on 14/09/2019.
//  Copyright © 2019 Михаил Дементьев. All rights reserved.
//

import Foundation

class ForecastWeather {
    private var _date: String!
    private var _temp: Double!
    private var _mainWeather: String!
    
    var date: String {
        if _date == nil {
            _date = ""
        }
        return _date
    }
    
    var temp: Double {
        if _temp == nil {
            _temp = 0.0
        }
        return _temp
    }
    
    var mainWeather: String {
        if _mainWeather == nil {
            _mainWeather = ""
        }
        return _mainWeather
    }
    
    init(weatherDict: Dictionary<String, AnyObject>) {
        if let temp = weatherDict["main"] as? Dictionary<String, AnyObject> {
            if let dayTemp = temp["temp"] as? Double {
                let rowValue = (dayTemp - 273.15).rounded(toPlaces: 0)
                self._temp = rowValue
            }
        }
        if let date = weatherDict["dt"] as? Double {
            
            let rowDate = Date(timeIntervalSince1970: date)
            
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "MM/dd"
            let currentDate = dateFormatter.string(from: rowDate)
            
            let timeFormattet = DateFormatter()
            timeFormattet.dateStyle = .none
            timeFormattet.timeStyle = .short
            let currentTime = timeFormattet.string(from: rowDate)
            
            self._date = "\(currentDate), \(currentTime)"
        }
        if let weather = weatherDict["weather"]?[0] as? Dictionary<String, AnyObject> {
            if let main = weather["main"] as? String {
                self._mainWeather = main
            }
        }
    }
}
