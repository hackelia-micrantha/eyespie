## Realtime

- could dispatch postgres flow actions to repositories
- repositories could maintain a list

### Strategies

1. Utilize redux

- realtime repository will dispatch updates
- main screen models can query base data to modify
- reducers will modify state
- require efficient data structures? map by id?
