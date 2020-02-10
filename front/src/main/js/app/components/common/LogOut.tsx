import React = require("react");
import { Button, Container, Form, FormGroup, Input, Label, Row, Col } from "reactstrap";
import { bindActionCreators, AnyAction, Dispatch } from "redux";
import SET_TOKEN_STRING from "../../actions/SetToken";
import { connect } from "react-redux";
import SET_USER_NAME from "../../actions/SetUserName";

class LogOut extends React.Component<Props, State> {
    constructor(props: any) {
        super(props);

        this.state = {
            stateToken: props.propsToken,
            stateUserName: props.propsUserName
        }
    }
    logOut = () => {
        this.setState({
            stateToken:null,
            stateUserName:null
        });
        this.props.SET_TOKEN_STRING(null);
        this.props.SET_USER_NAME(null);
    }
    checkUserData = (): boolean => {

        const username = this.state.stateUserName;
        const token = this.state.stateToken;
        console.log("валидация пропертей " + username + token);
        if (username == null || username == undefined || username == "") {
            return false;
        } else if (token == null || token == undefined || token == "") {
            return false;
        } else {
            return true;
        }
    }
    render() {
        let container: JSX.Element;
        if (this.checkUserData()) {
            container = <Container>
                <Form inline>
                    <FormGroup>
                        <Label for="login" className="mr-sm-2"><h1>You are logged in as:</h1></Label>
                        <h1><u>{this.state.stateUserName}</u></h1>
                    </FormGroup>&nbsp; &nbsp;
                    <Button color="success" onClick={this.logOut} >Log out</Button>
                </Form>
            </Container>
        } else {
            container = <Container>
                <Form inline>
                    <FormGroup>
                        <Label for="login" className="mr-sm-2"><h1>You are not logged in</h1></Label>
                    </FormGroup>
                </Form>
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
    SET_TOKEN_STRING: typeof SET_TOKEN_STRING,
    SET_USER_NAME: typeof SET_USER_NAME
}

const mapDispatchToProps = (dispatch: Dispatch): DispatchProps => ({
    ...bindActionCreators({ SET_TOKEN_STRING, SET_USER_NAME }, dispatch),
});

interface Props extends State, DispatchProps {
    token: string,
    userName: string
}
interface State {
    stateToken: string|null,
    stateUserName: string|null
}

export default connect(mapStateToProps, mapDispatchToProps)(LogOut);