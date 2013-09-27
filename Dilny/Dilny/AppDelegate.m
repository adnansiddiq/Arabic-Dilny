//
//  AppDelegate.m
//  Dilny
//
//  Created by Adnan Siddiq on 08/07/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "AppDelegate.h"

#import "DilnyViewController.h"

#import "LocationManager.h"

#import "ADUserCaller.h"

#import "ADItem.h"

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
            
    [LocationManager instance];
    
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    NSString *str = [userDefault objectForKey:USER_ID];
    
    if (str && !str.isEmpty) _userID = str;
    else _userID = nil;
    
    DilnyViewController *dilny = [[DilnyViewController alloc] initWithNibName:@"DilnyViewController" bundle:nil];
    self.navController = [[UINavigationController alloc] initWithRootViewController:dilny];
    self.window.rootViewController = self.navController;
    [self.window makeKeyAndVisible];
    return YES;
}

+ (AppDelegate *)sharedInstance {
    return  (AppDelegate *)[[UIApplication sharedApplication] delegate];
}

- (void)saveUserID {
    
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    if (_userID) {
        [userDefault setObject:_userID forKey:USER_ID];
    } else {
        [userDefault removeObjectForKey:USER_ID];
    }
    [userDefault synchronize];
}

- (void)loadFavourites {
    
    CLLocationCoordinate2D cor = [[LocationManager instance] currentCoordinate];

    [ADUserCaller loadFavWithInfo:@{kUserIDKey: _userID,
                                    kLatitudeKey: @(cor.latitude),
                                    kLongitudeKey: @(cor.longitude)}
                          success:^(NSArray *list) {
                              
                              self.favouriteItems = [NSMutableArray arrayWithArray:list];
                              
                          } failure:^(NSError *error) {
                              self.favouriteItems = nil;
                          }];
}


- (void)loadFavouritesWithSucess:(void (^)(void))success_
                         failure:(void (^)(NSError *error))failure_ {
    CLLocationCoordinate2D cor = [[LocationManager instance] currentCoordinate];
    
    [ADUserCaller loadFavWithInfo:@{kUserIDKey: _userID,
                                    kLatitudeKey: @(cor.latitude),
                                    kLongitudeKey: @(cor.longitude)}
                          success:^(NSArray *list) {
                              
                              self.favouriteItems = [NSMutableArray arrayWithArray:list];
                              success_();
                              
                          } failure:^(NSError *error) {
                              self.favouriteItems = nil;
                              failure_(error);
                          }];

}

- (void)addFavouriteItem:(ADItem *)item_ {
    
    if (self.favouriteItems == nil) {
        self.favouriteItems = [NSMutableArray array];
    }
    [self.favouriteItems addObject:item_];
}

- (void)removeFavouriteItem:(ADItem *)item_ {
    
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"itemID = %@", item_.itemID];
    NSArray *data = [self.favouriteItems filteredArrayUsingPredicate:predicate];
    if (data.count) {
        [self.favouriteItems removeObject:data[0]];
    }
}

- (BOOL)itemIsFavourite:(ADItem *)item_ {
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"itemID = %@", item_.itemID];
    NSArray *data = [self.favouriteItems filteredArrayUsingPredicate:predicate];
    return data.count;
}
@end
