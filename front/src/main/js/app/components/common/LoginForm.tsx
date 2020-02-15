import React = require("react");
import { connect } from "react-redux";
import { Button, Container, Form, FormGroup, Input, Label } from "reactstrap";
import { bindActionCreators, Dispatch } from "redux";
import SET_TOKEN_STRING from "../../actions/SetToken";
import SET_USER_NAME from "../../actions/SetUserName";
import BasicAuthTokenCreator from "../../utils/BasicAuthTokenCreator";
import ErrorComponentFactory from "../../utils/ErrorComponentFactory";
import MessageComponentFactory from "../../utils/MessageComponentFactory";
import ForbiddenMeesage from "./ForbiddenMeesage";
import LoadingErrorMessage from "./LoadingErrorMessage";


class LoginForm extends React.Component<Props, StateProps> {
    constructor(props: any) {
        super(props);
        this.state = {
            loading: false,
            loginSuccessful: false,
            messages: null,
            errors: null,
            username: "",
            password: "",
        }
    }
    controller = new AbortController();
    componentWillUnMount() {
        this.controller.abort();
    }
    handleLoginFormChanges = (event: any) => {
        const fieldId = event.target.id;
        const fieldValue = event.target.value;
        if ((fieldValue as string).length < 1) {
            return;
        }
        switch (fieldId) {
            case "userLogin": this.setState({ username: fieldValue });
                break;
            case "userPassword": this.setState({ password: fieldValue });
                break;

            default: console.debug("Field id is undefined!")
        }
    }

    logIn = async () => {
        const contextRoot = location.origin + location.pathname;
        // const requestURL = 'http://127.0.0.1:8887/statusList.json';
        // // const requestURL = `${contextRoot}rest/create/user`;
        const requestURL = `http://127.0.0.1:8080/database_monitoring/rest/login`;
        this.setState({ loading: true, errors: null, messages: null });
        const { username, password } = this.state;
        const postBody = {
            login: username,
            password: password
        }
        await fetch(requestURL, {
            signal: this.controller.signal,
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
                    const token = BasicAuthTokenCreator(this.state.username, this.state.password);
                    this.props.SET_TOKEN_STRING(token);
                    this.props.SET_USER_NAME(this.state.username);
                    this.setState({ loginSuccessful:true });
                }
                const messageList = MessageComponentFactory(json);
                const errorsList = ErrorComponentFactory(json);
                this.setState({ messages: messageList, errors: errorsList, loading: false });

            })
            .catch((error) => {
                console.debug(`Exception on request to ${requestURL}: ${error}`);
                if (error.name == 'AbortError') {
                    return;
                };
            this.setState({ loading: false,errors: [<LoadingErrorMessage key={'errorMessageBox'}/>]});
            });
    }

    render() {
        let container: JSX.Element;
        if (this.state.loginSuccessful) {
            container = <Container>
                <h1>
                    Welcome, {this.state.username}
                </h1>

            </Container>
        } else {
            container = <>
                <Container>
                    <Form inline onChange={this.handleLoginFormChanges}>
                        <FormGroup className="mb-2 mr-sm-2 mb-sm-0">
                            <Label for="login" className="mr-sm-2">Login</Label>
                            <Input type="text" id="userLogin" placeholder="Input your login" />
                        </FormGroup>
                        <FormGroup className="mb-2 mr-sm-2 mb-sm-0">
                            <Label for="password" className="mr-sm-2">Password</Label>
                            <Input type="password" id="userPassword" autoComplete="true" />
                        </FormGroup>
                        {this.state.loading ? <Button color="success" disabled>Login...</Button>
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
        }
        return (
            container
        );
    }


}
const mapStateToProps = (state: any) => ({
    token: state.token
});

interface DispatchProps {
    SET_TOKEN_STRING: typeof SET_TOKEN_STRING,
    SET_USER_NAME: typeof SET_USER_NAME
}

const mapDispatchToProps = (dispatch: Dispatch): DispatchProps => ({
    ...bindActionCreators({ SET_TOKEN_STRING, SET_USER_NAME }, dispatch),
});

interface Props extends StateProps, DispatchProps {
    token: string,
    userName: string
}

interface StateProps {
    username: string,
    password: string,
    loading: boolean,
    loginSuccessful: boolean,
    messages: Array<JSX.Element> | null,
    errors: Array<JSX.Element> | null
}

export default connect(mapStateToProps, mapDispatchToProps)(LoginForm);