/*
 * Copyright 2022 Infineon Technologies AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import UIKit

/**
 * UI view controller to represent the transaction log page
 */
class TransactionLogViewController: UIViewController {
    @IBOutlet weak var navigationBar:UINavigationBar!
    @IBOutlet weak var navigationItems: UINavigationItem!
    @IBOutlet weak var textViewLog: UITextView!
    @IBOutlet weak var lblError: UILabel!
    @IBOutlet weak var lblTitle: UILabel!
    @IBOutlet weak var btnShare: UIButton!
    
    let fileManager = FileManager.default
	
    /**
     * Called after the controller's view is loaded into memory.
     */
    override func viewDidLoad() {
        super.viewDidLoad()
        setupNavigationBar()
        btnShare.layer.cornerRadius = 25
        textViewLog.backgroundColor = nil
        lblTitle.isHidden = true
        textViewLog.isHidden = true
        lblError.isHidden = false
        btnShare.isHidden = true
        lblError.text = ERR_TRANSACTION_LOG_NOT_AVAILBALE
        guard  let url = fileManager.urls(for: .documentDirectory, in: .userDomainMask).first else {
            return
        }
        let path = url.appendingPathComponent("nfcbp").appendingPathComponent("log.txt")
        guard let data = fileManager.contents(atPath: path.path) else {
            
            return
        }
        
        let strString = String(decoding: (data), as: UTF8.self)
        let containts = strString.split(separator: "#")
        if(containts.count >= 2) {
            lblTitle.isHidden = false
            textViewLog.isHidden = false
            btnShare.isHidden = false
            lblError.isHidden = true
            lblTitle.text = "Transaction at \(String(containts[0]))"
            textViewLog.text = String(containts[1])         
        }
    }
    
    /**
     * Configures the navigation bar
     */
    func setupNavigationBar(){
        let menuButton = UIButton(type: .system)
        menuButton.setImage(UIImage(named:"left-arrow")?.withRenderingMode(.alwaysTemplate),for:.normal)
        menuButton.frame = CGRect(x: 0, y: 0, width: 0, height: 40)
        menuButton.contentMode  = .scaleAspectFit
        menuButton.addTarget(self, action: #selector(menuButtonPressed), for: .touchUpInside)
        navigationItems.leftBarButtonItem = UIBarButtonItem(customView: menuButton)
        navigationBar.shadowImage = UIImage()        
    }
	
	/**
     * Handles the share button click event
     */
    @IBAction func btnOnShareClick(_ sender: Any) {
        if(textViewLog.text != ""){
            let logText = TRANSACTION_LOG_INITIAL_TEXT + textViewLog.text! 
            let textToShare = [ logText ]
            let activityViewController = UIActivityViewController(activityItems: textToShare, applicationActivities: nil)
            activityViewController.popoverPresentationController?.sourceView = self.view // so that iPads won't crash
            activityViewController.excludedActivityTypes = [ UIActivity.ActivityType.airDrop, UIActivity.ActivityType.postToFacebook ]
            self.present(activityViewController, animated: true, completion: nil)
        }
    }
    
    /**
     * Handles the menu button pressed event in navigation view
     */
    @objc func menuButtonPressed() {
        dismiss(animated: true,completion: nil)
    }
    
    /**
     * Change the UIStatusBarStyle to the dark content mode
     */
    override var preferredStatusBarStyle: UIStatusBarStyle{
        return UIStatusBarStyle.darkContent
    }
}
