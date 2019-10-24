//
//  ForecastMainController + Card.swift
//  WeatherApp
//
//  Created by Михаил Дементьев on 24/09/2019.
//  Copyright © 2019 Михаил Дементьев. All rights reserved.
//

import UIKit
import CoreLocation

extension ForecastMainController: CLLocationManagerDelegate {
    
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        if (status == CLAuthorizationStatus.denied) {
            hideUI()
            locationManager.requestWhenInUseAuthorization() // Take Permission from the user again
        } else if (status == CLAuthorizationStatus.authorizedWhenInUse) {
            loadUI()
            // Get the location from the device
            currentlocation = locationManager.location
            
            // Pass the location coordinates to our API
            Location.sharedInstance.latitude = currentlocation.coordinate.latitude
            Location.sharedInstance.longitude = currentlocation.coordinate.longitude
            
            // Download the API Data
            currentWeather.downloadCurrentWeather(apiUrl: API_URL_COORDINATES) {
                // Update the UI after dowmnload is completed
                self.updateUI()
            }
        }
    }
    func setupLocation() {
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.requestWhenInUseAuthorization() // Take Permission from the user
        locationManager.startMonitoringSignificantLocationChanges()
    }
}
