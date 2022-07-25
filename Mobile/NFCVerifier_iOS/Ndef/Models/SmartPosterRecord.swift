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
import SwiftUI

/**
 * The SmartPoster Record Type is 'Sp'.
 * The smart Poster is the type of the record that combines multiple record under it
 * the concept of the smart poster is built around URI that are very powerful and can represent the information from the unique identifier to EPC to web addresses to SMS message to phone calls
 * the Smart Poster record defines a structure that associated a URI with several types of Metadata such as action, URI etc.
 */
class SmartPosterRecord: AbstractRecord {
    
    private var actionRecord:ActionRecord?
    private var uriRecord:URIRecord?
    
    private var titleRecords: [TextRecord] = []
    private var otherRecords: [AbstractRecord] = []
    
    /**
     * The Constructor creates the Smart Poster Record
     */
    override init() {
        super.init()
        setRecordType(recordType: RecordType(type: "Sp"))
    }
    
    /**
     * Method to get Action Record from the Smart Poster record
     */
    public func getActionRecord()->ActionRecord? {
        return actionRecord
    }
	
    /**
     * Method set Action Record
     */
    public func setActionRecord(actionRecord:ActionRecord){
        self.actionRecord = actionRecord
    }
    
    /**
     * Method return the URI Record
     */
    public func getUriRecord()->URIRecord?{
        return uriRecord
    }
	
    /**
     * Method set URI Record
     */
    public func seturiRecord(uriRecord:URIRecord){
        self.uriRecord = uriRecord
    }  
    
    /**
     * Method set Other Record
     */
    public func setOtherRecord(otherRecords: [AbstractRecord]){
        self.otherRecords = otherRecords
    }
    
    /**
     * Method return the URI Record
     */
    public func getOtherRecord()->[AbstractRecord]?{
        return otherRecords
    }
	
    /**
     * Add the Other record
     */
    public func addOtherRecord(otherRecord:AbstractRecord){
        otherRecords.append(otherRecord)
    }
    
    /**
     *  Method return the Title Records
     */
    public func getTitleRecord() ->[AbstractRecord]?{
        
        return titleRecords
    }
    
    /**
     * Method add the title Record or Text Text Record in Smart Poster Record
     */
    public func addTitleRecord(titleRecord:TextRecord){
        var dublicate = false
        
        for textRecord:TextRecord in self.titleRecords  {
            if(textRecord.getLocale() == titleRecord.getLocale()){
                dublicate = true
                break
            }
        }
        if(!dublicate){
            self.titleRecords.append(titleRecord)
        }
    }
}
