//
//  ADItemCell.h
//  Dilny
//
//  Created by Adnan Siddiq on 17/08/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "AsyncImageView.h"

#import "ADItem.h"

@interface ADItemCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UILabel *addressLabel;
@property (weak, nonatomic) IBOutlet UILabel *ratingLabel;
@property (weak, nonatomic) IBOutlet UILabel *distanceLabel;

@property (weak, nonatomic) IBOutlet AsyncImageView *itemImage;

@property (strong, nonatomic) ADItem *item;

@end
