import React from "react";
import {Table, TableBody, TableHeader, TableRow, TableRowColumn} from "material-ui/Table";
import {connect} from "react-redux";
import * as Action from "../actions";
import Group from "./Group";


class Admin extends React.Component {

    render() {
        if (!this.props.isLoaded) {
            this.props.loadGroups();
            this.props.loadRoles();
            this.props.loadPrivileges();
        }
        return (<div>
            <h1>Hello,Admin</h1>
            <Table
                fixedHeader={true}
                selectable={false}
            ><TableHeader adjustForCheckbox={false}
                          displaySelectAll={false}>
                <TableRow>
                    <TableRowColumn>ID</TableRowColumn>
                    <TableRowColumn>Name</TableRowColumn>
                    <TableRowColumn>CreateTime</TableRowColumn>
                </TableRow>
            </TableHeader>
                <TableBody displayRowCheckbox={false}
                           stripedRows={true}
                           showRowHover={true}>
                    {this.props.groupList.length == 0 ? <h1>Empty Table</h1> : this.props.groupList.map(group => (
                        <TableRow key={group.id}>
                            <TableRowColumn>{<Group group={group}/>}</TableRowColumn>
                            <TableRowColumn>{group.name}</TableRowColumn>
                            <TableRowColumn>{group.createTime}</TableRowColumn>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </div>);
    }
}

const mapDispatchToProps = dispatch=> {
    return {
        loadGroups: () => dispatch(Action.GROUP.requestAction()),
        loadRoles: () => dispatch(Action.PERMISSION_ROLE.requestAction()),
        loadPrivileges: () => dispatch(Action.PERMISSION_PRIVILEGE.requestAction()),
    }
};

Admin = connect(state => state.group, mapDispatchToProps)(Admin);

export default Admin;