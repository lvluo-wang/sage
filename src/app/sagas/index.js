import {takeLatest} from "redux-saga";
import {call, put} from "redux-saga/effects";
import * as Action from "../actions";
import * as API from "../services";

function getSaga(actionType, apiCaller) {
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

    function* fetchApi2() {
        yield* takeLatest(actionType[Action.REQUEST], fetchApi);
    }

    return fetchApi2;
}

export default [
    getSaga(Action.USER, API.getUserProfile),
    getSaga(Action.USER_ROLE, API.getUserRoles),
    getSaga(Action.USER_PRIVILEGE, API.getUserPrivileges),
]