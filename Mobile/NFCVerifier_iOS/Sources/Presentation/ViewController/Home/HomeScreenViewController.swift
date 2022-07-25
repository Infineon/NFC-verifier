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

import Foundation
import CoreNFC
import UIKit
import SwiftUI

/**
 * UI view controller to represent the home page
 */
class HomeScreenViewController: UIViewController, NFCListener {
    
    let logger:FileLogger = LoggerFactory.getLogger(logger: .File) as! FileLogger

    @IBOutlet weak var authenticationResultContainer: UIView!
    @IBOutlet weak var warningView: UIView!
    @IBOutlet weak var errorMessage:UILabel!
    @IBOutlet weak var warningMessage:UILabel!
    @IBOutlet weak var failureView: UIView!
    @IBOutlet weak var backgroudView: IFXBackground!    
    @IBOutlet weak var btnScanTag: UIButton!
    @IBOutlet weak var btnScanTagAgain: UIButton!
    @IBOutlet weak var btnCheckServer: UIButton!
	
    var embeddedViewController:AppFileInfoViewController?
	let nfcHelper = NFCHelper()
	
    /**
     * Called after the controller's view is loaded into memory.
     */
    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
        setupNavigationBar()
        if(checkLicenseAcceptance()){
            scanTag()
        } else {
            showLicense()
        }

    }
	
    /**
     * Called before the controller's view is loaded into memory.
     */
    override func viewWillAppear(_ animated: Bool) {
        setupUI()
    }
    
    /**
     * Setup the UI elements
     */
    private func setupUI() {
        backgroudView.isHidden = false
        failureView.isHidden = true
        btnCheckServer.isHidden = true
        warningView.isHidden = true
        authenticationResultContainer.isHidden = true
        btnScanTag.setTitle("Scan & verify", for: .normal)
        btnScanTag.layer.cornerRadius = 25
        btnCheckServer.layer.cornerRadius = 25
        btnScanTagAgain.isHidden = true
        btnScanTagAgain.layer.cornerRadius = 25
        btnScanTagAgain.layer.borderWidth = 2
        btnScanTagAgain.layer.borderColor = UIColor.init(named: "Berry")?.cgColor
   }
    
    /**
     * Configures the navigation bar
     */
    func setupNavigationBar(){
        let image = UIImage(named:"app-logo")?.withRenderingMode(.alwaysOriginal)
        let infineonIcon = UIImageView(image:   image)
        infineonIcon.contentMode  = .scaleAspectFit
        navigationItem.leftBarButtonItem = UIBarButtonItem(customView: infineonIcon)
        let menuButton = UIButton(type: .system)
        menuButton.setImage(UIImage(named:"menu-open")?.withRenderingMode(.alwaysTemplate),for:.normal)
        menuButton.frame = CGRect(x: 0, y: 0, width: 60, height: 300)
        menuButton.contentHorizontalAlignment = .right
        menuButton.contentMode  = .scaleAspectFit
        menuButton.tintColor = UIColor.init(named: "Engineering")!
        menuButton.addTarget(self, action: #selector(menuButtonPressed), for: .touchUpInside)
        navigationItem.rightBarButtonItem = UIBarButtonItem(customView: menuButton)
    }    
    
    /**
     * Menu button pressed event
     */
    @objc func menuButtonPressed() {
        let menuViewController = storyboard!.instantiateViewController(withIdentifier: "MenuViewController") as!MenuViewController
        menuViewController.modalPresentationStyle = .fullScreen
        self.present(menuViewController, animated: true,completion: nil)
    }
    
    /**
     * Handles the click event of scan again tag button
     */    
    @IBAction func btnScanTagAgainClick(_ sender: UIButton) {
        embeddedViewController?.workItem?.cancel()
        scanTag()
    }
    
    /**
     * Handles the click event of scan tag button
     */
    @IBAction func btnScanTagClick(_ sender: UIButton) {
        scanTag()
    }
    
    /**
     * Method to start polling for tag
     */
    func scanTag() {
        setupUI()

        // Start Polling of NFC Tag
        if NFCNDEFReaderSession.readingAvailable {
            // Set up the NFC session
            nfcHelper.startReading(delegate: nfcHelper,callback: self )
            nfcHelper.showError = true
        } else {
            
            self.backgroudView.isHidden = true
            self.warningView.isHidden = false
            self.warningMessage.text = ERR_MSG_UNSUPORT_NFC
            self.view.setNeedsDisplay()
        }
    }
    

    /** 
	 * Displays error message in the user interface
     * - Parameter message: Error message to be displayed
     */
    fileprivate func displayError(message: String) {
        DispatchQueue.main.async {
            self.backgroudView.isHidden = true
            self.failureView.isHidden = false
            self.errorMessage.text = message
            self.btnScanTag.setTitle("Scan again", for: .normal)
            self.view.setNeedsDisplay()
            self.nfcHelper.nfcSession?.invalidate(errorMessage: message)
        }
    }
	
    /**
     * Displays warning message in the user interface
     * - Parameter message: Error message to be displayed
     */
    fileprivate func displayWarning(message: String) {
        DispatchQueue.main.sync {
            self.backgroudView.isHidden = true
            self.warningView.isHidden = false
            self.warningMessage.text = message
            self.btnScanTag.setTitle("Scan again", for: .normal)
            self.view.setNeedsUpdateConstraints()
            self.view.setNeedsLayout()
            self.nfcHelper.nfcSession?.invalidate(errorMessage: message)
            
        }
    }
	
    /**
     * Method to print the logs
     */
    fileprivate func printLogs() {
        BrandVerificationManager.timeLogger.log(tag: "Step 4 Time")
        BrandVerificationManager.totalTimeLogger.log(tag: "Total Time")
        let islogger = BrandVerificationManager.totalTimeLogger.isPrintTime
        BrandVerificationManager.totalTimeLogger.isPrintTime = true
        BrandVerificationManager.totalTimeLogger.log(tag: "Total time taken:")
        BrandVerificationManager.totalTimeLogger.isPrintTime = islogger
    }

    /**
     * Event handler that handles the success event and displays the product and service information
     * - Parameter message: Message indicating success
     * - Parameter productInfoData: Product information from the tag
     * - Parameter serviceInfoData: Service information from the tag
	 * - Parameter uriRecord: URI record from the tag
     */
    func onNFCCommunicationSuccess(message: String , productInfoData productinfo: Data?, serviceInfoData serviceinfo: Data?, uriRecord: URIRecord?) {
        BrandVerificationManager.timeLogger.log(tag: "Step 3 Time")
        BrandVerificationManager.totalTimeLogger.log(tag: "Total Time")
        var loader: UIAlertController?
        DispatchQueue.main.sync {
            loader = showLoader()
            self.nfcHelper.nfcSession?.invalidate()
        }
        BrandVerificationManager.timeLogger.start()
        BrandVerificationManager.mutualAuthVerifyAPIRequest() { result, message in
            self.printLogs()
            self.logger.commitFile()
            DispatchQueue.main.sync {               
                 loader!.dismiss(animated: true) {
                    if(result) {
                        self.backgroudView.isHidden = true
                        self.authenticationResultContainer.isHidden = false
                        self.btnScanTagAgain.isHidden = false
                        self.view.setNeedsDisplay()
                        self.embeddedViewController?.configureData(productInfoData: productinfo, serviceInfoData: serviceinfo, urlRecord: uriRecord)
                    } else {
                        self.displayError(message: message)
                    }                  
                }
            }
        }
    }
   
    /**
     * Event handler that handles the failure event
     * - Parameter callbackType: Defines the type of error
     * - Parameter errorMessage: Error message describing the failure
     */
    func onNFCCommunicationError(callbackType: CallbackType, errorMessage: String) {
        logger.commitFile()
        switch (callbackType) {           
			case .Error:
				displayError(message: errorMessage)
			case .Warning:
				displayWarning(message: errorMessage)
        }
    }
    
    /**
     * Prepares the information view page
     */
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let vc = segue.destination as? AppFileInfoViewController,
                    segue.identifier == "authseg" {
            self.embeddedViewController = vc
        }
    }
	
    /**
     * Shows the loader view while verification is in-progress
	 * - Returns the loader view
     */
    func showLoader() -> UIAlertController{
        let alert = UIAlertController(title: "Verification in-progress.", message: "Please wait", preferredStyle: .alert)
        let loadingIndicator = UIActivityIndicatorView(frame: CGRect(x: 10, y: 20, width: 50, height: 50))
        loadingIndicator.hidesWhenStopped = true
        loadingIndicator.style = UIActivityIndicatorView.Style.large
        loadingIndicator.startAnimating()
        alert.view.addSubview(loadingIndicator)
        present(alert, animated: true, completion: nil)
        return alert
    }
	
    /**
     * Method to check if the EULA is accepted by the user
	 * - Returns true if the user has accepted EULA, else false
     */
    private func checkLicenseAcceptance() -> Bool {
        let pref = PreferenceHelper()
        if(pref.getIntPref(key: pref.IS_EUAL_ACCEPTED) == pref.ACCEPTED && pref.getStringPref(key: pref.EULA_VERSION) == Bundle().buildVersion) {
            return true
        }
        return false
    }
    
    /**
     * Method to display the EULA for user acceptance
     */
    private func showLicense(){
        let licenseAgreementViewController = storyboard!.instantiateViewController(withIdentifier: "LicenseAgreementViewController") as!LicenseAgreementViewController
        licenseAgreementViewController.isFirstTimeStart = true
        licenseAgreementViewController.modalPresentationStyle = .fullScreen
        licenseAgreementViewController.homeScreenViewController = self
        self.present(licenseAgreementViewController, animated: true,completion: nil)
        licenseAgreementViewController.navigationViewItems.title = "End-User License Agreement"
    }
    
    /**
     * Changes the UIStatusBarStyle to the dark content mode
     */
    override var preferredStatusBarStyle: UIStatusBarStyle{
        return UIStatusBarStyle.darkContent
    }
}
