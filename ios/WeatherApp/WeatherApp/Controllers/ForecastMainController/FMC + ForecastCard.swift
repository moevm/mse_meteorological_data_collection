//
//  ForecastMainController + ForecastCard.swift
//  WeatherApp
//
//  Created by Михаил Дементьев on 24/09/2019.
//  Copyright © 2019 Михаил Дементьев. All rights reserved.
//

import UIKit

extension ForecastMainController {
    enum CardState {
        case expanded
        case collapsed
    }
    
    func setupCard() {
//        visualEffectView = UIVisualEffectView()
//        visualEffectView.frame = self.view.frame
//        self.view.addSubview(visualEffectView)
        
        forecastCardController = ForecastCardController(nibName: "ForecastCardController", bundle: nil)
        self.addChild(forecastCardController)
        self.view.addSubview(forecastCardController.view)
        
        forecastCardController.view.frame = CGRect(x: 0, y: self.view.frame.height - cardHandleAreaHeight - tabbarHeight, width: self.view.bounds.width, height: cardHeight)
        print(self.view.frame.height)
        print(self.navBarHeight)
        print(self.cardHeight)
        print(self.tabbarHeight)
        print(self.cardHandleAreaHeight)
        forecastCardController.tableViewHeight.constant = self.cardHeight - self.navBarHeight - self.tabbarHeight - self.cardHandleAreaHeight - 15
        
        forecastCardController.view.clipsToBounds = true
        
        let tapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(ForecastMainController.handleCardTap(recognizer:)))
        let panGestureRecognizer = UIPanGestureRecognizer(target: self, action: #selector(ForecastMainController.handleCardPan(recognizer:)))
        
        forecastCardController.handleArea.addGestureRecognizer(tapGestureRecognizer)
        forecastCardController.handleArea.addGestureRecognizer(panGestureRecognizer)
        
        
    }
    
    @objc
    func handleCardTap(recognizer: UITapGestureRecognizer) {
        
    }
    
    @objc
    func handleCardPan(recognizer: UIPanGestureRecognizer) {
        switch recognizer.state {
        case .began:
            startInteractiveTransition(state: nextState, duration: 0.9)
        case .changed:
            let translation = recognizer.translation(in: self.forecastCardController.handleArea)
            var fractionComplete = translation.y / cardHeight
            fractionComplete = cardVisible ? fractionComplete : -fractionComplete
            updateInteractiveTransition(fractionCompleted: fractionComplete)
        case .ended:
            continuenteractiveTransition()
        default:
            break
        }
    }
    
    func animateTransitionIfNeeded (state: CardState, duration: TimeInterval) {
        if runningAnimations.isEmpty {
            let frameAnimator = UIViewPropertyAnimator(duration: duration, dampingRatio: 1) {
                switch state {
                case .expanded:
                    self.forecastCardController.view.frame.origin.y = self.view.frame.height - self.cardHeight + self.navBarHeight + 15
                case .collapsed:
                    self.forecastCardController.view.frame.origin.y = self.view.frame.height - self.cardHandleAreaHeight - self.tabbarHeight
                }
            }
            
            frameAnimator.addCompletion { _ in
                self.cardVisible = !self.cardVisible
                self.runningAnimations.removeAll()
            }
            
            frameAnimator.startAnimation()
            runningAnimations.append(frameAnimator)
            
            let cornerRadiusAnimator = UIViewPropertyAnimator(duration: duration, curve: .linear) {
                switch state {
                case .expanded:
                    self.forecastCardController.view.layer.cornerRadius = 24
                case .collapsed:
                    self.forecastCardController.view.layer.cornerRadius = 0

                }
            }
            
            cornerRadiusAnimator.startAnimation()
            runningAnimations.append(cornerRadiusAnimator)
        }
    }
    
    func startInteractiveTransition (state: CardState, duration: TimeInterval) {
        if runningAnimations.isEmpty {
            animateTransitionIfNeeded(state: state, duration: duration)
        }
        for animator in runningAnimations {
            animator.pauseAnimation()
            animationProgressWhenInterrupted = animator.fractionComplete
        }
    }
    
    func updateInteractiveTransition (fractionCompleted: CGFloat) {
        for animator in runningAnimations {
            animator.fractionComplete = fractionCompleted + animationProgressWhenInterrupted
        }
    }
    
    func continuenteractiveTransition () {
        for animator in runningAnimations {
            animator.continueAnimation(withTimingParameters: nil, durationFactor: 0)
        }
    }
}
