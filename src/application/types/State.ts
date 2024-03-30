import type {
  ThunkAction,
  ThunkDispatch as ReduxThunkDispatch,
} from 'redux-thunk';
import type {AnyAction} from 'redux';
import {store} from '@application/store';

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export type ThunkDispatch = ReduxThunkDispatch<RootState, unknown, AnyAction>;

export type AppThunk<ReturnType = void> = ThunkAction<
  ReturnType,
  RootState,
  unknown,
  AnyAction
>;

export interface AppState {
  timeInApp: number;
}
