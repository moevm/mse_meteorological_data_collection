//
//  DropDownList.swift
//  WeatherApp
//
//  Created by Михаил Дементьев on 28.11.2019.
//  Copyright © 2019 Михаил Дементьев. All rights reserved.
//

import UIKit

// Remove cursor and disable copy/paste for UITextField
class DropDownList: UITextField {

    override func caretRect(for position: UITextPosition) -> CGRect {
        return CGRect.zero
    }

    func selectionRects(for range: UITextRange) -> [Any] {
        return []
    }

    override func canPerformAction(_ action: Selector, withSender sender: Any?) -> Bool {

        if action == #selector(copy(_:)) || action == #selector(selectAll(_:)) || action == #selector(paste(_:)) || action == #selector(cut(_:)) {

            return false
        }

        return super.canPerformAction(action, withSender: sender)
    }
}
