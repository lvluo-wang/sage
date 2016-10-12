import React from "react";
import Divider from "material-ui/Divider";
import Paper from "material-ui/Paper";
import TextField from "material-ui/TextField";
import FlatButton from "material-ui/FlatButton";
import Dialog from "material-ui/Dialog";
import LOGIN from "../services";

class RegisterForm extends React.Component {
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
        const password2 = this.refs.repassword.input.value;
        if (!username) {
            this.setState({error: true, usernameError: "Username is required."});
            return;
        }
        if (!password) {
            this.setState({error: true, passwordError: "Password is required."});
            return;
        }
        if (password != password2) {
            this.setState({error: true, passwordError: "Password not match."});
            return;
        }
        LOGIN.register(username, password, loggedIn=> {
            if (!loggedIn)
                return this.setState({error: true, message: "Login failed!"});
            this.props.handleClose();
        });
    }

    render() {
        const actions = [
            <FlatButton
                label="Need Login?"
                secondary={true}
                onTouchTap={()=>this.props.handleSwitch()}
            />,
            <FlatButton
                label="Register"
                primary={true}
                onTouchTap={e=>this.handleSubmit(e)}
            />,
        ];
        return (<Dialog
            title="Register SAGE"
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
                <TextField floatingLabelText="re-Password"
                           type="password"
                           underlineShow={false}
                           fullWidth={false}
                           errorText={this.state.passwordError}
                           onChange={e=>this.clearError()}
                           ref="repassword"/>
                <Divider />
                {this.state.message}
            </Paper>
        </Dialog>);
    }
}

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
        });
    }

    render() {
        const actions = [
            <FlatButton
                label="Not yet registered?"
                secondary={true}
                onTouchTap={()=>this.props.handleSwitch()}
            />,
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


class LoginDialog extends React.Component {
    state = {
        open: false,
        register: false,
    };

    handleOpen = () => {
        this.setState({...this.state, open: true});
    };

    handleClose = () => {
        this.setState({...this.state, open: false, register: false});
    };

    render() {
        return (
            <form>
                <FlatButton {...this.props} label="Login" onTouchTap={this.handleOpen}/>
                {this.state.register ?
                    <RegisterForm open={this.state.open}
                                  handleSwitch={()=>this.setState({
                                      ...this.state,
                                      register: false
                                  })}
                                  handleClose={e=>this.handleClose()}/>
                    : <LoginForm open={this.state.open}
                                 handleSwitch={()=>this.setState({
                                     ...this.state,
                                     register: true
                                 })}
                                 handleClose={e=>this.handleClose()}/>
                }
            </form>
        );
    }
}

LoginDialog.muiName = 'FlatButton';

export default LoginDialog;