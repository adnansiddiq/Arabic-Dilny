//
//  AppDelegate.h
//  Dilny
//
//  Created by Adnan Siddiq on 08/07/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import <UIKit/UIKit.h>

@class ADItem;

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (strong, nonatomic) UINavigationController *navController;

@property (strong, nonatomic) NSString *userID;

@property (strong, nonatomic) NSMutableArray *favouriteItems;

+ (AppDelegate *)sharedInstance;

- (void)saveUserID;

- (void)loadFavourites;

- (void)loadFavouritesWithSucess:(void (^)(void))success_
                         failure:(void (^)(NSError *error))failure_;

- (void)addFavouriteItem:(ADItem *)item_;

- (void)removeFavouriteItem:(ADItem *)item_;

- (BOOL)itemIsFavourite:(ADItem *)item_;

@end
