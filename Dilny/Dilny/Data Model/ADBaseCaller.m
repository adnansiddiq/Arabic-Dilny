//
//  DemoCaller.m
//  CocoaAMF-iPhone
//
//  Created by Marc Bauer on 11.01.09.
//  Copyright 2009 nesiumdotcom. All rights reserved.
//

#import "ADBaseCaller.h"

@implementation ADBaseCaller

#pragma mark -
#pragma mark Initialization & Deallocation
- (id)initWithDelegate:(id<ADBaseCallerDelegate>)delegate_ {
    self = [super init];
    if (self) {
        _delegate = delegate_;
        _showProgressView = YES;
    }
    return self;
}

- (BOOL)isCompleted {
    if (_status == RequestStatusCompleted) {
        return YES;
    } else {
        return NO;
    }
}

#pragma mark -
#pragma mark Public methods


- (void) executeRequestType:(RequestType)type_
              requestMethod:(RequestMethod)method_
                        path:(NSString*)path_
                 parameters:(NSDictionary *)params_
               successBlock:(SuccessBlock)success_ {
    
    _status = RequestStatusInitate;
    
    NSString *urlString = [NSString stringWithFormat:@"%@%@", BASE_API, path_];
    NSLog(@"Request ------ %@", urlString);
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:urlString]];
    
    
    AFJSONRequestOperation *operation = [AFJSONRequestOperation
                                        JSONRequestOperationWithRequest:request
                                        success:^(NSURLRequest *request, NSHTTPURLResponse *response, id JSON) {
                                            
                                            NSObject *object = success_(JSON);
                                            NSString *requestType = [NSString stringWithFormat:@"%d", type_];
                                            NSDictionary *responseDict;
                                            responseDict = @{ DATA: object,
                                                        REQUEST_TYPE: requestType};
                                            _status = RequestStatusCompleted;
                                            [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
                                            [_delegate callerDidFinishLoading:self receivedObject:responseDict];
        
    }
                                        failure:^(NSURLRequest *request, NSHTTPURLResponse *response, NSError *error, id JSON) {

                                            _status = RequestStatusCompleted;
                                            [_delegate caller:self didFailWithError:error];
                                            [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
                                                                                               
                                                                                           }];
    
    [operation start];

    
    _status = RequestStatusInProgress;
    
    if (!self.showProgressView) return;
    
    if ([self.delegate respondsToSelector:@selector(caller:progressViewWithMessage:)]) {
        
        [_delegate caller:self progressViewWithMessage:@"جاري التحميل ..."];
    }
}

@end