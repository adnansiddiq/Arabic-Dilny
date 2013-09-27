//
//  DilnyViewController.h
//  Dilny
//
//  Created by Adnan Siddiq on 08/07/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADBaseViewController.h"

@interface DilnyViewController : ADBaseViewController <UISearchBarDelegate>

@property (weak, nonatomic) IBOutlet UILabel *tvLabel;

- (IBAction)nearbyAction:(UIButton *)sender;
- (IBAction)categoryAction:(UIButton *)sender;
- (IBAction)favouriteAction:(UIButton *)sender;
- (IBAction)addNewAction:(UIButton *)sender;
- (IBAction)infoAction:(UIButton *)sender;

- (IBAction)signInAction:(UIButton *)sender;
@end
