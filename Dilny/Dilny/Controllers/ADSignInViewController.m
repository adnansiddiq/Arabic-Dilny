//
//  ADSignInViewController.m
//  Dilny
//
//  Created by Adnan Siddiq on 21/09/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADSignInViewController.h"
#import "ADUserCaller.h"
#import "AppDelegate.h"

@interface ADSignInViewController () {
    
    UITextField *selectedTextField;
}

@property (weak, nonatomic) IBOutlet UITextField *txtEmail;
@property (weak, nonatomic) IBOutlet UITextField *txtPassword;
@property (weak, nonatomic) IBOutlet UIView *contentView;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;
@property (strong, nonatomic) IBOutlet UIView *accessoryView;
@property (weak, nonatomic) IBOutlet UIButton *btnCheckBox;

- (IBAction)doneKeyBoardAction:(UIBarButtonItem *)sender;
- (IBAction)changeTextField:(UISegmentedControl *)sender;
- (IBAction)signInAction:(UIButton *)sender;


@end

@implementation ADSignInViewController

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
    
    self.navigationController.navigationBarHidden = YES;
}

- (void)viewWillAppear:(BOOL)animated {
    
    [super viewWillAppear:animated];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillShow:) name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillHide:) name:UIKeyboardWillHideNotification object:nil];
    
    self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector(doneAction)];

}
- (void)viewWillDisappear:(BOOL)animated {
    
    [super viewWillDisappear:animated];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillHideNotification object:nil];
}

- (void)viewDidAppear:(BOOL)animated {
    
    [super viewDidAppear:animated];
    
    self.scrollView.contentSize = self.contentView.bounds.size;
    self.scrollView.autoresizesSubviews = NO;
}

- (IBAction)signInAction:(UIButton *)sender {
    
    [self.view endEditing:YES];
    
    if(self.txtEmail.text.isValidEmail) {
        
        if (self.txtPassword.text.isEmpty) {
            [self.txtPassword becomeFirstResponder];
        } else {
            [self showProgressViewFor:self.view withMessage:@"جاري تسجيل الدخول ..."];
            [ADUserCaller loginUserWithInfo:@{kEmailKey: self.txtEmail.text,
                                              kPasswordKey: self.txtPassword.text}
                                    success:^(NSString *responce){
                                        
                                        NSLog(@"Responce: %@", responce);
                                        
                                        if ([responce isEqualToString:ERROR_CODE_LOGIN_NOTEXIST]) {
                                            [self showAlertViewWithMessage:@"عفواً , بياناتك غير صحيحه"];
                                        } else {
                                            NSArray *component = [responce componentsSeparatedByString:@","];
                                            
                                            if ([component[1] isEqualToString:@"1"]) {
                                                [self showAlertViewWithMessage:@"لقد استلمت رسالة تفعيل عبر البريد نرجو التفعيل"];
                                            } else {
                                                
                                                AppDelegate *app = [AppDelegate sharedInstance];
                                                app.userID = component[0];
                                                if (self.btnCheckBox.isSelected) {
                                                    [app saveUserID];
                                                }
                                                [self performSelector:@selector(successfullResponce) withObject:nil afterDelay:0.0];
                                            }
                                        }
                                        
                                        [self hideProgressView];
                                        
                                    } failure:^(NSError *error) {
                                        [self hideProgressView];
                                        
                                        [self showAlertViewWithMessage:error.localizedDescription];
                                    }];
        }
        
    } else {
        [self showAlertViewWithMessage:@"عفواً , البريد الإلكتورني غير صحيح"];
    }
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
- (IBAction)checkBoxAction:(UIButton *)sender {
    
    sender.selected = !sender.selected;
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
    
    [self.scrollView setContentOffset:CGPointMake(0, textField.frame.origin.y - textField.frame.size.height) animated:YES];
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

- (void)keyboardWillShow:(NSNotification *)notification {
    
    NSDictionary *userInfo = [notification userInfo];
    NSValue* aValue = [userInfo objectForKey:UIKeyboardFrameEndUserInfoKey];
    CGRect keyboardRect = [aValue CGRectValue];
    keyboardRect = [self.view convertRect:keyboardRect fromView:nil];
    
    CGFloat keyboardTop = keyboardRect.origin.y;
    CGRect newTextViewFrame = self.scrollView.frame;
    newTextViewFrame.size.height = keyboardTop - self.scrollView.frame.origin.y;
    NSValue *animationDurationValue = [userInfo objectForKey:UIKeyboardAnimationDurationUserInfoKey];
    
    NSTimeInterval animationDuration;
    [animationDurationValue getValue:&animationDuration];
    [UIView beginAnimations:nil context:NULL];
    [UIView setAnimationDuration:animationDuration];
    self.scrollView.frame = newTextViewFrame;
    [UIView commitAnimations];
}


- (void)keyboardWillHide:(NSNotification *)notification {
    
    self.scrollView.frame = self.bottomView.bounds;
}

- (void)successfullResponce {
    
    [self dismissViewControllerAnimated:YES completion:nil];
    
    self.txtEmail.text = [NSString emptyString];
    self.txtPassword.text = [NSString emptyString];
}

- (void)previousTextFieldSelection {
    
    NSInteger nextTag;
    nextTag = selectedTextField.tag - 1;
    
    if (nextTag <= 0) {
        [self.txtEmail becomeFirstResponder];
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
