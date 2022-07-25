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
 * UI view controller to represent the license agreement page
 */
class LicenseAgreementViewController: UIViewController {
    
    @IBOutlet weak var bottomContraint: NSLayoutConstraint!
    @IBOutlet weak var tvLicense: UITextView!
    @IBOutlet weak var navigationViewItems: UINavigationItem!
    @IBOutlet weak var btnAccept: UIButton!
    @IBOutlet weak var navigationBar: UINavigationBar!
	
    var isFirstTimeStart = false
    var fileName = EULA_FILE_NAME
    var homeScreenViewController: HomeScreenViewController?
    
    /**
     * Called after the controller's view is loaded into memory.
     */
    override func viewDidLoad() {
        super.viewDidLoad()
        setupNavigationBar()
        setupUI()
        loadLicense()
    }
	
    /**
     * Handles the accept button press event
     */
    @IBAction func btnAcceptPressed(_ sender: Any) {
        // Store the user acceptance in the preference
		let pref = PreferenceHelper()
        pref.setIntPref(key: pref.IS_EUAL_ACCEPTED, value: pref.ACCEPTED)
        pref.setStringPref(key: pref.EULA_VERSION, value: Bundle().buildVersion)
		
        homeScreenViewController?.scanTag()
        dismiss(animated: true, completion: nil)       
    }
    
    /**
     * Method to load the license file and display in the UI
     */
    func loadLicense(){
        let licenseFile = Bundle.main.path(forResource: fileName, ofType: "txt")
        let licenseString = try? String(contentsOfFile: licenseFile!, encoding: String.Encoding.utf8)
        tvLicense.text = licenseString
    }
    
    /**
     * Setup the UI elements
     */
    func setupUI(){
        btnAccept.layer.cornerRadius = 25
        if(isFirstTimeStart){
            btnAccept.isHidden = false
            bottomContraint.constant = 66
        } else {
            btnAccept.isHidden = true
            bottomContraint.constant = 10
        }
    }
	
    /**
     * Configures the navigation bar
     */
    func setupNavigationBar(){
        let image = UIImage(named:"app-logo")?.withRenderingMode(.alwaysOriginal)
        let infineonIcon = UIImageView(image:   image)
        infineonIcon.contentMode  = .scaleAspectFit
        navigationViewItems.leftBarButtonItem = UIBarButtonItem(customView: infineonIcon)
        let menuButton = UIButton(type: .system)
        menuButton.setImage(UIImage(named:"menu-close")?.withRenderingMode(.alwaysTemplate),for:.normal)
        menuButton.frame = CGRect(x: 0, y: 0, width: 60, height: 300)
        menuButton.contentHorizontalAlignment = .right
        menuButton.contentMode  = .scaleAspectFit
        menuButton.addTarget(self, action: #selector(backButtonpressed), for: .touchUpInside)
        navigationViewItems.rightBarButtonItem = UIBarButtonItem(customView: menuButton)       
    }
	
    /**
     * Action to handle the back button pressed
     */
    @objc func backButtonpressed() {
        if(isFirstTimeStart){
            exit(0)
        } else {
            dismiss(animated: true, completion: nil)
        }
    }
	
    /**
     * Changes the UIStatusBarStyle to the dark content mode
     */
    override var preferredStatusBarStyle: UIStatusBarStyle{
        return UIStatusBarStyle.darkContent
    }
}
