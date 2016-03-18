//
//  AlarmStatusViewController.swift
//  Location Alarm
//
//  Created by Vaibhav Sharma on 3/5/16.
//  Copyright © 2016 VaibhavSharma. All rights reserved.
//

import UIKit
import CoreLocation
import MapKit
import AVFoundation

class AlarmStatusViewController: UIViewController,CLLocationManagerDelegate,MKMapViewDelegate {
    
    //IBOutlet
    @IBOutlet var map: MKMapView!
    
    // Instance Variable
    var timer = NSTimer()
    var locationManager = CLLocationManager()
    var calledOnce =  true
    var player: AVAudioPlayer = AVAudioPlayer()
    let destinationAnnotation = MKPointAnnotation()
    let currentAnnotation = MKPointAnnotation()
    
    @IBAction func GoBackButtonAction(sender: UIButton) {
        timer.invalidate()
        navigationController?.popViewControllerAnimated(true)
    }
    
    // Other Methods
    func calculateDistance(between start:CLLocation,and end:CLLocation) -> Double{
        //print(start.distanceFromLocation(end))
        //print(willVibrateOnly)
        return start.distanceFromLocation(end)
    }
    
    func mapView(mapView: MKMapView, rendererForOverlay overlay: MKOverlay) -> MKOverlayRenderer {
        
        // For rendering overlays
        
        let circle = MKCircleRenderer(overlay: overlay)
        circle.strokeColor = UIColor.redColor()
        circle.fillColor = UIColor(red: 100, green: 0, blue: 0, alpha: 0.05)
        circle.lineWidth = 1
        return circle
    }
    
    func addRadiusCircle(location: CLLocation){
        
        // Creating circular overlay
        
        let circle = MKCircle(centerCoordinate: location.coordinate, radius: (minDistanceToAlertUser * unitFactor) as CLLocationDistance)
        self.map.addOverlay(circle)
    }
    func createAnnotationView(forLocation location:CLLocation){
        
        // Creating Annotation View
        
        let locationCoordinate2D = CLLocationCoordinate2DMake(location.coordinate.latitude,location.coordinate.longitude)
        currentAnnotation.coordinate = locationCoordinate2D
        self.map.addAnnotation(currentAnnotation)
    }
    
    func createMapView(forLocation location:CLLocation){
        
        // Creating Map View
        
        map.removeAnnotation(currentAnnotation)
        
        let deltaLatidude: CLLocationDegrees = 0.05
        let deltaLongitude: CLLocationDegrees = 0.05
        let span: MKCoordinateSpan = MKCoordinateSpanMake(deltaLatidude, deltaLongitude)
        let locationCoordinate2D = CLLocationCoordinate2DMake(location.coordinate.latitude,location.coordinate.longitude)
        let region: MKCoordinateRegion = MKCoordinateRegionMake(locationCoordinate2D, span)
        
        self.map.setRegion(region, animated: true)
    }
    
    func WakeMeUp(){
        
        // Vibrate Phone
        
        AudioServicesPlaySystemSound (4095)
    }
    
    
    // Inherited Methods
    
    func locationManager(manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        
        // Update user's location
        
        if let currentLocation: CLLocation = locations.first{
            
            createMapView(forLocation: currentLocation)
            createAnnotationView(forLocation: currentLocation)
            
            let destination:CLLocation = CLLocation(latitude: locationCoordinate.latitude, longitude: locationCoordinate.longitude)
            let distance = calculateDistance(between: currentLocation, and: destination)
            
            if distance * unitFactor <= minDistanceToAlertUser * unitFactor {
                if calledOnce{
                    calledOnce = false
                    if !willVibrateOnly{
                        player.play()
                    }
                    timer = NSTimer.scheduledTimerWithTimeInterval(1, target: self, selector: Selector("WakeMeUp"), userInfo: nil, repeats: true)
                    locationManager.stopUpdatingLocation()
                    let title = "Wake Up!!!"
                    let message = "You have reached you spot"
                    let actionSheetController = UIAlertController(title: title, message: message, preferredStyle: .Alert)
                    let confirmAction = UIAlertAction(title: "Okay", style: .Default) { action in
                        self.timer.invalidate()
                        if !willVibrateOnly{
                            self.player.stop()
                        }
                    }
                    actionSheetController.addAction(confirmAction)
                    presentViewController(actionSheetController, animated: true, completion: nil)
                    let notification = UILocalNotification()
                    notification.alertBody = "You Have Reached" // text that will be displayed in the notification
                    notification.alertAction = "open" // text that is displayed after "slide to..." on the lock screen - defaults to "slide to view"
                    notification.fireDate = NSDate() // todo item due date (when notification will be fired)
                    notification.soundName = NSBundle.mainBundle().pathForResource("by_the_seaside__ios",ofType: "mp3")!
                    //notification.so
                    //"by_the_seaside__ios.mp3"//UILocalNotificationDefaultSoundName // play default sound
                    //notification.userInfo = ["UUID": item.UUID, ] // assign a unique identifier to the notification so that we can retrieve it later
                    //                    notification.category = "TODO_CATEGORY"
                    UIApplication.sharedApplication().scheduleLocalNotification(notification)
                }
            }
            
        }
        
    }
    
    
    override func viewWillAppear(animated: Bool) {
        
        self.map.delegate = self
        
        map.removeAnnotations(map.annotations)
        map.removeOverlays(map.overlays)
        
        calledOnce = true
        
        let destination:CLLocation = CLLocation(latitude: locationCoordinate.latitude, longitude: locationCoordinate.longitude)
        createMapView(forLocation: destination)
        destinationAnnotation.coordinate = locationCoordinate
        self.map.addAnnotation(destinationAnnotation)
        addRadiusCircle(destination)
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.allowsBackgroundLocationUpdates = true
        locationManager.requestAlwaysAuthorization()
        locationManager.startUpdatingLocation()
        
        
        do {
            
            try player = AVAudioPlayer(contentsOfURL: NSURL(fileURLWithPath: NSBundle.mainBundle().pathForResource("Alarm", ofType: "mp3")!))
        } catch {
            print("Problem with Audio File")
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
