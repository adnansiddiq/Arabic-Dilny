//
//  ADMapViewController.h
//  GlocalizME
//
//  Created by Adnan Siddiq on 04/01/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//
#import <MapKit/MapKit.h>
#import <CoreLocation/CoreLocation.h>
#import "ADBaseViewController.h"

#import "MyLocation.h"

@interface ADMapViewController : ADBaseViewController

@property (weak, nonatomic) IBOutlet MKMapView *itemMapView;

@property (strong, nonatomic) NSArray *allItems;

- (void)loadShopOnMapView;

@end
