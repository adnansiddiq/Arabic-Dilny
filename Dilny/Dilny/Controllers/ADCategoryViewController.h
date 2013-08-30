//
//  ADCategoryViewController.h
//  Dilny
//
//  Created by Adnan Siddiq on 17/08/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADBaseViewController.h"

#import "PullToRefreshView.h"

#import "ADCategoryCaller.h"

@interface ADCategoryViewController : ADBaseViewController <ADBaseCallerDelegate>

@property (weak, nonatomic) IBOutlet UITableView *categoryListView;
@end
