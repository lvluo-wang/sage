import React from "react";
import ReactDOM from "react-dom";
import {Router, Route, IndexRoute, hashHistory} from "react-router";
import LoginForm from "./app/login";
import App from "./app/App";
import Home from "./app/Home";


ReactDOM.render(<Router history={hashHistory}>
        <Route path="/" component={App}>
            <IndexRoute component={Home}/>
            <Route path="/login" component={LoginForm}/>
        </Route>
    </Router>,
    document.getElementById('app'));