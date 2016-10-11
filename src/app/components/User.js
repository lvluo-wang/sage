import React from "react";
import Chip from "material-ui/Chip";
import {Card, CardActions, CardHeader} from "material-ui/Card";
import Divider from "material-ui/Divider";
import * as API from "../services";


class User extends React.Component {
    state = {
        profile: {
            id: -1,
            claims: {}
        },
        roles: [],
        privileges: []
    };

    constructor(props) {
        super(props);
        API.getUserProfile().then(({ok, response})=> {
            if (ok) {
                let claims = {};
                response.claimList.map(a=>claims[API.UTIL.getName(a.type)] = {
                    title: API.UTIL.getLabel(a.type),
                    value: a.value
                });
                this.setState({
                    ...this.state,
                    profile: {
                        id: response.identity.id,
                        claims
                    }
                })
            }
        });
        API.getUserRoles().then(({ok, response})=> {
            if (ok) {
                this.setState({
                    ...this.state,
                    roles: response
                })
            }
        });
        API.getUserPrivileges().then(({ok, response})=> {
            if (ok) {
                this.setState({
                    ...this.state,
                    privileges: response
                })
            }
        })
    }

    style = {
        chip: {
            margin: 4,
        }, wrapper: {
            display: 'flex',
            flexWrap: 'wrap',
        },
    };

    getUserName() {
        let v = this.state.profile.claims['USERNAME'];
        if (v) {
            return v.value;
        }
        return this.state.profile.id;
    }

    render() {
        return (
            <div>
                <h3>Hello, { this.getUserName() || "用户" }</h3>
                <Divider/>
                {this.renderCard("Role List", this.state.roles)}
                <Divider/>
                {this.renderCard("Privilege List", this.state.privileges)}
            </div>
        );
    }

    renderCard(title, keys) {
        return (
            <Card>
                <CardHeader
                    title={title}
                    expanded={true}
                    showExpandableButton={true}
                />
                <CardActions expandable={true} style={this.style.wrapper}>
                    {keys.map(this.renderChip, this)}
                </CardActions>
            </Card>
        );
    }

    renderChip(data) {
        return (
            <Chip key={data} style={this.style.chip}>{data}</Chip>
        );
    }
}


export default User;