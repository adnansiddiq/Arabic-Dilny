//
//  NSDictionary+ADAdditions.m
//  GlocalizME
//
//  Created by Adnan Siddiq on 06/01/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "NSDictionary+ADAdditions.h"

@implementation NSDictionary (ADAdditions)

- (id)saveObjectForKey:(NSString *)key {
    if ([[self objectForKey:key] isEqual:[NSNull null]]) {
        return @"";
    } else {
        return [self objectForKey:key];
    }
}

- (NSInteger)intForKey:(NSString *)key {
    if ([[self objectForKey:key] isEqual:[NSNull null]]) {
        return 0;
    } else {
        return [[self objectForKey:key] intValue];
    }
}

- (NSNumber *)numberForKey:(NSString *)key {
    
    return [NSNumber numberWithInt:[self intForKey:key]];
}
- (NSNumber *)longNumberForKey:(NSString *)key {
    
    NSNumberFormatter *formatter = [[NSNumberFormatter alloc] init];
    [formatter setNumberStyle:NSNumberFormatterDecimalStyle];
    return [formatter numberFromString:[[self objectForKey:key] stringValue]];
}

- (NSURL *)urlForKey:(NSString *)key {
    return [NSURL URLWithString:[self objectForKey:key]];
}
- (CGFloat)doubleForKey:(NSString *)key {
    return [[self objectForKey:key] doubleValue];
}

- (NSDate *)dateForKey:(NSString *)key withFormate:(NSString *)formate_ {
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:formate_];
    return [formatter dateFromString:[self objectForKey:key]];
}

- (NSDate *)dateForKey:(NSString *)key {
    return [NSDate dateWithTimeIntervalSince1970:[[self objectForKey:key] floatValue]];
}

@end
