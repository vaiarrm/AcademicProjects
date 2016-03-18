//
//  ConfigurationViewController.swift
//  Location Alarm
//
//  Created by Vaibhav Sharma on 3/5/16.
//  Copyright © 2016 VaibhavSharma. All rights reserved.
//

import UIKit

class ConfigurationViewController: UIViewController,UIPickerViewDataSource,UIPickerViewDelegate {

    
    @IBOutlet weak var picker: UIPickerView!
    let units = ["Metric","FPS"]
    let minDistance = [500,1000,1500,2000,2500,3000,3500,4000]
    let sound = ["Vibrate","Alarm"]
    
    
    @IBAction func saveSettings(sender: UIButton) {
        let unit = units[picker.selectedRowInComponent(0)]
        if unit == "Metric"{
            unitFactor  = 1
        }else {
            unitFactor = 0.621371
        }
        minDistanceToAlertUser = Double(minDistance[picker.selectedRowInComponent(1)])
        let isAlarm = sound[picker.selectedRowInComponent(2)]
        if isAlarm == "Vibrate"{
            willVibrateOnly = true
        }else{
            willVibrateOnly = false
        }
        navigationController?.popViewControllerAnimated(true)
    }
    
    func numberOfComponentsInPickerView(pickerView: UIPickerView) -> Int {
        return 3
    }
    func pickerView(pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        if component == 0 {
            return units.count
        } else if component == 1{
            return minDistance.count
        }else{
            return sound.count
        }
    }
    func pickerView(pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if component == 0 {
            return units[row]
        } else if component == 1{
            return String(minDistance[row])
        }else{
            return sound[row]
        }
    }
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
