//
//  ADListViewController.h
//  Dilny
//
//  Created by Adnan Siddiq on 17/08/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADBaseViewController.h"

#import "PullToRefreshView.h"


@interface ADListViewController : ADBaseViewController

@property (weak, nonatomic) IBOutlet UITableView *listTableView;

@property (strong, nonatomic) NSArray *allItems;


- (void)loadData;

@end
