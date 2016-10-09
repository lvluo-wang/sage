import React from "react";
import {createAction} from "redux-actions";

let typeMap = {
    COUNTER: 'COUNTER',
    ADMIN_CHANGE_WORD: 'ADMIN_CHANGE_WORD',
    SET_USER: 'SET_USER',
    GET_USER: 'GET_USER',
};

const types = {};
Object
    .keys(typeMap)
    .forEach(k=> {
        let v = typeMap[k];
        types[v] = createAction(v);
    });

export const action = (dispatch, type, args) => dispatch(types[type](args));

export default typeMap;