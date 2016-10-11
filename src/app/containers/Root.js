import React from "react";
import injectTapEventPlugin from "react-tap-event-plugin";
import baseTheme from "material-ui/styles/baseThemes/lightBaseTheme";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import getMuiTheme from "material-ui/styles/getMuiTheme";
import {Provider} from "react-redux";
import {Router} from "react-router";

export default class Root extends React.Component {

    render() {
        // Needed for onTouchTap
        // http://stackoverflow.com/a/34015469/988941
        injectTapEventPlugin();
        const {store, history, routes, type, renderProps} = this.props;
        return (
            <Provider store={store}>
                <MuiThemeProvider muiTheme={getMuiTheme(baseTheme)}>
                    { type === 'server'
                        ? <RouterContext {...renderProps} />
                        : <Router history={history} routes={routes}/>
                    }
                </MuiThemeProvider>
            </Provider>
        )
    }
}

Root.propTypes = {
    store: React.PropTypes.object.isRequired,
    history: React.PropTypes.object.isRequired,
    routes: React.PropTypes.node.isRequired
};