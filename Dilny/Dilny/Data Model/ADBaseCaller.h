//
//  DemoCaller.h
//  CocoaAMF-iPhone
//
//  Created by Marc Bauer on 11.01.09.
//  Copyright 2009 nesiumdotcom. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <objc/message.h>
#import "AFNetworking.h"

#define ERROR               @"error"
#define MESSAGE             @"message"
#define REQUEST_TYPE        @"request_type"
#define RESPONSE            @"response"
#define RESPONSE_MESSAGE    @"resposne_message"
#define DATA                @"data"
#define STATUS_INFO         @"status_info"

#define BASE_API @"http://dilny.com/api/"



typedef enum {
    RequestTypeCategory = 0,
    RequestTypeList,
    RequestTypeRegister,
    RequestTypeLogin
    
} RequestType;

typedef enum {
    
    RequestMethodPost,
    RequestMethodGet,
    RequestMethodPut,
    RequestMethodDelete
    
} RequestMethod;

typedef enum {
    RequestStatusInitate,
    RequestStatusInProgress,
    RequestStatusCompleted
} RequestStatus;

typedef id (^SuccessBlock)(id);



@protocol ADBaseCallerDelegate;

@interface ADBaseCaller : NSObject<NSXMLParserDelegate> {
    
}

@property (assign, nonatomic) BOOL showProgressView;
@property (nonatomic, assign) RequestStatus status;
@property (nonatomic, weak) id <ADBaseCallerDelegate> delegate;

- (id)initWithDelegate:(id<ADBaseCallerDelegate>)delegate_;

- (void) executeRequestType:(RequestType)type_
              requestMethod:(RequestMethod)method_
                     path:(NSString*)path_
                 parameters:(NSDictionary *)params_
               successBlock:(SuccessBlock)success_;



@end


@protocol ADBaseCallerDelegate <NSObject>
- (void)callerDidFinishLoading:(ADBaseCaller *)caller receivedObject:(NSObject *)object;
- (void)caller:(ADBaseCaller *)caller didFailWithError:(NSError *)error;

@optional

- (void)caller:(ADBaseCaller *)caller progressViewWithMessage:(NSString *)message_;
@end

