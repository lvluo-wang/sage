import React from "react";
import {Link, IndexLink} from "react-router";

const App = (props) => (
    <div>
        <h1>Sage Project</h1>
        <ul>
            <li><IndexLink to="/" activeClassName="active">Home</IndexLink></li>
            <li><Link to="/login">Login</Link></li>
        </ul>
        {props.children}
    </div>
);

App.propTypes = {
    children: React.PropTypes.object,
};

export default App;