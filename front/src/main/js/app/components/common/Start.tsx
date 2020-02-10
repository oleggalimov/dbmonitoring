import React = require("react");
import { connect } from "react-redux";
import { NavItem, Nav } from "reactstrap";
import Container from "reactstrap/lib/Container";
import { bindActionCreators, Dispatch } from "redux";
import SET_TOKEN_STRING from "../../actions/SetToken";
import { NavLink } from "react-router-dom";

class Start extends React.Component<Props, State> {
    constructor(props: any) {
        super(props);
        this.state = {
            stateToken: this.props.propsToken,
            stateUserName: this.props.propsUserName
        }
    }
    render() {
        let container: JSX.Element;
        const token = this.state.stateToken;
        if (token == "" || token == null || token == undefined) {
            container = <Container>
                <h1>
                    You are not authorized. Please <NavLink to={`login`} > sign in </NavLink>
                </h1>

            </Container>
        } else {
            container = <Container>
                <h1>
                    Welcome, {this.state.stateUserName}
                </h1>

            </Container>
        }

        return (container);
    }
}


const mapStateToProps = (state: any) => ({
    propsToken: state.token,
    propsUserName: state.userName
});

interface DispatchProps {
    SET_TOKEN_STRING: typeof SET_TOKEN_STRING
}

const mapDispatchToProps = (dispatch: Dispatch): DispatchProps => ({
    ...bindActionCreators({ SET_TOKEN_STRING }, dispatch),
});

interface Props extends State, DispatchProps {
    propsToken: string,
    propsUserName:string
}

interface State {
    stateToken: string,
    stateUserName:string
}

export default connect(mapStateToProps, mapDispatchToProps)(Start);