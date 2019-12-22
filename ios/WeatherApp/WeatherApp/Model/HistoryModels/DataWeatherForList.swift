//
//  DataWeatherForList.swift
//  WeatherApp
//
//  Created by Михаил Дементьев on 21.12.2019.
//  Copyright © 2019 Михаил Дементьев. All rights reserved.
//

import Foundation

class DataWeatherForList: NSObject, NSCoding {
    
    let titleName: String
    let date: String?
    let temperature: String?
    let pressure: String?
    let overcast: String?
    let phenomenon: String?
    let wind: String?
    let directionWind: String?
    
    init(titleName: String, date: String?, temperature: String?, pressure: String?, overcast: String?, phenomenon: String?, wind: String?, directionWind: String?) {
        self.titleName = titleName
        self.date = date
        self.temperature = temperature
        self.pressure = pressure
        self.overcast = overcast
        self.phenomenon = phenomenon
        self.wind = wind
        self.directionWind = directionWind
    }
    
    required convenience init(coder aCoder: NSCoder) {
        let titleName = aCoder.decodeObject(forKey: "titleName") as! String
        let date = aCoder.decodeObject(forKey: "date") as! String
        let temperature = aCoder.decodeObject(forKey: "temperature") as! String
        let pressure = aCoder.decodeObject(forKey: "pressure") as! String
        let overcast = aCoder.decodeObject(forKey: "overcast") as! String
        let phenomenon = aCoder.decodeObject(forKey: "phenomenon") as! String
        let wind = aCoder.decodeObject(forKey: "wind") as! String
        let directionWind = aCoder.decodeObject(forKey: "directionWind") as! String
        self.init(titleName: titleName, date: date, temperature: temperature, pressure: pressure, overcast: overcast, phenomenon: phenomenon, wind: wind, directionWind: directionWind)
    }
    
    func encode(with acoder: NSCoder) {
        acoder.encode(titleName, forKey: "titleName")
        acoder.encode(date, forKey: "date")
        acoder.encode(temperature, forKey: "temperature")
        acoder.encode(pressure, forKey: "pressure")
        acoder.encode(overcast, forKey: "overcast")
        acoder.encode(phenomenon, forKey: "phenomenon")
        acoder.encode(wind, forKey: "wind")
        acoder.encode(directionWind, forKey: "directionWind")
    }
}
