//
//  ADSignUpViewController.m
//  Dilny
//
//  Created by Adnan Siddiq on 05/09/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADSignUpViewController.h"
#import "ADUserCaller.h"

@interface ADSignUpViewController () {
    
    UITextField *selectedTextField;
    
}

@end

@implementation ADSignUpViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.title = @"تسجيل مستخدم جديد";
    
    self.navigationController.navigationBarHidden = YES;
    
    self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector(doneAction)];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillShow:) name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillHide:) name:UIKeyboardWillHideNotification object:nil];
}
- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillHideNotification object:nil];
}

- (void)viewDidAppear:(BOOL)animated {
    
    [super viewDidAppear:animated];
    
    self.contentScrollView.contentSize = self.contentView.bounds.size;
    
    self.contentScrollView.autoresizesSubviews = NO;
}
- (void)dealloc {
    
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillHideNotification object:nil];
}

- (void)sendFeedbackAction {
    
    NSDictionary *info = @{kEmailKey: self.txtEmail.text,
                           kPasswordKey: self.txtPassword.text,
                           kNameKey: self.txtName.text};
    [self showProgressViewFor:self.view withMessage:@""];
    [ADUserCaller registerUserWithInfo:info
                               success:^(NSString *responce) {
                                   
                                   [self hideProgressView];
                                   
                                   if ([responce isEqualToString:ERROR_CODE_SIGNUP_USEREXIST]) {
                                       [self showAlertViewWithMessage:@"عفواً , هذا المستخدم موجود مسبقاً"];
                                   } else {
                                       [UIAlertView alertViewWithTitle:@"Dilny"
                                                               message:@"لقد استلمت رسالة تفعيل عبر البريد نرجو التفعيل"
                                                     cancelButtonTitle:@"Cancel"
                                                     otherButtonTitles:nil
                                                             onDismiss:nil onCancel:^{
                                                                 [self successfullResponce];
                                                             }];
                                   }
                               } failure:^(NSError *error) {
                                   NSLog(@"Error Code %d", error.code);
                                   [self hideProgressView];
                                   [self showAlertViewWithMessage:error.localizedDescription];
                               }];
    
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    NSInteger nextTag = textField.tag + 1;
    UIResponder* nextResponder = [textField.superview viewWithTag:nextTag];
    if (nextResponder) {
        [nextResponder becomeFirstResponder];
    } else {
        [textField resignFirstResponder];
    }
    return NO;
}


- (IBAction)doneKeyBoardAction:(UIBarButtonItem *)sender {
    
    [selectedTextField resignFirstResponder];
}

- (IBAction)changeTextField:(UISegmentedControl *)sender {
    if (sender.selectedSegmentIndex == 0) {
        [self previousTextFieldSelection];
    } else {
        [self nextTextFieldSelection];
    }
}

- (IBAction)signupAction:(UIButton *)sender {
    
    [self.view endEditing:YES];
    
    if (self.txtName.text.isEmpty) {
        self.txtName.backgroundColor = [UIColor redColor];
        [self.txtName becomeFirstResponder];
        return;
    }
    if (self.txtPassword.text.isEmpty) {
        self.txtPassword.backgroundColor = [UIColor redColor];
        [self.txtPassword becomeFirstResponder];
        return;
    }
    
    if (self.txtPassword.text.length < 5 || self.txtPassword.text.length > 40) {
        [self showAlertViewWithMessage:@"عفواً, كلمة المرور أقل من ٥ حروف أو أكثر من ٤٠ حرف"];
        return;
    }
    if (self.txtName.text.length < 5 || self.txtName.text.length > 40) {
        [self showAlertViewWithMessage:@"عفواً, اسم المستخدم أقل من ٥ حروف أو  أكثر من ٤٠ حرف"];
        return;
    }

    
    if (self.txtEmail.text.isValidEmail) {
        [self sendFeedbackAction];
        
    } else {
        [self showAlertViewWithMessage:@"عفواً , البريد الإلكتورني غير صحيح"];
    }

}


- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
    
    textField.backgroundColor = [UIColor whiteColor];
    return YES;
}

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField {
    
    if (textField.inputAccessoryView == nil) {
        
        textField.inputAccessoryView = self.accessoryView;
    }
    selectedTextField = textField;
    
    return YES;
}


- (void)textFieldDidBeginEditing:(UITextField *)textField {
    
    [self.contentScrollView setContentOffset:CGPointMake(0, textField.frame.origin.y - textField.frame.size.height) animated:YES];
}


- (void)keyboardWillShow:(NSNotification *)notification {
    
    NSDictionary *userInfo = [notification userInfo];
    NSValue* aValue = [userInfo objectForKey:UIKeyboardFrameEndUserInfoKey];
    CGRect keyboardRect = [aValue CGRectValue];
    keyboardRect = [self.view convertRect:keyboardRect fromView:nil];
    
    CGFloat keyboardTop = keyboardRect.origin.y;
    CGRect newTextViewFrame = self.contentScrollView.frame;
    newTextViewFrame.size.height = keyboardTop - self.contentScrollView.frame.origin.y;
    NSValue *animationDurationValue = [userInfo objectForKey:UIKeyboardAnimationDurationUserInfoKey];
    
    NSTimeInterval animationDuration;
    [animationDurationValue getValue:&animationDuration];
    [UIView beginAnimations:nil context:NULL];
    [UIView setAnimationDuration:animationDuration];
    self.contentScrollView.frame = newTextViewFrame;
    [UIView commitAnimations];
}


- (void)keyboardWillHide:(NSNotification *)notification {
    
    self.contentScrollView.frame = self.bottomView.bounds;
}

- (void)successfullResponce {
    
    [self dismissViewControllerAnimated:YES completion:nil];
    
    self.txtName.text = [NSString emptyString];
    self.txtEmail.text = [NSString emptyString];
    self.txtPassword.text = [NSString emptyString];
}

- (void)previousTextFieldSelection {
    
    NSInteger nextTag;
    nextTag = selectedTextField.tag - 1;
    
    if (nextTag <= 0) {
        [self.txtName becomeFirstResponder];
        return;
    }
    UIResponder* nextResponder = [selectedTextField.superview viewWithTag:nextTag];
    if (nextResponder) {
        [nextResponder becomeFirstResponder];
    } else {
        [selectedTextField resignFirstResponder];
    }
}

- (void)nextTextFieldSelection {
    
    NSInteger nextTag = selectedTextField.tag + 1;
    UIResponder* nextResponder = [selectedTextField.superview viewWithTag:nextTag];
    if (nextResponder) {
        [nextResponder becomeFirstResponder];
    } else {
        [selectedTextField resignFirstResponder];
    }
}

@end
