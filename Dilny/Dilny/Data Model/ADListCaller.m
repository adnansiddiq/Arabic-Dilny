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
    
    NSString *_title;
}

@end


@implementation ADListCaller

- (NSArray *)parseResponce:(NSArray *)data_ {
    
    NSMutableArray *result = [NSMutableArray array];
    
    for (NSDictionary *dict in data_) {
        ADItem *item = [[ADItem alloc] initWithDictionary:dict];
        [result addObject:item];
    }
    if (result.count == 0) _noMoreLoading = YES;
    
    return [NSArray arrayWithArray:result];
}

- (void)searchByType:(NSString *)path_
            category:(NSString *)catID_
            latitude:(CGFloat)lat_
           longitude:(CGFloat)lng_
              radius:(CGFloat)radius_
    withProgressView:(BOOL)shown_ {
    
    _pageNumber = 0;
    
    _catID = catID_ ;
    _lat = lat_;
    _lng = lng_;
    _radius = radius_;
    _path = path_;;
    
    _noMoreLoading = NO;
    
    [self loadListWithAnimation:shown_];
    
}

- (void)searchByType:(NSString *)path_
               title:(NSString *)title_
            category:(NSString *)catID_
            latitude:(CGFloat)lat_
           longitude:(CGFloat)lng_
              radius:(CGFloat)radius_
    withProgressView:(BOOL)shown_ {
    
    _pageNumber = 0;
    
    _catID = catID_ ;
    _lat = lat_;
    _lng = lng_;
    _radius = radius_;
    _path = path_;
    _title = title_;
    
    _noMoreLoading = NO;
    
    [self loadListWithAnimation:shown_];
    
}

- (void)loadListWithAnimation:(BOOL)shown_ {
    
    self.showProgressView = shown_;
    
    NSString *strURL;
    
    if (_title) {
        strURL = [NSString stringWithFormat:@"%@/title/%@/lat/%f/lng/%f/radius/%g/page/%d/cat/%@", _path, _title, _lat, _lng, _radius, _pageNumber, _catID];
    } else {
        strURL = [NSString stringWithFormat:@"%@/lat/%f/lng/%f/radius/%g/page/%d/cat/%@", _path, _lat, _lng, _radius, _pageNumber, _catID];
    }
    
    [self executeRequestType:RequestTypeList
               requestMethod:RequestMethodGet
                        path:strURL
                  parameters:nil successBlock:^(id JSON) {
                      _pageNumber ++;
                      return [self parseResponce:JSON];
                  }];

}


@end
