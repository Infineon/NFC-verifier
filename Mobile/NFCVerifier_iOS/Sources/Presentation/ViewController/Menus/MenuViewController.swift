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
 * UI view controller to represent the menu page
 */
class MenuViewController: UIViewController {

    @IBOutlet weak var lblOptions: UILabel!
    @IBOutlet weak var lblAbout: UILabel!
    @IBOutlet weak var lblHelp : UILabel!
    @IBOutlet weak var lblHome : UILabel!
    @IBOutlet weak var lblLog : UILabel!
    
    @IBOutlet weak var navigationItems: UINavigationItem!
    @IBOutlet weak var navigationBar: UINavigationBar!


	/**
	 * Called after the controller's view is loaded into memory.
	 */
    override func viewDidLoad() {
       super.viewDidLoad()
        
       let tapAbout = UITapGestureRecognizer(target: self, action:  #selector(tapFunction))
       lblAbout.addGestureRecognizer(tapAbout)
       let tapOption = UITapGestureRecognizer(target: self, action:  #selector(tapFunction))
       lblOptions.addGestureRecognizer(tapOption)
       let tapHelp = UITapGestureRecognizer(target: self, action:  #selector(tapFunction))
       lblHelp.addGestureRecognizer(tapHelp)
       let lblHomeTab = UITapGestureRecognizer(target: self, action:  #selector(tapFunction))
       lblHome.addGestureRecognizer(lblHomeTab)
       let lblLogTab = UITapGestureRecognizer(target: self, action:  #selector(tapFunction))
       lblLog.addGestureRecognizer(lblLogTab)    
    }

	/**
	 * Called after the controller's sub views are loaded
	 */
    override func viewDidLayoutSubviews() {
          setupNavigationBar()
    }
	
    /**
     * Method to handle menu item tap event
     */
    @objc func tapFunction(sender:UITapGestureRecognizer) {
       
      if(sender.view == lblAbout) {
          let aboutViewController = storyboard!.instantiateViewController(withIdentifier: "AboutViewController") as!AboutViewController
         aboutViewController.modalPresentationStyle = .fullScreen
         self.present(aboutViewController, animated: true,completion: nil)
      }
      if(sender.view == lblOptions) {
          let optionsViewController = storyboard!.instantiateViewController(withIdentifier: "OptionsViewController") as!OptionsViewController
          optionsViewController.modalPresentationStyle = .fullScreen
          self.present(optionsViewController, animated: true,completion: nil)
      }
  
      if(sender.view == lblHelp) {
          let helpViewController = storyboard!.instantiateViewController(withIdentifier: "HelpViewController") as!HelpViewController
           helpViewController.modalPresentationStyle = .fullScreen
          self.present(helpViewController, animated: true,completion: nil)
      }
  
      if(sender.view == lblHome) {
         dismiss(animated: true, completion: nil)
      }
      if(sender.view == lblLog) {
           let transactionLogViewController = storyboard!.instantiateViewController(withIdentifier: "TransactionLogViewController") as!TransactionLogViewController
             transactionLogViewController.modalPresentationStyle = .fullScreen
             self.present(transactionLogViewController, animated: true,completion: nil)
      }
   }

	/**
	* Configures the navigation bar
	*/
    func setupNavigationBar(){
        let infineonIcon = UIImageView(image:   UIImage(named:"app-logo")?.withRenderingMode(.alwaysOriginal))
        infineonIcon.contentMode  = .scaleAspectFit
        infineonIcon.layer.minificationFilter = CALayerContentsFilter.trilinear
        navigationItems.leftBarButtonItem = UIBarButtonItem(customView: infineonIcon)
        let menuButton = UIButton(type: .system)
        menuButton.setImage(UIImage(named:"menu-close")?.withRenderingMode(.alwaysTemplate),for:.normal)
        menuButton.frame = CGRect(x: 0, y: 0, width: 60, height: 300)
        menuButton.contentHorizontalAlignment = .right
        menuButton.contentMode  = .scaleAspectFit
        menuButton.addTarget(self, action: #selector(closeButtonpressed), for: .touchUpInside)
        navigationItems.rightBarButtonItem = UIBarButtonItem(customView: menuButton)
        navigationBar.shadowImage = UIImage()
    }

    /**
     * Handles the close button press event in navigation view
     */
    @objc func closeButtonpressed() {
        dismiss(animated: true, completion: nil)

    }
	
	/**
	 * Change the UIStatusBarStyle to the dark content mode
	 */
	override var preferredStatusBarStyle: UIStatusBarStyle{
		return UIStatusBarStyle.darkContent
	}
}
