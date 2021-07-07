//
//  FMC + NavBar&SearchBar.swift
//  WeatherApp
//
//  Created by Михаил Дементьев on 04/10/2019.
//  Copyright © 2019 Михаил Дементьев. All rights reserved.
//

import Foundation

import UIKit

extension ForecastMainController {
    
    @objc func handleShowSearchBar(){
        search(shouldShow: true)
        searchBar.becomeFirstResponder()
    }
    
    func buildNavigationBar(){
        searchBar.sizeToFit()
        searchBar.barStyle = .default
        searchBar.delegate = self
        self.tabBarController?.tabBar.unselectedItemTintColor = #colorLiteral(red: 1, green: 1, blue: 1, alpha: 1)
        UIBarButtonItem.appearance(whenContainedInInstancesOf:[UISearchBar.self]).tintColor = UIColor.white
        let textFieldInsideSearchBar = searchBar.value(forKey: "searchField") as? UITextField
        textFieldInsideSearchBar?.textColor = .white
        textFieldInsideSearchBar?.tintColor = .white
        textFieldInsideSearchBar?.backgroundColor = #colorLiteral(red: 0, green: 0, blue: 0, alpha: 0.25)
        
        navBarHeight = mNavigationBar.frame.height
        mNavigationBar.prefersLargeTitles = true
        mNavigationBar.barStyle = .black
        mNavigationItem.rightBarButtonItem = UIBarButtonItem(barButtonSystemItem: .search, target: self, action: #selector(handleShowSearchBar))
        mNavigationItem.rightBarButtonItem?.tintColor = .white
        //mNavigationBar.setTitleVerticalPositionAdjustment(CGFloat(40), for: UIBarMetrics.default)
    }
    
    func showSearchBarButton(shouldShow: Bool){
        if shouldShow {
            mNavigationItem.rightBarButtonItem = UIBarButtonItem(barButtonSystemItem: .search, target: self, action: #selector(handleShowSearchBar))
            mNavigationItem.rightBarButtonItem?.tintColor = .white
        } else {
            mNavigationItem.rightBarButtonItem = nil
        }
    }
    
    func search(shouldShow: Bool){
        showSearchBarButton(shouldShow: !shouldShow)
        searchBar.showsCancelButton = shouldShow
        mNavigationItem.titleView = shouldShow ? searchBar : nil
        
    }
}

extension ForecastMainController: UISearchBarDelegate {
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        search(shouldShow: false)
    }
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        currentWeather.downloadCurrentWeather(apiUrl: "\(API_URL_LOCATION)\(searchBar.text ?? "")\(APPID)") {
            self.forecastCardController.downloadForecastWeather(upiUrl: "\(FORECAST_API_URL_LOCATION)\(searchBar.text ?? "")\(APPID)"){
                self.search(shouldShow: false)
                self.updateUI()
            }
        }
    }
    
}
