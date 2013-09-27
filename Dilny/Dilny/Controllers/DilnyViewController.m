//
//  DilnyViewController.m
//  Dilny
//
//  Created by Adnan Siddiq on 08/07/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "DilnyViewController.h"

#import "ADCategoryViewController.h"

#import "ADAboutViewController.h"

#import "ADSignUpViewController.h"

#import "ADSignInViewController.h"

#import "ADItemsViewController.h"

#import "AppDelegate.h"

#import "LocationManager.h"

@interface DilnyViewController () {
    
}

@property (weak, nonatomic) IBOutlet UIButton *btnLogin;
@property (weak, nonatomic) IBOutlet UISearchBar *searchBar;
@end

@implementation DilnyViewController

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
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(locationFound:) name:kLocationStatusChanged object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(addressFound:) name:kAddressFindDone object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(addressNotFound:) name:kAddressNOResult object:nil];
}

- (void)viewWillAppear:(BOOL)animated {
    
    [super viewWillAppear:animated];
    
    if([[AppDelegate sharedInstance] userID]) {
        [self.btnLogin setImage:[UIImage imageNamed:@"out.png"] forState:UIControlStateNormal];
        [self.btnLogin addTarget:self action:@selector(logOutAction:) forControlEvents:UIControlEventTouchUpInside];
        
        [[AppDelegate sharedInstance] loadFavourites];
    } else {
        [self.btnLogin setImage:[UIImage imageNamed:@"ic_sigin.png"] forState:UIControlStateNormal];
        [self.btnLogin addTarget:self action:@selector(signInAction:) forControlEvents:UIControlEventTouchUpInside];
    }
    
}

- (IBAction)nearbyAction:(UIButton *)sender {
    
    ADItemsViewController *listVC = [[ADItemsViewController alloc] initWithNibName:@"ADItemsViewController" bundle:nil];
    listVC.catID = @"all";
    listVC.searchPath = @"searchNearby";
    listVC.mapSelected = NO;
    [self.navigationController pushViewController:listVC animated:YES];
}

- (IBAction)categoryAction:(UIButton *)sender {
    
    ADCategoryViewController *catVC = [[ADCategoryViewController alloc] initWithNibName:@"ADCategoryViewController" bundle:nil];
    [self.navigationController pushViewController:catVC animated:YES];
}

- (IBAction)favouriteAction:(UIButton *)sender {
    
    if ([[AppDelegate sharedInstance] userID] == nil) {
        [self showAlertViewWithMessage:@"عفواً , لم تقم بتسجيل الدخول بعد"];
        return;
    }
    
    if ([[[AppDelegate sharedInstance] favouriteItems] count]) {
        
        ADItemsViewController *listVC = [[ADItemsViewController alloc] initWithNibName:@"ADItemsViewController" bundle:nil];
        listVC.isFavourite = YES;
        listVC.mapSelected = NO;
        [self.navigationController pushViewController:listVC animated:YES];

    } else {
        [self showProgressViewFor:self.view withMessage:@"جاري التحميل ..."];
        
        [[AppDelegate sharedInstance] loadFavouritesWithSucess:^(){
            
            [self hideProgressView];
            ADItemsViewController *listVC = [[ADItemsViewController alloc] initWithNibName:@"ADItemsViewController" bundle:nil];
            listVC.isFavourite = YES;
            listVC.mapSelected = NO;
            [self.navigationController pushViewController:listVC animated:YES];

            
        } failure:^(NSError *error) {
            
            [self hideProgressView];
            
            [self showAlertViewWithMessage:error.localizedDescription];
        }];
    }
}

- (IBAction)addNewAction:(UIButton *)sender {
}

- (IBAction)mapAction:(UIButton *)sender {
    
    ADItemsViewController *listVC = [[ADItemsViewController alloc] initWithNibName:@"ADItemsViewController" bundle:nil];
    listVC.catID = @"all";
    listVC.searchPath = @"searchNearby";
    listVC.mapSelected = YES;
    [self.navigationController pushViewController:listVC animated:YES];

}

- (IBAction)infoAction:(UIButton *)sender {
    
    ADAboutViewController *aboutVC = [[ADAboutViewController alloc] initWithNibName:@"ADAboutViewController" bundle:nil];
    [self.navigationController pushViewController:aboutVC animated:YES];
}

