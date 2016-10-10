import React from "react";
import {createStore, applyMiddleware} from "redux";
import createLogger from "redux-logger";
import rootReducer from "./reducers";
import createSagaMiddleware, {END} from "redux-saga";


export default function configureStore(isDev, initProps) {

    let middleWire;

    const sagaMiddleWire = createSagaMiddleware();
    if (isDev) {
        middleWire = [createLogger(), sagaMiddleWire];
    } else {
        middleWire = [sagaMiddleWire];
    }
    const store = createStore(
        rootReducer,
        initProps,
        applyMiddleware(...middleWire)
    );
    store.runSaga = sagaMiddleWire.run;
    store.close = () => store.dispatch(END);

    return store;
};