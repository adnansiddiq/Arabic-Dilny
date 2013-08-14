//
//  ADBaseViewController.h
//  Massimiliano Valeriani
//
//  Created by Adnan Siddiq on 22/12/2012.
//  Copyright (c) 2012 Adnan Siddiq. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "PullToRefreshView.h"
#import "MBProgressHUD.h"
#import "NSDate+DateFormatter.h"
#import "Constant.h"


typedef void (^RevealBlock)();

@interface ADBaseViewController : UIViewController<MBProgressHUDDelegate, PullToRefreshViewDelegate, UITableViewDataSource, UITableViewDelegate> {

    MBProgressHUD *HUD;
    BOOL bannerIsVisible;
    
    
@private
    RevealBlock _revealBlock;
}

- (id)initWithTitle:(NSString *)title withRevealBlock:(RevealBlock)revealBlock;

- (void)showAlertViewWithMessage:(NSString *)message;

- (void)showProgressViewFor:(UIView *)view withMessage:(NSString *)string;
- (void)hideProgressView;

- (BOOL)isDeviceiPhone;

@end
