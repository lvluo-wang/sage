import React from "react";
import LoginForm from "../components/LoginForm";
import AppBar from "material-ui/AppBar";
import IconButton from "material-ui/IconButton";
import IconMenu from "material-ui/IconMenu";
import MenuItem from "material-ui/MenuItem";
import MoreVertIcon from "material-ui/svg-icons/navigation/more-vert";
import ActionHome from "material-ui/svg-icons/action/home";
import LOGIN from "../services";
import {toLink, refresh} from "../routes";
import {connect} from "react-redux";
import * as Action from "../actions";

class Logged extends React.Component {

    render() {
        return (
            <IconMenu
                {...this.props}
                iconButtonElement={
                    <IconButton><MoreVertIcon /></IconButton>
                }
                targetOrigin={{horizontal: 'right', vertical: 'top'}}
                anchorOrigin={{horizontal: 'right', vertical: 'top'}}
            >
                <MenuItem primaryText="Refresh" onTouchTap={()=> refresh()}/>
                <MenuItem primaryText="User" onTouchTap={() => toLink("/user")}/>
                {this.props.isAdmin && <MenuItem primaryText="Admin" onTouchTap={() => toLink("/admin")}/>}
                <MenuItem primaryText="Sign out" onTouchTap={()=> LOGIN.logout()}/>
            </IconMenu>
        )
    }
}

Logged.muiName = 'IconMenu';

const mapStateToProps = state => {
    return {
        isAdmin: state.user.isAdmin
    }
};

Logged = connect(mapStateToProps)(Logged);


class App extends React.Component {

    state = {
        loggedIn: LOGIN.isLoggedIn()
    };

    constructor(props) {
        super(props);
    }

    updateAuth(loggedIn) {
        this.setState({
            loggedIn
        })
    }

    componentWillMount() {
        LOGIN.onChange = (e) => this.updateAuth(e);
    }

    render() {
        if (this.state.loggedIn) {
            this.props.getUser();
            this.props.getUserRoles();
            this.props.getUserPrivileges();

        }
        return (<div>
            <AppBar title="Project SAGE"
                    iconElementLeft={<IconButton><ActionHome /></IconButton>}
                    onLeftIconButtonTouchTap={()=>toLink("/")}
                    iconElementRight={this.state.loggedIn ? <Logged /> : <LoginForm />}/>
            {this.props.children}
        </div>)
    }
}


const mapDispatchToProps = dispatch => {
    return {
        getUser: () => dispatch(Action.action(Action.USER[Action.REQUEST])),
        getUserRoles: () => dispatch(Action.action(Action.USER_ROLE[Action.REQUEST])),
        getUserPrivileges: () => dispatch(Action.action(Action.USER_PRIVILEGE[Action.REQUEST])),
    }
};

App = connect(null, mapDispatchToProps)(App);


export default App;