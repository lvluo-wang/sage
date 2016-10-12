import storage from "./storage";
import moment from "moment";
import * as auth from "./auth";
import {refresh} from "./routes";
require('es6-promise').polyfill();
require("isomorphic-fetch");


const API_URL = 'http://localhost:8081';
// const API_URL = '';
const API_ROOT = API_URL + '/';

// Fetches an API response and normalizes the result JSON according to schema.
// This makes every API response have the same shape, regardless of how nested it was.
function callApi(endpoint, options) {
    const fullUrl = (endpoint.indexOf(API_ROOT) === -1) ? API_ROOT + endpoint : endpoint;
    if (API_URL != '') {
        options = {
            ...options,
            mode: 'cors'
        }
    }
    return fetch(fullUrl, options)
        .then(response => {
            if (response.ok) {
                const ts = parseInt(response.headers.get('X-TIMESTAMP'));
                if (ts > 0) {
                    storage.save('global', 'sts', ts);
                    storage.save('global', 'cts', moment().unix());
                }
                return response.json().then(json=> {
                    return {
                        ok: true,
                        response: json
                    }
                }).catch(e=> {
                    return {
                        ok: true,
                        response: {}
                    }
                });
            }
            try {
                return response.json().then(json=> {
                    return {
                        ok: false,
                        error: json.message
                    }
                });
            } catch (e) {
                //
            }
            return Promise.reject({
                ok: false,
                error: "Server access failed"
            });
        });
}

function getTokenHeader() {
    let token = LOGIN.getToken();
    if (!token) {
        throw new Error("Need Login")
    }
    let data = [auth.getClientId(), token.id, getTimestamp(), auth.getNonce()].join('|');
    let hash = auth.getHmac(token.accessSecret, data, "B64");
    return 'SAGE-HMAC ' + hash + data;
}

function postApi(url, body, needAuth = false) {
    let headers = {
        "Content-Type": "application/json"
    };
    if (needAuth) {
        headers = {
            ...headers,
            "Authorization": getTokenHeader()
        }
    }
    return callApi(url, {
        method: "POST",
        headers,
        body: JSON.stringify(body)
    })
}

function putApi(url, body, needAuth = false) {
    let headers = {
        "Content-Type": "application/json"
    };
    if (needAuth) {
        headers = {
            ...headers,
            "Authorization": getTokenHeader()
        }
    }
    return callApi(url, {
        method: "PUT",
        headers,
        body: JSON.stringify(body)
    })
}

function getApi(url, needAuth = false) {
    let headers = {};
    if (needAuth) {
        headers = {
            ...headers,
            "Authorization": getTokenHeader()
        }
    }
    return callApi(url, {
        method: "GET",
        headers
    })
}

function delApi(url, needAuth = false) {
    let headers = {};
    if (needAuth) {
        headers = {
            ...headers,
            "Authorization": getTokenHeader()
        }
    }
    return callApi(url, {
        method: "DELETE",
        headers
    })
}


export function getTimestamp() {
    let tdiff = storage.get('global', 'sts') - storage.get('global', 'cts');
    if (tdiff) {
        return moment().unix() + tdiff;
    }
    return moment().unix();
}

// api services
export const getUserByName = username => getApi(`members/username/${username}`);
export const loginApi = (uid, cid, nonce, ts, sign) => postApi(`tokens`, {uid, cid, nonce, ts, sign});
export const registerApi = (cid, password, salt, username) => postApi(`members`, {cid, password, salt, username});
// token base
export const getUserRoles = () => getApi(`members/roles`, true);
export const getUserPrivileges = () => getApi(`members/privileges`, true);
export const getUserProfile = () => getApi(`members/profile`, true);
// admin base
export const getGroups = () => getApi(`groups`, true);
export const getGroupDetail = (id) => getApi(`groups/${id}/detail`, true);
export const groupAddRole = ({id, role}) => postApi(`groups/${id}/role/${role}`, {}, true);
export const groupDelRole = ({id, role}) => delApi(`groups/${id}/role/${role}`, {}, true);
export const groupAddPrivilege = ({id, privilege}) => postApi(`groups/${id}/privilege/${privilege}`, {}, true);
export const groupDelPrivilege = ({id, privilege}) => delApi(`groups/${id}/privilege/${privilege}`, {}, true);

