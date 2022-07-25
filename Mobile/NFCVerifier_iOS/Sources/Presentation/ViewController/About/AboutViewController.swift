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
 * UI view controller to represent the about page
 */
class AboutViewController: UIViewController {
     let HEADER_HEIGHT = 90.0
     let ROW_HEIGHT = 55.0
    let SOCIAL_FB_LINK = ""
    let SOCIAL_INSTAGRAM_LINK = ""
    let SOCIAL_YOUTUBE_LINK = ""
    let SOCIAL_LINKEDIN_LINK = ""
    let SOCIAL_TWITTER_LINK = ""

    @IBOutlet weak var tableView:UITableView!
    @IBOutlet weak var navigationBar:UINavigationBar!
    @IBOutlet weak var navigationItems: UINavigationItem!
    @IBOutlet weak var imgSocialFBIcon:UIImageView!
    @IBOutlet weak var imgSocialLinkedInIcon:UIImageView!
    @IBOutlet weak var imgSocialTwitterIcon:UIImageView!
    @IBOutlet weak var imgSocialYouTubeIcon:UIImageView!
    @IBOutlet weak var imgSocialInstaIcon:UIImageView!
    @IBOutlet weak var lblVersionInfo:UILabel!
    
    /**
     * Called after the controller's view is loaded into memory
     */
    override func viewDidLoad() {
        super.viewDidLoad()
        setupNavigationBar()
        setupTableView()
        setupClickHandlers()
        lblVersionInfo.text  = "Version \(Bundle().shortVersion) Build \(Bundle().buildVersion)"
    }
	
    /**
     * Attaches the tap event handler to the links
     */
    fileprivate func setupClickHandlers() {
         
        // Register Facebook tap event
        let tapImgSocialFBIcon = UITapGestureRecognizer(target: self, action:  #selector(tapFunction))
        imgSocialFBIcon.addGestureRecognizer(tapImgSocialFBIcon)
        
        // Register LinkedIn tap event
        let tapImgSocialLinkedInIcon = UITapGestureRecognizer(target: self, action:  #selector(tapFunction))
        imgSocialLinkedInIcon.addGestureRecognizer(tapImgSocialLinkedInIcon)
        
        // Register Twitter tap event
        let tapImgSocialTwitterIcon = UITapGestureRecognizer(target: self, action:  #selector(tapFunction))
        imgSocialTwitterIcon.addGestureRecognizer(tapImgSocialTwitterIcon)
        
        // Register YouTube tap event
        let tapImgSocialYouTubeIcon = UITapGestureRecognizer(target: self, action:  #selector(tapFunction))
        imgSocialYouTubeIcon.addGestureRecognizer(tapImgSocialYouTubeIcon)
        
        // Register Instagram tap event
        let tapImgSocialInstaIcon = UITapGestureRecognizer(target: self, action:  #selector(tapFunction))
        imgSocialInstaIcon.addGestureRecognizer(tapImgSocialInstaIcon)        
    }
	
    /**
     * Opens the link in the web browser
	 * - Parameter link: URL to be opened
     */
    fileprivate func openLinkInBrowser(link: String) {
        if let url = URL(string: link) {
            UIApplication.shared.open(url)
        }
    }
	
    /**
     * Implements the event handler for the links
     */
    @objc  func tapFunction(sender:UITapGestureRecognizer) {
        
        switch sender.view {
            case imgSocialFBIcon:
                openLinkInBrowser(link: SOCIAL_FB_LINK)
            
            case imgSocialLinkedInIcon:
                openLinkInBrowser(link: SOCIAL_LINKEDIN_LINK)

            case imgSocialTwitterIcon:
                openLinkInBrowser(link: SOCIAL_TWITTER_LINK)

            case imgSocialYouTubeIcon:
                openLinkInBrowser(link: SOCIAL_YOUTUBE_LINK)

            case imgSocialInstaIcon:
                openLinkInBrowser(link: SOCIAL_INSTAGRAM_LINK)

            default:
                break
        }
    }
	
    /**
     * Setup the table view sources
     */
    fileprivate func setupTableView() {
        tableView.dataSource = self
        tableView.delegate = self
        let nibAboutTableViewCell  = UINib(nibName: "AboutTableViewCell", bundle: nil)
        tableView.register(nibAboutTableViewCell, forCellReuseIdentifier: "AboutTableViewCell")
        let nibHeaderTableViewCell  = UINib(nibName: "HeaderTableViewCell", bundle: nil)
        tableView.register(nibHeaderTableViewCell, forCellReuseIdentifier: "HeaderTableViewCell")
        
        let nibAboutLicenseTableViewCell  = UINib(nibName: "AboutLicenseTableViewCell", bundle: nil)
        tableView.register(nibAboutLicenseTableViewCell, forCellReuseIdentifier: "AboutLicenseTableViewCell")
    }
	
    /**
     * Returns the height to be used for a row in a specified location
     * - Parameter tableView: The table view requesting this information
     * - Parameter indexPath: An index path that locates a row in tableView
     * - Returns A non-negative floating-point value that specifies the height (in points) that row should be
     */
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if(indexPath.row == 0 ) {
            return HEADER_HEIGHT
        } else {
            return ROW_HEIGHT
        }
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
        //Current Code, default colour is black
        dismiss(animated: true,completion: nil)
    }
    
    /**
     * change the UIStatusBarStyle to the dark Content mode
     */
    override var preferredStatusBarStyle: UIStatusBarStyle{
        return UIStatusBarStyle.darkContent
    }
}


