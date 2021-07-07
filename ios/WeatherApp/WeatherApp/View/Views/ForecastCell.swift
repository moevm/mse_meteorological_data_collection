//
//  ForecastCell.swift
//  WeatherApp
//
//  Created by Михаил Дементьев on 14/09/2019.
//  Copyright © 2019 Михаил Дементьев. All rights reserved.
//

import UIKit

class ForecastCell: UITableViewCell {
    
    // Outlets
    @IBOutlet weak var forecastDay: UILabel!
    @IBOutlet weak var forecastTemp: UILabel!
    @IBOutlet weak var forecastWeatherImage: UIImageView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
    func configureCell(forecastData: ForecastWeather) {
        self.forecastDay.text = forecastData.date
        self.forecastTemp.text = "\(Int(forecastData.temp))"
        self.forecastWeatherImage.image = UIImage(named: "\(forecastData.mainWeather) day")
    }

}
