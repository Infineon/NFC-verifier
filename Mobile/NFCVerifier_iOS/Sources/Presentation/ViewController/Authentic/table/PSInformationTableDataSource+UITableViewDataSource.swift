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
extension AppFileInfoViewController : UITableViewDataSource, UITableViewDelegate {
    
    /**
     * Asks the data source to return the number of sections in the table view.
     * - Parameter tableView: An object representing the table view requesting this information.
     * - Returns The number of sections in tableView.
     */
    func numberOfSections(in tableView: UITableView) -> Int {
        if(informationDataHeaderList == nil ){
            return 0
        }
        return informationDataHeaderList!.count
    }
	
    /**
     * Tells the data source to return the number of rows in a given section of a table view.
     * - Parameter  tableView: The table-view object requesting this information.
     * - Parameter section: An index number identifying a section in tableView.
     */
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return informationDataHeaderList![section].informationList!.count+1
    }
    
    /**
     * Asks the data source for a cell to insert in a particular location of the table view.
     *  - Parameter tableView:A table-view object requesting the cell.
     *  - Parameter indexPath: An index path locating a row in tableView.
     */
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
       if(indexPath.row == 0 ) {
        let cell =  tableView.dequeueReusableCell(withIdentifier: "SectionTitleViewCell", for: indexPath) as? SectionTitleViewCell
           cell?.title.text = informationDataHeaderList![indexPath.section].header!
            return cell!
       } else {
           
            let cell =  tableView.dequeueReusableCell(withIdentifier: "PropertyTableViewCell", for: indexPath) as? PropertyTableViewCell
           let name = informationDataHeaderList![indexPath.section].informationList![indexPath.row-1].title
            cell?.propertyName.text = name
           let value = informationDataHeaderList![indexPath.section].informationList![indexPath.row-1].value
           cell?.propertyValue.text = value
            return cell!
       }
    }
	
    /**
     * Tells the delegate a row is selected.
     * - Parameter tableView: A table view informing the delegate about the new row selection.
     * - Parameter  indexPath: An index path locating the new selected row in tableView.
     */
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if indexPath.row == 0 {
            return
        }
        let value = informationDataHeaderList![indexPath.section].informationList![indexPath.row-1].value
        if(value!.count>30) {
               let popUpViewController:PopUpViewController = PopUpViewController(nibName: "PopUpViewController", bundle: nil)
            let name = informationDataHeaderList![indexPath.section].informationList![indexPath.row-1].title
                      
            popUpViewController.modalPresentationStyle = .overCurrentContext
            popUpViewController.modalTransitionStyle = UIModalTransitionStyle.crossDissolve
               self.present(popUpViewController, animated: true,completion: nil)
            popUpViewController.lblTitle.text = name
            popUpViewController.lblValue.text = value! + ""
        }
    }
}
