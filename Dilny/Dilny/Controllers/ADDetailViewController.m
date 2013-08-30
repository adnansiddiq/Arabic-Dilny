//
//  ADDetailViewController.m
//  Dilny
//
//  Created by Adnan Siddiq on 17/08/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADDetailViewController.h"

@interface ADDetailViewController ()

@end

@implementation ADDetailViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.lblTitle.text = _item.title;
    self.lblAddress.text = [NSString stringWithFormat:@"%@, %@ %@", _item.country, _item.address1, _item.address2];
    self.lblPhone.text = _item.phone;
    self.lblDistance.text = [NSString stringWithFormat:@"%d كم",[_item.distance integerValue]];
    self.lblRating.text = [_item.rating stringValue];
    self.itemImage.imageURL = _item.imageURL;
    
    [self.webReview loadHTMLString:_item.reviews baseURL:nil];
    
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    
    self.contentScrollView.contentSize = self.contentView.frame.size;
}

@end
