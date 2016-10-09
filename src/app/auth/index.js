import React from "react";

function pretendRequest(username, pass, cb) {
    setTimeout(() => {
        console.log(username);
        console.log(pass);
        if (username === 'daniel' && pass === 'password1') {
            cb({
                authenticated: true,
                token: Math.random().toString(36).substring(7)
            })
        } else {
            cb({authenticated: false})
        }
    }, 0)
}

export default {
    login(username, pass, cb){
        cb = arguments[arguments.length - 1];
        if (localStorage.token) {
            this.f(cb, true);
            return;
        }

        pretendRequest(username, pass, (res)=> {
            if (res.authenticated) {
                localStorage.token = res.token;
                this.f(cb, true);
            } else {
                this.f(cb, false);
            }
        })
    },

    getToken(){
        return localStorage.token;
    },

    logout(cb) {
        delete localStorage.token;
        this.f(cb, false);
    },

    loggedIn() {
        return !!localStorage.token
    },

    f (cb, v) {
        if (cb) cb(v);
        this.onChange(v);
    },

    onChange(v) {
    }
}

