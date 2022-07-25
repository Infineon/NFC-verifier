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
import CoreNFC

/**
 * Helper class for handling the NFC reader session
 */
class NFCHelper:NSObject, NFCTagReaderSessionDelegate {
    
    var showError = true
    var callback: NFCListener?
    var nfcChannel:NFCChannel?
    var nfcSession:NFCTagReaderSession?=nil
    
    /**
     * Starts the NFC tag reader session
     * - Parameter delegate: Handler of the NFC tag reader session delegate
     * - Parameter callback: Callback listener to handle NFC events
     */
    func startReading(delegate: NFCTagReaderSessionDelegate, callback: NFCListener){
        self.callback = callback
        nfcSession = NFCTagReaderSession.init(pollingOption: NFCTagReaderSession.PollingOption.iso14443, delegate: delegate)        
        nfcSession?.alertMessage = MSG_HOLD_TAG
        nfcSession?.begin()
    }
	
    /**
     * Notifies the delegate the reason for invalidating a reader session.
     *  - Parameter session:  The session that has become invalid. Your app should discard any references it has to this session.
     *  - Parameter error: The error indicating the reason for invalidation of the session.
     */
    func tagReaderSession(_ session: NFCTagReaderSession, didInvalidateWithError errorMessage: Error) {  
        if(self.showError) {           
            // Check the invalidation reason from the returned error.
            if let readerError = errorMessage as? NFCReaderError {
                // Show an alert when the invalidation reason is not because of a success read
                // during a single tag read mode, or user cancelled a multi-tag read mode session
                // from the UI or programmatically using the invalidate method call.
                if (readerError.code != .readerSessionInvalidationErrorFirstNDEFTagRead)
                    && (readerError.code != .readerSessionInvalidationErrorUserCanceled) {
                    // Indicate the tag operation event that it action failed
                    self.callback?.onNFCCommunicationError(callbackType: .Warning, errorMessage: errorMessage.localizedDescription)
                }
            }
        }
    }
    
    /**
     * Tells the delegate that the reader session is active.
     * - Parameter session: The active reader session. Only one session can be active at a time.
     */
    func tagReaderSessionDidBecomeActive(_ session: NFCTagReaderSession)
    {
    }
	
    /**
     * Tells the delegate that the session detected NFC tags.
     * - Parameter session: session The session that detected the tags.
     * - Parameter tags: An array of NFC tags detected by the session.
     */
    func tagReaderSession(_ session: NFCTagReaderSession, didDetect tags: [NFCTag]) {
        startCommunication(session, didDetect: tags)        
    }
	
    /**
     * Handles the the error message from the NFC session
     * - Parameter error: Error message describing failure
     */
    fileprivate func handleErrorMessage(_ error: Error) {
        if(error.localizedDescription.contains("Missing required entitlement") ) {
            self.callback?.onNFCCommunicationError(callbackType: .Warning, errorMessage: "Unsupported tag")
        } else {
            self.callback?.onNFCCommunicationError(callbackType: .Warning, errorMessage: "Tag connection lost")
        }
    }
    
    /**
     * Method to handle the tag presented event and start communication with the tag
     * - Parameter session: Ongoing NFC tag reader session
     * - Parameter tags: Handle of the tags detected
     */
    func startCommunication(_ session: NFCTagReaderSession, didDetect tags: [NFCTag]){
        if tags.count > 1 {
            self.callback?.onNFCCommunicationError(callbackType: .Warning, errorMessage: "Multiple tags found")
            return
        }
        if case let NFCTag.iso7816(tag) = tags.first! {
            session.connect(to: tags.first!) { [self] (error: Error?) in
                if let error = error {
                    self.showError = false
                    self.handleErrorMessage(error)
                    return
                }
                self.nfcSession?.alertMessage = MSG_READING
                self.nfcChannel = NFCChannel(tagISO7816: tag, readerSession: session)
				
				// Perform brand verification operation
                BrandVerificationManager.performBrandVerification(nfcChannel: self.nfcChannel, nfcEventCallback: self.callback)
            }
        }
    }    
}
