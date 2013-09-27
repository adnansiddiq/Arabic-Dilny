//
//  ADBaseTableViewController.m
//  Dilny
//
//  Created by Adnan Siddiq on 21/09/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADBaseTableViewController.h"

@interface ADBaseTableViewController ()

@end

@implementation ADBaseTableViewController

- (void)showProgressViewFor:(UIView *)view withMessage:(NSString *)string {
    HUD = [MBProgressHUD showHUDAddedTo:view animated:YES];
    HUD.delegate = self;
    HUD.labelText = string;
}
- (void)hideProgressView {
    [HUD hide:YES];
}

- (void)hudWasHidden:(MBProgressHUD *)hud {
	[HUD removeFromSuperview];
	HUD = nil;
}

- (IBAction)doneAction {
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (BOOL)isDeviceiPhone {
    
    if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
        return YES;
    } else {
        return NO;
    }
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 0;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    return nil;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    return 0.0f;
}

@end
