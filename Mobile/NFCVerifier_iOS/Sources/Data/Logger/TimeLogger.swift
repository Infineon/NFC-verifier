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

/**
 * Helper class to calculate the time taken for an operation
 */
class TimeLogger  {
    var isPrintTime = Utils.isInDebugMode

    var startTime = NSDate()

    // Singleton instance
    let logger = LoggerFactory.getLogger(logger: .File)
    
    /**
     * Method to set the start time
     */
    public func start(){
        startTime = NSDate()
    }
	
    /**
     * Method to return the time taken from the start time
     * - Returns the time taken as string
     */
    public func get() -> String{
        let currentTime = NSDate()
        let executionTime = currentTime.timeIntervalSince(startTime as Date) * 1000
        return String( format:"%.2f ms" ,executionTime)
    }
    
    /**
     * Method to log the time information with a tag
     * - Parameter tag: Keyword in which the log to be tagged
     */
    public func log(tag:String) {
        if isPrintTime {
            let currentTime = NSDate()
            let executionTime = currentTime.timeIntervalSince(startTime as Date) * 1000
            logger.log(tag: "\(tag)", value: executionTime)
        }
    }
}
