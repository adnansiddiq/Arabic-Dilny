//
//  ADListCaller.m
//  Dilny
//
//  Created by Adnan Siddiq on 17/08/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADListCaller.h"

@interface ADListCaller () {
    
    NSString *_catID;
    CGFloat _lat;
    CGFloat _lng;
    CGFloat _radius;
    
    NSInteger _pageNumber;
    
    NSString *_path;
}

@end


@implementation ADListCaller

- (NSArray *)parseResponce:(NSArray *)data_ {
    
    NSMutableArray *result = [NSMutableArray array];
    
    for (NSDictionary *dict in data_) {
        ADItem *item = [[ADItem alloc] initWithDictionary:dict];
        [result addObject:item];
    }
    
    return [NSArray arrayWithArray:result];
}

- (void)searchByCategory:(NSNumber *)catID_
                latitude:(CGFloat)lat_
               longitude:(CGFloat)lng_
                  radius:(CGFloat)radius_
        withProgressView:(BOOL)shown_ {
    
    _pageNumber = 0;
    
    _catID = [catID_ stringValue];
    _lat = lat_;
    _lng = lng_;
    _radius = radius_;
    _path = @"searchByCat";
    
    [self loadListWithAnimation:shown_];
    
}

- (void)loadListWithAnimation:(BOOL)shown_ {
    
    self.showProgressView = shown_;
    
    NSString *strURL = [NSString stringWithFormat:@"%@/lat/%f/lng/%f/radius/%g/page/%d/cat/%@", _path, _lat, _lng, _radius, _pageNumber, _catID];
    
    [self executeRequestType:RequestTypeList
               requestMethod:RequestMethodGet
                        path:strURL
                  parameters:nil successBlock:^(id JSON) {
                      _pageNumber ++;
                      return [self parseResponce:JSON];
                  }];

}


@end
