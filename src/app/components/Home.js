import React from "react";

export class Home extends React.Component {
    constructor(props) {
        super();
        this.state = {
            age: props.age
        };
    }

    onMakeOlder() {
        this.setState({
            age: this.state.age + 3
        });
    }

    render() {
        return (
            <div>
                <p>In a new Component</p>
                <p>Hello {this.props.name} age {this.state.age}</p>
                <ul>
                    {this.props.hobby.map((hobby, i)=><li key={i}>{hobby}</li>)}
                </ul>
                <button className="btn btn-primary" onClick={()=>this.onMakeOlder()}>Make me older</button>
            </div>
        );
    }
}

Home.propTypes = {
    name: React.PropTypes.string,
    hobby: React.PropTypes.array,
}