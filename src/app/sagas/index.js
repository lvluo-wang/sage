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
    getSaga(Action.GROUP, API.getGroups),
    getSaga(Action.GROUP_DETAIL, API.getGroupDetail),
    getSaga(Action.GROUP_ADD_ROLE, API.groupAddRole),
    getSaga(Action.GROUP_DEL_ROLE, API.groupDelRole),
    getSaga(Action.GROUP_ADD_PRIVILEGE, API.groupAddPrivilege),
    getSaga(Action.GROUP_DEL_PRIVILEGE, API.groupDelPrivilege),
    getSaga(Action.PERMISSION_ROLE, API.getRoles),
    getSaga(Action.PERMISSION_PRIVILEGE, API.getPrivileges),
]