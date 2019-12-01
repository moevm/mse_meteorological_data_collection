//
//  FHC + TextFieldDelegate.swift
//  WeatherApp
//
//  Created by Михаил Дементьев on 29.11.2019.
//  Copyright © 2019 Михаил Дементьев. All rights reserved.
//

import FirebaseFirestore
import Foundation
import UIKit

extension ForecastHistoryController: UITextFieldDelegate {
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        self.resignFirstResponder()
        return true
    }

    func textFieldDidBeginEditing(_ textField: UITextField) {
        guard let text = textField.text, text.isEmpty else { return }
        for elements in dropDownElements {
            guard textField == (elements["textField"] as? UITextField), let picker = (elements["picker"] as? UIPickerView) else { continue }
            picker.selectRow(0, inComponent: 0, animated: true)
            
            self.pickerView(picker, didSelectRow: 0, inComponent: 0)
        }
    }
}
