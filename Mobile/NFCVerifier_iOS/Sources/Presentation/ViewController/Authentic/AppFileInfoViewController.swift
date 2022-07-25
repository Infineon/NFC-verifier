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
import UIKit

/**
 * UI view controller to represent the info page
 */
class AppFileInfoViewController : UIViewController {
    let PRODUCTSECTION_INDEX = 0
    let SERVICESECTION_INDEX = 1
    var informationDataHeaderList: [InformationDataModelHeader]?
	var url:String?
    var workItem:DispatchWorkItem?
    
    @IBOutlet weak var lblErrorProductInfo: UILabel!
    @IBOutlet weak var btnViewProductViewPage: UIButton!
    @IBOutlet weak var tableView: UITableView!
	  
    /**
     * Handles the click event of 'view product webpage' button
     */
    @IBAction func tabViewProduct(_ sender: Any) {      
        if let urlstr = url {
            var urlString = ""
				// If URL query parameter is already present, append the parameter else add the parameter
                if urlstr.contains("?") {
                    urlString = "\(urlstr)&ref=app"
                } else {
                    urlString = "\(urlstr)?ref=app"
                }
            
            if let url = URL(string: urlString) {
                UIApplication.shared.open(url)
            }
        }
       workItem?.cancel()        
    }
	
    /**
     * Called after the controller's view is loaded into memory
     */
    override func viewDidLoad() {
        super.viewDidLoad()
        setupView()
        
    }
	
    /**
     * Setup the UI elements 
     */
    func  setupView(){        
        btnViewProductViewPage.layer.cornerRadius = 25
        setupTableView()
    }
	
    /**
     * Setup the table view sources
     */
    fileprivate func setupTableView() {
        tableView.dataSource = self
        tableView.delegate = self
        tableView.allowsSelection = true
        tableView.backgroundColor = nil
        let nibSectionTitleViewCell  = UINib(nibName: "SectionTitleViewCell", bundle: nil)
        tableView.register(nibSectionTitleViewCell, forCellReuseIdentifier: "SectionTitleViewCell")
        let nibPropertyTableViewCell  = UINib(nibName: "PropertyTableViewCell", bundle: nil)
        tableView.register(nibPropertyTableViewCell, forCellReuseIdentifier: "PropertyTableViewCell")
    }
    
    /**
     * Method to configure the product information and service information
     */
    func configureData(productInfoData: Data?, serviceInfoData:Data?, urlRecord:URIRecord?){
        informationDataHeaderList = []
        let dateFormatterPrint = DateFormatter()
        lblErrorProductInfo.isHidden = true
        dateFormatterPrint.dateFormat = "dd-MMM-yyyy"
        if(productInfoData != nil) {
            let productinfo = ProductInfoDecoder.decode(payload: productInfoData!)
            if(productinfo != nil) {
                prepareProductInformationData(dateFormatterPrint, productinfo)
                if(productinfo?.profileInfo != ProfileType.A10 && serviceInfoData != nil) {
                    let serviceinfo = ServiceInfoDecoder.decode(payload: serviceInfoData!)
                    if(serviceinfo != nil){
                        prepareServiceInformationData(dateFormatterPrint, serviceinfo!)
                    }
                }
            } else {                
                lblErrorProductInfo.isHidden = false
                view.layoutIfNeeded()
            }
        }
        
        if let urls = urlRecord {
            url = urls.getUri()            
        } else {
            btnViewProductViewPage.isHidden = true
            loadViewIfNeeded()
        }
        self.tableView.reloadData()
        self.handleAutoLaunchAction()
    }
    
    /**
     * Dispatches the auto-launch event of product web page, if configured in the user preferences
     */
    fileprivate func handleAutoLaunchAction() {
        let pref = PreferenceHelper();
        let queue = DispatchQueue(label: "queue", attributes: .concurrent)
        workItem = DispatchWorkItem { [self] in
            tabViewProduct(btnViewProductViewPage!)
        }
        if(pref.getIntPref(key: pref.TAG_AUTO_OPEN_BUTTON) == 1){
            let dispatchAfter = DispatchTimeInterval.seconds(pref.getIntPref(key: pref.TAG_AUTO_OPEN_TIME))
            DispatchQueue.main.asyncAfter(deadline: .now() + dispatchAfter+3) { [self] in
                queue.async(execute: workItem!)
            }
        }
    }
	
    /**
     * Creates a list of the product information
	 * - Parameter dateFormatterPrint: Formatter to format the date
	 * - Parameter productinfo: Product information data
     */
    fileprivate func prepareProductInformationData(_ dateFormatterPrint: DateFormatter, _ productinfo: ProductInfo?) {
        var productList = [CustomFieldItem]()
        productList.append(CustomFieldItem(title:"Model number",value:productinfo!.modelNumber))
        productList.append(CustomFieldItem(title:"Manufacturer",value:productinfo!.manufacturerName))
        productList.append(CustomFieldItem(title:"Product serial",value:productinfo!.serialNumber))
        if(productinfo!.manufactureDate != nil) {
            productList.append(CustomFieldItem(title:"Manufactured on",value:dateFormatterPrint.string(from: productinfo!.manufactureDate!)))
        }
        productList.append(CustomFieldItem(title:"Product code",value:productinfo!.productCode.data.hexEncodedString().uppercased()))

        for customField in productinfo!.customField {
            productList.append(CustomFieldItem(title:customField.title!,value:customField.value!))
        }        
        informationDataHeaderList!.append(InformationDataModelHeader(header: "Product Information",informationList: productList))
    }
	
    /**
     * Creates a list of the service information
	 * - Parameter dateFormatterPrint: Formatter to format the date
	 * - Parameter serviceinfo: Service information data
     */
    fileprivate func prepareServiceInformationData(_ dateFormatterPrint: DateFormatter, _ serviceinfo: ServiceInfo?) {
        var serviceList = [CustomFieldItem]()
        if(serviceinfo!.warrantyValidityDate != nil) {
            serviceList.append(CustomFieldItem(title:"Warranty validity",value:dateFormatterPrint.string(from: serviceinfo!.warrantyValidityDate!)))
        }        
        if(serviceinfo!.lastServiceDate != nil) {
            serviceList.append(CustomFieldItem(title:"Last service",value:dateFormatterPrint.string(from: serviceinfo!.lastServiceDate!)))
        }
        if(serviceinfo!.lastServiceDate != nil) {
            serviceList.append(CustomFieldItem(title:"Purchased on",value:dateFormatterPrint.string(from: serviceinfo!.purchaseDate!)))
        }
        for customeField in serviceinfo!.customField { serviceList.append(CustomFieldItem(title:customeField.title!,value:customeField.value!))
        }
        informationDataHeaderList!.append(InformationDataModelHeader(header: "Service Information",informationList: serviceList))
    }
	
    /**
     * Notifies the view controller that its view is about to be removed from a view hierarchy.
     * - Parameter animated:If true, the disappearance of the view is being animated.
     */
    override func viewWillDisappear(_ animated: Bool) {
        if(  workItem != nil) {
            workItem?.cancel()
        }
    }
    
    /**
     * Change the UIStatusBarStyle to the dark content mode
     */
    override var preferredStatusBarStyle: UIStatusBarStyle{
        return UIStatusBarStyle.darkContent
    }    
}


