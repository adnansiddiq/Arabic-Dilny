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
    
    if ([self systemVersion] != 7) {
        CGRect frame = self.topView.frame;
        frame.size.height -= 20;
        self.topView.frame = frame;
        
        frame = self.bottomView.frame;
        frame.origin.y -= 20;
        frame.size.height += 20;
        self.bottomView.frame = frame;
        
        frame = self.logoImage.frame;
        frame.origin.y = 5;
        frame.size.height = 38;
        self.logoImage.frame = frame;
    }
}

- (IBAction)doneAction {
    [self dismissViewControllerAnimated:YES completion:nil];
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

- (NSInteger)systemVersion {
    
    NSString *v = [[UIDevice currentDevice] systemVersion];
    NSArray *comp = [v componentsSeparatedByString:@"."];
    return [comp[0] integerValue];
}
- (void)showAlertViewWithMessage:(NSString *)message {
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"دلني"
                                                        message:message
                                                       delegate:nil
                                              cancelButtonTitle:NSLocalizedString(@"OK", nil)
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

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 0;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    return nil;
}

- (IBAction)backAction {
    [self.navigationController popViewControllerAnimated:YES];
}

@end
