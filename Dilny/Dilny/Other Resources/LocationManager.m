//
//  LocationManager.m
//  minirideServices
//
//  Created by Ahsan Ali on 5/4/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



#import <CoreLocation/CLGeocoder.h>
#import "LocationManager.h"

#define DISTANCE_THRESHOLD      10.0

NSString *const kLocationUpdateStarted  = @"LocationUpdateStarted";
NSString *const kLocationUpdateStopped  = @"LocationUpdateStopped";
NSString *const kLocationStatusChanged  = @"LocationStatusChanged";
NSString *const kReverseGeoCodeDone     = @"ReverseGeoCodeDone";
NSString *const kGeoLocationProcessed   = @"GeoLocationProcessed";


static LocationManager *instance = nil;

@implementation LocationManager

@synthesize currentCity=mCurrentCity;


#pragma mark init Methods

+ (LocationManager *) instance{
    
    if (instance == nil) {
        
            instance = [[LocationManager alloc] init];
        
    }
    
    return instance;
}

- (void)internalInitialize {    
	mLocationManager = [[CLLocationManager alloc] init];
	mLocationManager.desiredAccuracy = kCLLocationAccuracyBest;
	mLocationManager.delegate = self;
	mLocationManager.distanceFilter = DISTANCE_THRESHOLD;
	[mLocationManager startUpdatingLocation];
}

- (id)init {
    self = [super init];
    if (self) {
        [self internalInitialize];
    }
    return self;
}

- (void) startUpdatingLocation {
    [mLocationManager startUpdatingLocation];
}

- (void) stopUpdatingLocation {
	[mLocationManager stopUpdatingLocation];
}

- (void) fireLocationChangeSuccessEvent{
    [[NSNotificationCenter defaultCenter] postNotificationName:kLocationStatusChanged object:[NSNumber numberWithBool:YES]];
    
}
- (void) fireLocationChangeFailEvent{
    [[NSNotificationCenter defaultCenter] postNotificationName:kLocationStatusChanged object:[NSNumber numberWithBool:NO]];    
}

- (void) fireReverseGeocodeSuccessEvent {
    [[NSNotificationCenter defaultCenter] postNotificationName:kReverseGeoCodeDone object:[NSNumber numberWithBool:YES]];
}

- (void) fireGeoCodeAddressSuccessEvent {
    [[NSNotificationCenter defaultCenter] postNotificationName:kGeoLocationProcessed object:[NSNumber numberWithBool:YES]];
}

-(CLLocationCoordinate2D)currentCoordinate {
    return mCurrentLocation.coordinate;
}

-(CLLocationCoordinate2D)geocodedCoordinate {
    
    return mGeocodedCoordinate;
    
}

#pragma mark Location Manager Delegate

- (void)locationManager:(CLLocationManager *)_manager didUpdateToLocation:(CLLocation *)_newLocation fromLocation:(CLLocation *)_oldLocation {
    mCurrentLocation = _newLocation;
    [self fireLocationChangeSuccessEvent];

}
- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error{
    NSLog(@"%@",error);
}

- (CGFloat)ditstanceFromLocation:(CLLocation *)location {
    return [mCurrentLocation distanceFromLocation:location]/1609.344;
}


@end
