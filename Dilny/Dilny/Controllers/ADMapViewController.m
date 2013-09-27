//
//  ADMapViewController.m
//  GlocalizME
//
//  Created by Adnan Siddiq on 04/01/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADMapViewController.h"

#import "ADItemsViewController.h"

#import "ADItem.h"

@interface ADMapViewController () {
    
}

@end

@implementation ADMapViewController

- (id)initWithTitle:(NSString *)title withRevealBlock:(RevealBlock)revealBlock {
    if (self = [super initWithTitle:title withRevealBlock:revealBlock]) {
		self.title = title;
        
	}
	return self;
}



- (void)viewDidLoad {
    [super viewDidLoad];
}


- (void)loadMapView {
    NSMutableArray *mAnnotations = [NSMutableArray array];
    int i = 0;
    for (ADItem *item in _allItems) {
        MyLocation *annotation=[[MyLocation alloc] initWithName:item.title address:item.address1 coordinate:[item cordinate]];
        annotation.tag = i++;
        [mAnnotations addObject:annotation];
    }
    [self.itemMapView addAnnotations:mAnnotations];
}

- (void)loadShopOnMapView {
    [self.itemMapView removeAnnotations:_itemMapView.annotations];
    [self loadMapView];
    [self zoomToFitMapAnnotations];
}

#pragma mark - BaseCaller Delegate


- (void)zoomToFitMapAnnotations {
    
    if ([_itemMapView.annotations count] == 0) return;
    int i = 0;
    MKMapPoint points[[_itemMapView.annotations count]];
    
    for (id<MKAnnotation> annotation in [_itemMapView annotations])
        points[i++] = MKMapPointForCoordinate(annotation.coordinate);
    
    MKPolygon *poly = [MKPolygon polygonWithPoints:points count:i];
    MKMapRect rect = [poly boundingMapRect];
    
    if (rect.size.width < 12000) {rect.size.width = 12000; rect.origin.x -= 6000;}
    if (rect.size.height < 12000) {rect.size.height = 12000; rect.origin.y -= 6000;}
    
    [_itemMapView setRegion:MKCoordinateRegionForMapRect(rect) animated:YES];
}
- (void)mapView:(MKMapView *)mapView didAddAnnotationViews:(NSArray *)views {
	//[self zoomToFitMapAnnotations];
}
- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id <MKAnnotation>)annotation{

    MKAnnotationView *annView = nil;
    if([annotation isKindOfClass:[MyLocation class]]){
        annView = (MKPinAnnotationView*)[self.itemMapView dequeueReusableAnnotationViewWithIdentifier:@"annotationBigId"];
        if(annView == nil) {
            annView = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"annotationBigId"];
            annView.rightCalloutAccessoryView = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
            annView.canShowCallout = YES;
            MyLocation *temp = (MyLocation *)annotation;
            annView.tag = temp.tag;
            [(MKPinAnnotationView*)annView setAnimatesDrop:YES];
        }
    }
    return annView;
}
- (void)mapView:(MKMapView *)mapView annotationView:(MKAnnotationView *)view calloutAccessoryControlTapped:(UIControl *)control {

    ADItemsViewController *parent = (ADItemsViewController *)self.parentViewController;
    [parent selectedItemDetailWithIndex:view.tag];
}

@end
