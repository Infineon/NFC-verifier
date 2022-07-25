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
 * Visual representation of a single header row in the about page table view
 */
class HeaderTableViewCell: UITableViewCell {

	// Outlet of the label to be displayed
    @IBOutlet weak var label : UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
	
    /**
     * Select or click event of the table view cell
     */
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }   
}
