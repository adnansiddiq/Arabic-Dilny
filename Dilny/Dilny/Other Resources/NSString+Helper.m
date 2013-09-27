//
//  NSString+Helper.m
//  Vantenfall
//
//  Created by Meta Design on 8/13/13.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "NSString+Helper.h"


@implementation NSString (Helper)


- (BOOL)isEmpty {
    
    return !self.length;
}

- (BOOL)isValidEmail {
    
    BOOL stricterFilter = YES; // Discussion http://blog.logichigh.com/2010/09/02/validating-an-e-mail-address/
    NSString *stricterFilterString = @"[A-Z0-9a-z\\._%+-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}";
    NSString *laxString = @".+@([A-Za-z0-9]+\\.)+[A-Za-z]{2}[A-Za-z]*";
    NSString *emailRegex = stricterFilter ? stricterFilterString : laxString;
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", emailRegex];
    return [emailTest evaluateWithObject:self];
}


+ (id)emptyString {
    return @"";
}


+ (id)StringWithInteger:(NSInteger)integer_ {
    return [NSString stringWithFormat:@"%d", integer_];
}

+ (id)whiteSpace {
    
    return @" ";
}

- (id)localizeString {
    return NSLocalizedString(self, nil);
}

- (id)terminatingWhiteSpace {
    return [self stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
}

- (BOOL)isNumber {
    
    NSString *stricterFilterString = @"[0-9]*";
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", stricterFilterString];
    BOOL test  = [emailTest evaluateWithObject:self];
    
    return test;
  
}

- (BOOL)isNumaricOneAlpha {
    
    NSString *stricterFilterString = @"[0-9]+([A-Za-z]{1})";
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", stricterFilterString];
    return [emailTest evaluateWithObject:self];
}

- (BOOL)isAlpha {
    
    NSString *stricterFilterString = @"[a-zA-Z+Ü+Ö+Ä+ö+ü+ä]*";
    NSPredicate *strPredicate = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", stricterFilterString];
    return [strPredicate evaluateWithObject:self];


}


+ (id)documentDirectoryPathWithFile:(NSString *)fileName_ {
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *filePath = [documentsDirectory stringByAppendingPathComponent:fileName_];
    return filePath;
}


- (NSString*)MD5 {
	// Create pointer to the string as UTF8
    const char *ptr = [self UTF8String];
    
 	// Create byte array of unsigned chars
    unsigned char md5Buffer[CC_MD5_DIGEST_LENGTH];
    
	// Create 16 bytes MD5 hash value, store in buffer
    CC_MD5(ptr, strlen(ptr), md5Buffer);
    
	// Convert unsigned char buffer to NSString of hex values
    NSMutableString *output = [NSMutableString stringWithCapacity:CC_MD5_DIGEST_LENGTH * 2];
    for(int i = 0; i < CC_MD5_DIGEST_LENGTH; i++)
		[output appendFormat:@"%02x",md5Buffer[i]];
    
    return output;
}

- (id)percentageEncoded {
    
    
    return [self stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
}
@end
