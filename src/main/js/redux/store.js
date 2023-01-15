import { configureStore } from '@reduxjs/toolkit';
import { todoApi } from './todoApi.js';

export const store = configureStore({
    reducer: {
        [todoApi.reducerPath]: todoApi.reducer,
    },
    middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(todoApi.middleware),
});
