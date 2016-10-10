const REQUEST = 'REQUEST';
const SUCCESS = 'SUCCESS';
const FAILURE = 'FAILURE';

function createRequestTypes(base) {
    return [REQUEST, SUCCESS, FAILURE].reduce((acc, type) => {
        acc[type] = `${base}_${type}`;
        return acc
    }, {})
}

export const USER = createRequestTypes('USER');


function action(type, payload = {}) {
    return {type, ...payload}
}

export const user = {
    request: login => action(USER.REQUEST, {login}),
    success: (login, response) => action(USER.SUCCESS, {login, response}),
    failure: (login, error) => action(USER.FAILURE, {login, error}),
};