- (IBAction)signInAction:(UIButton *)sender {
    
    [UIAlertView alertViewWithTitle:@"دلني"
                            message:@"أهلاً بك , هل لديك حساب , أم انك مستخدم جديد ؟!"
                  cancelButtonTitle:@"تسجيل الدخول"
                  otherButtonTitles:@[@"تسجيل عضوية جديدة"]
                          onDismiss:^(int buttonIndex) {

                              ADSignInViewController *signup = [[ADSignInViewController alloc] initWithNibName:@"ADSignInViewController" bundle:nil];
                              UINavigationController *navController = [[UINavigationController alloc] initWithRootViewController:signup];
                              [self presentViewController:navController animated:YES completion:nil];

                          } onCancel:^{
                              
                              ADSignUpViewController *signup = [[ADSignUpViewController alloc] initWithNibName:@"ADSignUpViewController" bundle:nil];
                              UINavigationController *navController = [[UINavigationController alloc] initWithRootViewController:signup];
                              [self presentViewController:navController animated:YES completion:nil];

                          }];
    
}

- (void)logOutAction:(UIButton *)sender {
    [[AppDelegate sharedInstance] setUserID:nil];
    [[AppDelegate sharedInstance] saveUserID];
    
    [self.btnLogin setImage:[UIImage imageNamed:@"ic_sigin.png"] forState:UIControlStateNormal];
    [self.btnLogin addTarget:self action:@selector(signInAction:) forControlEvents:UIControlEventTouchUpInside];
}

- (void)updateProgressText:(NSNumber *)index {
    
    if (index.intValue == 1) {
        self.tvLabel.text = @"جاري تحديد المكان .";
        [self performSelector:@selector(updateProgressText:) withObject:[NSNumber numberWithInt:2] afterDelay:0.5];
    } else if (index.intValue == 2) {
        self.tvLabel.text = @"جاري تحديد المكان ..";
        [self performSelector:@selector(updateProgressText:) withObject:[NSNumber numberWithInt:3] afterDelay:0.5];
    } else if (index.intValue == 3) {
        self.tvLabel.text = @"جاري تحديد المكان ...";
        [self performSelector:@selector(updateProgressText:) withObject:[NSNumber numberWithInt:0] afterDelay:0.5];
    } else {
        self.tvLabel.text = @"جاري تحديد المكان ";
        [self performSelector:@selector(updateProgressText:) withObject:[NSNumber numberWithInt:1] afterDelay:0.5];
    }
}
#pragma mark - Notification Method
- (void)locationFound:(NSNotification *)notification {
    NSNumber *status = notification.object;
    if ([status boolValue]) {
        self.tvLabel.text = @"جاري تحديد المكان .";
        [self performSelector:@selector(updateProgressText:) withObject:[NSNumber numberWithInt:2] afterDelay:0.5];
    } else {
        
    }
}

- (void)addressFound:(NSNotification *)notification {
    
    
    [NSObject cancelPreviousPerformRequestsWithTarget:self];
    
    NSNumber *status = notification.object;
    if ([status boolValue]) {
        self.tvLabel.text = [[LocationManager instance] fullAddress];
    } else {
        self.tvLabel.text = @"عفواً , حدث خطأ في الاتصال";
    }

}

- (void)addressNotFound:(NSNotification *)notification {

    [NSObject cancelPreviousPerformRequestsWithTarget:self];

    self.tvLabel.text = @"عفواً , تعذر الوصول لعنوان مكانك الحالي";
}


#pragma mark - SearchBar Delegate

- (BOOL)searchBarShouldBeginEditing:(UISearchBar *)searchBar {
    [searchBar setShowsCancelButton:YES animated:YES];
    return YES;
}
- (BOOL)searchBarShouldEndEditing:(UISearchBar *)searchBar {
    [searchBar setShowsCancelButton:NO animated:YES];
    return YES;
}

- (void)searchBarCancelButtonClicked:(UISearchBar *) searchBar {
    [searchBar resignFirstResponder];
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar {
    [self.view endEditing:YES];
    
    ADItemsViewController *listVC = [[ADItemsViewController alloc] initWithNibName:@"ADItemsViewController" bundle:nil];
    listVC.catID = @"all";
    listVC.searchText = searchBar.text;
    listVC.searchPath = @"searchByText";
    listVC.mapSelected = YES;
    [self.navigationController pushViewController:listVC animated:YES];
    
    searchBar.text = [NSString emptyString];

}
@end
