//
//  ADBaseTableViewController.h
//  Dilny
//
//  Created by Adnan Siddiq on 21/09/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "MBProgressHUD.h"

@interface ADBaseTableViewController : UITableViewController<MBProgressHUDDelegate> {
    
    MBProgressHUD *HUD;
}

- (void)showProgressViewFor:(UIView *)view withMessage:(NSString *)string;
- (void)hideProgressView;

- (BOOL)isDeviceiPhone;

- (IBAction)doneAction;


@end
