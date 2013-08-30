//
//  UIImage+Resize.h
//  CougarsFirst
//
//  Created by Adnan Siddiq on 31/10/2012.
//  Copyright (c) 2012 Adnan Siddiq. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIImage (Resize)

-(UIImage *)imageByScalingToSize:(CGSize)targetSize;
-(UIImage *)resizeImageToSize:(CGSize)targetSize;

@end
