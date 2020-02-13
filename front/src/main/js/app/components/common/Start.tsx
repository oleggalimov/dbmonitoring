import React = require("react");
import { connect } from "react-redux";
import { bindActionCreators, Dispatch } from "redux";
import SET_TOKEN_STRING from "../../actions/SetToken";
import Authorisation from "./Authorisation";

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
        return <Authorisation token = {this.state.stateToken} userName={this.state.stateUserName}/>
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