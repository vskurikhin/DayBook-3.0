import {createReducer} from '@reduxjs/toolkit';
import {userLogin, userLogout} from './userActions.js';

const initState = [
    { id: 1, name: null, auth: false },
]

export const userReducer = createReducer(initState, (builder) => {
    builder
        .addCase(userLogin, (state, action) => { // userLogin.toString() === 'USER_LOGIN'
            const item = state.find((item) => item.id === 1);
            item.auth = true; // мутация state
            item.name = action.payload;
        })
        .addCase(userLogout, (state, action) => { // userLogout.toString() === 'USER_LOGOUT'
            const item = state.find((item) => item.id === 1);
            item.auth = false; // мутация state
            item.name = null;
        })
});