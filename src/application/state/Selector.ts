import type {RootState, AppState} from '@application/types';
import type {TypedUseSelectorHook} from 'react-redux';
import {createSelector} from '@reduxjs/toolkit';
import {useSelector} from 'react-redux';

export const selectTimeInApp = createSelector(
  (state: RootState): AppState => state.app,
  (state: AppState): number => state.timeInApp,
);

export const useAppSelector: TypedUseSelectorHook<AppState> = useSelector;
