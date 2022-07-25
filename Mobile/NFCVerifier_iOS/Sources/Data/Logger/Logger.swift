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
 * Generic logger protocol class contains the logger methods for different data types
 */
protocol Logger {

    /**
     * Method to print a message string
	 *  - Parameter tag: Keyword in which the log to be tagged
	 *  - Parameter value: Message string to be logged
     */
    func log(tag:String, value:String)
	
    /**
     * Method to print an integer value
	 *  - Parameter tag: Keyword in which the log to be tagged
	 *  - Parameter value: Integer value to be logged
     */
    func log(tag:String, value:Int)

    /**
     * Method to print a float value
	 *  - Parameter tag: Keyword in which the log to be tagged
	 *  - Parameter value: Float value to be logged
     */
    func log(tag:String, value:Float)

    /**
     * Method to print a double value
	 *  - Parameter tag: Keyword in which the log to be tagged
	 *  - Parameter value: Double value to be logged
     */
    func log(tag:String, value:Double)
}
