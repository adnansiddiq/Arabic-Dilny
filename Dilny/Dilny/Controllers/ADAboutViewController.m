//
//  ADAboutViewController.m
//  Dilny
//
//  Created by Adnan Siddiq on 30/08/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADAboutViewController.h"
#import "ADWebViewController.h"

@interface ADAboutViewController ()

@end

@implementation ADAboutViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title = @"About";
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)mailSiteAction:(UIButton *)sender {
    
    if ([MFMailComposeViewController canSendMail]) {
    
        MFMailComposeViewController *mailVC = [[MFMailComposeViewController alloc] init];
        mailVC.mailComposeDelegate = self;
        [mailVC setSubject:@"Sent From Dilny iOS App"];
        [mailVC setToRecipients:@[@"Dilny.com@gmail.com"]];
        [self presentViewController:mailVC animated:YES completion:nil];
        
    }
    
}

- (IBAction)homeSiteAction:(UIButton *)sender {
    ADWebViewController *webVC = [[ADWebViewController alloc] initWithNibName:@"ADWebViewController" bundle:nil];
    webVC.urlString = @"http://www.dilny.com";
    [self.navigationController pushViewController:webVC animated:YES];
}

- (IBAction)mailAction:(UIButton *)sender {
    
    if ([MFMailComposeViewController canSendMail]) {
        
        MFMailComposeViewController *mailVC = [[MFMailComposeViewController alloc] init];
        mailVC.mailComposeDelegate = self;
        [mailVC setSubject:@"Sent From Dilny iOS App"];
        [mailVC setToRecipients:@[@"info@etqanApps.com"]];
        [self presentViewController:mailVC animated:YES completion:nil];
        
    }

}

- (IBAction)homeAction:(UIButton *)sender {
    
    ADWebViewController *webVC = [[ADWebViewController alloc] initWithNibName:@"ADWebViewController" bundle:nil];
    webVC.urlString = @"http://www.EtqanApps.com";
    [self.navigationController pushViewController:webVC animated:YES];
}

- (IBAction)facebookAction:(UIButton *)sender {
    ADWebViewController *webVC = [[ADWebViewController alloc] initWithNibName:@"ADWebViewController" bundle:nil];
    webVC.urlString = @"https://www.facebook.com/EtqanApps";
    [self.navigationController pushViewController:webVC animated:YES];
}

- (IBAction)twitterAction:(UIButton *)sender {
    ADWebViewController *webVC = [[ADWebViewController alloc] initWithNibName:@"ADWebViewController" bundle:nil];
    webVC.urlString = @"http://www.twitter.com/EtqanApps";
    [self.navigationController pushViewController:webVC animated:YES];
}


- (void)mailComposeController:(MFMailComposeViewController *)controller didFinishWithResult:(MFMailComposeResult)result error:(NSError *)error {

    
    [controller dismissViewControllerAnimated:YES completion:^ {
        switch (result) {
                
            case MFMailComposeResultCancelled:
                
                break;
                
            case MFMailComposeResultFailed:
                [self showAlertViewWithMessage:error.localizedDescription];
                break;
                
            case MFMailComposeResultSaved:
                [self showAlertViewWithMessage:@"Email Saved."];
                break;
                
            case MFMailComposeResultSent:
                [self showAlertViewWithMessage:@"Email Sent."];
                break;
                
            default:
                break;
        }

            }];
}
@end
