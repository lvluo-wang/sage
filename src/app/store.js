import React from "react";
import {createStore, combineReducers, applyMiddleware} from "redux";
import createLogger from "redux-logger";
import reducers from "./reducers";

const logger = createLogger();


const store = createStore(
    combineReducers(reducers),
    {},
    applyMiddleware(logger)
);


export default store;