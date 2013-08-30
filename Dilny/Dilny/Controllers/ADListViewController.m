//
//  ADListViewController.m
//  Dilny
//
//  Created by Adnan Siddiq on 17/08/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADListViewController.h"

#import "ADDetailViewController.h"

#import "ADItemCell.h"

#import "ADLoadingViewCell.h"

#import "LocationManager.h"

@interface ADListViewController () {
    
    ADListCaller *_listCaller;
    
    NSArray *_allItems;
    
    PullToRefreshView *_pullToRefresh;
    
    BOOL _isMoreLoading;
}

@end

@implementation ADListViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        
        _listCaller = [[ADListCaller alloc] initWithDelegate:self];
        _allItems = @[];
        
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _pullToRefresh = [[PullToRefreshView alloc] initWithScrollView:(UIScrollView *) self.listTableView];
    _pullToRefresh.delegate = self;
    [self.listTableView addSubview:_pullToRefresh];

    
    CLLocationCoordinate2D cor = [[LocationManager instance] currentCoordinate];
    [_listCaller searchByCategory:_catID latitude:cor.latitude longitude:cor.longitude radius:50 withProgressView:YES];
    
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    [self.listTableView deselectRowAtIndexPath:[self.listTableView indexPathForSelectedRow] animated:YES];
}

- (void)pullToRefreshViewShouldRefresh:(PullToRefreshView *)view {

    CLLocationCoordinate2D cor = [[LocationManager instance] currentCoordinate];
    [_listCaller searchByCategory:_catID latitude:cor.latitude longitude:cor.longitude radius:50 withProgressView:NO];

}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return _isMoreLoading?_allItems.count+1:_allItems.count;
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

    ADDetailViewController *detailVC = [[ADDetailViewController alloc] initWithNibName:@"ADDetailViewController" bundle:nil];
    detailVC.item = _allItems[indexPath.row];
    [self.navigationController pushViewController:detailVC animated:YES];

}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath {
    
        if (indexPath.row == _allItems.count - 2) {
            if (!_isMoreLoading) {
                [[NSOperationQueue mainQueue] addOperationWithBlock:^{
                    [_listTableView insertRowsAtIndexPaths:[NSArray arrayWithObject:[NSIndexPath indexPathForRow:_allItems.count inSection:0]]
                                          withRowAnimation:UITableViewRowAnimationFade];
                }];
                
                [_listCaller loadListWithAnimation:NO];
                _isMoreLoading = YES;
            }
        }
}


#pragma mark - Delgates

- (void)callerDidFinishLoading:(ADBaseCaller *)caller receivedObject:(NSObject *)object {
    
    NSDictionary *data = (NSDictionary *)object;
    _allItems = [_allItems arrayByAddingObjectsFromArray:data[DATA]];
    [_listTableView reloadData];
    
    [self hideProgressView];
    
    _isMoreLoading = NO;
    
}
- (void)caller:(ADBaseCaller *)caller didFailWithError:(NSError *)error {
    
}

- (void)caller:(ADBaseCaller *)caller progressViewWithMessage:(NSString *)message_ {
    
    [self showProgressViewFor:self.view withMessage:message_];
}


@end
