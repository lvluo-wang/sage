import React from "react";
import LoginForm from "../components/LoginForm";
import AppBar from "material-ui/AppBar";
import IconButton from "material-ui/IconButton";
import IconMenu from "material-ui/IconMenu";
import MenuItem from "material-ui/MenuItem";
import MoreVertIcon from "material-ui/svg-icons/navigation/more-vert";
import ActionHome from "material-ui/svg-icons/action/home";
import LOGIN from "../sagas/login";
import {toLink} from "../routes";

const Logged = (props) => (
    <IconMenu
        {...props}
        iconButtonElement={
            <IconButton><MoreVertIcon /></IconButton>
        }
        targetOrigin={{horizontal: 'right', vertical: 'top'}}
        anchorOrigin={{horizontal: 'right', vertical: 'top'}}
    >
        <MenuItem primaryText="Refresh" onTouchTap={()=>window.location.href = "/"}/>
        <MenuItem primaryText="User" onTouchTap={() => toLink("/user")}/>
        <MenuItem primaryText="Sign out" onTouchTap={()=> LOGIN.logout()}/>
    </IconMenu>
);

Logged.muiName = 'IconMenu';


class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            loggedIn: LOGIN.isLoggedIn()
        };
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
        return (<div>
            <AppBar title="Project SAGE"
                    iconElementLeft={<IconButton><ActionHome /></IconButton>}
                    onLeftIconButtonTouchTap={()=>toLink("/")}
                    iconElementRight={this.state.loggedIn ? <Logged /> : <LoginForm />}/>
            {this.props.children}
        </div>)
    }
}


export default App;