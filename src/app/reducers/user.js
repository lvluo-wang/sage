import React from "react";
import typeMap from "../actions";
import {handleAction} from "redux-actions";


let userReducer = handleAction(typeMap.SET_USER, (state, action)=> {
    return {
        ...state,
        user: action.payload
    }
}, {
    user: null
});

export default userReducer;
