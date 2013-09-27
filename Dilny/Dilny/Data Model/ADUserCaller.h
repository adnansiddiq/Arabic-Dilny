//
//  ADUserCaller.h
//  Dilny
//
//  Created by Adnan Siddiq on 06/09/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "AFNetworking.h"

extern const NSString *kEmailKey;
extern const NSString *kPasswordKey;
extern const NSString *kNameKey;

extern const NSString *kUserIDKey;
extern const NSString *kLocationIDKey;

extern const NSString *kLatitudeKey;
extern const NSString *kLongitudeKey;


@interface ADUserCaller : NSObject

+ (void)registerUserWithInfo:(NSDictionary *)dict_
                     success:(void (^)(NSString *responce))succes_
                     failure:(void (^)(NSError *error))failure_;

+ (void)loginUserWithInfo:(NSDictionary *)info_
                  success:(void (^)(NSString *responce))succes_
                  failure:(void (^)(NSError *error))failure_;

+ (void)favLocationWithInfo:(NSDictionary *)info_
                    success:(void (^)(NSString *responce))succes_
                    failure:(void (^)(NSError *error))failure_;


+ (void)loadFavWithInfo:(NSDictionary *)info_
                success:(void (^)(NSArray *list))succes_
                failure:(void (^)(NSError *error))failure_;


@end
