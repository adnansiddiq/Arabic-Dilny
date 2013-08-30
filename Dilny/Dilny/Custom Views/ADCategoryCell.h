//
//  ADCategoryCell.h
//  Dilny
//
//  Created by Adnan Siddiq on 17/08/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "AsyncImageView.h"

#import "ADCategory.h"

@interface ADCategoryCell : UITableViewCell

@property (weak, nonatomic) IBOutlet AsyncImageView *catImageView;
@property (weak, nonatomic) IBOutlet UILabel *catNameLabel;


@property (strong, nonatomic) ADCategory *category;
@end
