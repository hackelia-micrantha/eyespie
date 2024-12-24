#import <Foundation/Foundation.h>

@interface MobuildEnvuscator : NSObject

- (instancetype)init;

- (NSString *)getValueForKey:(NSString *)key error:(NSError **)error;
- (size_t)size;
- (NSArray<NSString *> *)keys:(NSError **)error;

@end
