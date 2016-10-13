import React from "react";
export const UNAUTH = "Unauthorized";
export const REQUEST = 'REQUEST';
export const SUCCESS = 'SUCCESS';
export const FAILURE = 'FAILURE';

const requestTypeMap = {};

function createRequestTypes(base) {
    const type = [REQUEST, SUCCESS, FAILURE].reduce((acc, type) => {
        acc[type] = `${base}_${type}`;
        return acc
    }, {});
    Object.keys(type).map(key=>requestTypeMap[type[key]] = {type: base, request: key});
    type.matchType = (fullType) => {
        const v = requestTypeMap[fullType];
        if (v) {
            return true;
        }
        return false;
    };
    type.getRequestType = (fullType) => {
        const v = requestTypeMap[fullType];
        if (v) {
            return v.request;
        }
        return null;
    };
    type.createReducer = (initState, onSuccess, onFailure)=> {
        return (state = initState, action)=> {
            let v = requestTypeMap[action.type];
            if (!v) {
                return state;
            }
            let {type, request} = v;
            if (type != base) {
                return state;
            }
            if (request == SUCCESS) {
                if (onSuccess) {
                    return onSuccess(state, action.payload);
                }
            } else if (request == FAILURE) {
                if (onFailure) {
                    return onFailure(state, action.payload);
                }
            }
            return state;
        }
    };
    type.requestAction = (payload = {})=> {
        return {
            type: type[REQUEST],
            payload,
        }
    };
    type.successAction = (payload = {})=> {
        return {
            type: type[SUCCESS],
            payload,
        }
    };
    type.failAction = (payload = {})=> {
        return {
            type: type[FAILURE],
            payload,
        }
    };
    return type;
}

export const USER = createRequestTypes('USER');

export const GROUP = createRequestTypes('GROUP');
export const GROUP_CREATE = createRequestTypes('GROUP_CREATE');
export const GROUP_DELETE = createRequestTypes('GROUP_DELETE');
export const GROUP_RENAME = createRequestTypes('GROUP_RENAME');


export const GROUP_DETAIL = createRequestTypes('GROUP_DETAIL');
export const GROUP_ADD_ROLE = createRequestTypes('GROUP_ADD_ROLE');
export const GROUP_DEL_ROLE = createRequestTypes('GROUP_DEL_ROLE');
export const GROUP_ADD_PRIVILEGE = createRequestTypes('GROUP_ADD_PRIVILEGE');
export const GROUP_DEL_PRIVILEGE = createRequestTypes('GROUP_DEL_PRIVILEGE');

export const PERMISSION_ROLE = createRequestTypes('PERMISSION_ROLE');
export const PERMISSION_PRIVILEGE = createRequestTypes('PERMISSION_PRIVILEGE');