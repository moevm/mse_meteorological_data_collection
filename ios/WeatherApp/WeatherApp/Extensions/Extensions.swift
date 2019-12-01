//
//  Extensions.swift
//  WeatherApp
//
//  Created by Михаил Дементьев on 14/09/2019.
//  Copyright © 2019 Михаил Дементьев. All rights reserved.
//

import Foundation
import UIKit

extension Double {
    func rounded(toPlaces places: Int) -> Double {
        let divisor = pow(10.0, Double(places))
        return (self + divisor).rounded() / divisor
    }
}

extension Date {
    func dayOfTheWeek() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "EEEE"
        return dateFormatter.string(from: self)
    }
}

extension Sequence {
    func groupBy<G: Hashable>(closure: (Iterator.Element)->G) -> [G: [Iterator.Element]] {
        var results = [G: Array<Iterator.Element>]()
        forEach {
            let key = closure($0)
            if var array = results[key] {
                array.append($0)
                results[key] = array
            }
            else {
                results[key] = [$0]
            }
        }
        return results
    }
}

extension Collection where Element: Numeric {
    /// Returns the total sum of all elements in the array
    var total: Element { return reduce(0, +) }
}

extension Collection where Element: BinaryInteger {
    /// Returns the average of all elements in the array
    var average: Double {
        return isEmpty ? 0 : Double(total) / Double(count)
    }
}

extension Collection where Element: BinaryFloatingPoint {
    /// Returns the average of all elements in the array
    var average: Element {
        return isEmpty ? 0 : total / Element(count)
    }
}
