//
//  ForecastMainController + Interface.swift
//  WeatherApp
//
//  Created by Михаил Дементьев on 24/09/2019.
//  Copyright © 2019 Михаил Дементьев. All rights reserved.
//


import UIKit

extension ForecastMainController {
    
    func applyEffect(){
        specialEffect(view: specialBG, intencity: 25)
    }
    
    func specialEffect(view: UIView, intencity: Double){
        let horizontalMotion = UIInterpolatingMotionEffect(keyPath: "center.x", type: .tiltAlongHorizontalAxis)
        horizontalMotion.minimumRelativeValue = -intencity
        horizontalMotion.maximumRelativeValue = intencity
        
        let verticalMotion = UIInterpolatingMotionEffect(keyPath: "center.y", type: .tiltAlongVerticalAxis)
        verticalMotion.minimumRelativeValue = -intencity
        verticalMotion.maximumRelativeValue = intencity
        
        let movement = UIMotionEffectGroup()
        movement.motionEffects = [horizontalMotion, verticalMotion]
        view.addMotionEffect(movement)
    }
    
    func updateUI(){
        mNavigationItem.title = currentWeather.cityName
        currentCityTemp.text = "\(Int(currentWeather.currentTemp))"
        weatherType.text = currentWeather.weatherDescription
        currentDate.text = currentWeather.date
        weatherImage.image = UIImage(named: "\(currentWeather.weatherType) \(currentWeather.dayOrNight)")
        specialBG.image = UIImage(named: "\(currentWeather.dayOrNight)_bg")
        windSpeed.text = "Wind \(currentWeather.windSpeed) m/s"
        windDeg.text = getWindDirection(deg: currentWeather.windDeg)
        humidity.text = "Humidity \(currentWeather.humidity)%"
    }
    
    func loadUI(){
        weatherType.isHidden = false
        weatherImage.isHidden = false
        currentCityTemp.isHidden = false
        currentDate.isHidden = false
    }
    
    func hideUI(){
        weatherType.isHidden = true
        weatherImage.isHidden = true
        currentCityTemp.isHidden = true
        currentDate.isHidden = true
    }
    
    func getWindDirection(deg: Double) -> String{
        switch deg {
        case 0...15:
            return "Northern"
        case 16...74:
            return "Northeastern"
        case 75...105:
            return "Eastern"
        case 106...164:
            return "Southeastern"
        case 165...195:
            return "Southern"
        case 196...254:
            return "Southwestern"
        case 255...285:
            return "Western"
        case 286...344:
            return "Northwestern"
        case 345...360:
            return "Northern"
        default:
            return "ErrorData"
        }
    }
}
