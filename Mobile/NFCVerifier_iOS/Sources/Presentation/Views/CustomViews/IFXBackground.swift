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

import UIKit

/**
 * Background UI element with IFX theme. Can be used for page backgrounds. 
 */
class IFXBackground: UIView {
    
    var path:UIBezierPath!
	
    override init(frame: CGRect) {
        super.init(frame: frame)       
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    /**
     * Loads the sub views 
     */
    override func layoutSubviews() {
        super.layoutSubviews()
    }
	
    /**
     * Draws custom shapes in the UI
     */
    override func draw(_ rect: CGRect) {
        self.createShape()
    }
    
    /**
     *  Method creates a custom shape that is used in the bottom part of the screen 
     */
    func createShape(){
        path = UIBezierPath()
        path.move(to: CGPoint(x:0.0 , y:0.0))
        path.addLine(to: CGPoint(x:0.0 , y:self.frame.size.height))
        let slop = (95/100) * self.frame.size.height
        path.addLine(to: CGPoint(x:self.frame.size.width , y:slop ))
        path.addLine(to: CGPoint(x:self.frame.size.width , y:0.0))
        path.close()
        UIColor.init(named: "Ocean")!.setFill()
        path.fill()
    }
}
