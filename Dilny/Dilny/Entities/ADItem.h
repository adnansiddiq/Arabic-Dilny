//
//  ADItem.h
//  Dilny
//
//  Created by Adnan Siddiq on 17/08/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ADItem : NSObject

@property (strong, nonatomic) NSString *address1;
@property (strong, nonatomic) NSString *address2;
@property (strong, nonatomic) NSNumber *catID;
@property (strong, nonatomic) NSString *country;
@property (strong, nonatomic) NSString *description;
@property (strong, nonatomic) NSString *distance;
@property (strong, nonatomic) NSNumber *itemID;
@property (strong, nonatomic) NSURL *imageURL;
@property (strong, nonatomic) NSString *latitude;
@property (strong, nonatomic) NSString *longitude;
@property (strong, nonatomic) NSString *phone;
@property (strong, nonatomic) NSNumber *rating;
@property (strong, nonatomic) NSString *reviews;
@property (strong, nonatomic) NSURL *thumb_img;
@property (strong, nonatomic) NSString *title;

- (id)initWithDictionary:(NSDictionary *)dict_;

@end
