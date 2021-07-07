//
//  StatViewController.swift
//  WeatherApp
//
//  Created by Михаил Дементьев on 21.12.2019.
//  Copyright © 2019 Михаил Дементьев. All rights reserved.
//

import UIKit

class StatViewController: UIViewController {
    
    
    @IBOutlet var temperatureLabels: [UILabel]!
    @IBOutlet var pressuresLabels: [UILabel]!
    @IBOutlet var windLabels: [UILabel]!
    
    var temperatures: [Double] = []
    var pressures: [Double] = []
    var winds: [Double] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        print(temperatures)
        print(pressures)
        print(winds)
        
        self.view.backgroundColor = UIColor.black.withAlphaComponent(0.6)
        
        self.showAnimate()
        
        temperatureLabels[0].text = temperatureLabels[0].text! + " " + calculateAverage(list: temperatures)
        temperatureLabels[1].text = temperatureLabels[1].text! + " " + calculateMin(list: temperatures)
        temperatureLabels[2].text = temperatureLabels[2].text! + " " + calculateMax(list: temperatures)
        temperatureLabels[3].text = temperatureLabels[3].text! + " " + String(Int(calculateMode(list: temperatures)!.mostFrequent[0]))
        
        pressuresLabels[0].text = pressuresLabels[0].text! + " " + calculateAverage(list: pressures)
        pressuresLabels[1].text = pressuresLabels[1].text! + " " + calculateMin(list: pressures)
        pressuresLabels[2].text = pressuresLabels[2].text! + " " + calculateMax(list: pressures)
        pressuresLabels[3].text = pressuresLabels[3].text! + " " + String(Int(calculateMode(list: pressures)!.mostFrequent[0]))
        
        windLabels[0].text = windLabels[0].text! + " " + calculateAverage(list: winds)
        windLabels[1].text = windLabels[1].text! + " " + calculateMin(list: winds)
        windLabels[2].text = windLabels[2].text! + " " + calculateMax(list: winds)
        windLabels[3].text = windLabels[3].text! + " " + String(Int(calculateMode(list: winds)!.mostFrequent[0]))
        
    }
    
    @IBAction func closeView(_ sender: Any) {
        self.removeAnimate()
        self.view.removeFromSuperview()
    }
    
    func showAnimate()
    {
        self.view.transform = CGAffineTransform(scaleX: 1.3, y: 1.3)
        self.view.alpha = 0.0;
        UIView.animate(withDuration: 0.25, animations: {
            self.view.alpha = 1.0
            self.view.transform = CGAffineTransform(scaleX: 1.0, y: 1.0)
        });
    }
    
    func removeAnimate()
    {
        UIView.animate(withDuration: 0.25, animations: {
            self.view.transform = CGAffineTransform(scaleX: 1.3, y: 1.3)
            self.view.alpha = 0.0;
            }, completion:{(finished : Bool)  in
                if (finished)
                {
                    self.view.removeFromSuperview()
                }
        });
    }
    
    func calculateAverage(list: [Double]) -> String {
        return String(Int(list.average))
    }
    
    func calculateMin(list: [Double]) -> String {
        return String(Int(list.min()!))
    }
    
    func calculateMax(list: [Double]) -> String {
        return String(Int(list.max()!))
    }
    
    func calculateMode(list: [Double]) -> (mostFrequent: [Double], count: Double)? {
        var counts: [Double: Double] = [:]

        list.forEach { counts[$0] = (counts[$0] ?? 0) + 1 }
        if let count = counts.max(by: {$0.value < $1.value})?.value {
            return (counts.filter{$0.value == count}.map{$0.key}, count)
        }
        return nil
    }
    
}
