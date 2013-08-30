//
//  DilnyViewController.m
//  Dilny
//
//  Created by Adnan Siddiq on 08/07/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "DilnyViewController.h"

#import "ADCategoryViewController.h"

@interface DilnyViewController ()

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
    
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)nearbyAction:(UIButton *)sender {
}

- (IBAction)categoryAction:(UIButton *)sender {
    
    ADCategoryViewController *catVC = [[ADCategoryViewController alloc] initWithNibName:@"ADCategoryViewController" bundle:nil];
    
    [self.navigationController pushViewController:catVC animated:YES];
}

- (IBAction)favouriteAction:(UIButton *)sender {
}

- (IBAction)addNewAction:(UIButton *)sender {
}

- (IBAction)infoAction:(UIButton *)sender {
}
@end
