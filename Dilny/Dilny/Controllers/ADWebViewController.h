//
//  ADWebViewController.h
//  Massimiliano Valeriani
//
//  Created by Adnan Siddiq on 22/12/2012.
//  Copyright (c) 2012 Adnan Siddiq. All rights reserved.
//

#import "ADBaseViewController.h"

@interface ADWebViewController : ADBaseViewController<UIGestureRecognizerDelegate> {
    NSTimer *timer;
    BOOL _show;
}

@property (weak, nonatomic) IBOutlet UIWebView *webView;
@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *indicator;
@property (weak, nonatomic) IBOutlet UIToolbar *bottomBar;

@property (strong, nonatomic) NSString *urlString;

- (IBAction)backAction:(id)sender;
- (IBAction)reloadAction:(id)sender;
@end
