//
//  LocationManager.h
//  minirideServices
//
//  Created by Ahsan Ali on 5/4/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>

extern NSString *const kLocationUpdateStarted;
extern NSString *const kLocationUpdateStopped;
extern NSString *const kLocationStatusChanged;
extern NSString *const kReverseGeoCodeDone;
extern NSString *const kGeoLocationProcessed;

@interface LocationManager : NSObject<CLLocationManagerDelegate>{
    
    CLLocationManager *mLocationManager;
    CLLocationCoordinate2D mGeocodedCoordinate;
    
    BOOL mHasInitialCoordinatesRetrieved;
    CLLocation *mCurrentLocation;
    
    NSString *mCurrentCity;
}

@property (readonly, strong) NSString *currentCity;


+ (LocationManager *) instance;
- (void) startUpdatingLocation;
- (void) stopUpdatingLocation;
- (CLLocationCoordinate2D) currentCoordinate;

- (CGFloat)ditstanceFromLocation:(CLLocation *)location;


@end


