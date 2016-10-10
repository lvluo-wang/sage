import React from "react";
let nonce = require("nonce")();
let jsSHA = require("jssha");


export function getHmac(key, text, type = "HEX") {
    let jsobj = new jsSHA("SHA-256", "TEXT");
    jsobj.setHMACKey(key, "TEXT");
    jsobj.update(text);
    return jsobj.getHMAC(type);
}

export function getTokenHash(clientId, nonce, timestamp, identityId, secret) {
    let data = [clientId, nonce, timestamp, identityId].join('|');
    let hash = getHmac(secret, data, "B64");
    return 'SAGE-HMAC ' + hash + data;
}

export function getClientId() {
    return 1000;
}

export function getNonce() {
    return nonce();
}