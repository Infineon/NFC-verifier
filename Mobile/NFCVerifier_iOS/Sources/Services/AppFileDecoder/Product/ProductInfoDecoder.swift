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
 * Decodes the product information received from the tag
 */
class ProductInfoDecoder : AppFileDecoder {

    /**
     * Max manufacturer Length supported by A10 profile
     */
    private static let A10_MANUFACTURER_NAME_MAX_LEN:Int = 21
    /**
     * Max manufacturer Length supported by B10 profile
     */
    private static let B10_MANUFACTURER_NAME_MAX_LEN:Int = 64
    /**
     * Max manufacturer Length supported by  B20 profile
     */
    private static let B20_MANUFACTURER_NAME_MAX_LEN:Int = 64
    
	/*
	 * Decodes the product information received from the tag
	 * - Parameter payload: Product information payload as bytes
	 * - Returns the decoded product information
	 */
    static func decode(payload: Data) -> ProductInfo? {
            var index = 0 ;

            // Profile info
            let profileInfo = parseProductProfileType(payload: payload,  index: &index)
            if(profileInfo != nil  && payload.count >= calculateProductInfoSize(byte: payload)) {
                //Layout Version Info
                let layoutVersion = payload[index]
                index += 1
                //
                let modelNumber = parseModelNumber(payload: payload, index: &index)
                let manufactureDate = parseDateBytes(payload: payload, index: &index)
                let serialNumber = parseSerialNumber(payload: payload, index: &index)
                let productCode = parseProductCode(payload: payload, index: &index)
                let manufacturerName = parseManufacturerName(payload: payload, index: &index, type: profileInfo!)
                let customField = parseCustomField(payload: payload, index: &index)
                let productInfo = ProductInfo(profileInfo: profileInfo!, layoutVersion: layoutVersion, modelNumber: modelNumber, manufactureDate: manufactureDate, serialNumber: serialNumber, productCode: productCode, manufacturerName: manufacturerName, customField:customField)
                return productInfo
            }
            return nil
    }
	
    /**
     * Returns the length of the manufacturer name based on the profile type
     *  - Parameter profileType: Product profile type
     *  - Returns the length of the manufacturer name
     */
    static func getManufacturerNameLen(type :ProfileType) ->  Int{
        switch type {
			case .A10:
				return A10_MANUFACTURER_NAME_MAX_LEN
			case .B10:
				return B10_MANUFACTURER_NAME_MAX_LEN
			case .B20:
				return B20_MANUFACTURER_NAME_MAX_LEN
        }
    }
	
    /**
     * Parses the model number field
     *  - Parameter payload: Product information payload
	 *  - Parameter index: Index of the model number field
     *  - Returns the model number as string
     */
    static func parseModelNumber(payload: Data, index: inout Int) -> String {
        let range: Range<Data.Index> = index..<index+8
        index += 8
        return String(bytes: payload.subdata(in: range), encoding: .utf8)!
    }
	
    /**
     * Parses the product profile type
     *  - Parameter payload: Product information payload
	 *  - Parameter index: Index of the profile type field
     *  - Returns the decoded product profile type
     */
    static func parseProductProfileType(payload: Data, index: inout Int) -> ProfileType? {
        var  productProfileType :UInt16 = UInt16()
        if payload.count > 0 {
            productProfileType += UInt16(payload[index]) << 8
            index += 1
            productProfileType += UInt16(payload[index]) << 0
            index += 1
            
            if(productProfileType == ProfileType.A10.rawValue){
                return ProfileType.A10
            } else if(productProfileType == ProfileType.B10.rawValue){
                return ProfileType.B10
            }
            else if(productProfileType == ProfileType.B20.rawValue){
               return ProfileType.B20
           }
        }
        return nil
    }
	
    /**
     * Parses the serial number field
     *  - Parameter payload: Product information payload
	 *  - Parameter index: Index of the serial number field
     *  - Returns the serial number as string
     */
    static func parseSerialNumber(payload: Data, index: inout Int) -> String {
        let range: Range<Data.Index> = index..<index+8
        index += 8
        return String(bytes: payload.subdata(in: range), encoding: .utf8)!
        
    }
	
    /**
     * Parses the product code field
     *  - Parameter payload: Product information payload
	 *  - Parameter index: Index of the product code field
     *  - Returns the decoded product code
     */
    static func parseProductCode(payload: Data, index: inout Int) -> UInt16 {
        var  productCode :UInt16 = UInt16()
        productCode += UInt16(payload[index+1]) << 8
        productCode += UInt16(payload[index]) << 0
        index += 2
        return productCode
    }
    
	/**
     * Parses the manufacturer name field
     *  - Parameter payload: Product information payload
	 *  - Parameter index: Index of the manufacturer name field
	 *  - Parameter type: Profile type
     *  - Returns the decoded manufacturer name
     */
    static func parseManufacturerName(payload: Data, index: inout Int, type:ProfileType) -> String {
        
        let range = getManufacturerNameRange(payload:payload,type: type, index: &index)
        let data = payload.subdata(in: range)
        return String(bytes: data, encoding: .utf8)!
    }
	
	/**
     * Returns the range of the manufacturer name field in the payload byte array
     *  - Parameter payload: Product information payload
	 *  - Parameter type: Profile type
	 *  - Parameter index: Index of the manufacturer name field
     *  - Returns the range of the manufacturer name field
     */
    static func getManufacturerNameRange(payload: Data, type:ProfileType, index: inout Int) -> Range<Data.Index>  {               
        let length = UInt8(payload[index])
        
        let range: Range<Data.Index> = (index+1)..<index+Int(length+1)

        switch type {
        case .A10:
            index += ProductInfoDecoder.A10_MANUFACTURER_NAME_MAX_LEN
        case .B10:
            index += ProductInfoDecoder.B10_MANUFACTURER_NAME_MAX_LEN
        case .B20:
            index += ProductInfoDecoder.B20_MANUFACTURER_NAME_MAX_LEN
        }
        return range
    }
	
    /**
     * Calculates the length of the product info file. This method is used during 
	 * read binary operation to avoid reading excess bytes. 
     *  - Parameter byte: Partial product information payload read from the first read binary operation
     *  - Returns the length of the product information file
     */
    static func calculateProductInfoSize(byte:Data)-> Int{        
        var index = 0 ;
        let profileInfo = ProductInfoDecoder.parseProductProfileType(payload: byte, index: &index)
        if(profileInfo != nil) {
            let manufacturerNameLen =  ProductInfoDecoder.getManufacturerNameLen(type: profileInfo!)
            let customFieldOffset = 24 + manufacturerNameLen
            let custom = byte.subdata(in: customFieldOffset..<(customFieldOffset+2))
            let customFieldLen = Utils.byteToShort(data: custom)
            let length = (22+manufacturerNameLen+2+customFieldLen)
            return length
        }
        return 0
    }
}
