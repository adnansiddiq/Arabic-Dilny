//
//  ADWebViewController.m
//  Massimiliano Valeriani
//
//  Created by Adnan Siddiq on 22/12/2012.
//  Copyright (c) 2012 Adnan Siddiq. All rights reserved.
//

#import "ADWebViewController.h"

@interface ADWebViewController ()

@end

@implementation ADWebViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    NSURL *url = [NSURL URLWithString:_urlString];
    NSURLRequest *request = [NSURLRequest requestWithURL:url];
    [self.webView loadRequest:request];
    
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(showBottomBar)];
    tap.numberOfTapsRequired = 2;
    tap.delegate = self;
    [self.webView addGestureRecognizer:tap];
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer {
    return YES;
}

- (void)setHideTimer {
    [timer invalidate];
    timer = nil;
    timer = [NSTimer scheduledTimerWithTimeInterval:5 target:self selector:@selector(hidebottomBar) userInfo:nil repeats:NO];
}

- (void)showBottomBar {
    if (_show) {
        [self hidebottomBar];
        [timer invalidate];
        timer = nil;
    } else {
        [UIView animateWithDuration:0.5 animations:^{
            self.bottomBar.alpha = 1;
        }];
        
        [self setHideTimer];
        _show = YES;
    }
}
- (void)hidebottomBar {
    _show = NO;
    [UIView animateWithDuration:1 animations:^{
        
        self.bottomBar.alpha = 0;
    }];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

- (IBAction)backAction:(id)sender {
    [self setHideTimer];
    
    if ([self.webView canGoBack]) {
        [self.webView goBack];
    }
}

- (IBAction)reloadAction:(id)sender {
    [self setHideTimer];
    
    if (!self.webView.isLoading) {
        //[self.webView reload];
        NSURL *url = [NSURL URLWithString:_urlString];
        NSURLRequest *request = [NSURLRequest requestWithURL:url];
        [self.webView loadRequest:request];
    }
}

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType {
    NSLog(@"%@", [request.URL absoluteString]);
    return YES;
}

- (void)webViewDidStartLoad:(UIWebView *)webView {
    [self.indicator startAnimating];
}

- (void)webViewDidFinishLoad:(UIWebView *)webView {
    [self.indicator stopAnimating];
    
    [self hidebottomBar];
}

- (void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error {
    if (!webView.isLoading) {
        [self.indicator stopAnimating];
    }
}
@end
