import {combineReducers} from '@reduxjs/toolkit';
import {reducer as app} from '@application/state';

export const rootReducer = combineReducers({
  app,
});
