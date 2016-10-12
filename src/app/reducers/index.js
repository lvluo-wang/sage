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
        let claims = {};
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


const group = (state = {
    isLoaded: false,
    groupList: []
}, action) => {
    if (Action.GROUP[Action.SUCCESS] == action.type) {
        return {
            ...state,
            isLoaded: true,
            groupList: action.payload.items
        }
    } else if (Action.GROUP[Action.FAILURE] == action.type) {
        console.log(action);
        return {
            ...state,
            isLoaded: true
        }
    }
    return state;
};

const groupDetail = (state = {
    isLoaded: false,
    detail: {}
}, action) => {
    if (Action.GROUP_DETAIL[Action.SUCCESS] == action.type) {
        return {
            ...state,
            isLoaded: true,
            detail: action.payload
        }
    } else if (Action.GROUP_DETAIL[Action.FAILURE] == action.type) {
        console.log(action);
        return {
            ...state,
            isLoaded: true
        }
    }
    return state;
};

const groupModified = (state = 0, action) => {
    if (Action.GROUP_DEL_PRIVILEGE[Action.SUCCESS] == action.type
        || Action.GROUP_ADD_PRIVILEGE[Action.SUCCESS] == action.type
        || Action.GROUP_ADD_ROLE[Action.SUCCESS] == action.type
        || Action.GROUP_DEL_ROLE[Action.SUCCESS] == action.type) {
        return state + 1;
    }
    return state;
};

const permissionRole = (state = [], action) => {
    if (Action.PERMISSION_ROLE[Action.SUCCESS] == action.type) {
        return action.payload;
    }
    return state;
};

const permissionPrivilege = (state = [], action) => {
    if (Action.PERMISSION_PRIVILEGE[Action.SUCCESS] == action.type) {
        return action.payload;
    }
    return state;
};

const permission = combineReducers({
    roles: permissionRole,
    privileges: permissionPrivilege
});


const rootReducer = combineReducers({user, log, group, groupDetail, groupModified, permission});

export default rootReducer;