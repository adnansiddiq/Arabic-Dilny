//
//  ADCategoryCaller.m
//  Dilny
//
//  Created by Adnan Siddiq on 17/08/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADCategoryCaller.h"

@implementation ADCategoryCaller


- (NSArray *)parseCatResponce:(NSArray *)data_ {
    
    NSMutableArray *result = [NSMutableArray array];
    
    for (NSDictionary *dict in data_) {
        ADCategory *cat = [[ADCategory alloc] initWithDictionary:dict];
        [result addObject:cat];
    }
    
    return [NSArray arrayWithArray:result];
}

- (void)loadCategoriesWithProgressView:(BOOL)shown_ {
    
    self.showProgressView = shown_;
    
    [self executeRequestType:RequestTypeCategory
               requestMethod:RequestMethodGet
                        path:@"getCats/0/0"
                  parameters:nil
                successBlock:^(id JSON) {

                    return [self parseCatResponce:JSON];
                }];
}

@end
