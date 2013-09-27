//
//  ADItemsViewController.m
//  Dilny
//
//  Created by Adnan Siddiq on 22/09/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADItemsViewController.h"

#import "ADDetailViewController.h"

#import "ADListViewController.h"

#import "ADMapViewController.h"

#import "LocationManager.h"

#import "AppDelegate.h"


@interface ADItemsViewController () {
    
    ADListViewController *_listVC;
    ADMapViewController *_mapVC;
    
    UIViewController *_currentVC;
    
    NSArray *_allItems;

}

@property (weak, nonatomic) IBOutlet UIButton *btnChangeScreen;
@end

@implementation ADItemsViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        
        _listCaller = [[ADListCaller alloc] initWithDelegate:self];
        _allItems = @[];
        
    }
    return self;
}

- (void)setUpChildVC {
    
    _mapVC = [[ADMapViewController alloc] initWithNibName:@"ADMapViewController" bundle:nil];
    _listVC = [[ADListViewController alloc] initWithNibName:@"ADListViewController" bundle:nil];
    
    [self addChildViewController:_mapVC];
    [self addChildViewController:_listVC];
    
    if (_mapSelected) {
        
        [self.bottomView addSubview:_mapVC.view];
        _currentVC = _mapVC;
        
    } else {
        
        [self.bottomView addSubview:_listVC.view];
        _currentVC = _listVC;
    }
    
    self.btnChangeScreen.selected = !_mapSelected;
}

- (void)viewDidLoad {
    [super viewDidLoad];

    [self setUpChildVC];

    if (_isFavourite) {
        _allItems = [[AppDelegate sharedInstance] favouriteItems];
        
        [self performSelector:@selector(loadDataOnSelectedView) withObject:Nil afterDelay:0.0];
        return;
    }
    
    
    CLLocationCoordinate2D cor = [[LocationManager instance] currentCoordinate];
    
    if (_searchText) {
        [_listCaller searchByType:_searchPath
                            title:_searchText
                         category:_catID
                         latitude:cor.latitude
                        longitude:cor.longitude
                           radius:2000
                 withProgressView:YES];
        
    } else {
        [_listCaller searchByType:_searchPath
                         category:_catID
                         latitude:cor.latitude
                        longitude:cor.longitude
                           radius:50
                 withProgressView:YES];
    }

}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    
    _listVC.view.frame = self.bottomView.bounds;
    _mapVC.view.frame = self.bottomView.bounds;
}

- (IBAction)changeScreenAction:(UIButton *)sender {
    
    sender.selected = !sender.selected;
    
    if (!sender.selected) {
        [self switchToView:_mapVC];
        _mapVC.allItems = _allItems;
        [_mapVC loadShopOnMapView];
        
    } else {
        [self switchToView:_listVC];
        [_listVC loadData];
        _currentVC = _listVC;
    }
}

- (void) switchToView:(UIViewController *)controller{
    
    if (_currentVC == controller) {
        return;
    }
    
    [self transitionFromViewController:_currentVC
                      toViewController:controller
                              duration:0.5
                               options:UIViewAnimationOptionTransitionFlipFromLeft
                            animations:^{
                                
                                
                            } completion:^(BOOL finished) {
                                
                                _currentVC = controller;
                            }];
    
}


- (void)moreLoading {
    
    if (_listCaller.noMoreLoading) return;
    
    [_listCaller loadListWithAnimation:NO];

}

- (void)refreshData {
    CLLocationCoordinate2D cor = [[LocationManager instance] currentCoordinate];
    
    if (_searchText) {
        [_listCaller searchByType:_searchPath
                            title:_searchText
                         category:_catID
                         latitude:cor.latitude
                        longitude:cor.longitude
                           radius:2000
                 withProgressView:NO];
        
    } else {
        [_listCaller searchByType:_searchPath
                         category:_catID
                         latitude:cor.latitude
                        longitude:cor.longitude
                           radius:50
                 withProgressView:NO];
    }
}

- (void)loadDataOnSelectedView {
    _mapVC.allItems = _allItems;
    _listVC.allItems = _allItems;
    
    if (!self.btnChangeScreen.selected) {
        [_mapVC loadShopOnMapView];
    } else {
        [_listVC loadData];
    }
}

- (void)callerDidFinishLoading:(ADBaseCaller *)caller receivedObject:(NSObject *)object {
    
    NSDictionary *data = (NSDictionary *)object;
    _allItems = [_allItems arrayByAddingObjectsFromArray:data[DATA]];
    
    [self loadDataOnSelectedView];
    [self hideProgressView];
    
}
- (void)caller:(ADBaseCaller *)caller didFailWithError:(NSError *)error {
    
    [self hideProgressView];
    [self showAlertViewWithMessage:error.localizedDescription];
}

- (void)caller:(ADBaseCaller *)caller progressViewWithMessage:(NSString *)message_ {
    
    [self showProgressViewFor:self.view withMessage:message_];
}

- (void)selectedItemDetailWithIndex:(NSInteger)index_ {
    
    ADDetailViewController *detailVC = [[ADDetailViewController alloc] initWithNibName:@"ADDetailViewController" bundle:nil];
    detailVC.item = _allItems[index_];
    [self.navigationController pushViewController:detailVC animated:YES];

}
@end
