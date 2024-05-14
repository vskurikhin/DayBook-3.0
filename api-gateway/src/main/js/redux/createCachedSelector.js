import {createSelector} from 'reselect';

const cache = {};

export const createCachedSelector = (...funcs) => {
    return (keySelector) => {
        const selector = (...args) => {
            const cacheKey = keySelector(...args);
            let cacheResponse = cache[cacheKey];
            if (cacheResponse === undefined) {
                cacheResponse = createSelector(...funcs);
                cache[cacheKey] = cacheResponse;
            }
            return cacheResponse(...args);
        };
        return selector;
    };
};
