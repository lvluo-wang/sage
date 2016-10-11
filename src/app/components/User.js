import React from "react";
import Chip from "material-ui/Chip";
import {Card, CardActions, CardHeader} from "material-ui/Card";
import Divider from "material-ui/Divider";
import {connect} from "react-redux";


class User extends React.Component {

    constructor(props) {
        super(props);
        this.state = props;
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
        if (this.state.profile.id < 0) {
            return null;
        } else {
            return this.state.profile.id;
        }
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
User = connect(state => state.user)(User);


export default User;