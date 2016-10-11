import {takeLatest} from "redux-saga";
import {call, put} from "redux-saga/effects";
import * as Action from "../actions";
import * as API from "../services";

function* callSage(actionType, apiCaller) {
    function* fetchApi(action) {
        try {
            const payload = yield call(()=>apiCaller(action.payload));
            if (payload.ok) {
                yield put(Action.action(actionType[Action.SUCCESS], {payload: payload.response}));
            } else {
                yield put(Action.action(actionType[Action.FAILURE], {message: "Access Failed"}));
            }
        } catch (e) {
            yield put(Action.action(actionType[Action.FAILURE], {message: e.message}));
        }
    }

    yield* takeLatest(actionType[Action.REQUEST], fetchApi);
}


function* getUserProfile() {
    yield* callSage(Action.USER, API.getUserProfile);
}
function* getUserRoles() {
    yield* callSage(Action.USER_ROLE, API.getUserRoles);
}
function* getUserPrivileges() {
    yield* callSage(Action.USER_PRIVILEGE, API.getUserPrivileges);
}

export default [
    getUserProfile,
    getUserRoles,
    getUserPrivileges
]