import React from "react";
import auth from "../auth";
import {withRouter} from "react-router";


class LoginForm extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            error: false
        }
    }

    handleSubmit(event) {
        event.preventDefault();

        const username = this.refs.username.value;
        const password = this.refs.password.value;
        console.log(this.refs);

        auth.login(username, password, (loggedIn) => {
            if (!loggedIn)
                return this.setState({error: true});

            const {location} = this.props;

            if (location.state && location.state.nextPathname) {
                this.props.router.replace(location.state.nextPathname)
            } else {
                this.props.router.replace('/')
            }
        })
    }

    render() {
        return (
            <div className="ui middle aligned center aligned grid">
                <div className="column six wide">
                    <h2 className="ui teal image header">
                        <div className="content">Login to your account</div>
                    </h2>
                    <form className="ui small form" onSubmit={(e)=>this.handleSubmit(e)}>
                        <div className="ui stacked segment">
                            <div className="field">
                                <div className="ui left icon input">
                                    <i className="user icon"/>
                                    <input ref="username" type="text" placeholder="Username"/>
                                </div>
                            </div>
                            <div className="field">
                                <div className="ui left icon input">
                                    <i className="lock icon"/>
                                    <input ref="password" type="password" placeholder="Password"/>
                                </div>
                            </div>
                            <button type="submit" className="ui fluid large teal submit button">Login
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        );
    }
}

export default withRouter(LoginForm);