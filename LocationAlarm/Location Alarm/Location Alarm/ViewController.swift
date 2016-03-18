//
//  ViewController.swift
//  Location Alarm
//
//  Created by Vaibhav Sharma on 3/5/16.
//  Copyright © 2016 VaibhavSharma. All rights reserved.
//

import UIKit
import MapKit
import CoreLocation


// Varaibles For History
var coordinateHistory = [CLLocation]()
var addressHistory = [String]()
var coordToDict = Dictionary<CLLocation,String>()

// Configuration Varibles
var minDistanceToAlertUser: Double = 1000
var willVibrateOnly: Bool = true
var unitFactor:Double = 1

// Other variables shared
var locationCoordinate:CLLocationCoordinate2D = CLLocationCoordinate2DMake(0, 0)
var fromHistory = false
var index = -1


class ViewController: UIViewController, CLLocationManagerDelegate, MKMapViewDelegate {
    
    // IBOutlets
    @IBOutlet weak var addressInputString: UITextField!
    @IBOutlet var map: MKMapView!
    @IBOutlet var textCollection: [UITextField]!
    
    // Instance Varaibles
    var locationManager = CLLocationManager()
    let destinationAnnotation = MKPointAnnotation()
    let currentAnnotation = MKPointAnnotation()
    
    // IBActions
    
    @IBAction func textEditEndedScreenTap(sender: UIControl) {
        /*
        To bring keyboard down
        */
        for tf in textCollection{
            tf.resignFirstResponder()
        }
        addressInputString.resignFirstResponder()
    }
    
    @IBAction func textEditEnded(sender: UITextField) {
        /*
        To bring keyboard down
        */
        sender.resignFirstResponder()
    }
    
    @IBAction func addressSearchAction(sender: UIButton) {
        /*
        This method will search location information based on the text input from the user.
        Method uses CLGeocoder to convert address string to co-ordinate informaiton.
        Currently App only uses first location information. This is the reason it requires users to provide correct address
        */
        
        
        locationManager.stopUpdatingLocation()
        addressInputString.resignFirstResponder()
        if let address = addressInputString.text {
            let geoCoder1 = CLGeocoder()
            dispatch_after(0,dispatch_get_main_queue(), { () -> Void in
                geoCoder1.geocodeAddressString(address, completionHandler:{(placemarks, error) -> Void in
                    if((error) != nil){
                        // Check for incorrect input
                        let title = "Incorrect Input"
                        let message = "Error in Processing Address Input"
                        let actionSheetController = UIAlertController(title: title, message: message, preferredStyle: .Alert)
                        let confirmAction = UIAlertAction(title: "Okay", style:.Default, handler: nil)
                        actionSheetController.addAction(confirmAction)
                        self.presentViewController(actionSheetController, animated: true, completion: nil)
                    }
                    if let placemark = placemarks?.first { // Taking the first value from the arrays returned
                        locationCoordinate = placemark.location!.coordinate
                        let location = CLLocation(latitude: locationCoordinate.latitude, longitude: locationCoordinate.longitude)
                        self.createMapView(forLocation: location)
                        self.createAnnotationView(forLocation: location)
                        self.addRadiusCircle(location)
                        if let _ = coordToDict[location]{
                            // Already in History
                            // Do Nothing
                        }else{
                            // Saving History
                            coordToDict[location] = address
                            addressHistory.append(address)
                            coordinateHistory.append(location)
                            NSUserDefaults.standardUserDefaults().setObject(addressHistory, forKey: "addressHistory")
                            let coordinateHistoryData = NSKeyedArchiver.archivedDataWithRootObject(coordinateHistory)
                            NSUserDefaults.standardUserDefaults().setObject(coordinateHistoryData, forKey:"coordinateHistory")
                            let coordToDictData = NSKeyedArchiver.archivedDataWithRootObject(coordToDict)
                            NSUserDefaults.standardUserDefaults().setObject(coordToDictData, forKey: "coordToDict")
                            
                        }
                    }
                })
            })
        }else{
            let title = "Incorrect Input"
            let message = "Error in Address Input"
            let actionSheetController = UIAlertController(title: title, message: message, preferredStyle: .Alert)
            let confirmAction = UIAlertAction(title: "Okay", style:.Default, handler: nil)
            actionSheetController.addAction(confirmAction)
            presentViewController(actionSheetController, animated: true, completion: nil)
        }
    }
    
    
    //Other Methods Defined
    
