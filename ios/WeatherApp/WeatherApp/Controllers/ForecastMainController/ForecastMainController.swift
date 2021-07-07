//
//  ViewController.swift
//  WeatherApp
//
//  Created by Михаил Дементьев on 11/09/2019.
//  Copyright © 2019 Михаил Дементьев. All rights reserved.
//

import UIKit
import CoreLocation
import Alamofire

class ForecastMainController: UIViewController{
    
    //Outlets
    @IBOutlet weak var weatherType: UILabel!
    @IBOutlet weak var weatherImage: UIImageView!
    @IBOutlet weak var currentCityTemp: UILabel!
    @IBOutlet weak var currentDate: UILabel!
    @IBOutlet weak var specialBG: UIImageView!
    @IBOutlet weak var windDeg: UILabel!
    @IBOutlet weak var windSpeed: UILabel!
    @IBOutlet weak var humidity: UILabel!
    @IBOutlet weak var mNavigationBar: UINavigationBar!
    @IBOutlet weak var mNavigationItem: UINavigationItem!
    
    
    //Constants
    let locationManager = CLLocationManager()
    
    //Variables
    var currentWeather: CurrentWeather!
    var currentlocation: CLLocation!
    var forecastWeather: ForecastWeather!
    var forecastArray = [ForecastWeather]()
    
    //ForecastCard
    //------->
    var forecastCardController: ForecastCardController!
    var visualEffectView: UIVisualEffectView!
    
    let cardHeight: CGFloat = 600
    let cardHandleAreaHeight: CGFloat = 50
    var tabbarHeight: CGFloat = 49
    var navBarHeight: CGFloat = 0
    
    var cardVisible = false
    var nextState: CardState {
        return cardVisible ? .collapsed : .expanded
    }
    
    var runningAnimations = [UIViewPropertyAnimator]()
    var animationProgressWhenInterrupted: CGFloat = 0
    //--------<
    
    //SearchBar
    let searchBar = UISearchBar()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        buildNavigationBar()
        applyEffect()
        currentWeather = CurrentWeather()
        callDelegates()
        setupLocation()
        setupCard()
    }
    
    func callDelegates() {
        locationManager.delegate = self
    }
}
