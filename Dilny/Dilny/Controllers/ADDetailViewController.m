//
//  ADDetailViewController.m
//  Dilny
//
//  Created by Adnan Siddiq on 17/08/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADDetailViewController.h"
#import "AppDelegate.h"

#import "ADUserCaller.h"

@interface ADDetailViewController ()

@property (weak, nonatomic) IBOutlet UIButton *btnFav;
@property (weak, nonatomic) IBOutlet UIButton *btnMap;
@property (weak, nonatomic) IBOutlet UIButton *btnShare;
@property (weak, nonatomic) IBOutlet UIButton *btnCall;

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
    
    if ([[AppDelegate sharedInstance] userID]) {
        
        self.btnFav.enabled = YES;
        self.btnFav.selected = [[AppDelegate sharedInstance] itemIsFavourite:_item];
        
    } else {
        
        self.btnFav.enabled = NO;
    }
    
    if (_item.phone.isEmpty) {
        self.btnCall.enabled = NO;
    }
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    
    self.contentScrollView.contentSize = self.contentView.frame.size;
}

- (IBAction)btnFavouritAction:(UIButton *)sender {
    
    [self showProgressViewFor:self.view withMessage:@"جاري التقييم , الرجاء الإنتظار .."];
    
    [ADUserCaller favLocationWithInfo:@{kUserIDKey: [[AppDelegate sharedInstance] userID],
                                        kLocationIDKey: _item.itemID}
                              success:^(NSString *responce) {
                                  
                                  [self hideProgressView];
                                  
                                  if ([responce isEqualToString:FAV_SUCCESS_RESPONCE]) {
                                      
                                      self.btnFav.selected = !self.btnFav.selected;
                                      
                                      if (self.btnFav.selected) {
                                          [[AppDelegate sharedInstance] addFavouriteItem:_item];
                                      } else {
                                          [[AppDelegate sharedInstance] removeFavouriteItem:_item];
                                      }
                                  } else {
                                      [self showAlertViewWithMessage:@"Unknown Error."];
                                  }
                                  
                              } failure:^(NSError *error) {
                                  
                                  [self hideProgressView];
                                  
                                  [self showAlertViewWithMessage:error.localizedDescription];
                                  
                              }];
    
}

- (IBAction)btnMapAction:(id)sender {
    
}

- (IBAction)btnShareAction:(id)sender {
    
    NSString *locationString = [NSString stringWithFormat:@"http://maps.google.com/maps?q=%@,%@", _item.latitude, _item.longitude];
    NSArray* dataToShare = @[_item.title, [NSURL URLWithString:locationString]];
    
    
    UIActivityViewController* activityViewController = [[UIActivityViewController alloc] initWithActivityItems:dataToShare
                                                                                         applicationActivities:nil];
    
    [self presentViewController:activityViewController animated:YES completion:^{}];
}

- (IBAction)btnCallAction:(id)sender {
    
    
    NSString *cleanedString = [[_item.phone componentsSeparatedByCharactersInSet:[[NSCharacterSet characterSetWithCharactersInString:@"0123456789-+()"] invertedSet]] componentsJoinedByString:@""];
    NSURL *telURL = [NSURL URLWithString:[NSString stringWithFormat:@"tel:%@", cleanedString]];
    UIApplication *myApp = [UIApplication sharedApplication];
    NSLog(@"making call with %@",telURL);
    [myApp openURL:telURL];
}

@end
