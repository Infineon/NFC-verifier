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

/*
 * Contains the about page and help page links
 */

let TYPE_LINK_OUTSIDE_APP = "TYPE_LINK_OUTSIDE_APP"
let TYPE_LINK_INSIDE_APP = "TYPE_LINK_INSIDE_APP"
let TYPE_FILE_TEXT = "TYPE_FILE_TEXT"
let TYPE_FILE_HTML = "TYPE_FILE_HTML"

var aboutSectionData  =  ["About", "Licenses"]
var aboutRowData = [
    [TableModel(name: "Sample Link 1", link: "", type:TYPE_LINK_INSIDE_APP),
     TableModel(name: "Sample Link 2", link: "", type:TYPE_LINK_INSIDE_APP),
     TableModel(name: "Sample Link 3", link: "", type:TYPE_LINK_INSIDE_APP),
     TableModel(name: "Sample Link 4", link: "", type:TYPE_LINK_INSIDE_APP),
     ],
    [TableModel(name: "Sample Link 1", link: "EndUserLicense", type:TYPE_FILE_TEXT),
     TableModel(name: "Sample Link 2", link: "ThirdPartyLicense", type:TYPE_FILE_TEXT)]
   ]

var helpSectionData  =  ["Sample Links"]
var helpRowData = [
    [TableModel(name: "Sample Link 1", link: "", type:TYPE_FILE_HTML),
     TableModel(name: "Sample Link 2", link: "", type:TYPE_FILE_HTML),
     TableModel(name: "Sample Link 3", link: "", type:TYPE_FILE_HTML),
     TableModel(name: "Sample Link 4", link: "", type:TYPE_FILE_HTML)]
  ]
