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

const group = (state = {
    groupList: {},
    isLoaded: false,
}, action)=> {
    if (action.type == Action.GROUP[Action.SUCCESS]) {
        return {
            ...state,
            groupList: action.payload.items,
            isLoaded: true
        }
    } else if (action.type == Action.GROUP_CREATE[Action.SUCCESS]
        || action.type == Action.GROUP_DELETE[Action.SUCCESS]
        || action.type == Action.GROUP_RENAME[Action.SUCCESS]) {
        return {
            ...state,
            isLoaded: false
        };
    }
    return state;
};

const groupDetail = (state = {
    detail: {},
    isLoaded: false,
}, action)=> {
    if (action.type == Action.GROUP_DETAIL[Action.SUCCESS]) {
        return {
            ...state,
            detail: action.payload,
            isLoaded: true
        }
    } else if (action.type == Action.GROUP_ADD_PRIVILEGE[Action.SUCCESS]
        || action.type == Action.GROUP_DEL_PRIVILEGE[Action.SUCCESS]
        || action.type == Action.GROUP_ADD_ROLE[Action.SUCCESS]
        || action.type == Action.GROUP_DEL_ROLE[Action.SUCCESS]) {
        return {
            ...state,
            isLoaded: false
        };
    }
    return state;
};

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