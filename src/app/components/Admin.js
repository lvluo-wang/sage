import React from "react";
import {connect} from "react-redux";
import typeMap, {action} from "../actions";

export class Admin extends React.Component {
    render() {
        return (
            <div>
                <div>{"Hello, " + this.props.count || 0}</div>
                <button className="ui primary button" onClick={this.props.onClick}>Add</button>
            </div>
        );
    }
}

const mapStateToProps = (state)=> {
    return {
        count: state.counter.count
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onClick: e=>action(dispatch, typeMap.COUNTER)
    }
};

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(Admin);