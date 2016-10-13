import React from "react";
import {Card, CardHeader, CardText} from "material-ui/Card";
import {Table, TableBody, TableHeader, TableRow, TableRowColumn} from "material-ui/Table";
import {connect} from "react-redux";
import * as Action from "../actions";
import Group from "./Group";
import List from "material-ui/svg-icons/action/list";
import TextField from "material-ui/TextField";
import RaisedButton from "material-ui/RaisedButton";
import {FormattedDate} from "react-intl";


class RenameRow extends React.Component {
    state = {
        isEditable: false,
        value: "",
    };

    handleClick(name) {
        if (!this.state.isEditable) {
            this.setState({
                isEditable: true,
                value: this.state.isEditable ? name : this.props.group.name
            });
        }
    }

    onKeyEnter(event) {
        if (event.key === 'Enter') {
            let name = this.state.value;
            if (this.props.isValidName(name)) {
                if (this.props.group.name != name) {
                    this.props.renameGroup(this.props.group.id, name);
                }
                this.setState({
                    isEditable: false,
                    value: name
                });
            }
        }
    }

    handleChange(event) {
        this.setState({
            ...this.state,
            value: event.target.value,
        });
    }


    render() {
        return (<TableRowColumn {...this.props} onClick={()=>this.handleClick("")}>{this.state.isEditable
            ? <TextField value={this.state.value}
                         onChange={(e)=>this.handleChange(e)}
                         onKeyPress={(e)=>this.onKeyEnter(e)}
                         onMouseLeave={()=>this.setState({...this.state, isEditable: false})}/>
            : this.props.group.name}</TableRowColumn>);
    }
}

class Admin extends React.Component {

    componentWillMount() {
        this.props.loadGroups();
        this.props.loadRoles();
        this.props.loadPrivileges();
    }

    componentWillReceiveProps(nextProps) {
        if (!nextProps.isLoaded) {
            nextProps.loadGroups();
        }
    }

    state = {
        value: ""
    };

    handleChange = (event) => {
        this.setState({
            value: event.target.value,
        });
    };

    createGroup() {
        let name = this.state.value;
        if (!this.isValidName(name)) {
            return;
        }
        this.props.createGroup(name);
        this.setState({
            value: ""
        });
    }

    isValidName(name) {
        return /^[a-zA-Z0-9\u4E00-\u9FA5\uF900-\uFA2D][-_ a-zA-Z0-9\u4E00-\u9FA5\uF900-\uFA2D]*$/.test(name);
    }

    render() {
        return (<div>
            <Card >
                <CardHeader
                    title={<List/>}
                    expanded={true}
                    showExpandableButton={false}
                />
                <CardText>
                    <Table
                        fixedHeader={true}
                        selectable={false}
                    ><TableHeader adjustForCheckbox={false}
                                  displaySelectAll={false}>
                        <TableRow>
                            <TableRowColumn>ID</TableRowColumn>
                            <TableRowColumn>Name</TableRowColumn>
                            <TableRowColumn>CreateTime</TableRowColumn>
                            <TableRowColumn>Operation</TableRowColumn>
                        </TableRow>
                    </TableHeader>
                        <TableBody displayRowCheckbox={false}
                                   stripedRows={true}
                                   showRowHover={true}>
                            {this.props.groupList.length > 0 && this.props.groupList.map(group => (
                                <TableRow key={group.id}>
                                    <TableRowColumn>{<Group group={group}/>}</TableRowColumn>
                                    <RenameRow {...this.props}
                                               group={group}
                                               isValidName={this.isValidName}
                                    />
                                    <TableRowColumn><FormattedDate value={group.createTime}
                                                                   format="short"/></TableRowColumn>
                                    <TableRowColumn>
                                        <RaisedButton label="Delete"
                                                      secondary={true}
                                                      onMouseDown={()=>this.props.deleteGroup(group.id)}
                                        />
                                    </TableRowColumn>
                                </TableRow>
                            ))}
                            <TableRow key={-1}>
                                <TableRowColumn> </TableRowColumn>
                                <TableRowColumn><TextField
                                    value={this.state.value}
                                    onChange={this.handleChange}
                                /></TableRowColumn>
                                <TableRowColumn> </TableRowColumn>
                                <TableRowColumn>
                                    <RaisedButton label="Create"
                                                  primary={true}
                                                  disabled={!this.isValidName(this.state.value)}
                                                  onMouseDown={()=>this.createGroup()}
                                    />
                                </TableRowColumn>
                            </TableRow>
                        </TableBody>
                    </Table>
                </CardText>
            </Card>
        </div>);
    }
}

const mapDispatchToProps = dispatch=> {
    return {
        loadGroups: () => dispatch(Action.GROUP.requestAction()),
        loadRoles: () => dispatch(Action.PERMISSION_ROLE.requestAction()),
        loadPrivileges: () => dispatch(Action.PERMISSION_PRIVILEGE.requestAction()),
        createGroup: (name) => dispatch(Action.GROUP_CREATE.requestAction(name)),
        deleteGroup: (id) => dispatch(Action.GROUP_DELETE.requestAction(id)),
        renameGroup: (id, name) => dispatch(Action.GROUP_RENAME.requestAction({id, name})),
    }
};

Admin = connect(state => state.group, mapDispatchToProps)(Admin);

export default Admin;