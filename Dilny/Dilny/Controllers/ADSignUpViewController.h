//
//  ADSignUpViewController.h
//  Dilny
//
//  Created by Adnan Siddiq on 05/09/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADBaseViewController.h"

@interface ADSignUpViewController : ADBaseViewController

@property (weak, nonatomic) IBOutlet UIScrollView *contentScrollView;
@property (weak, nonatomic) IBOutlet UIView *contentView;
@property (strong, nonatomic) IBOutlet UIView *accessoryView;
@property (weak, nonatomic) IBOutlet UITextField *txtName;
@property (weak, nonatomic) IBOutlet UITextField *txtEmail;
@property (weak, nonatomic) IBOutlet UITextField *txtPassword;

- (IBAction)doneKeyBoardAction:(UIBarButtonItem *)sender;
- (IBAction)changeTextField:(UISegmentedControl *)sender;
- (IBAction)signupAction:(UIButton *)sender;
@end
