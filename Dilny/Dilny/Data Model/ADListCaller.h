//
//  ADListCaller.h
//  Dilny
//
//  Created by Adnan Siddiq on 17/08/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADBaseCaller.h"

#import "ADItem.h"

@interface ADListCaller : ADBaseCaller

- (void)searchByCategory:(NSNumber *)catID_
                latitude:(CGFloat)lat_
               longitude:(CGFloat)lng_
                  radius:(CGFloat)radius_
        withProgressView:(BOOL)shown_;

- (void)loadListWithAnimation:(BOOL)shown_;

@end
