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
import WebKit

/**
 * UI view controller to represent the web page
 */
class WebViewController: UIViewController,WKUIDelegate {
    
    @IBOutlet weak var webView: WKWebView!
    @IBOutlet weak var navigationBar:UINavigationBar!
    @IBOutlet weak var navigationItems: UINavigationItem!
    
    /**
     * Called after the controller's view is loaded into memory.
     */
    override func viewDidLoad() {
        super.viewDidLoad()
        setupNavigationBar()
    }
	
    /**
     * Loads the webpage in the webview
	 * - Parameter webURL: Webpage URL to be loaded
     */
    func loadWebUrl(webURL:String) {
        let url = URL(string:webURL)
        let urlRequest = URLRequest(url: url!)
        webView.load(urlRequest)
    }
	
    /**
     * Loads a local file in the webview. This is used to display help content. 
	 * - Parameter filename: HTML file to be loaded
     */
    func loadLocalData(filename:String){
        
        let htmlFile = Bundle.main.path(forResource: filename, ofType: "html")
        let htmlString = try? String(contentsOfFile: htmlFile!, encoding: String.Encoding.utf8)
        let baseUrlWk = Bundle.main.resourceURL
        
        webView.loadHTMLString(htmlString!, baseURL: baseUrlWk)       
    }

    /**
     * Configures the navigation bar
     */
    func setupNavigationBar(){
        let menuButton = UIButton(type: .system)
        menuButton.setImage(UIImage(named:"left-arrow")?.withRenderingMode(.alwaysTemplate),for:.normal)
        menuButton.frame = CGRect(x: 0, y: 0, width: 0, height: 40)
        menuButton.contentMode  = .scaleAspectFit
        menuButton.addTarget(self, action: #selector(backButtonPressed), for: .touchUpInside)
        navigationItems.leftBarButtonItem = UIBarButtonItem(customView: menuButton)
        navigationBar.shadowImage = UIImage()        
    }
	
    /**
     * Handles the back button press event in the navigation view
     */
    @objc func backButtonPressed() {
        dismiss(animated: true,completion: nil)
    }
    
    /**
     * Changes the UIStatusBarStyle to the dark content mode
     */
    override var preferredStatusBarStyle: UIStatusBarStyle{
        return UIStatusBarStyle.darkContent
    }
}
