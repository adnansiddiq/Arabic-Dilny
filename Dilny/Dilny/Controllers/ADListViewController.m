//
//  ADListViewController.m
//  Dilny
//
//  Created by Adnan Siddiq on 17/08/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADListViewController.h"

#import "ADDetailViewController.h"

#import "ADItemsViewController.h"

#import "ADItemCell.h"

#import "ADLoadingViewCell.h"

#import "AppDelegate.h"

@interface ADListViewController () {
    
    PullToRefreshView *_pullToRefresh;
    UIRefreshControl *_refreshControl;
    
    BOOL _isMoreLoading;
}

@end

@implementation ADListViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        
        _allItems = @[];
        
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.navigationController.navigationBarHidden = YES;
    ADItemsViewController *vc = (ADItemsViewController *)self.parentViewController;

    if (vc.isFavourite) {
        _isMoreLoading = NO;
        return;
    }
    
    if (NSClassFromString(@"UIRefreshControl")) {
        
        _refreshControl = [[UIRefreshControl alloc] init];
        _refreshControl.tintColor = [UIColor greenColor];
        [_refreshControl addTarget:self action:@selector(refreshData:) forControlEvents:UIControlEventValueChanged];
        [self.listTableView addSubview:_refreshControl];
        
    } else {
        _pullToRefresh = [[PullToRefreshView alloc] initWithScrollView:(UIScrollView *) self.listTableView];
        _pullToRefresh.delegate = (id)self;
        [self.listTableView addSubview:_pullToRefresh];
    }
    
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    [self.listTableView reloadData];

    [self.listTableView deselectRowAtIndexPath:[self.listTableView indexPathForSelectedRow] animated:YES];
}
- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    
}

- (void)refreshData:(UIRefreshControl *)sender {
    
    ADItemsViewController *vc = (ADItemsViewController *)self.parentViewController;
    [vc refreshData];
}

- (void)pullToRefreshViewShouldRefresh:(PullToRefreshView *)view {

    ADItemsViewController *vc = (ADItemsViewController *)self.parentViewController;
    [vc refreshData];
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return _isMoreLoading?_allItems.count + 1 : _allItems.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.row == _allItems.count) {
        ADLoadingViewCell *loading = (ADLoadingViewCell *)[[[NSBundle mainBundle] loadNibNamed:@"ADLoadingViewCell" owner:nil options:nil] objectAtIndex:0];
        return loading;
    }

    
    static NSString *CellIdentifier = @"Cell";
    
    ADItemCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) {
        cell = (ADItemCell *)[[[NSBundle mainBundle] loadNibNamed:@"ADItemCell" owner:nil options:nil] objectAtIndex:0];
    }

    cell.item = _allItems[indexPath.row];
    
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.row == _allItems.count) {
        return 40.0f;
    }

    return 125.0f;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {

    ADItemsViewController *vc = (ADItemsViewController *)self.parentViewController;
    [vc selectedItemDetailWithIndex:indexPath.row];
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath {

    ADItemsViewController *vc = (ADItemsViewController *)self.parentViewController;
    
    if (vc.listCaller.noMoreLoading) return;
    
        if (indexPath.row == _allItems.count - 2) {
            if (!_isMoreLoading) {
                _isMoreLoading = YES;
                [[NSOperationQueue mainQueue] addOperationWithBlock:^{
                    NSLog(@"%d", _allItems.count);
                    [self.listTableView insertRowsAtIndexPaths:[NSArray arrayWithObject:[NSIndexPath indexPathForRow:_allItems.count inSection:0]]
                                          withRowAnimation:UITableViewRowAnimationFade];
                }];
                
                [vc moreLoading];
            }
        }
}


#pragma mark - Delgates

- (void)loadData {

    _isMoreLoading = NO;
    [self.listTableView reloadData];
    
    if (_pullToRefresh) {
        [_pullToRefresh finishedLoading];
    } else {
        [_refreshControl endRefreshing];
    }
    
    UIView *v = [self.view viewWithTag:10];
    if (v) {
        [v removeFromSuperview];
    }
    
    if (_allItems.count == 0) {
        
        UILabel *noResult = [[UILabel alloc] initWithFrame:self.view.bounds];
        noResult.numberOfLines = 0;
        noResult.textColor = [UIColor blackColor];
        noResult.tag = 10;
        noResult.backgroundColor = [UIColor whiteColor];
        noResult.text = @"اتوجد اماكن مفضلة لديك، بامكانك تصفح الاماكن ، والضغط على النجمه لاضافة الموقع للمفضلة";
        noResult.textAlignment = NSTextAlignmentCenter;
        [self.view addSubview:noResult];
    }

}


@end
