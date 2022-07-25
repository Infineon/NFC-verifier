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
extension HelpViewController : UITableViewDataSource, UITableViewDelegate {
    
    /**
     * Asks the data source to return the number of sections in the table view.
     * - Parameter tableView:The table-view object requesting this information.
     * - Parameter section:An index number identifying a section in tableView.
     */
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return helpRowData[section].count+1
    }
    
    /**
     * Asks the data source for a cell to insert in a particular location of the table view.
     *  - Parameter tableView:A table-view object requesting the cell.
     *  - Parameter indexPath: An index path locating a row in tableView.
     */
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {        
        if(indexPath.row == 0) {  // return Header cell
            let cell =  tableView.dequeueReusableCell(withIdentifier: "HeaderTableViewCell", for: indexPath) as? HeaderTableViewCell
            cell?.label.text =  helpSectionData[indexPath.section]
        
            return cell!
        } else {  // return normal cell
            let cell =  tableView.dequeueReusableCell(withIdentifier: "AboutTableViewCell", for: indexPath) as? AboutTableViewCell
            cell?.lblLink.text = helpRowData[indexPath.section][indexPath.row-1].name
            cell?.lblLink.highlightedTextColor = UIColor(named: "Ocean")
            cell?.imgage.image = UIImage(named: "link-solid")?.withTintColor(UIColor(named: "Engineering")!)
            cell?.imgage.highlightedImage = UIImage(named: "link-solid")?.withTintColor(UIColor(named: "Ocean")!)
            return cell!
        }
    }
    
    /**
     * Asks the data source to return the number of sections in the table view.
     * - Parameter tableView: An object representing the table view requesting this information.
     * - Returns The number of sections in tableView.
     */
    func numberOfSections(in tableView: UITableView) -> Int {
        return helpSectionData.count
    }
	
    /**
     * Asks the delegate for the height to use for a row in a specified location.
     * - Parameter tableView:The table view requesting this information.
     * - Parameter indexPath:An index path that locates a row in tableView.
     * - Returns A nonnegative floating-point value that specifies the height (in points) that row should be.
     */
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if(indexPath.row == 0 ) {
            return HelpViewController.HEADER_HEIGHT
        } else {
            return HelpViewController.ROW_HEIGHT
        }
    }
	
    /**
     * Tells the delegate a row is selected.
     * - Parameter tableView: A table view informing the delegate about the new row selection.
     * - Parameter  indexPath: An index path locating the new selected row in tableView.
     */
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if(indexPath.row > 0 ) {
            if(indexPath.section == 0  && !helpRowData[indexPath.section][indexPath.row-1].link.isEmpty ) {
                let webViewController = storyboard!.instantiateViewController(withIdentifier: "WebViewController") as!WebViewController
                webViewController.modalPresentationStyle = .fullScreen
                self.present(webViewController, animated: true,completion: nil)
                webViewController.navigationItems.title = helpRowData[indexPath.section][indexPath.row-1].name
                webViewController.loadLocalData(filename: helpRowData[indexPath.section][indexPath.row-1].link)
            }
        }
    }  
}
