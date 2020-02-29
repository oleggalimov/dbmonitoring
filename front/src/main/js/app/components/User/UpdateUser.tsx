import React = require("react");
import { Button, Col, Container, Form, FormGroup, Input, Label, Row, Spinner, CustomInput } from "reactstrap";
import { Instance } from "../../Interfaces/Instance";
import ErrorComponentFactory from "../../utils/ErrorComponentFactory";
import InstanceInfoValidator from "../../utils/InstanceInfoValidator";
import MessageComponentFactory from "../../utils/MessageComponentFactory";
import ForbiddenMeesage from "../common/ForbiddenMeesage";
import LoadingErrorMessage from "../common/LoadingErrorMessage";
import { User } from "../../Interfaces/User";
import UserInfoValidator from "../../utils/UserInfoValidator";
import Switch from "@material-ui/core/Switch";
import { connect } from "react-redux";
import Authorisation from "../common/Authorisation";


class UpdateUser extends React.Component<Props, State> {
    constructor(props: any) {
        super(props);
        this.state = this.getInitState(props.propsToken);
    }

    getInitState(token: string | null) {
        return {
            stateToken: token,
            loading: false,
            messages: null,
            errors: null,
            userInfoLoaded: false,

            login: "",
            firstName: "",
            lastName: "",
            personNumber: "",
            status: false,
            email: "",
            userPassword: "",
            userRole: false,
            adminRole: false,
            userAdminRole: false
        }
    }

    abortController = new AbortController();
    componentWillUnmount() {
        this.abortController.abort();
    }

    loginFieldChangeHandler = (event: any) => {
        this.setState({
            login: event.target.value
        });
    }
    fieldChangeHandler = (event: any) => {
        this.setState({
            login: event.target.value
        });
    }
    clearButtonHandler = () => {
        this.setState({
            firstName: "",
            lastName: "",
            personNumber: "",
            status: false,
            email: "",
            userPassword: "",
            userRole: false,
            adminRole: false,
            userAdminRole: false
        });
    }
    userFormChangeHandler = (event: any) => {
        const fieldId = event.target.id;
        const fieldValue = event.target.value.trim();
        switch (fieldId) {
            case "userLogin": this.setState({ login: fieldValue });
                break;
            case "userPassword": this.setState({ userPassword: fieldValue });
                break;
            case "email": this.setState({ email: fieldValue });
                break;
            case "firstName": this.setState({ firstName: fieldValue });
                break;
            case "lastName": this.setState({ lastName: fieldValue });
                break;
            case "personNumber": this.setState({ personNumber: fieldValue });
                break;
            case "userRole": this.setState({ userRole: !this.state.userRole });
                break;
            case "userAdminRole": this.setState({ userAdminRole: !this.state.userAdminRole });
                break;
            case "adminRole": this.setState({ adminRole: !this.state.adminRole });
                break;
            default: console.debug("Field id is undefined!")
        }
    }

