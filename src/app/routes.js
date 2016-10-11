import React from "react";
import {Route, IndexRoute, browserHistory} from "react-router";
import App from "./containers/App";
import HomePage from "./components/Home";
import UserPage from "./components/User";
import AdminPage from "./components/Admin";
import NotFoundPage from "./components/NotFound";
import LOGIN from "./services";

export const history = browserHistory;

export function refresh() {
    window.location.href = "/";
}
export function toLink(url) {
    browserHistory.push(url);
}

export default (
    <Route path={"/"} component={App}>
        <IndexRoute component={HomePage}/>
        <Route path="user" component={UserPage} onEnter={()=>LOGIN.checkLoggedIn()}/>
        <Route path="admin" component={AdminPage} onEnter={()=>LOGIN.checkAdmin()}/>
        <Route path="*" style={{textAlign: 'center'}} component={NotFoundPage}/>
    </Route>
)