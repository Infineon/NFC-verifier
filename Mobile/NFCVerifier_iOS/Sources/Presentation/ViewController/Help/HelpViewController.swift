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
 * UI view controller to represent the help page
 */
class HelpViewController: UIViewController {
    static let HEADER_HEIGHT = 90.0
    static let ROW_HEIGHT = 60.0
    
	@IBOutlet weak var tableView:UITableView!
    @IBOutlet weak var navigationBar:UINavigationBar!
    @IBOutlet weak var navigationItems: UINavigationItem!
    @IBOutlet weak var downloadView:UIView!
    
    var pdfUrl : URL!
    
    /**
     * Called after the controller's view is loaded into memory.
     */
    override func viewDidLoad() {
        super.viewDidLoad()
        setupNavigationBar()
        setupTableView()
        registerClickEvents()
        tableView.backgroundView = nil
    }
    
    /**
     * Registers the click handler
     */
    fileprivate func registerClickEvents() {
        let tapDownloadView = UITapGestureRecognizer(target: self, action:  #selector(tapFunction))
        downloadView.addGestureRecognizer(tapDownloadView)
    }
	
    /**
     * Opens the user guide document URL in web browser
     */
    @objc  func tapFunction(sender:UITapGestureRecognizer) {        
        if(sender.view == downloadView) {
            let urlString = ""
            if let url = URL(string: urlString) {
                UIApplication.shared.open(url)
            }
        }
    }
	
    /**
     * Setup the table view sources
     */
    fileprivate func setupTableView() {
        tableView.dataSource = self
        tableView.delegate = self
        tableView.backgroundColor = nil
        let nibAboutTableViewCell  = UINib(nibName: "AboutTableViewCell", bundle: nil)
        tableView.register(nibAboutTableViewCell, forCellReuseIdentifier: "AboutTableViewCell")
        let nibHeaderTableViewCell  = UINib(nibName: "HeaderTableViewCell", bundle: nil)
        tableView.register(nibHeaderTableViewCell, forCellReuseIdentifier: "HeaderTableViewCell")        
    }
    
    /**
     * Configures the navigation bar
     */
    func setupNavigationBar(){     
        let infineonIcon = UIImageView(image:   UIImage(named:"app-logo")?.withRenderingMode(.alwaysOriginal))
        infineonIcon.contentMode  = .scaleAspectFit
        navigationItems.leftBarButtonItem = UIBarButtonItem(customView: infineonIcon)
        
        let menuButton = UIButton(type: .system)
        menuButton.setImage(UIImage(named:"menu-close")?.withRenderingMode(.alwaysTemplate),for:.normal)
        menuButton.frame = CGRect(x: 0, y: 0, width: 0, height: 40)
        menuButton.contentMode  = .scaleAspectFit
        menuButton.frame = CGRect(x: 0, y: 0, width: 60, height: 300)
        menuButton.contentHorizontalAlignment = .right
        menuButton.addTarget(self, action: #selector(menuButtonPressed), for: .touchUpInside)
        navigationItems.rightBarButtonItem = UIBarButtonItem(customView: menuButton)
        navigationBar.shadowImage = UIImage()        
    }
    
    /**
     * Menu button pressed event
     */
    @objc func menuButtonPressed() {
        dismiss(animated: true, completion: nil)

    }
    
    /**
     * Change the UIStatusBarStyle to the dark content mode
     */
    override var preferredStatusBarStyle: UIStatusBarStyle{
        return UIStatusBarStyle.darkContent
    }
}

