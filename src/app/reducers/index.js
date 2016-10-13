import {combineReducers} from "redux";
import * as Action from "../actions";
import * as API from "../services";


const user = Action.USER.createReducer({
    isAdmin: false,
    profile: {
        id: -1,
        claims: {}
    },
    roles: [],
    privileges: []
}, (state, payload)=> {
    let claims = {};
    payload.claimList.map(c=>claims[API.UTIL.getName(c.type)] = {
        title: API.UTIL.getLabel(c.type),
        value: c.value
    });
    return {
        ...state,
        profile: {
            id: payload.identity.id,
            claims
        },
        isAdmin: payload.roles.includes('ROLE_ADMIN'),
        roles: payload.roles,
        privileges: payload.privileges
    };
});

const group = Action.GROUP.createReducer({
    isLoaded: false,
    groupList: []
}, (state, payload)=> {
    return {
        ...state,
        isLoaded: true,
        groupList: payload.items
    }
}, (state, payload)=> {
    return {
        ...state,
        isLoaded: true
    }
});

const groupDetail = Action.GROUP_DETAIL.createReducer({
    isLoaded: false,
    detail: {}
}, (state, payload)=> {
    return {
        ...state,
        isLoaded: true,
        detail: payload
    }
}, (state, payload)=> {
    return {
        ...state,
        isLoaded: true
    }
});

const permission = combineReducers({
    roles: Action.PERMISSION_ROLE.createReducer([],
        (state, payload)=> {
            return payload;
        }),
    privileges: Action.PERMISSION_PRIVILEGE.createReducer([],
        (state, payload)=> {
            return payload;
        })
});


const rootReducer = combineReducers({user, group, groupDetail, permission});

export default rootReducer;