import React from "react";
import typeMap from "../actions";
import {handleAction} from "redux-actions";


let counterReducer = handleAction(typeMap.COUNTER, (state, action)=> {
    return {
        ...state,
        count: state.count + 1
    }
}, {
    count: 0
});

export default counterReducer;
