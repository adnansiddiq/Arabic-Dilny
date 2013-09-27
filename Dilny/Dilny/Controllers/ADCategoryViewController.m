//
//  ADCategoryViewController.m
//  Dilny
//
//  Created by Adnan Siddiq on 17/08/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADCategoryViewController.h"

#import "ADItemsViewController.h"

#import "ADCategoryCell.h"

@interface ADCategoryViewController () {
    
    ADCategoryCaller *_catCaller;
    
    NSArray *_allCategories;
    
    PullToRefreshView *_pullToRefresh;
    UIRefreshControl *_refreshControl;
}

@property (weak, nonatomic) IBOutlet UIButton *btnDone;

@end

@implementation ADCategoryViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        _catCaller = [[ADCategoryCaller alloc] initWithDelegate:self];
        _allCategories = @[];
    }
    return self;
}

- (void)viewDidLoad {
    
    [super viewDidLoad];
    
    self.navigationController.navigationBarHidden = YES;
    
    if (NSClassFromString(@"UIRefreshControl")) {
         _refreshControl = [[UIRefreshControl alloc] init];
        _refreshControl.tintColor = [UIColor greenColor];
        [_refreshControl addTarget:self action:@selector(refreshData:) forControlEvents:UIControlEventValueChanged];
        [self.categoryListView addSubview:_refreshControl];
    } else {
        _pullToRefresh = [[PullToRefreshView alloc] initWithScrollView:(UIScrollView *) self.categoryListView];
        _pullToRefresh.delegate = self;
        [self.categoryListView addSubview:_pullToRefresh];

    }
    
    [_catCaller loadCategoriesWithProgressView:YES];
    
    self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemDone target:self action:@selector(doneAction)];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    [self.categoryListView deselectRowAtIndexPath:[self.categoryListView indexPathForSelectedRow] animated:YES];
}

- (void)refreshData:(UIRefreshControl *)sender {
    [_catCaller loadCategoriesWithProgressView:NO];
}
- (void)pullToRefreshViewShouldRefresh:(PullToRefreshView *)view {
    
    [_catCaller loadCategoriesWithProgressView:NO];
}



#pragma mark  - UITableViewDelegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return _allCategories.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"Cell";
    
    ADCategoryCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) {
        cell = (ADCategoryCell *)[[[NSBundle mainBundle] loadNibNamed:@"ADCategoryCell" owner:nil options:nil] objectAtIndex:0];
    }
    
    cell.category = _allCategories[indexPath.row];
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    ADCategory *cat = [_allCategories objectAtIndex:indexPath.row];
    
    ADItemsViewController *listVC = [[ADItemsViewController alloc] initWithNibName:@"ADItemsViewController" bundle:nil];
    listVC.catID = [cat.catID stringValue];
    listVC.searchPath = @"searchByCat";
    listVC.mapSelected = NO;
    [self.navigationController pushViewController:listVC animated:YES];
}

#pragma mark - Delegate

- (void)callerDidFinishLoading:(ADBaseCaller *)caller receivedObject:(NSObject *)object {
    
    NSDictionary *result = (NSDictionary *)object;
    [self hideProgressView];
    _allCategories = result[DATA];
    
    if (_pullToRefresh) {
        [_pullToRefresh finishedLoading];
    } else {
        [_refreshControl endRefreshing];
    }
    
    [self.categoryListView reloadData];
    
}

- (void)caller:(ADBaseCaller *)caller didFailWithError:(NSError *)error {
    
    if (_pullToRefresh)
        [_pullToRefresh finishedLoading];
    else
        [_refreshControl endRefreshing];
    [self hideProgressView];
}


- (void)caller:(ADBaseCaller *)caller progressViewWithMessage:(NSString *)message_ {
    
    [self showProgressViewFor:self.view withMessage:message_];
}

@end
