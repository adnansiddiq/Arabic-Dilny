//
//  NSDictionary+ADAdditions.h
//  GlocalizME
//
//  Created by Adnan Siddiq on 06/01/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSDictionary (ADAdditions)

- (NSInteger)intForKey:(NSString *)key;

- (NSNumber *)numberForKey:(NSString *)key;

- (NSURL *)urlForKey:(NSString *)key;

- (CGFloat)doubleForKey:(NSString *)key;

- (id)saveObjectForKey:(NSString *)key;

- (NSNumber *)longNumberForKey:(NSString *)key;

- (NSDate *)dateForKey:(NSString *)key;

- (NSDate *)dateForKey:(NSString *)key withFormate:(NSString *)formate_;

- (NSString *)string4Key:(NSString *)key_;

@end
