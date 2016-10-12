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

export const GROUP = createRequestTypes('GROUP');
export const GROUP_DETAIL = createRequestTypes('GROUP_DETAIL');
export const GROUP_ADD_ROLE = createRequestTypes('GROUP_ADD_ROLE');
export const GROUP_DEL_ROLE = createRequestTypes('GROUP_DEL_ROLE');
export const GROUP_ADD_PRIVILEGE = createRequestTypes('GROUP_ADD_PRIVILEGE');
export const GROUP_DEL_PRIVILEGE = createRequestTypes('GROUP_DEL_PRIVILEGE');

export const PERMISSION_ROLE = createRequestTypes('PERMISSION_ROLE');
export const PERMISSION_PRIVILEGE = createRequestTypes('PERMISSION_PRIVILEGE');


export function action(type, payload = {}) {
    return {type, ...payload}
}