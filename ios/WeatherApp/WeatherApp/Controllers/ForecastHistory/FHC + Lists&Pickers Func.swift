//
//  FHC + Lists&Pickers Func.swift
//  WeatherApp
//
//  Created by Михаил Дементьев on 29.11.2019.
//  Copyright © 2019 Михаил Дементьев. All rights reserved.
//

import FirebaseFirestore
import Foundation
import UIKit

extension ForecastHistoryController {
    
    func initPickers(){
        countryPicker.isEnabled = false
        countryLoader.startAnimating()
        regionPicker.isEnabled = false
        cityPicker.isEnabled = false
    }
    
    func initDropDownLists(){
        let elementPicker = UIPickerView()
        elementPicker.delegate = self
        
        dropDownLists = [
            countryPicker,
            regionPicker,
            cityPicker,
            intervalPicker,
            dateStartPicker,
            dateEndPicker
        ]
        self.choicePickers(for: self.dropDownLists)
        self.createToolBars(for: self.dropDownLists)
        countryPicker.isEnabled = true
    }
    
    func createToolBars(for textFields: [DropDownList]) {
        let toolbar = UIToolbar()
        toolbar.sizeToFit()
        let spaceButton = UIBarButtonItem(barButtonSystemItem: UIBarButtonItem.SystemItem.flexibleSpace, target: nil, action: nil)
        let atriString = NSAttributedString(string: "Готово", attributes: [NSAttributedString.Key.font : UIFont(name: "Marker Felt", size: 12.0)!])
        let doneButton = UIBarButtonItem(title: atriString.string, style: .plain, target: self, action: #selector(closeKeyboard))
        toolbar.setItems([spaceButton, doneButton], animated: true)
        toolbar.isUserInteractionEnabled = true

        toolbar.tintColor = .white
        toolbar.barTintColor = #colorLiteral(red: 0.6862745098, green: 0.3215686275, blue: 0.8705882353, alpha: 0.3894971391)
        
        
        for textField in textFields {
            textField.inputAccessoryView = toolbar
        }
    }
    
    @objc func closeKeyboard () {
        view.endEditing(true)
    }
    
    func choicePickers(for textFields: [DropDownList]) {

        for textField in textFields {
            let elementPicker = UIPickerView()
            elementPicker.delegate = self
            elementPicker.tintColor = .white
            elementPicker.backgroundColor = #colorLiteral(red: 0.6862745098, green: 0.3215686275, blue: 0.8705882353, alpha: 0.3894971391)

            let datePicker = UIDatePicker()
            datePicker.datePickerMode = .date
            datePicker.tintColor = .white
            datePicker.backgroundColor = #colorLiteral(red: 0.6862745098, green: 0.3215686275, blue: 0.8705882353, alpha: 0.3894971391)
            
            if (textField == countryPicker as UITextField ||
                textField == regionPicker as UITextField ||
                textField == cityPicker as UITextField ||
                textField == intervalPicker as UITextField) {
                dropDownElements.append(["picker": elementPicker, "textField": textField])
                textField.delegate = self
                textField.inputView = elementPicker
            } else if textField == dateStartPicker as UITextField {
                datePicker.addTarget(self, action: #selector(ForecastHistoryController.dateStartChanged(datePicker:)), for: .valueChanged)
                dropDownElements.append(["picker": datePicker, "textField": textField])
                textField.inputView = datePicker
            } else if textField == dateEndPicker as UITextField {
                datePicker.addTarget(self, action: #selector(ForecastHistoryController.dateEndChanged(datePicker:)), for: .valueChanged)
                dropDownElements.append(["picker": datePicker, "textField": textField])
                textField.inputView = datePicker
            }
        }
    }
    
    @objc func dateStartChanged(datePicker: UIDatePicker) {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "dd/MM/yyyy"
        dateStartPicker.text = dateFormatter.string(from: datePicker.date)
        dateFormatter.dateFormat = "yyyy"
        searchData?.startYear = dateFormatter.string(from: datePicker.date)
    }
    
    @objc func dateEndChanged(datePicker: UIDatePicker) {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "dd/MM/yyyy"
        dateEndPicker.text = dateFormatter.string(from: datePicker.date)
        dateFormatter.dateFormat = "yyyy"
        searchData?.endYear = dateFormatter.string(from: datePicker.date)
    }
    
    func getRowsInDropDownList(_ pickerView: UIPickerView) -> Int {
        var intReturn = 0

        for elements in dropDownElements {

            guard pickerView == elements["picker"] as? UIPickerView else { continue }
            
            switch elements["textField"] {
            case countryPicker as UITextField:
                intReturn = countryArray.count
            case regionPicker as UITextField:
                intReturn = regionArray.count
            case cityPicker as UITextField:
                intReturn = cityArray.count
            case intervalPicker as UITextField:
                intReturn = intervalArray.count
            default:
                continue
            }
        }

        return intReturn
    }
    
    func getRowStringInDropDownList(_ pickerView: UIPickerView, _ row: Int) -> String {
        var stringReturn = ""

        for elements in dropDownElements {

            guard pickerView == elements["picker"] as? UIPickerView else { continue }

            switch elements["textField"] {
                
            case countryPicker as UITextField:
                if let countryName = countryArray[row] {
                    stringReturn = countryName
                }
            case regionPicker as UITextField:
                if let regionName = regionArray[row] {
                    stringReturn = regionName
                }
            case cityPicker as UITextField:
                if let cityName = cityArray[row] {
                    stringReturn = cityName
                }
            case intervalPicker as UITextField:
                if let intervalTitle = intervalArray[row] {
                    stringReturn = intervalTitle
                }
            default:
                continue
            }
        }
        return stringReturn
    }
    
    func doWithSelectedItem(_ pickerView: UIPickerView, _ row: Int) {
        for elements in dropDownElements {

            var textFieldString = ""

            guard pickerView == elements["picker"] as? UIPickerView else { continue }

            switch elements["textField"] {

            case countryPicker as UITextField:
                if let countryName = countryArray[row] {
                    textFieldString = countryName
                    regionLoader.startAnimating()
                    let docRef = db?.collection("countries").document(countryName)
                    docRef?.getDocument(completion: { (documentSnapshot, err) in
                        if let document = documentSnapshot?.data() as? Dictionary<String, [String]> {
                            if let regionArray = document["areas"]{
                                (self.regionPicker)?.text = ""
                                (self.cityPicker)?.text = ""
                                self.regionLoader.stopAnimating()
                                
                                if regionArray.count > 0 {
                                    self.regionArray = regionArray
                                    self.regionPicker.isEnabled = true
                                } else {
                                    self.regionPicker.isEnabled = false
                                    self.cityPicker.isEnabled = false
                                    (self.regionPicker)?.text = "Список областей отсутствует"
                                    (self.cityPicker)?.text = "Список городов отсутствует"
                                }
                            }
                        }
                    })
                }
            case regionPicker as UITextField:
                if let regionName = regionArray[row] {
                    textFieldString = regionName
                    cityLoader.startAnimating()
                    let docRef = db?.collection("areas").document(regionName)
                    docRef?.getDocument(completion: { (documentSnapshot, err) in
                        if let document = documentSnapshot?.data() {
                            if let cityDictionaty = document["cities"] as? Dictionary<String, String> {
                                self.cityArray.removeAll()
                                (self.cityPicker)?.text = ""
                                self.cityPicker.isEnabled = true
                                self.cityLoader.stopAnimating()
                                for (city, index) in cityDictionaty {
                                    self.cityArray.append(city)
                                    self.searchData?.cityIndex = index
                                }
                            }
                        }
                    })
                }
            case cityPicker as UITextField:
                if let cityName = cityArray[row] {
                    textFieldString = cityName
                }
            case intervalPicker as UITextField:
                if let intervalTitle = intervalArray[row] {
                    textFieldString = intervalTitle
                }
            default:
                continue
            }
            (elements["textField"] as? UITextField)?.text = textFieldString
        }
    }
}
