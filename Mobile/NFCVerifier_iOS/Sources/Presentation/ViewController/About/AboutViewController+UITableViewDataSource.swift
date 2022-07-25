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
 * Extension for UITableViewDataSource, UITableViewDelegate to handle table view events
 */
extension AboutViewController : UITableViewDataSource, UITableViewDelegate {
    
    /**
     * Asks the data source to return the number of sections in the table view.
     * - Parameter tableView:The table-view object requesting this information.
     * - Parameter section:An index number identifying a section in tableView.
     */
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return aboutRowData[section].count+1
    }
	
    /**
     * Asks the data source for a cell to insert in a particular location of the table view.
     *  - Parameter tableView:A table-view object requesting the cell.
     *  - Parameter indexPath: An index path locating a row in tableView.
     */
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        if(indexPath.row == 0) {  // Returns the Header View cell
            let cell =  tableView.dequeueReusableCell(withIdentifier: "HeaderTableViewCell", for: indexPath) as? HeaderTableViewCell
            cell?.label.text =  aboutSectionData[indexPath.section]
        
            return cell!
        } else { // Returns the rows
            if(aboutRowData[indexPath.section][indexPath.row-1].type == TYPE_FILE_TEXT)
            {
                let cell =  tableView.dequeueReusableCell(withIdentifier: "AboutLicenseTableViewCell", for: indexPath) as? AboutLicenseTableViewCell
                cell?.lblLink.text = aboutRowData[indexPath.section][indexPath.row-1].name
                cell?.lblLink.highlightedTextColor = UIColor(named: "Ocean")
                return cell!
                
            } else {
                let cell =  tableView.dequeueReusableCell(withIdentifier: "AboutTableViewCell", for: indexPath) as? AboutTableViewCell
                cell?.lblLink.text = aboutRowData[indexPath.section][indexPath.row-1].name
                cell?.lblLink.highlightedTextColor = UIColor(named: "Ocean")
                cell?.imgage.image = UIImage(named: "link-solid")?.withTintColor(UIColor(named: "Engineering")!)
                cell?.imgage.highlightedImage = UIImage(named: "link-solid")?.withTintColor(UIColor(named: "Ocean")!)

                return cell!
            }
            
        }
    }
	
    /**
     * Asks the data source to return the number of sections in the table view.
     * - Parameter tableView: An object representing the table view requesting this information.
     * - Returns The number of sections in tableView.
     */
    func numberOfSections(in tableView: UITableView) -> Int {
        return aboutSectionData.count
    }
	
    /**
     * Tells the delegate a row is selected.
     * - Parameter tableView: A table view informing the delegate about the new row selection.
     * - Parameter  indexPath: An index path locating the new selected row in tableView.
     */
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if(indexPath.row > 0 && !aboutRowData[indexPath.section][indexPath.row-1].link.isEmpty) {
            if(aboutRowData[indexPath.section][indexPath.row-1].type == TYPE_LINK_OUTSIDE_APP ) {
                if let url = URL(string: aboutRowData[indexPath.section][indexPath.row-1].link) {
                    UIApplication.shared.open(url)
                }
            }
            else if(aboutRowData[indexPath.section][indexPath.row-1].type == TYPE_LINK_INSIDE_APP ) {
                let webViewController = storyboard!.instantiateViewController(withIdentifier: "WebViewController") as!WebViewController
                webViewController.modalPresentationStyle = .fullScreen
                self.present(webViewController, animated: true,completion: nil)
                webViewController.loadWebUrl(webURL: aboutRowData[indexPath.section][indexPath.row-1].link)
            }
            else if(aboutRowData[indexPath.section][indexPath.row-1].type == TYPE_FILE_TEXT ) {
                let licenseAgreementViewController = storyboard!.instantiateViewController(withIdentifier: "LicenseAgreementViewController") as!LicenseAgreementViewController
                licenseAgreementViewController.isFirstTimeStart = false
                licenseAgreementViewController.fileName = aboutRowData[indexPath.section][indexPath.row-1].link
                licenseAgreementViewController.modalPresentationStyle = .fullScreen
                self.present(licenseAgreementViewController, animated: true,completion: nil)
                licenseAgreementViewController.navigationViewItems.title = aboutRowData[indexPath.section][indexPath.row-1].name
                
            }           
        }
    }  
}
