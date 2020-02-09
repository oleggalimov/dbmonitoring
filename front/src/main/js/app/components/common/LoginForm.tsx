import React = require("react");
import { Button, Container, Form, FormGroup, Input, Label } from "reactstrap";
import { bindActionCreators, AnyAction, Dispatch } from "redux";
import SET_TOKEN_STRING from "../../actions/SetToken";
import ForbiddenMeesage from "./ForbiddenMeesage";
import { connect } from "react-redux";

class LoginForm extends React.Component<Props, StateProps> {
    constructor(props: any) {
        super(props);
        this.state = {
            loading: false,
            messages: null,
            errors: null
        }
    }
    handleLoginFormChanges = () => {

    }

    logIn = async () => {
        const contextRoot = location.origin + location.pathname;
        // const requestURL = 'http://127.0.0.1:8887/statusList.json';
        // const requestURL = `${contextRoot}rest/create/user`;
        const requestURL = `http://127.0.0.1:8080/database_monitoring/rest/login`;
        this.setState({ loading: true, errors: null, messages: null });

        // const { userLogin, userPassword } = this.state;
        const postBody = {
            login: "userLogin",
            password: "userPassword"
        }
        await fetch(requestURL, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(postBody)
        })
            .then((response) => {
                if (response.status == 403) {
                    this.setState({ loading: false, messages: [<ForbiddenMeesage key={'forbiddenMessageBox'} />] });
                    return null;
                } else if (response.status == 200) {
                    return response.json();
                } else {
                    throw Error(`Response status: ${response.status}`);
                }
            })
            .then((json) => {
                if (json == null) {
                    return;
                }
                const successFlag = json['success'] as boolean;
                if (successFlag) {
                    // const token = BasicAuthTokenCreator(this.state.userLogin, this.state.userPassword);
                    // this.setState({ basicToken: token });
                }
                // const messageList = MessageComponentFactory(json);
                // const errorsList = ErrorComponentFactory(json);
                // this.setState({ messages: messageList, errors: errorsList, loading: false });

            })
            .catch((error) => {
                console.debug(`Exception: ${error}`);
                this.setState({ loading: false });
            });
    }

    render() {
        return (
            <>
                <Container>
                    <Form inline onChange={this.handleLoginFormChanges}>
                        <FormGroup className="mb-2 mr-sm-2 mb-sm-0">
                            <Label for="login" className="mr-sm-2">Login</Label>
                            <Input type="text" id="login" placeholder="Input your login" />
                        </FormGroup>
                        <FormGroup className="mb-2 mr-sm-2 mb-sm-0">
                            <Label for="password" className="mr-sm-2">Password</Label>
                            <Input type="password" id="password" autoComplete="true" />
                        </FormGroup>
                        {this.state.loading ? <Button color="success" disabled>Sign in</Button>
                            :
                            <Button color="success" onClick={this.logIn}>Sign in</Button>
                        }

                    </Form>
                </Container>
                <br />
                <Container>
                    {this.state.messages}
                    {this.state.errors}
                </Container>
            </>
        );
    }


}
const mapStateToProps = (state: any) => ({ 
    token: state.token
});

interface DispatchProps {
    SET_TOKEN_STRING: typeof SET_TOKEN_STRING
}

const mapDispatchToProps = (dispatch: Dispatch): DispatchProps => ({
    ...bindActionCreators({ SET_TOKEN_STRING }, dispatch),
});

interface Props extends StateProps, DispatchProps {
    token: string
}

interface StateProps {
    loading: boolean,
    messages: Array<JSX.Element> | null,
    errors: Array<JSX.Element> | null
}

export default connect(mapStateToProps, mapDispatchToProps)(LoginForm);