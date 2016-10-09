import React from "react";


export class Users extends React.Component {
    render() {
        return (
            <table className="table table-striped table-hover">
                <thead>
                <tr>
                    <th>User Id</th>
                    <th>Salt</th>
                    <th>isBlocked</th>
                    <th>CreateTime</th>
                </tr>
                </thead>
                <tbody>
                {this.props.users.map((user, i)=> {
                    return ( <tr className={i % 2 == 1 ? "info" : "active"} key={i + ""}>
                        <td>{user.id}</td>
                        <td>{user.salt}</td>
                        <td>{user.isBlocked}</td>
                        <td>{user.createTime}</td>
                    </tr>);
                })}
                </tbody>
            </table>
        )
    }
}