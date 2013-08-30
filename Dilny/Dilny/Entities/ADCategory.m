//
//  ADCategory.m
//  Dilny
//
//  Created by Adnan Siddiq on 17/08/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADCategory.h"

@implementation ADCategory

- (id)initWithDictionary:(NSDictionary *)dict_ {
    
    self = [super init];
    
    if (self) {
        _catID = [dict_ numberForKey:@"id"];
        _icon = [dict_ numberForKey:@"icon"];
        _imageURL = [dict_ urlForKey:@"image"];
        _catName = [dict_ objectForKey:@"name"];
    }
    
    return self;
}

@end
