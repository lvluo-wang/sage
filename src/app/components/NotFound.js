import React from "react";
const style = {
    width: '50%',
    margin: '0 auto',
    textAlign: 'center',
};

export default class NotFound extends React.Component {
    render() {
        return (<h3 style={style}>404 Not Found</h3>)
    }
}