    loadUserInfo = async () => {

        if (this.state.login.trim().length == 0) {
            return;
        }
        const contextRoot = location.origin + location.pathname;
        const requestURL = `${contextRoot}rest/list/user/${this.state.login}`;
        this.setState({ loading: true, errors: null, messages: null });
        const headers = new Headers();
        headers.append('Accept', 'application/json');
        headers.append('Content-Type', 'application/json');
        headers.append('Authorization', `Basic ${this.state.stateToken}`);
        await fetch(requestURL, {
            signal: this.abortController.signal,
            method: 'GET',
            headers: headers
        })
            .then((response) => {
                if (response.status == 403 || response.status == 401) {
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
                    const userInfo: User = json['body']["USERS"][0];
                    let { firstName, lastName, personNumber, email, roles, status } = userInfo;

                    this.setState({
                        userInfoLoaded: true,
                        firstName: firstName,
                        lastName: lastName,
                        personNumber: personNumber,
                        email: email,
                        status: status == "ACTIVE"
                    });
                    roles.forEach(role => {
                        switch (role) {
                            case "USER":
                                this.setState({ userRole: true });
                                break;
                            case "USER_ADMIN":
                                this.setState({ userAdminRole: true });
                                break;
                            case "ADMIN":
                                this.setState({ adminRole: true });
                                break;
                        }
                    });
                }
                const messageList = MessageComponentFactory(json);
                const errorsList = ErrorComponentFactory(json);
                this.setState({ messages: messageList, errors: errorsList, loading: false });

            })
            .catch((error) => {
                console.debug(`Exception on request to ${requestURL}: ${error}`);
                if (error.name == 'AbortError') {
                    return;
                }
                this.setState({ loading: false, errors: [<LoadingErrorMessage key={'errorMessageBox'} />] });
            });
    }
    sendDataToServer = async () => {
        this.setState({ errors: null, messages: null });
        const validationMessages: Array<JSX.Element> = UserInfoValidator(this.state);
        if (validationMessages.length > 0) {
            this.setState({
                messages: validationMessages
            });
            return;
        }

        const contextRoot = location.origin + location.pathname;
        const requestURL = `${contextRoot}rest/update/user`;
        const roles: Array<string> = new Array();
        if (this.state.userRole) {
            roles.push("USER");
        }
        if (this.state.adminRole) {
            roles.push("ADMIN");
        }
        if (this.state.userAdminRole) {
            roles.push("USER_ADMIN");
        }
        const { login, firstName, lastName, personNumber, email, userPassword, status } = this.state;
        const userInfo: User = {
            login: login,
            roles: roles,
            firstName: firstName,
            lastName: lastName,
            personNumber: personNumber,
            status: status ? "ACTIVE" : "BLOCKED",
            email: email,
            password: userPassword
        }
        this.setState({ loading: true, errors: null, messages: null });
        const headers = new Headers();
        headers.append('Accept', 'application/json');
        headers.append('Content-Type', 'application/json');
        headers.append('Authorization', `Basic ${this.state.stateToken}`);
        await fetch(requestURL, {
            signal: this.abortController.signal,
            method: 'PUT',
            headers:headers,
            body: JSON.stringify(userInfo)
        }).then((response) => {
            if (response.status == 403 || response.status == 401) {
                this.setState({ loading: false, messages: [<ForbiddenMeesage key={'forbiddenMessageBox'} />] });
                return null;
            }
            else if (response.status == 200) {
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
                    this.setState(this.getInitState(this.state.stateToken));
                }
                const messageList = MessageComponentFactory(json);
                const errorsList = ErrorComponentFactory(json);
                this.setState({
                    loading: false, messages: messageList, errors: errorsList
                });
            })
            .catch((error) => {
                console.debug(`Exception on request to ${requestURL}: ${error}`);
                if (error.name == 'AbortError') {
                    return;
                }
                this.setState({ loading: false, errors: [<LoadingErrorMessage key={'errorMessageBox'} />] });
            });
    }

