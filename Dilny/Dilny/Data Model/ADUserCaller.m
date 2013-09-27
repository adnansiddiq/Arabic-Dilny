//
//  ADUserCaller.m
//  Dilny
//
//  Created by Adnan Siddiq on 06/09/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADUserCaller.h"
#import "ADItem.h"

const NSString *kEmailKey = @"email";
const NSString *kPasswordKey = @"password";
const NSString *kNameKey = @"name";

const NSString *kUserIDKey = @"userid";
const NSString *kLocationIDKey = @"locationid";

const NSString *kLatitudeKey = @"lat";
const NSString *kLongitudeKey = @"lng";


@implementation ADUserCaller

+ (void)registerUserWithInfo:(NSDictionary *)dict_
                     success:(void (^)(NSString *responce))succes_
                     failure:(void (^)(NSError *error))failure_ {
    
    NSString *strURL = [NSString stringWithFormat:@"http://dilny.com/api/siginUp/mail/%@/password/%@/login/%@", dict_[kEmailKey], dict_[kPasswordKey], dict_[kNameKey]];
    
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:[strURL percentageEncoded]]];
    
    AFHTTPRequestOperation *operation = [[AFHTTPRequestOperation alloc] initWithRequest:request];
    
    [operation setCompletionBlockWithSuccess:^(AFHTTPRequestOperation *operation, id responseObject) {
        succes_(responseObject);
    } failure: ^(AFHTTPRequestOperation *operation, NSError *error) {
        failure_(error);
    }];
    
    [operation start];
    
}

+ (void)loginUserWithInfo:(NSDictionary *)info_
                  success:(void (^)(NSString *responce))succes_
                  failure:(void (^)(NSError *error))failure_ {
    
    NSString *password = info_[kPasswordKey];
    
    NSString *strURL = [NSString stringWithFormat:@"http://dilny.com/api/siginIn/mail/%@/password/%@", info_[kEmailKey], [password MD5]];
    
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:[strURL percentageEncoded]]];
    
    AFHTTPRequestOperation *operation = [[AFHTTPRequestOperation alloc] initWithRequest:request];
    
    [operation setCompletionBlockWithSuccess:^(AFHTTPRequestOperation *operation, id responseObject) {
        succes_(operation.responseString);
    } failure: ^(AFHTTPRequestOperation *operation, NSError *error) {
        failure_(error);
    }];
    
    [operation start];
}

+ (void)favLocationWithInfo:(NSDictionary *)info_
                    success:(void (^)(NSString *responce))succes_
                    failure:(void (^)(NSError *error))failure_ {
    
    NSString *strURL = [NSString stringWithFormat:@"http://dilny.com/api/addToFavorites/id/%@/user/%@", info_[kLocationIDKey], info_[kUserIDKey]];
    
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:[strURL percentageEncoded]]];
    
    AFHTTPRequestOperation *operation = [[AFHTTPRequestOperation alloc] initWithRequest:request];
    
    [operation setCompletionBlockWithSuccess:^(AFHTTPRequestOperation *operation, id responseObject) {
        succes_(operation.responseString);
    } failure: ^(AFHTTPRequestOperation *operation, NSError *error) {
        failure_(error);
    }];
    
    [operation start];

}

+ (void)loadFavWithInfo:(NSDictionary *)info_
                success:(void (^)(NSArray *list))succes_
                failure:(void (^)(NSError *error))failure_ {
    
    NSString *strURL = [NSString stringWithFormat:@"http://dilny.com/api/getFavoritesList/user/%@/lat/%@/lng/%@", info_[kUserIDKey], info_[kLatitudeKey], info_[kLongitudeKey]];
    
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:[strURL percentageEncoded]]];
    
    AFJSONRequestOperation *operation = [AFJSONRequestOperation JSONRequestOperationWithRequest:request
                                                                                        success:^(NSURLRequest *request, NSHTTPURLResponse *response, id JSON) {
                                                                                            
                                                                                            NSMutableArray *result = [NSMutableArray array];
                                                                                            for (NSDictionary *dict in (NSArray*)JSON) {
                                                                                                ADItem *item = [[ADItem alloc] initWithDictionary:dict];
                                                                                                [result addObject:item];
                                                                                            }

                                                                                            succes_([NSArray arrayWithArray:result]);
                                                                                            
                                                                                        } failure:^(NSURLRequest *request, NSHTTPURLResponse *response, NSError *error, id JSON) {
                                                                                            
                                                                                            failure_(error);
                                                                                        }];
    [operation start];

}


@end
