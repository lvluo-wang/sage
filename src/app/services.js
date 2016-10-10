import storage from "./storage";
import moment from "moment";
require("isomorphic-fetch");


// const API_URL = 'http://localhost:8081';
const API_URL = '';
const API_ROOT = API_URL + '/';

// Fetches an API response and normalizes the result JSON according to schema.
// This makes every API response have the same shape, regardless of how nested it was.
function callApi(endpoint, options) {
    const fullUrl = (endpoint.indexOf(API_ROOT) === -1) ? API_ROOT + endpoint : endpoint;
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
                });
            } else {
                return Promise.reject({
                    ok: false,
                    error: "Server access failed"
                })
            }
        });
}


function postApi(url, body) {
    return callApi(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    })
}

function getApi(url) {
    return callApi(url, {
        method: "GET"
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
export const login = (uid, cid, nonce, ts, sign) => postApi(`tokens`, {uid, cid, nonce, ts, sign});
