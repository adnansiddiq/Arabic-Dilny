//
//  LocationManager.m
//  minirideServices
//
//  Created by Ahsan Ali on 5/4/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



#import <CoreLocation/CLGeocoder.h>
#import "LocationManager.h"
#import "AFNetworking.h"

#define DISTANCE_THRESHOLD      10.0

NSString *const kLocationUpdateStarted  = @"LocationUpdateStarted";
NSString *const kLocationUpdateStopped  = @"LocationUpdateStopped";
NSString *const kReverseGeoCodeDone     = @"ReverseGeoCodeDone";
NSString *const kGeoLocationProcessed   = @"GeoLocationProcessed";

NSString *const kLocationStatusChanged  = @"LocationStatusChanged";

NSString *const kAddressFindDone        = @"AddressFindDone";
NSString *const kAddressNOResult        = @"AddressNoResult";

static LocationManager *instance = nil;

@implementation LocationManager

#pragma mark init Methods

+ (LocationManager *)instance {
    
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

- (void) fireAddressFindSuccess {
    [[NSNotificationCenter defaultCenter] postNotificationName:kAddressFindDone object:[NSNumber numberWithBool:YES]];
}

- (void) fireAddressFindFail {
    [[NSNotificationCenter defaultCenter] postNotificationName:kAddressFindDone object:[NSNumber numberWithBool:NO]];
}

- (void) fireAddressNotFound {
    [[NSNotificationCenter defaultCenter] postNotificationName:kAddressNOResult object:nil];
}


-(CLLocationCoordinate2D)currentCoordinate {
    return mCurrentLocation.coordinate;
}

-(CLLocationCoordinate2D)geocodedCoordinate {
    
    return mGeocodedCoordinate;
    
}

#pragma mark Location Manager Delegate

- (void)locationManager:(CLLocationManager *)_manager didUpdateToLocation:(CLLocation *)_newLocation fromLocation:(CLLocation *)_oldLocation {
    
    if ([_newLocation distanceFromLocation:_oldLocation] >= 100.0f || mCurrentLocation == nil) {
        
        mCurrentLocation = _newLocation;
        [self fireLocationChangeSuccessEvent];
        [self getAddressInformationAboutLocation];
    }
}
- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error{
    
    [self fireLocationChangeFailEvent];
}

- (CGFloat)ditstanceFromLocation:(CLLocation *)location {
    return [mCurrentLocation distanceFromLocation:location]/1609.344;
}


- (void)getAddressInformationAboutLocation {
    
    CLLocationCoordinate2D cor = [self currentCoordinate];
    
    NSString *strURL = [NSString stringWithFormat:@"http://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&sensor=true&language=ar", cor.latitude, cor.longitude];
    
    NSLog(@"Loaction Call %@", strURL);
    
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:[strURL percentageEncoded]]];
    
    AFJSONRequestOperation *operation = [AFJSONRequestOperation JSONRequestOperationWithRequest:request
                                                                                        success:^(NSURLRequest *request, NSHTTPURLResponse *response, id JSON) {
                                                                                            
                                                                                            NSString *status = [JSON objectForKey:@"status"];
                                                                                            if ([status isEqualToString:@"ZERO_RESULTS"]) {
                                                                                                
                                                                                                [self fireAddressNotFound];
                                                                                                
                                                                                            } else if ([status isEqualToString:@"OK"]) {
                                                                                                
                                                                                                [self parseResponce:JSON];
                                                                                                [self fireAddressFindSuccess];
                                                                                                
                                                                                            } else {
                                                                                                
                                                                                                [self fireAddressFindFail];
                                                                                            }
                                                                                            
                                                                                        } failure:^(NSURLRequest *request, NSHTTPURLResponse *response, NSError *error, id JSON) {
                                                                                            
                                                                                            [self fireAddressFindFail];
                                                                                            
                                                                                        }];
    [operation start];

}

- (void)parseResponce:(NSDictionary *)json {
    
    NSArray *codeCountry = @[@"country", @"political"];
    NSArray *codeLocality = @[@"locality", @"political"];
    NSArray *codeSubLocality = @[@"sublocality", @"political"];
    NSArray *codeSteatNumebr = @[@"street_number"];
    NSArray *codeAdminLevel1 = @[@"administrative_area_level_1", @"political"];
    NSArray *codeAdminLevel2 = @[@"administrative_area_level_2", @"political"];
    NSArray *codeAdminLevel3 = @[@"administrative_area_level_3", @"political"];
    NSArray *codeRoute = @[@"route"];
    NSArray *codePostalCode = @[@"postal_code"];
    
    _country = nil;
    _locality = nil;
    _subLocality = nil;
    _streatNumber = nil;
    _adminLevel1 = nil;
    _adminLevel2 = nil;
    _adminLevel3 = nil;
    _route = nil;
    _postalCode = nil;

    
    NSArray *results = [json objectForKey:@"results"];
    
    NSDictionary * info = results[0];
    
    _fullAddress = info[@"formatted_address"];
    _latitude = [info[@"geometry"][@"location"][@"lat"] floatValue];
    _longtitude = [info[@"geometry"][@"location"][@"lng"] floatValue];
    
    NSArray *addressComp = info[@"address_components"];
    
    for (NSDictionary *item in addressComp) {
        
        NSArray *types = item[@"types"];
        
        if ([types isEqualToArray:codeCountry]) {
            _country = item[@"long_name"];
        } else if ([types isEqualToArray:codeLocality]) {
            _locality = item[@"long_name"];
        } else if ([types isEqualToArray:codeSubLocality]) {
            _subLocality = item[@"long_name"];
        } else if ([types isEqualToArray:codeSteatNumebr]) {
            _streatNumber = item[@"long_name"];
        } else if ([types isEqualToArray:codeAdminLevel1]) {
            _adminLevel1 = item[@"long_name"];
        } else if ([types isEqualToArray:codeAdminLevel2]) {
            _adminLevel2 = item[@"long_name"];
        } else if ([types isEqualToArray:codeAdminLevel3]) {
            _adminLevel3 = item[@"long_name"];
        } else if ([types isEqualToArray:codeRoute]) {
            _route = item[@"long_name"];
        } else if ([types isEqualToArray:codePostalCode]) {
            _postalCode = item[@"long_name"];
        }
    }
    
    _city = [NSString emptyString];
    if (_subLocality) {
        _city = [_subLocality stringByAppendingString:@", "];
    }
    if (_locality) {
        _city = [_city stringByAppendingFormat:@"%@, ", _locality];
    }
    if (_adminLevel1) {
        _city = [_city stringByAppendingString:_adminLevel1];
    }
    if (_adminLevel2) {
        _city = [_city stringByAppendingFormat:@", %@", _adminLevel2];
    }
    if (_adminLevel3) {
        _city = [_city stringByAppendingFormat:@", %@", _adminLevel3];
    }
    
    _address = [NSString emptyString];
    
    if (_streatNumber) {
        _address = _streatNumber;
    }
    if (_route) {
        _address = [_address stringByAppendingFormat:@", %@", _route];
    }
    
    NSLog(@"%@ --- %@", _city, _address);
}

@end
