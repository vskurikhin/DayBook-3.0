import {auth, first} from './userSelectors.js';

export const selectors = {
    user: {
        auth: auth,
        first: first,
    },
};
