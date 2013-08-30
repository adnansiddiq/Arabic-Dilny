//
//  ADDetailViewController.h
//  Dilny
//
//  Created by Adnan Siddiq on 17/08/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADBaseViewController.h"

#import "AsyncImageView.h"

#import "ADItem.h"

@interface ADDetailViewController : ADBaseViewController

@property (weak, nonatomic) IBOutlet UILabel *lblTitle;
@property (weak, nonatomic) IBOutlet AsyncImageView *itemImage;
@property (weak, nonatomic) IBOutlet UILabel *lblAddress;
@property (weak, nonatomic) IBOutlet UILabel *lblPhone;
@property (weak, nonatomic) IBOutlet UILabel *lblDistance;
@property (weak, nonatomic) IBOutlet UILabel *lblRating;
@property (weak, nonatomic) IBOutlet UIWebView *webReview;

@property (strong, nonatomic) ADItem *item;

@property (weak, nonatomic) IBOutlet UIScrollView *contentScrollView;
@property (weak, nonatomic) IBOutlet UIView *contentView;

@end
