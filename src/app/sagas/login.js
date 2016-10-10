import React from "react";
import storage from "../storage";
import * as API from "../services";
import {getTimestamp} from "../services";
import * as auth from "../auth";

function getLoginHash(identityId, clientId, nonce, timestamp, password) {
    let hash = [identityId, clientId, nonce, timestamp].join('|');
    return auth.getHmac(password, hash, "B64");
}


export default {
    login(username, pass, callback) {
        let token = storage.get("global", "token");
        if (token) {
            this.doCallback(callback, true);
            return;
        }
        API.getUserByName(username).then(({ok, response})=> {
            if (!ok) {
                this.doCallback(callback, false);
            } else {
                let id = response.id;
                let salt = response.salt;
                let password = auth.getHmac(salt, pass);
                let cid = auth.getClientId();
                let nonce = auth.getNonce();
                let ts = getTimestamp();
                let hash = getLoginHash(id, cid, nonce, ts, password);
                API.login(id, cid, nonce, ts, hash).then((isOk, newToken)=> {
                    if (!isOk) {
                        this.doCallback(callback, false);
                    }
                    storage.save("global", "token", newToken);
                    this.doCallback(callback, true);
                }, ()=>this.doCallback(callback, false));
            }
        }, ()=>this.doCallback(callback, false));
    },
    logout(callback) {
        storage.remove("global", "token");
        this.doCallback(callback, false);
    },
    isLoggedIn() {
        return storage.get("global", "token") ? true : false;
    },
    checkLoggedIn(){
        if (!this.isLoggedIn()) {
            window.location.href = "/"
        }
    },
    doCallback(callback, isLogin) {
        if (callback) {
            callback(isLogin);
        }
        this.onChange(isLogin);
    },
    onChange(e){

    }
}