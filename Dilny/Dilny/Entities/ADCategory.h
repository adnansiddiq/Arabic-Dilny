//
//  ADCategory.h
//  Dilny
//
//  Created by Adnan Siddiq on 17/08/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ADCategory : NSObject

@property (strong, nonatomic) NSNumber *catID;
@property (strong, nonatomic) NSNumber *icon;
@property (strong, nonatomic) NSURL *imageURL;
@property (strong, nonatomic) NSString *catName;

- (id)initWithDictionary:(NSDictionary *)dict_;

@end
