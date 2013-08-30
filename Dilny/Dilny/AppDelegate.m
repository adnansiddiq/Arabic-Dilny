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



@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
    [LocationManager instance];
    
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    
    DilnyViewController *dilny = [[DilnyViewController alloc] initWithNibName:@"DilnyViewController" bundle:nil];
    self.navController = [[UINavigationController alloc] initWithRootViewController:dilny];
    self.window.rootViewController = self.navController;
    
    [self.window makeKeyAndVisible];
    return YES;
}


@end
