//
//  ADAboutViewController.h
//  Dilny
//
//  Created by Adnan Siddiq on 30/08/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADBaseViewController.h"
#import <MessageUI/MessageUI.h>

@interface ADAboutViewController : ADBaseViewController <MFMailComposeViewControllerDelegate, UINavigationControllerDelegate>

- (IBAction)mailSiteAction:(UIButton *)sender;
- (IBAction)homeSiteAction:(UIButton *)sender;

- (IBAction)mailAction:(UIButton *)sender;
- (IBAction)homeAction:(UIButton *)sender;
- (IBAction)facebookAction:(UIButton *)sender;
- (IBAction)twitterAction:(UIButton *)sender;


@end
