//
//  NSString+Helper.h
//  Vantenfall
//
//  Created by Meta Design on 8/13/13.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSString (Helper)

- (BOOL)isEmpty;

- (BOOL)isValidEmail;


+ (id)emptyString;

+ (id)StringWithInteger:(NSInteger)integer_;

- (id)localizeString;

+ (id)whiteSpace;

- (id)terminatingWhiteSpace;

- (BOOL)isNumber;

- (BOOL)isNumaricOneAlpha;

- (BOOL)isAlpha;

+ (id)documentDirectoryPathWithFile:(NSString *)fileName_;

- (NSString*)MD5;

- (id)percentageEncoded;

@end
