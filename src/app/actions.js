export const REQUEST = 'REQUEST';
export const SUCCESS = 'SUCCESS';
export const FAILURE = 'FAILURE';

function createRequestTypes(base) {
    return [REQUEST, SUCCESS, FAILURE].reduce((acc, type) => {
        acc[type] = `${base}_${type}`;
        return acc
    }, {})
}

export const USER = createRequestTypes('USER');
export const USER_ROLE = createRequestTypes('USER_ROLE');
export const USER_PRIVILEGE = createRequestTypes('USER_PRIVILEGE');


export function action(type, payload = {}) {
    return {type, ...payload}
}