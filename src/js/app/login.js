import React from "react";
import "./login.css";

export default class LoginForm extends React.Component {
    render() {
        return (<div className="form-login">
            <form id="login" onSubmit={this.submitHandler}>
                <h1>Member Login</h1>
                <label id="id-label">Username</label>
                <input id="username" type="text" placeholder="username"></input>
                <label id="id-label">Password</label>
                <input id="password" type="password" placeholder="password"></input>
                <input type="submit" id="submit" value="Login"></input>
            </form>
        </div>);
    }


    submitHandler() {
        console.log()
    }
};