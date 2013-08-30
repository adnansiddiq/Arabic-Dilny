//
//  UIImage+Resize.m
//  CougarsFirst
//
//  Created by Adnan Siddiq on 31/10/2012.
//  Copyright (c) 2012 Adnan Siddiq. All rights reserved.
//

#import "UIImage+Resize.h"

static inline double radians (double degrees) {
    return degrees * M_PI/180;
}

@implementation UIImage (Resize)

-(UIImage*)imageByScalingToSize:(CGSize)targetSize {
	UIImage* sourceImage = self;
	CGFloat targetWidth = targetSize.width;
	CGFloat targetHeight = targetSize.height;
    
	CGImageRef imageRef = [sourceImage CGImage];
	CGBitmapInfo bitmapInfo = CGImageGetBitmapInfo(imageRef);
	CGColorSpaceRef colorSpaceInfo = CGImageGetColorSpace(imageRef);
    
	if (bitmapInfo == kCGImageAlphaNone) {
		bitmapInfo = kCGImageAlphaNoneSkipLast;
	}
    
	CGContextRef bitmap;
    
	if (sourceImage.imageOrientation == UIImageOrientationUp || sourceImage.imageOrientation == UIImageOrientationDown) {
		bitmap = CGBitmapContextCreate(NULL, targetWidth, targetHeight, CGImageGetBitsPerComponent(imageRef), CGImageGetBytesPerRow(imageRef), colorSpaceInfo, bitmapInfo);
        
	} else {
		bitmap = CGBitmapContextCreate(NULL, targetHeight, targetWidth, CGImageGetBitsPerComponent(imageRef), CGImageGetBytesPerRow(imageRef), colorSpaceInfo, bitmapInfo);
        
	}
    
	if (sourceImage.imageOrientation == UIImageOrientationLeft) {
		CGContextRotateCTM (bitmap, radians(90));
		CGContextTranslateCTM (bitmap, 0, -targetHeight);
        
	} else if (sourceImage.imageOrientation == UIImageOrientationRight) {
		CGContextRotateCTM (bitmap, radians(-90));
		CGContextTranslateCTM (bitmap, -targetWidth, 0);
        
	} else if (sourceImage.imageOrientation == UIImageOrientationUp) {
		// NOTHING
	} else if (sourceImage.imageOrientation == UIImageOrientationDown) {
		CGContextTranslateCTM (bitmap, targetWidth, targetHeight);
		CGContextRotateCTM (bitmap, radians(-180.));
	}
    
	CGContextDrawImage(bitmap, CGRectMake(0, 0, targetWidth, targetHeight), imageRef);
	CGImageRef ref = CGBitmapContextCreateImage(bitmap);
	UIImage* newImage = [UIImage imageWithCGImage:ref];
    
	CGContextRelease(bitmap);
	CGImageRelease(ref);
    
	return newImage; 
}


-(UIImage *)resizeImageToSize:(CGSize)targetSize {
    
    UIImage *image = self;
	int w = image.size.width;
    int h = image.size.height;
	
	CGImageRef imageRef = [image CGImage];
	
	int width, height;
	
	int destWidth = targetSize.width;
	int destHeight = targetSize.height;
	if(w > h){
		width = destWidth;
		height = h*destWidth/w;
	} else {
		height = destHeight;
		width = w*destHeight/h;
	}
	
	CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
    
	CGContextRef bitmap;
	bitmap = CGBitmapContextCreate(NULL, width, height, 8, 4 * width, colorSpace, kCGImageAlphaPremultipliedFirst);
	
	if (image.imageOrientation == UIImageOrientationLeft) {
		CGContextRotateCTM (bitmap, M_PI/2);
		CGContextTranslateCTM (bitmap, 0, -height);
		
	} else if (image.imageOrientation == UIImageOrientationRight) {
		CGContextRotateCTM (bitmap, -M_PI/2);
		CGContextTranslateCTM (bitmap, -width, 0);
		
	} else if (image.imageOrientation == UIImageOrientationUp) {
		
	} else if (image.imageOrientation == UIImageOrientationDown) {
		CGContextTranslateCTM (bitmap, width,height);
		CGContextRotateCTM (bitmap, -M_PI);
		
	}
	
	CGContextDrawImage(bitmap, CGRectMake(0, 0, width, height), imageRef);
	CGImageRef ref = CGBitmapContextCreateImage(bitmap);
	UIImage *result = [UIImage imageWithCGImage:ref];
	
	CGContextRelease(bitmap);
	CGImageRelease(ref);
	
	return result;
}
@end
