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
 * Helper class providing method to make HTTP requests and receive response
 */
class HTTPRequestHelper {

    /**
     * Method to make HTTP request and receive response
     * - Parameter urlString: HTTP request URL
     * - Parameter requestType: HTTP request type (POST, GET,...)
     * - Parameter onSuccess: Callback event to handle successful response
	 * - Parameter onFailure: Callback event to handle failure response
     */
    static func request<T>(urlString: String, requestType: RequestType, param: T, onSuccess: @escaping (Int, Data?)  -> Void, onFailure: @escaping (Int, String)  -> Void) where T : Encodable {
        
        let url = URL(string: urlString.trimmingCharacters(in: .whitespaces))
        guard let requestUrl = url else {
            onFailure(400,"\(ERR_MSG_VER_URL) \(String(describing: urlString))")
            return
        }
		
        var request = URLRequest(url: requestUrl)		
        request.httpMethod = requestType.rawValue
		
		// JSON request body 
        var requestDataJson:Data?
        do{
            requestDataJson =  try JSONEncoder().encode(param)        
        }catch let jsonErr{
            print(jsonErr)
        }
        request.httpBody = requestDataJson

        let task = URLSession.shared.dataTask(with: request) { (data, response, error) in
           
            if let httpResponse = response as? HTTPURLResponse {
               if(httpResponse.statusCode >= 200 && httpResponse.statusCode <= 299) {
                    onSuccess(httpResponse.statusCode,data)
               } else {
                   switch(httpResponse.statusCode) {
                        case 500:
                            let detail = String(data: data!, encoding: String.Encoding.utf8) as String?
                            if (detail?.lowercased().contains("limit exceeded") == true) {
                                onFailure(httpResponse.statusCode,ERR_MSG_VER_LIMIT_EXCEEDED)
                                break
                            }
                            onFailure(httpResponse.statusCode,ERR_MSG_REST_RESPONSE_500)
                            break
                        case 403:
                           onFailure(httpResponse.statusCode,ERR_MSG_REST_RESPONSE_403)
                           break
                       default:
                           do {
                               let response = try JSONDecoder().decode(APIError.self, from: data!)
                               onFailure(httpResponse.statusCode,response.message!)
                           } catch {
                               onFailure(httpResponse.statusCode,httpResponse.description)
                           }
                   }                    
                }
            }
            else if error != nil {
                onFailure(400, error!.localizedDescription)
            }
        }
        task.resume()
    }   
}
