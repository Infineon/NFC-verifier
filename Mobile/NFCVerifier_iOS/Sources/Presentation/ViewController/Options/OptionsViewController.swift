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
 * UI view controller to represent the options page
 */
class OptionsViewController: UIViewController {

    @IBOutlet weak var labelAutoOpenTime: UILabel!
    @IBOutlet weak var autoOpenTimeStepper: UIStepper!
    @IBOutlet weak var autoOpenLinkSwitch: UISwitch!
    @IBOutlet weak var navigationBar: UINavigationBar!
    @IBOutlet weak var navigationViewItems: UINavigationItem!
    
    /**
     * Called after the controller's view is loaded into memory.
     */
    override func viewDidLoad() {
        super.viewDidLoad()
        setupNavigationBar()
        loadDefaults()
    }
    
    /**
     * Method to load the default settings or the pre-configured preference
     */
    func loadDefaults() {
        let pref = PreferenceHelper();
        let buttonStatus = pref.getIntPref(key: pref.TAG_AUTO_OPEN_BUTTON);
        var autoOpenDuration = pref.getIntPref(key: pref.TAG_AUTO_OPEN_TIME);
        if(buttonStatus <= 0) {
            autoOpenLinkSwitch.setOn(false, animated: false)
        } else {
            autoOpenLinkSwitch.setOn(true, animated: false)
        }
        if(autoOpenDuration <= 0) {
            autoOpenDuration = 3;
        }
        labelAutoOpenTime.text = autoOpenDuration.description;
        autoOpenTimeStepper.value = Double(autoOpenDuration);
    }
    
    /**
     * Handles the switch button event
     */
    @IBAction func switchClicked(_ sender: Any) {
        let pref = PreferenceHelper();
        if(autoOpenLinkSwitch.isOn) {
            pref.setIntPref(key: pref.TAG_AUTO_OPEN_BUTTON, value: 1);
        } else {
            pref.setIntPref(key: pref.TAG_AUTO_OPEN_BUTTON, value: 0);
        }
    }
	
    /**
     * Handles the stepper value changed event
     */
    @IBAction func stepperValueChanged(_ sender: UIStepper) {
        let pref = PreferenceHelper();
        labelAutoOpenTime.text = Int(sender.value).description
        pref.setIntPref(key: pref.TAG_AUTO_OPEN_TIME, value: Int(sender.value));
    }
    
    /**
     * Configures the navigation bar
     */
    func setupNavigationBar(){
        let infineonIcon = UIImageView(image:   UIImage(named:"app-logo")?.withRenderingMode(.alwaysOriginal))
        infineonIcon.contentMode  = .scaleToFill
        navigationViewItems.leftBarButtonItem = UIBarButtonItem(customView: infineonIcon)
        let menuButton = UIButton(type: .system)
        menuButton.setImage(UIImage(named:"menu-close")?.withRenderingMode(.alwaysTemplate),for:.normal)
        menuButton.frame = CGRect(x: 0, y: 0, width: 60, height: 300)
        menuButton.contentHorizontalAlignment = .right
        menuButton.contentMode  = .scaleAspectFit
        menuButton.addTarget(self, action: #selector(backButtonPressed), for: .touchUpInside)
        navigationViewItems.rightBarButtonItem = UIBarButtonItem(customView: menuButton)
        navigationBar.shadowImage = UIImage()
    }
    
    /**
     * Handles the back button press event
     */
    @objc func backButtonPressed() {
        dismiss(animated: true, completion: nil)
    }
    
    /**
     * Changes the UIStatusBarStyle to the dark content mode
     */
    override var preferredStatusBarStyle: UIStatusBarStyle{
        return UIStatusBarStyle.darkContent
    }
}
