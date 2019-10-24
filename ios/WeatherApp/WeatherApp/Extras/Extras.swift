//
//  Extras.swift
//  WeatherApp
//
//  Created by Михаил Дементьев on 14/09/2019.
//  Copyright © 2019 Михаил Дементьев. All rights reserved.
//

import Foundation

let APPID = "&appid=5bd5c5654104ed292b0fd8983dc6e237"

let API_URL_COORDINATES = "http://api.openweathermap.org/data/2.5/weather?lat=\(Location.sharedInstance.latitude!)&lon=\(Location.sharedInstance.longitude!)&appid=5bd5c5654104ed292b0fd8983dc6e237"

let API_URL_LOCATION = "http://api.openweathermap.org/data/2.5/weather?q="

let FORECAST_API_URL_COORDINATES = "http://api.openweathermap.org/data/2.5/forecast?lat=\(Location.sharedInstance.latitude!)&lon=\(Location.sharedInstance.longitude!)&appid=5bd5c5654104ed292b0fd8983dc6e237"

let FORECAST_API_URL_LOCATION = "http://api.openweathermap.org/data/2.5/forecast?q="

typealias DownloadComplete = () -> ()
