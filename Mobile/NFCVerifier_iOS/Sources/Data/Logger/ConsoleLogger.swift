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
 * Logger class to log the messages into the console of the IDE
 */
class ConsoleLogger : Logger {
    /**
     * Prefix
     */
    private let TAG = "NFCBPSK"
    /**
     * Singletone Logger instance
     */
    static let logger : Logger = ConsoleLogger()

	/*
	 * Initializes the class
	 */	
    private init(){
        
    }
	
    /**
     * Method to print an integer value
	 *  - Parameter tag: Keyword in which the log to be tagged
	 *  - Parameter value: Integer value to be logged
     */
    func log(tag: String, value: Int) {
        print("\(TAG) \(tag) : \(value)")
        
    }
	
    /**
     * Method to print a float value
	 *  - Parameter tag: Keyword in which the log to be tagged
	 *  - Parameter value: Float value to be logged
     */
    func log(tag: String, value: Float) {
        print("\(TAG) \(tag) : \(String(format: "%.2f", value))")
    }
	
    /**
     * Method to print a double value
	 *  - Parameter tag: Keyword in which the log to be tagged
	 *  - Parameter value: Double value to be logged
     */
    func log(tag: String, value: Double) {
        print("\(TAG) \(tag) : \(String(format: "%.2f", value))")
    }
	
    /**
     * Method to print a message
	 *  - Parameter tag: Keyword in which the log to be tagged
	 *  - Parameter value: String message to be logged
     */
    func log(tag:String, value:String) {
        print("\(TAG) \(tag) : \(value)")
    }   
}