    render() {
        let container:JSX.Element;
        const token = this.state.stateToken;
        if (token == "" || token == null || token == undefined) {
            container= <Authorisation />
        } else {
            if (this.state.loading) {
                container =  <>
                    <Container>
                        <Row>
                            <Col className="row justify-content-center" >
                                <Spinner color="primary" type="grow" style={{ width: '8rem', height: '8rem' }} />
                            </Col>
                        </Row>
                    </Container>
                </>
            } else if (this.state.userInfoLoaded == false) {
                container =  <>
                    <Container>
                        <Form onKeyPress={event => {
                            if (event.key === "Enter") {
                                this.loadUserInfo();
                            }
                        }}>
                            <Row form>
                                <Col>
                                    <FormGroup>
                                        <Label for="instanceId"><b>USER ID FOR UPDATE: </b></Label>
                                        <Input onChange={this.loginFieldChangeHandler} type="text" name="instanceIdentifier" id="instanceId" placeholder="Input instance id..." />
                                    </FormGroup>
                                </Col>
                            </Row>
                            {this.state.login == "" ?
                                <Row>
                                    <Col className="row justify-content-end" >
                                        <Button color="success" disabled>Load data</Button>&nbsp;
                                </Col>
                                </Row>
                                :
                                <Row>
                                    <Col className="row justify-content-end" >
                                        <Button onClick={this.loadUserInfo} color="success">Load data</Button>&nbsp;
                                </Col>
                                </Row>
                            }
                        </Form>
                    </Container>
                    <br />
                    <Container>
                        {this.state.messages}
                        {this.state.errors}
                    </Container>
                </>
            } else {
                container =  <>
                    <Container>
                        <Form>
                            <Row form>
                                <Col md={4}>
                                    <FormGroup>
                                        <Label for="userLogin"><b>LOGIN: </b></Label>
                                        <Input type="text" name="userLoginField" id="userLogin" value={this.state.login} disabled />
                                    </FormGroup>
                                </Col>
                                <Col>
                                    <FormGroup>
                                        <Label for="userPassword"><b>PASSWORD: </b></Label>
                                        <Input
                                            onChange={this.userFormChangeHandler} type="password" name="userPasswordField" id="userPassword"
                                            placeholder="Input user password..." value={this.state.userPassword} autoComplete="on" />
                                    </FormGroup>
                                </Col>
                                <Col>
                                    <FormGroup>
                                        <Label for="email"><b>E-MAIL: </b></Label>
                                        <Input
                                            onChange={this.userFormChangeHandler} type="email" name="userEmailField" id="email"
                                            placeholder="Input user e-mail..." value={this.state.email} />
                                    </FormGroup>
                                </Col>
                            </Row>
                            <Row>
                                <Col>
                                    <FormGroup>
                                        <Label for="firstName"><b>FIRST NAME: </b></Label>
                                        <Input
                                            onChange={this.userFormChangeHandler} type="text" name="firstNameField" id="firstName"
                                            placeholder="Input first name..." value={this.state.firstName} />
                                    </FormGroup>
                                </Col>
                                <Col>
                                    <FormGroup>
                                        <Label for="lastName"><b>LAST NAME: </b></Label>
                                        <Input
                                            onChange={this.userFormChangeHandler} type="text" name="lastNameField" id="lastName"
                                            placeholder="Input last name..." value={this.state.lastName} />
                                    </FormGroup>
                                </Col>
                                <Col>
                                    <FormGroup>
                                        <Label for="personNumber"><b>PERSON NUMBER: </b></Label>
                                        <Input
                                            onChange={this.userFormChangeHandler} type="text" name="personNumberField" id="personNumber"
                                            placeholder="Input last person number..." value={this.state.personNumber} />
                                    </FormGroup>
                                </Col>
                            </Row>
                            <Row>
                                <Col>
                                    <FormGroup check>
                                        <Label><b>USER ROLES: </b></Label>
                                        <br />
                                        <Label check>
                                            <Input id="userRole" type="checkbox" onChange={this.userFormChangeHandler} checked={this.state.userRole} />{' '}
                                            User
                                        </Label>
                                        <br />
                                        <Label check>
                                            <Input id="userAdminRole" type="checkbox" onChange={this.userFormChangeHandler} checked={this.state.userAdminRole} />{' '}
                                            User administrator
                                        </Label>
                                        <br />
                                        <Label check>
                                            <Input id="adminRole" type="checkbox" onChange={this.userFormChangeHandler} checked={this.state.adminRole} />{' '}
                                            Administrator
                                        </Label>
    
                                    </FormGroup>
                                    <br />
                                </Col>
                            </Row>
                            <Row>
                                <Col>
                                    <FormGroup>
                                        ACTIVE USER <Switch color="primary" checked={this.state.status} onChange={() => this.setState({ status: !this.state.status })} />
                                    </FormGroup>
                                </Col>
                            </Row>
                            <Row >
                                <Col md={6}></Col>
                                <Col md={6} className="row justify-content-end">
                                    <Button onClick={this.clearButtonHandler} color="warning">Clear</Button>&nbsp;
                                        <Button onClick={this.sendDataToServer} color="success">Submit</Button>
                                </Col>
                            </Row>
                        </Form>
                    </Container>
                    <br />
                    <Container>
                        {this.state.messages}
                        {this.state.errors}
                    </Container>
                </>
            }
        }
        return container;
    }
}

const mapStateToProps = (state: any) => ({
    propsToken: state.token
});


interface Props extends State {
    token: string
}
interface State {
    stateToken: string | null,
    login: string,
    firstName: string,
    lastName: string,
    personNumber: string,
    status: boolean,
    email: string,
    userPassword: string,
    loading: boolean,
    userInfoLoaded: boolean,
    messages: Array<JSX.Element> | null,
    errors: Array<JSX.Element> | null,
    userRole: boolean,
    adminRole: boolean,
    userAdminRole: boolean
}

export default connect(mapStateToProps)(UpdateUser);