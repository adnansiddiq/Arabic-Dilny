//
//  ADBaseViewController.m
//  Massimiliano Valeriani
//
//  Created by Adnan Siddiq on 22/12/2012.
//  Copyright (c) 2012 Adnan Siddiq. All rights reserved.
//

#import "ADBaseViewController.h"

#pragma mark -
#pragma mark Private Interface
@interface ADBaseViewController ()
- (void)revealSidebar;
@end


@implementation ADBaseViewController

- (id)initWithTitle:(NSString *)title withRevealBlock:(RevealBlock)revealBlock {
    if (self = [super initWithNibName:nil bundle:nil]) {
		self.title = [title capitalizedString];
		_revealBlock = [revealBlock copy];
        
        UIBarButtonItem *item = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"menu"]
                                                                 style:UIBarButtonItemStyleBordered target:self action:@selector(revealSidebar)];
        
		self.navigationItem.leftBarButtonItem = item;
	}
	return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    //[self.navigationController.navigationBar setBackgroundImage:[UIImage imageNamed:@"top_nav"] forBarMetrics:UIBarMetricsDefault];
    
    
    
}

- (void)revealSidebar {
	_revealBlock();
}

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

- (void)showAlertViewWithMessage:(NSString *)message {
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:APP_NAME
                                                        message:message
                                                       delegate:nil
                                              cancelButtonTitle:NSLocalizedString(@"OK_KEY", nil)
                                              otherButtonTitles:nil];
    [alertView show];
}

- (BOOL)isDeviceiPhone {
    
    if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
        return YES;
    } else {
        return NO;
    }
}


@end