export const getRoles = () => getApi(`grants/roles`, true);
export const getPrivileges = () => getApi(`grants/privileges`, true);


export const UTIL = {};


UTIL.getName = (e)=> {
    if (e && e.name) {
        return e.name;
    }
    return e;
};

UTIL.getLabel = (e)=> {
    if (e && e.label) {
        return e.label;
    }
    return e;
};

const LOGIN = {};

LOGIN.onChange = (e) => {
};

LOGIN.doCallback = (callback, isLogin) => {
    if (callback) {
        callback(isLogin);
    }
    LOGIN.onChange(isLogin);
};
LOGIN.getToken = ()=> {
    try {
        const {id, isAdmin, expireTime, accessSecret} = storage.get("global", "token", {});
        if (getTimestamp() >= expireTime || !id || !accessSecret) {
            return {};
        }
        return {
            id,
            isAdmin,
            accessSecret
        }
    } catch (e) {
        return {};
    }
};

LOGIN.isLoggedIn = ()=> {
    const {id, accessSecret} = LOGIN.getToken();
    return id != undefined && accessSecret != undefined;
};

LOGIN.isAdmin = ()=> {
    const {id, isAdmin, accessSecret} = LOGIN.getToken();
    return id != undefined && accessSecret != undefined && isAdmin;
};

function doLogin(id, password, callback) {
    let cid = auth.getClientId();
    let nonce = auth.getNonce();
    let ts = getTimestamp();
    let hash = [id, cid, nonce, ts].join('|');
    hash = auth.getHmac(password, hash, "B64");
    loginApi(id, cid, nonce, ts, hash).then(({ok, response})=> {
        if (!ok) {
            LOGIN.doCallback(callback, false);
            return;
        }
        const token = response;
        storage.save("global", "token", token);
        getUserRoles().then(({ok, response})=> {
            try {
                token.isAdmin = ok && response.includes('ROLE_ADMIN');
            } catch (e) {
            }
            storage.save("global", "token", token);
            LOGIN.doCallback(callback, true);
        });
    }, ()=>LOGIN.doCallback(callback, false));
}
LOGIN.login = (username, pass, callback)=> {
    const v = LOGIN.isLoggedIn();
    if (v) {
        LOGIN.doCallback(callback, true);
        return;
    }
    getUserByName(username).then(({ok, response})=> {
        if (!ok) {
            LOGIN.doCallback(callback, false);
        } else {
            let id = response.id;
            let salt = response.salt;
            let password = auth.getHmac(salt, pass);
            doLogin(id, password, callback);
        }
    }, ()=>LOGIN.doCallback(callback, false));
};


LOGIN.register = (username, pass, callback) => {
    let cid = auth.getClientId();
    let salt = auth.getNonce();
    const password = auth.getHmac(salt, pass);
    registerApi(cid, password, salt, username)
        .then(({ok, response})=> {
            if (!ok) {
                LOGIN.doCallback(callback, false);
                return;
            }
            const {id} = response;
            doLogin(id, password, callback);
        });
};

LOGIN.logout = (callback)=> {
    storage.remove("global", "token");
    LOGIN.doCallback(callback, false);
    refresh();
};

LOGIN.checkLoggedIn = ()=> {
    if (!LOGIN.isLoggedIn()) {
        refresh();
    }
};

LOGIN.checkAdmin = ()=> {
    if (!LOGIN.isAdmin()) {
        refresh("/404");
    }
};

export default LOGIN;