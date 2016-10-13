import React from "react";
import FlatButton from "material-ui/FlatButton";
import Dialog from "material-ui/Dialog";
import Chip from "material-ui/Chip";
import {Card, CardActions, CardHeader} from "material-ui/Card";
import {Popover, PopoverAnimationVertical} from "material-ui/Popover";
import Menu from "material-ui/Menu";
import MenuItem from "material-ui/MenuItem";
import {connect} from "react-redux";
import * as Action from "../actions";
import Avatar from "material-ui/Avatar";
import AddCircle from "material-ui/svg-icons/content/add-circle";


class PermissionPaper extends React.Component {

    style = {
        chip: {
            margin: 4,
        }, wrapper: {
            display: 'flex',
            flexWrap: 'wrap',
        },
    };

    constructor(props) {
        super(props);
        this.state = {
            open: false,
            values: props.detail() || [],
            full: props.total || {}
        }
    }

    handleRequestClose() {
        this.setState({
            ...this.state,
            open: false,
        });
    }

    handleTouchTap = (event) => {
        // This prevents ghost click.
        event.preventDefault();
        this.setState({
            ...this.state,
            open: true,
            anchorEl: event.currentTarget,
        });
    };

    handleAddGroup = (k) => {
        this.props.addGroup(k);
        this.setState({
            ...this.state,
            values: [...this.state.values, k]
        })
    };

    handleDelGroup = (k) => {
        this.props.delGroup(k);
        this.setState({
            ...this.state,
            values: this.state.values.filter(a=>a != k)
        })
    };


    render() {
        return (<Card expanded={true}>
            <CardHeader
                title={this.props.title + " Management"}
                expanded={true}
                showExpandableButton={false}
            />
            <CardActions expandable={true} style={this.style.wrapper}>
                {this.state.values.map(this.renderChip, this)}
                <Chip key="+"
                      style={this.style.chip}
                      onTouchTap={e=>this.handleTouchTap(e)}>
                    <Avatar backgroundColor={this.context.muiTheme.palette.primary1Color}>+</Avatar>
                    Add {this.props.title}
                </Chip>
                <Popover
                    open={this.state.open}
                    anchorEl={this.state.anchorEl}
                    anchorOrigin={{horizontal: 'left', vertical: 'bottom'}}
                    targetOrigin={{horizontal: 'left', vertical: 'top'}}
                    onRequestClose={(e)=>this.handleRequestClose()}
                    animation={PopoverAnimationVertical}
                >
                    <Menu onItemTouchTap={()=>this.handleRequestClose()}>
                        {Object.keys(this.state.full)
                            .filter(k=>!this.state.values.includes(k))
                            .map(k=>this.renderMenuItem(k))}
                    </Menu>
                </Popover>
            </CardActions>
        </Card>);
    }

    getValue(k) {
        const v = this.state.full[k];
        return v || k;
    }

    renderMenuItem(k) {
        return (
            <MenuItem leftIcon={<AddCircle />}
                      primaryText={this.getValue(k)}
                      onTouchTap={()=>this.handleAddGroup(k)}/>
        );
    }

    renderChip(data) {
        return (
            <Chip key={data} style={this.style.chip}
                  onRequestDelete={()=>this.handleDelGroup(data)}>{this.getValue(data)}</Chip>
        );
    }
}

PermissionPaper.prototypes = {
    addGroup: React.PropTypes.func,
    delGroup: React.PropTypes.func,
};

PermissionPaper.contextTypes = {
    muiTheme: React.PropTypes.object.isRequired,
};


class Group extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            open: false
        };
    }

    handleOpen = () => {
        this.props.getGroupDetail(this.props.group.id);
        this.setState({...this.state, open: true});
    };

    handleClose = () => {
        this.setState({...this.state, open: false});
    };

    shouldComponentUpdate(nextProps, nextState) {
        if (nextProps.detail.id != this.props.group.id) {
            return false;
        }
        return true;
    }

    render() {
        const actions = [
            <FlatButton
                label="Close"
                primary={true}
                onTouchTap={e=>this.handleClose()}
            />,
        ];
        return (<div>
            <FlatButton {...this.props} primary={true} label={this.props.group.id} onTouchTap={this.handleOpen}/>
            <Dialog
                title={"Group - " + this.props.group.name}
                actions={actions}
                modal={false}
                open={this.state.open}
                onRequestClose={()=>this.handleClose()}
            >
                <PermissionPaper addGroup={(key)=>this.props.addRoleToGroup(this.props.group.id, key)}
                                 delGroup={(key)=>this.props.delRoleToGroup(this.props.group.id, key)}
                                 detail={()=>this.props.detail.roles}
                                 total={this.props.permissionRoles}
                                 title="Roles"/>
                <PermissionPaper addGroup={(key)=>this.props.addPrivilegeToGroup(this.props.group.id, key)}
                                 delGroup={(key)=>this.props.delPrivilegeToGroup(this.props.group.id, key)}
                                 detail={()=>this.props.detail.privileges}
                                 total={this.props.permissionPrivileges}
                                 title="Privileges"/>
            </Dialog>
        </div>);
    }
}

const mapDispatchToProps = dispatch=> {
    return {
        getGroupDetail: (id) => dispatch(Action.action(Action.GROUP_DETAIL[Action.REQUEST], {payload: id})),
        addRoleToGroup: (id, role) => dispatch(Action.action(Action.GROUP_ADD_ROLE[Action.REQUEST], {
            payload: {
                id,
                role
            }
        })),
        delRoleToGroup: (id, role) => dispatch(Action.action(Action.GROUP_DEL_ROLE[Action.REQUEST], {
            payload: {
                id,
                role
            }
        })),
        addPrivilegeToGroup: (id, privilege) => dispatch(Action.action(Action.GROUP_ADD_PRIVILEGE[Action.REQUEST], {
            payload: {
                id,
                privilege
            }
        })),
        delPrivilegeToGroup: (id, privilege) => dispatch(Action.action(Action.GROUP_DEL_PRIVILEGE[Action.REQUEST], {
            payload: {
                id,
                privilege
            }
        })),
    }
};

Group = connect(state => state.groupDetail, mapDispatchToProps)(Group);
Group = connect(state => {
        const permissionRoles = {};
        state.permission.roles.map(a=>permissionRoles[a.name] = a.label);
        return {permissionRoles}
    }
)(Group);
Group = connect(state => {
        const permissionPrivileges = {};
        state.permission.privileges.map(a=>permissionPrivileges[a.name] = a.label);
        return {permissionPrivileges}
    }
)(Group);

export default Group;