import {combineReducers} from "redux";
import * as Action from "../actions";
import * as API from "../services";

const log = (state = {}, action)=> {
    console.log(state, action);
    return state;
};

const user = (state = {
    isAdmin: false,
    profile: {
        id: -1,
        claims: {}
    },
    roles: [],
    privileges: []
}, action) => {
    if (Action.USER[Action.SUCCESS] == action.type) {
        let claims = {}
        action.payload.claimList.map(c=>claims[API.UTIL.getName(c.type)] = {
            title: API.UTIL.getLabel(c.type),
            value: c.value
        });
        return {
            ...state,
            profile: {
                id: action.payload.identity.id,
                claims
            }
        };
    } else if (Action.USER_ROLE[Action.SUCCESS] == action.type) {
        return {
            ...state,
            roles: action.payload,
            isAdmin: action.payload.includes('ROLE_ADMIN')
        }
    } else if (Action.USER_PRIVILEGE[Action.SUCCESS] == action.type) {
        return {
            ...state,
            privileges: action.payload
        }
    } else if (action.type.endsWith(Action.FAILURE)) {
        console.log(action);
    }
    return state;
};

const rootReducer = combineReducers({user, log});

export default rootReducer;