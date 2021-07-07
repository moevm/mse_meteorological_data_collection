//
//  Location.swift
//  WeatherApp
//
//  Created by Михаил Дементьев on 14/09/2019.
//  Copyright © 2019 Михаил Дементьев. All rights reserved.
//

import Foundation

class Location {
    static var sharedInstance = Location()
    
    var longitude: Double!
    var latitude: Double!
}
