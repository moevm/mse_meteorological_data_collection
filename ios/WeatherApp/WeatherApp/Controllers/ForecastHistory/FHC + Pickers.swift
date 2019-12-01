//
//  FHC + Pickers.swift
//  WeatherApp
//
//  Created by Михаил Дементьев on 29.11.2019.
//  Copyright © 2019 Михаил Дементьев. All rights reserved.
//

import FirebaseFirestore
import Foundation
import UIKit

extension ForecastHistoryController: UIPickerViewDelegate, UIPickerViewDataSource {
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return getRowsInDropDownList(pickerView)
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return getRowStringInDropDownList(pickerView, row)
    }
    
    func pickerView(_ pickerView: UIPickerView,
                    didSelectRow row: Int,
                    inComponent component: Int) {

        
        doWithSelectedItem(pickerView, row)
    }
}
