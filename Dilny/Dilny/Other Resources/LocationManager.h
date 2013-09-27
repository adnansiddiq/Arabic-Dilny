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

extern NSString *const kAddressFindDone;
extern NSString *const kAddressNOResult;

@interface LocationManager : NSObject<CLLocationManagerDelegate>{
    
    CLLocationManager *mLocationManager;
    CLLocationCoordinate2D mGeocodedCoordinate;
    
    BOOL mHasInitialCoordinatesRetrieved;
    CLLocation *mCurrentLocation;

}


@property (readonly, strong, nonatomic) NSString *fullAddress;
@property (readonly, assign, nonatomic) CGFloat latitude;
@property (readonly, assign, nonatomic) CGFloat longtitude;

@property (readonly, strong, nonatomic) NSString *country;
@property (readonly, strong, nonatomic) NSString *locality;
@property (readonly, strong, nonatomic) NSString *subLocality;
@property (readonly, strong, nonatomic) NSString *streatNumber;
@property (readonly, strong, nonatomic) NSString *adminLevel1;
@property (readonly, strong, nonatomic) NSString *adminLevel2;
@property (readonly, strong, nonatomic) NSString *adminLevel3;
@property (readonly, strong, nonatomic) NSString *route;
@property (readonly, strong, nonatomic) NSString *postalCode;


@property (readonly, strong, nonatomic) NSString *city;
@property (readonly, strong, nonatomic) NSString *address;


+ (LocationManager *) instance;
- (void) startUpdatingLocation;
- (void) stopUpdatingLocation;
- (CLLocationCoordinate2D) currentCoordinate;

- (CGFloat)ditstanceFromLocation:(CLLocation *)location;


@end


