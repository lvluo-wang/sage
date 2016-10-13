import {takeLatest} from "redux-saga";
import {call, put} from "redux-saga/effects";
import * as Action from "../actions";
import * as API from "../services";

function getSaga(actionType, apiCaller) {
    function* fetchApi(action) {
        try {
            const payload = yield call(()=>apiCaller(action.payload));
            if (payload.ok) {
                yield put(actionType.successAction(payload.response));
            } else {
                yield put(actionType.failAction(payload.error));
            }
        } catch (e) {
            yield put(actionType.failAction(e.message));
        }
    }

    function* fetchApi2() {
        yield* takeLatest(actionType[Action.REQUEST], fetchApi);
    }

    return fetchApi2;
}

export default [
    getSaga(Action.USER, () => API.getApi(`members/profile`, true)),
    getSaga(Action.GROUP, () => API.getApi(`groups`, true)),
    getSaga(Action.GROUP_DETAIL, (id) => API.getApi(`groups/${id}/detail`, true)),
    getSaga(Action.GROUP_ADD_ROLE, ({id, role}) => API.postApi(`groups/${id}/role/${role}`, {}, true)),
    getSaga(Action.GROUP_DEL_ROLE, ({id, role}) => API.delApi(`groups/${id}/role/${role}`, {}, true)),
    getSaga(Action.GROUP_ADD_PRIVILEGE, ({id, privilege}) => API.postApi(`groups/${id}/privilege/${privilege}`, {}, true)),
    getSaga(Action.GROUP_DEL_PRIVILEGE, ({id, privilege}) => API.delApi(`groups/${id}/privilege/${privilege}`, {}, true)),
    getSaga(Action.PERMISSION_ROLE, () => API.getApi(`grants/roles`, true)),
    getSaga(Action.PERMISSION_PRIVILEGE, () => API.getApi(`grants/privileges`, true)),
]