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
 * Logger class to log the messages into a file
 */
class FileLogger : Logger {
    let dateFormatter = DateFormatter()
    
	// Singleton object initialization
    static let logger : Logger = FileLogger()
    var logDataBuffer = ""
    let fileManager = FileManager.default
    var filePath: URL?
    
	/*
	 * Initializes the class
	 */
    private init(){
        dateFormatter.dateFormat = "dd-MM-yyyy HH:mm:ss"
    }
    
    /**
     * Method to return the logger handle
	 *  - Returns the logger handle
     */
    public static func getLogger() -> Logger {        
        return logger;
    }
   
    /**
     * Method to print a byte array
	 *  - Parameter tag: Keyword in which the log to be tagged
	 *  - Parameter value: Byte array to be logged
     */
    func log(tag:String, value: Data) {
        var i = 0
        var separator = "\n"
        while(i<=tag.count){
            separator.append(" ")
            i += 1
        }
        
        writeFile(data: "\(tag) \(value.hexEncodedString().uppercased().unfoldSubSequences(limitedTo: 24).joined(separator:separator ))")
    }
	
    /**
     * Method to print an integer value
	 *  - Parameter tag: Keyword in which the log to be tagged
	 *  - Parameter value: Integer value to be logged
     */
    func log(tag: String, value: Int) {
        writeFile(data: "\(tag) \(value)")
    }
	
    /**
     * Method to print a float value
	 *  - Parameter tag: Keyword in which the log to be tagged
	 *  - Parameter value: Float value to be logged
     */
    func log(tag: String, value: Float) {
        writeFile(data: "\(tag) \(value)")
    }
	
    /**
     * Method to print a double value
	 *  - Parameter tag: Keyword in which the log to be tagged
	 *  - Parameter value: Double value to be logged
     */
    func log(tag: String, value: Double) {
        writeFile(data: "\(tag) \(value)")
    }
	
    /**
     * Method to print a message
	 *  - Parameter tag: Keyword in which the log to be tagged
	 *  - Parameter value: String message to be logged
     */
    func log(tag:String, value:String) {
        writeFile(data: "\(tag) \(value)")
       
    }
	
	/**
     * Method to generate the header for the log
	 *  - Returns a formatted date string to be used as header
     */
    private func getHeaderText() -> Data {       
        let date = Date()
        return "\(dateFormatter.string(from: date)) #".data(using: .utf8)!
    }
    
	/**
     * Method to create the log file
     */
    private func createFile() {
        guard  let url = fileManager.urls(for: .documentDirectory, in: .userDomainMask).first else {
            return
        }
        do {
            let urlFolder = url.appendingPathComponent("nfcbp")
            try fileManager.createDirectory(at: urlFolder, withIntermediateDirectories: true, attributes: [:])
            filePath = urlFolder.appendingPathComponent("log.txt")
            if(!fileManager.fileExists(atPath: filePath!.path)) {
                fileManager.createFile(atPath: filePath!.path, contents: getHeaderText(), attributes: [ .creationDate:Date() ])
            }    
        } catch {
        }
    }
	
	/**
     * Method to write string data into the file
	 *  - Parameter data: Message string to be logged
     */
    private func writeFile(data:String)  {
        logDataBuffer +=  "\n\(data)"
    }
    
	/**
     * Method to reset the log by clearing the buffer
     */
    func resetLog() {
            let date = Date()
            logDataBuffer = "\(dateFormatter.string(from: date)) #"
    }
    
	/**
     * Method to commit the logs to the file
     */
    public func commitFile()  {
        do {
            createFile()
            print(logDataBuffer)
            try logDataBuffer.write(to: filePath!, atomically: true, encoding: .utf8)
        } catch {           
        }        
    }    
}

