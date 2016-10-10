import {combineReducers} from "redux";

const log = (state = {}, action)=> {
    console.log(state, action);
    return state;
}

const rootReducer = combineReducers({log});

export default rootReducer;