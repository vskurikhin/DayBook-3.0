import {configureStore} from '@reduxjs/toolkit';
import {todoApi} from './todoApi.js';
import {userReducer} from "./userReducer";

export const store = configureStore({
    reducer: {
        [todoApi.reducerPath]: todoApi.reducer,
        user: userReducer,
    },
    middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(todoApi.middleware),
    devTools: process.env.NODE_ENV !== 'production',
});
