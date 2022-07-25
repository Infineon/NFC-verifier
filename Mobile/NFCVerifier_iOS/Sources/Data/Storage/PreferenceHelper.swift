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
 * Helper class to store and retrieve preferences in iOS
 */
public class PreferenceHelper {
    
    let TAG_AUTO_OPEN_BUTTON =  "TAG_AUTO_OPEN_BUTTON"
    let TAG_AUTO_OPEN_TIME =  "TAG_AUTO_OPEN_TIME"
    let IS_EUAL_ACCEPTED =  "EUAL"
    let EULA_VERSION = "EULA_VERSION"
    let ACCEPTED = 1
    let NOT_ACCEPTED = 0
	
    /**
     * Stores integer preferences
     * - Parameter key: Key identifier under which the value to be stored
     * - Parameter value: Integer value to be stored
     */
    func setIntPref(key: String, value: Int) {
        UserDefaults.standard.set(value, forKey: key);
    }

    /**
     * Stores string preferences
     * - Parameter key: Key identifier under which the value to be stored
     * - Parameter value: String to be stored
     */
    func setStringPref(key: String, value: String) {
        UserDefaults.standard.set(value, forKey: key);
    }
	
    /**
     * Returns the stored integer value based on the key
     * - Parameter key: Key identifier under which the value is stored
     * - Returns the integer value
     */
    func getIntPref(key: String) -> Int {
        return UserDefaults.standard.integer(forKey: key);
    }
    
    /**
     * Returns the stored string based on the key
     * - Parameter key: Key identifier under which the value is stored
     * - Returns the string
     */
    func getStringPref(key: String) -> String? {
        return UserDefaults.standard.string(forKey: key);
    }    
}
