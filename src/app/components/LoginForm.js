import React from "react";
import Divider from "material-ui/Divider";
import Paper from "material-ui/Paper";
import TextField from "material-ui/TextField";
import FlatButton from "material-ui/FlatButton";
import Dialog from "material-ui/Dialog";
import LOGIN from "../services";
import {connect} from "react-redux";
import * as Action from "../actions";

class LoginForm extends React.Component {
    state = {
        error: false,
        usernameError: "",
        passwordError: "",
        message: "",
    };

    clearError() {
        this.setState({
            error: false,
            usernameError: "",
            passwordError: "",
            message: "",
        })
    }

    handleSubmit(event) {
        event.preventDefault();

        const username = this.refs.username.input.value;
        const password = this.refs.password.input.value;
        if (!username) {
            this.setState({error: true, usernameError: "Username is required."});
            return;
        }
        if (!password) {
            this.setState({error: true, passwordError: "Password is required."});
            return;
        }
        LOGIN.login(username, password, loggedIn=> {
            if (!loggedIn)
                return this.setState({error: true, message: "Login failed!"});
            this.props.handleClose();
            this.props.getUser();
            this.props.getUserRoles();
            this.props.getUserPrivileges();
        });
    }

    render() {
        const actions = [
            <FlatButton
                label="Login"
                primary={true}
                onTouchTap={e=>this.handleSubmit(e)}
            />,
        ];
        return (<Dialog
            title="Login to SAGE"
            actions={actions}
            modal={false}
            open={this.props.open}
            onRequestClose={()=>this.props.handleClose()}
        >
            <Paper zDepth={0}>
                <TextField floatingLabelText="Username"
                           underlineShow={false}
                           fullWidth={false}
                           errorText={this.state.usernameError}
                           onChange={e=>this.clearError()}
                           ref="username"/>
                <Divider />
                <TextField floatingLabelText="Password"
                           type="password"
                           underlineShow={false}
                           fullWidth={false}
                           errorText={this.state.passwordError}
                           onChange={e=>this.clearError()}
                           ref="password"/>
                <Divider />
                {this.state.message}
            </Paper>
        </Dialog>);
    }
}

const mapDispatchToProps = dispatch => {
    return {
        getUser: () => dispatch(Action.action(Action.USER[Action.REQUEST])),
        getUserRoles: () => dispatch(Action.action(Action.USER_ROLE[Action.REQUEST])),
        getUserPrivileges: () => dispatch(Action.action(Action.USER_PRIVILEGE[Action.REQUEST])),
    }
};
LoginForm = connect(state=>state, mapDispatchToProps)(LoginForm);


class LoginDialog extends React.Component {
    state = {
        open: false,
    };

    handleOpen = () => {
        this.setState({open: true});
    };

    handleClose = () => {
        this.setState({open: false});
    };

    render() {
        return (
            <form>
                <FlatButton {...this.props} label="Login" onTouchTap={this.handleOpen}/>
                <LoginForm open={this.state.open}
                           handleClose={e=>this.handleClose()}/>
            </form>
        );
    }
}

LoginDialog.muiName = 'FlatButton';

export default LoginDialog;