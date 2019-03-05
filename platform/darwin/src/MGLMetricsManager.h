#import <Foundation/Foundation.h>
#import "MGLFoundation.h"

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSUInteger, MGLMetricType) {
    MGLMetricTypePerformance = 0,
};

@protocol MGLMetricsDelegate <NSObject>

- (BOOL)shouldHandleMetric:(MGLMetricType)metricType;
- (void)didCollectMetric:(MGLMetricType)metricType withAttributes:(NSDictionary *)attributes;

@end

typedef void (^MGLEventHandler)(NSString *eventName, NSDictionary *attributes);

MGL_EXPORT
@interface MGLMetricsManager : NSObject

@property (class, nonatomic, readonly) MGLMetricsManager *sharedManager;

@property (nonatomic, weak) id<MGLMetricsDelegate> delegate;

#if TARGET_OS_IOS
- (void)pushMetric:(MGLMetricType)metricType withAttributes:(NSDictionary *)attributes;
#endif

@end

NS_ASSUME_NONNULL_END