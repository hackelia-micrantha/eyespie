## Nearby Things, Players

1. Background location services on device

- when location changes, and enabled, queue location to server
- sort by distance from last updated
- pop from queue on a timer or significant event
- push to server

2. Realtime server

- recieve player location updates
- table trigger to calculate nearby things
- updates to player things table

#### Note

- GraphQL client does not have realtime support?

3. Device updates

- tables changes pushed to subscribers
- data requeried
