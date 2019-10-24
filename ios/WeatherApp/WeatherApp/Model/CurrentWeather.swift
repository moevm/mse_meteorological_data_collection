//
//  CurrentWeather.swift
//  WeatherApp
//
//  Created by Михаил Дементьев on 14/09/2019.
//  Copyright © 2019 Михаил Дементьев. All rights reserved.
//

import Foundation
import Alamofire
import SwiftyJSON

class CurrentWeather {
    
    private var _cityName: String!
    private var _date: String!
    private var _weatherType: String!
    private var _currentTemp: Double!
    private var _weatherDescription: String!
    private var _dayOrNight: String!
    private var _windSpeed: Double!
    private var _windDeg: Double!
    private var _humidity: Int!
    
    var cityName: String {
        if _cityName == nil {
            _cityName = ""
        }
        return _cityName
    }
    
    var date: String {
        if _date == nil {
            _date = ""
        }
        return _date
    }
    
    var weatherType: String {
        if _weatherType == nil {
            _weatherType = ""
        }
        return _weatherType
    }
    
    var currentTemp: Double {
        if _currentTemp == nil {
            _currentTemp = 0.0
        }
        return _currentTemp
    }
    
    var weatherDescription: String {
        if _weatherDescription == nil {
            _weatherDescription = ""
        }
        return _weatherDescription
    }
    
    var dayOrNight: String {
        if _dayOrNight == nil {
            _dayOrNight = ""
        }
        return _dayOrNight
    }
    
    var windSpeed: Double {
        if _windSpeed == nil {
            _windSpeed = 0.0
        }
        return _windSpeed
    }
    
    var windDeg: Double {
        if _windDeg == nil {
            _windDeg = -1.0
        }
        return _windDeg
    }
    
    var humidity: Int {
        if _humidity == nil {
            _humidity = -1
        }
        return _humidity
    }
    
    func downloadCurrentWeather(apiUrl: String, completed: @escaping DownloadComplete) {
        Alamofire.request(apiUrl).responseJSON { (response) in
            let result = response.result
            if let resultValue = result.value {
                let json = JSON(resultValue)
                self._cityName = json["name"].stringValue
                if let tempDate = json["dt"].double {
                    let convertedDate = Date(timeIntervalSince1970: tempDate)
                    let dateFormatter = DateFormatter()
                    dateFormatter.dateStyle = .medium
                    dateFormatter.timeStyle = .none
                    let currentDate = dateFormatter.string(from: convertedDate)
                    self._date = "\(currentDate)"
                }
                self._weatherType = json["weather"][0]["main"].stringValue
                if let downloadedTemp = json["main"]["temp"].double {
                    self._currentTemp = (downloadedTemp - 273.15).rounded(toPlaces: 0)
                }
                self._weatherDescription = json["weather"][0]["description"].stringValue
                if let sunset = json["sys"]["sunset"].double, let sunrise = json["sys"]["sunrise"].double, let curDate = json["dt"].double {
                    if(curDate > sunset || curDate < sunrise) {
                        self._dayOrNight = "night"
                    } else {
                        self._dayOrNight = "day"
                    }
                }
                self._windSpeed = json["wind"]["speed"].double
                self._windDeg = json["wind"]["deg"].double
                self._humidity = json["main"]["humidity"].int
            }
            
            completed()
        }
    }
    
    
}
