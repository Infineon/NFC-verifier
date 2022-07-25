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
 * Extension for package versioning
 */
extension Bundle {
    /**
     * Represents the short version of application
     */
    var shortVersion: String {
        
        if let result = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String {
            return result
        } else {
            return ""
        }
    }
    
    /**
     * Represents the build version of application
     */
    var buildVersion: String {
        if let result = Bundle.main.infoDictionary?["CFBundleVersion"] as? String {
            return result
        } else {
            return ""
        }
    }
	
    /**
     * Represents the full version (short version + build version) of application
     */
    var fullVersion: String {
        return "\(shortVersion)(\(buildVersion))"
    }
}