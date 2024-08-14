#import <Foundation/Foundation.h>

@interface MobuildEnvuscator : NSObject

- (instancetype) init;

- (NSString *) getConfigValueForKey: (NSString *) key error: (NSError **) error;
- (size_t) numberOfValues;

@end
