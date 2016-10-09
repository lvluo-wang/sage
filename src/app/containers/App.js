import React from "react";
import {Router, Route, IndexRoute, browserHistory, Link, IndexLink} from "react-router";
import Admin from "../components/Admin";
import LoginForm from "../components/LoginForm";
import auth from "../auth";

class Main extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            loggedIn: auth.loggedIn()
        };
    }

    updateAuth(loggedIn) {
        this.setState({
            loggedIn
        })
    }

    componentWillMount() {
        auth.onChange = (e) => this.updateAuth(e);
        auth.login()
    }

    render() {
        let link;
        if (!this.state.loggedIn) {
            link = (
                <div className="ui inverted menu">
                    <IndexLink className="item" activeClassName={"active"} to="/">Home</IndexLink>
                    <Link className="right item" activeClassName={"active"} to="/login">Sign in</Link>
                </div>
            )
        } else {
            link = (
                <div className="ui inverted menu">
                    <IndexLink className="item" activeClassName={"active"} to="/">Home</IndexLink>
                    <Link className="item" activeClassName={"active"} to="/admin">Users</Link>
                    <Link className="right item" activeClassName={"active"} to="/logout">Log out</Link>
                </div>
            )
        }

        return (
            <div className="ui container">
                {link}
                <div className="ui container">
                    {this.props.children}
                </div>
            </div>
        )
    }
}

const Home = (props) => {
    return (
        <div>
            <h3>Welcome to Project Sage</h3>
        </div>
    );
};

class Logout extends React.Component {
    componentDidMount() {
        auth.logout();
        browserHistory.push("/login");
    }

    render() {
        return <p>You are now logged out</p>
    }
}


function requireAuth(nextState, replace) {
    if (!auth.loggedIn()) {
        replace({
            pathname: '/login',
            state: {nextPathname: nextState.location.pathname}
        })
    }
}

export default class App extends React.Component {
    render() {
        return (
            <Router history={browserHistory}>
                <Route path={"/"} component={Main}>
                    <IndexRoute component={Home}/>
                    <Route path={"/admin"} component={Admin} onEnter={requireAuth}/>
                    <Route path={"/login"} component={LoginForm}/>
                    <Route path={"/logout"} component={Logout}/>
                </Route>
            </Router>
        );
    }
}