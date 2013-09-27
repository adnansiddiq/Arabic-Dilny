//
//  ADItemsViewController.h
//  Dilny
//
//  Created by Adnan Siddiq on 22/09/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADBaseViewController.h"

#import "ADListCaller.h"

@interface ADItemsViewController : ADBaseViewController<ADBaseCallerDelegate>

@property (strong, nonatomic) NSString *catID;
@property (strong, nonatomic) NSString *searchPath;
@property (strong, nonatomic) NSString *searchText;
@property (strong, nonatomic) ADListCaller *listCaller;

@property (assign, nonatomic) BOOL isFavourite;

@property (assign, nonatomic) BOOL mapSelected;

//@property (assign, nonatomic) BOOL isMoreLoading;

- (void)moreLoading;
- (void)refreshData;

- (void)selectedItemDetailWithIndex:(NSInteger)index_;

@end
