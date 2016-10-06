import React from "react";
import "./login.css";

export default class RegisterForm extends React.Component {
    render() {
        return (<div className="form-register">
            <form id="register">
                <h1>Member Login</h1>
                <label id="id-label">Username</label>
                <input id="username" type="text" placeholder="username"></input>
                <label id="id-label">Password</label>
                <input id="password" type="password" placeholder="password"></input>
                <label id="id-label">Password</label>
                <input id="password-confirm" type="password" placeholder="password"></input>
                <input type="submit" id="submit" value="Register"></input>
            </form>
        </div>);
    }
};