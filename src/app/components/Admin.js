import React from "react";
import {connect} from "react-redux";


class Admin extends React.Component {

    constructor(props = {
        groupList: []
    }) {
        super(props);
    }

    render() {
        return (<div>
            <h1>Hello,Admin</h1>
        </div>);
    }
}

Admin = connect(state => {
    return {
        groupList: state.group
    }
})(Admin);

export default Admin;