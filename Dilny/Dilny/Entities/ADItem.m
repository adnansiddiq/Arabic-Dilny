//
//  ADItem.m
//  Dilny
//
//  Created by Adnan Siddiq on 17/08/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADItem.h"

@implementation ADItem


- (id)initWithDictionary:(NSDictionary *)dict_ {
    
    self = [super init];
    
    if (self) {
        _address1 = [dict_ string4Key:@"address_line_1"];
        _address2 = [dict_ string4Key:@"address_line_2"];
        _catID = [dict_ numberForKey:@"cat_id"];
        _country = dict_[@"country"];
        _description = dict_[@"desc"];
        _distance = dict_[@"distance"];
        _itemID = [dict_ numberForKey:@"id"];
        _imageURL = [dict_ urlForKey:@"image"];
        _latitude = dict_[@"lat"];
        _longitude = dict_[@"lang"];
        _phone = dict_[@"phone"];
        _rating = [dict_ longNumberForKey:@"ratings"];
        _reviews = dict_[@"reviews"];
        _thumb_img = [dict_ urlForKey:@"thumb"];
        _title = dict_[@"title"];
    }
    return self;
}
@end
