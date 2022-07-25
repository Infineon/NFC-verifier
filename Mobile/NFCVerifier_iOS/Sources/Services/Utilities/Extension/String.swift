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
 * Extension for the string to convert to byte array
 */
extension String {

	/**
	 * Converts the hex encoded string to a byte array
	 */
    func hexDecodedData() -> Data {
		// Get the UTF8 characters of this string
		let chars = Array(utf8)
	
		// Keep the bytes in an UInt8 array and later convert it to Data
		var bytes = [UInt8]()
		bytes.reserveCapacity(count / 2)
	
		// It is a lot faster to use a lookup map instead of strtoul
		let map: [UInt8] = [
			0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, // 01234567
			0x08, 0x09, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 89:;<=>?
			0x00, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x00, // @ABCDEFG
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 // HIJKLMNO
		]
	
		// Grab two characters at a time, map them and turn it into a byte
		for i in stride(from: 0, to: count, by: 2) {
			let index1 = Int(chars[i] & 0x1F ^ 0x10)
			let index2 = Int(chars[i + 1] & 0x1F ^ 0x10)
			bytes.append(map[index1] << 4 | map[index2])
		}
	
		return Data(bytes)
    }
}
