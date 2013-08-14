//
//  NSDate+DateFormatter.m
//  Massimiliano Valeriani
//
//  Created by Adnan Siddiq on 25/12/2012.
//  Copyright (c) 2012 Adnan Siddiq. All rights reserved.
//

#import "NSDate+DateFormatter.h"

@implementation NSDate (DateFormatter)

- (NSString *)stringWithFormate:(NSString *)formate {
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    NSArray *prefered = [NSLocale preferredLanguages];
    if ([[prefered objectAtIndex:0] isEqualToString:@"it"]) {
        NSLocale *it_CH = [[NSLocale alloc] initWithLocaleIdentifier:@"it_CH"];
        [dateFormatter setLocale:it_CH];
    }
    
    [dateFormatter setDateFormat:formate];
    return [dateFormatter stringFromDate:self];
}

@end
