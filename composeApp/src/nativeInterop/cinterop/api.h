#ifndef MOBUILD_ENVUSCATOR_API_H
#define MOBUILD_ENVUSCATOR_API_H

#include <stddef.h>

extern size_t
mobuild_envuscator_get_config_value(const unsigned char *key, size_t, unsigned char *result,
                                    size_t);

extern size_t mobuild_envuscator_number_of_values();

#endif