    func longPressOnMapAction(gestureRecognizer: UIGestureRecognizer){
        /*
        This mehtod is used to obtain location information using long press on the map
        */
        locationManager.stopUpdatingLocation()
        if gestureRecognizer.state == UIGestureRecognizerState.Began{
            let pointPressedOnMap = gestureRecognizer.locationInView(self.map)
            let newPoint:CLLocationCoordinate2D = map.convertPoint(pointPressedOnMap, toCoordinateFromView: self.map)
            let location = CLLocation(latitude: newPoint.latitude, longitude: newPoint.longitude)
            locationCoordinate = newPoint
            CLGeocoder().reverseGeocodeLocation(location) { (placemarks, error) -> Void in
                //var title = ""
                if error == nil {
                    let p = placemarks![0]
                    var subThoroughfare: String = ""
                    var thoroughfare: String = ""
                    
                    if p.thoroughfare != nil{
                        thoroughfare = p.thoroughfare!
                    }
                    if p.subThoroughfare != nil{
                        subThoroughfare = p.subThoroughfare!
                        
                    }
                    self.addressInputString.text = "\(subThoroughfare) \(thoroughfare)"
                    
                    if let _ = coordToDict[location]{
                        
                    }else{
                        coordToDict[location] = "\(subThoroughfare) \(thoroughfare)"
                        addressHistory.append("\(subThoroughfare) \(thoroughfare)")
                        coordinateHistory.append(location)
                        NSUserDefaults.standardUserDefaults().setObject(addressHistory, forKey: "addressHistory")
                        let coordinateHistoryData = NSKeyedArchiver.archivedDataWithRootObject(coordinateHistory)
                        NSUserDefaults.standardUserDefaults().setObject(coordinateHistoryData, forKey:"coordinateHistory")
                        let coordToDictData = NSKeyedArchiver.archivedDataWithRootObject(coordToDict)
                        NSUserDefaults.standardUserDefaults().setObject(coordToDictData, forKey: "coordToDict")
                        
                    }
                    self.createMapView(forLocation: location)
                    self.createAnnotationView(forLocation: location)
                    self.addRadiusCircle(location)
                }
            }
        }
    }
    func alarmFromHistory(){
        /*
        Build location information from historical search results
        */
        locationManager.stopUpdatingLocation()
        if index == -1{
            return
        }
        let  location = coordinateHistory[index]
        let addressString = addressHistory[index]
        locationCoordinate = CLLocationCoordinate2DMake(location.coordinate.latitude,location.coordinate.longitude)
        createMapView(forLocation: location)
        createAnnotationView(forLocation: location)
        self.addRadiusCircle(location)
        self.addressInputString.text = addressString
        index = -1
        fromHistory = false
    }
    
    
    func createAnnotationView(forLocation location:CLLocation){
        
        // For creating Annotaion view on Map
        
        let locationCoordinate2D = CLLocationCoordinate2DMake(location.coordinate.latitude,location.coordinate.longitude)
        let annotation = MKPointAnnotation()
        annotation.coordinate = locationCoordinate2D
        self.map.addAnnotation(annotation)
    }
    
    func createMapView(forLocation location:CLLocation){
        
        // To Show Map View
        
        
        map.removeAnnotations(map.annotations)
        map.removeOverlays(map.overlays)
        
        let deltaLatidude: CLLocationDegrees = 0.05
        let deltaLongitude: CLLocationDegrees = 0.05
        let span: MKCoordinateSpan = MKCoordinateSpanMake(deltaLatidude, deltaLongitude)
        let locationCoordinate2D = CLLocationCoordinate2DMake(location.coordinate.latitude,location.coordinate.longitude)
        let region: MKCoordinateRegion = MKCoordinateRegionMake(locationCoordinate2D, span)
        
        self.map.setRegion(region, animated: true)
    }
    
    func addRadiusCircle(location: CLLocation){
        
        // To add overlay on Map
        
        let circle = MKCircle(centerCoordinate: location.coordinate, radius: (minDistanceToAlertUser * unitFactor) as CLLocationDistance)
        self.map.addOverlay(circle)
    }
    
    
    // Inherited Methods
    
    func mapView(mapView: MKMapView, rendererForOverlay overlay: MKOverlay) -> MKOverlayRenderer {
        let circle = MKCircleRenderer(overlay: overlay)
        circle.strokeColor = UIColor.redColor()
        circle.fillColor = UIColor(red: 100, green: 0, blue: 0, alpha: 0.05)
        circle.lineWidth = 1
        return circle
    }
    
    func locationManager(manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        if let currentLocation: CLLocation = locations.first{
            createMapView(forLocation: currentLocation)
            createAnnotationView(forLocation: currentLocation)
            locationManager.stopUpdatingLocation()
        }
    }
    override func viewWillAppear(animated: Bool) {
        self.map.delegate = self
        addressInputString.text = ""
        locationCoordinate = CLLocationCoordinate2DMake(0, 0)
        
        map.removeAnnotations(map.annotations)
        map.removeOverlays(map.overlays)
        
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.requestAlwaysAuthorization()
        //locationManager.requestWhenInUseAuthorization()
        locationManager.startUpdatingLocation()
        let ulpgr = UILongPressGestureRecognizer(target: self, action: "longPressOnMapAction:")
        ulpgr.minimumPressDuration = 2
        map.addGestureRecognizer(ulpgr)
        
        if fromHistory{
            alarmFromHistory()
        }
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        if NSUserDefaults.standardUserDefaults().objectForKey("coordinateHistory") != nil{
            coordinateHistory = NSKeyedUnarchiver.unarchiveObjectWithData(NSUserDefaults.standardUserDefaults().objectForKey("coordinateHistory")! as! NSData) as! [CLLocation]
        }
        
        if NSUserDefaults.standardUserDefaults().objectForKey("addressHistory") != nil{
            addressHistory = NSUserDefaults.standardUserDefaults().objectForKey("addressHistory")! as! [String]
        }
        
        if NSUserDefaults.standardUserDefaults().objectForKey("coordToDict") != nil{
            coordToDict = NSKeyedUnarchiver.unarchiveObjectWithData(NSUserDefaults.standardUserDefaults().objectForKey("coordToDict")! as! NSData) as! Dictionary<CLLocation,String>
        }
        
        
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
    
    override func shouldPerformSegueWithIdentifier(identifier: String, sender: AnyObject?) -> Bool {
        if identifier == "setAlarm"{
            let zero = CLLocationCoordinate2DMake(0, 0)
            if locationCoordinate.latitude == zero.latitude && locationCoordinate.longitude == zero.longitude  {
                let title = "Incorrect Destination"
                let message = "Will Face Difficulty To Wake You Up"
                let actionSheetController = UIAlertController(title: title, message: message, preferredStyle: .Alert)
                let confirmAction = UIAlertAction(title: "Okay", style: .Default, handler: nil)
                actionSheetController.addAction(confirmAction)
                presentViewController(actionSheetController, animated: true, completion: nil)
                return false;
            }else{
                return true
            }
        }
        print(identifier)
        return true
    }
    
    
    
}

