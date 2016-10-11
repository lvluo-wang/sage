import React from "react";
let jsSHA = require("jssha");


export function getHmac(key, text, type = "HEX") {
    let jsobj = new jsSHA("SHA-256", "TEXT");
    jsobj.setHMACKey(key, "TEXT");
    jsobj.update(text);
    return jsobj.getHMAC(type);
}

export function getClientId() {
    return 1000;
}

export function getNonce() {
    let text = "";
    let possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for (let i = 0; i < 16; i++) {
        text += possible.charAt(Math.floor(Math.random() * possible.length));
    }
    return text;
}