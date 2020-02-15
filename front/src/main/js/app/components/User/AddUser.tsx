import React = require("react");
import { connect } from "react-redux";
import { Button, Col, Container, Form, FormGroup, Input, Label, Row, Spinner } from "reactstrap";
import { User } from "../../Interfaces/User";
import ErrorComponentFactory from "../../utils/ErrorComponentFactory";
import MessageComponentFactory from "../../utils/MessageComponentFactory";
import UserInfoValidator from "../../utils/UserInfoValidator";
import Authorisation from "../common/Authorisation";
import ForbiddenMeesage from "../common/ForbiddenMeesage";
import LoadingErrorMessage from "../common/LoadingErrorMessage";

class AddUser extends React.Component<Props, State> {
    constructor(props: any) {
        super(props);
        console.log(JSON.stringify(props));
        this.state = this.getInitState(props.propsToken);
    }
    getInitState = (token:string|null) => {
        return {
            stateToken: token,
            login: "",
            firstName: "",
            lastName: "",
            personNumber: "",
            status: "",
            email: "",
            userPassword: "",
            sendingData: false,
            gotResult: false,
            messages: null,
            errors: null,
            userRole: true,
            adminRole: false,
            userAdminRole: false
        }
    }
    abortController = new AbortController();

    fieldChangeHandler = (event: any) => {
        const fieldId = event.target.id;
        const fieldValue = event.target.value;
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
    clearButtonHandler = () => {
        this.setState(this.getInitState(this.state.stateToken));
    }
    componentWillUnmount() {
        this.abortController.abort();
    }

    sendDataToServer = async () => {
        this.setState({ errors: null, messages: null });
        const validationMessages: Array<JSX.Element> = UserInfoValidator(this.state);
        if (validationMessages.length > 0) {
            this.setState({
                sendingData: false, gotResult: true, messages: validationMessages
            });
            return;
        }
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
        const { login, firstName, lastName, personNumber, email, userPassword } = this.state;
        const userInfo: User = {
            login: login,
            roles: roles,
            firstName: firstName,
            lastName: lastName,
            personNumber: personNumber,
            status: "ACTIVE",
            email: email,
            password: userPassword
        }

        // const contextRoot = location.origin + location.pathname;
        // // const requestURL = `${contextRoot}rest/create/user`;
        const requestURL = `http://127.0.0.1:8080/database_monitoring/rest/create/user/`;

        this.setState({ sendingData: true, gotResult: false, errors: null, messages: null });
        const headers = new Headers();
        headers.append('Accept', 'application/json');
        headers.append('Content-Type', 'application/json');
        headers.append('Authorization', `Basic ${this.state.stateToken}`);
        await fetch(requestURL, {
            signal: this.abortController.signal,
            method: 'POST',
            headers: headers,
            body: JSON.stringify(userInfo)
        }).then((response) => {
            if (response.status == 403 || response.status == 401) {
                this.setState({ sendingData: false, gotResult: true, messages: [<ForbiddenMeesage key={'forbiddenMessageBox'} />] });
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
                    sendingData: false, gotResult: true, messages: messageList, errors: errorsList
                });
            })
            .catch((error) => {
                console.debug(`Exception on request to ${requestURL}: ${error}`);
                if (error.name == 'AbortError') {
                    return;
                }
                this.setState({ sendingData: false, gotResult: true, errors: [<LoadingErrorMessage key={'errorMessageBox'} />] });
            });
    }
    render() {
        let buttonColumn: JSX.Element;
        if (this.state.sendingData) {
            buttonColumn = <Col md={6} className="row justify-content-end">
                <Button disabled color="warning">Clear</Button>&nbsp;
                <Button disabled color="success">Submit</Button>
            </Col>
        } else {
            buttonColumn = <Col md={6} className="row justify-content-end">
                <Button onClick={this.clearButtonHandler} color="warning">Clear</Button>&nbsp;
                <Button onClick={this.sendDataToServer} color="success">Submit</Button>
            </Col>
        }
        let container:JSX.Element;
        const token = this.state.stateToken;
        if (token == "" || token == null || token == undefined) {
            container= <Authorisation />
        } else {
            container = <>
            <Container>
                <Form>
                    <Row form>
                        <Col md={4}>
                            <FormGroup>
                                <Label for="userLogin"><b>LOGIN: </b></Label>
                                <Input
                                    onChange={this.fieldChangeHandler} type="text" name="userLoginField" id="userLogin"
                                    placeholder="Input user login..." value={this.state.login} />
                            </FormGroup>
                        </Col>
                        <Col>
                            <FormGroup>
                                <Label for="userPassword"><b>PASSWORD: </b></Label>
                                <Input
                                    onChange={this.fieldChangeHandler} type="password" name="userPasswordField" id="userPassword"
                                    placeholder="Input user password..." value={this.state.userPassword} autoComplete="on" />
                            </FormGroup>
                        </Col>
                        <Col>
                            <FormGroup>
                                <Label for="email"><b>E-MAIL: </b></Label>
                                <Input
                                    onChange={this.fieldChangeHandler} type="email" name="userEmailField" id="email"
                                    placeholder="Input user e-mail..." value={this.state.email} />
                            </FormGroup>
                        </Col>
                    </Row>
                    <Row>
                        <Col>
                            <FormGroup>
                                <Label for="firstName"><b>FIRST NAME: </b></Label>
                                <Input
                                    onChange={this.fieldChangeHandler} type="text" name="firstNameField" id="firstName"
                                    placeholder="Input first name..." value={this.state.firstName} />
                            </FormGroup>
                        </Col>
                        <Col>
                            <FormGroup>
                                <Label for="lastName"><b>LAST NAME: </b></Label>
                                <Input
                                    onChange={this.fieldChangeHandler} type="text" name="lastNameField" id="lastName"
                                    placeholder="Input last name..." value={this.state.lastName} />
                            </FormGroup>
                        </Col>
                        <Col>
                            <FormGroup>
                                <Label for="personNumber"><b>PERSON NUMBER: </b></Label>
                                <Input
                                    onChange={this.fieldChangeHandler} type="text" name="personNumberField" id="personNumber"
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
                                    <Input id="userRole" type="checkbox" onChange={this.fieldChangeHandler} checked={this.state.userRole} />{' '}
                                    User
                                </Label>
                                <br />
                                <Label check>
                                    <Input id="userAdminRole" type="checkbox" onChange={this.fieldChangeHandler} checked={this.state.userAdminRole} />{' '}
                                    User administrator
                                </Label>
                                <br />
                                <Label check>
                                    <Input id="adminRole" type="checkbox" onChange={this.fieldChangeHandler} checked={this.state.adminRole} />{' '}
                                    Administrator
                                </Label>
                            </FormGroup>
                            <br />
                        </Col>
                    </Row>
                    <Row >
                        <Col md={6}></Col>
                        {buttonColumn}
                    </Row>
                </Form>
            </Container>
            <br />
            <Container>
                <Row>
                    <Col className="row justify-content-center" >{this.state.sendingData ? <Spinner color="secondary" type="grow" style={{ width: '8rem', height: '8rem' }} /> : null}</Col>
                </Row>
                <Row>
                    <Col>
                        {this.state.gotResult ? this.state.messages : null}
                        {this.state.gotResult ? this.state.errors : null}
                    </Col>
                </Row>
            </Container>
        </>
        }
        return (container);
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
    status: string, 
    email: string, 
    userPassword: string,
    sendingData: boolean, 
    gotResult: boolean, 
    messages: Array<JSX.Element> | null, 
    errors: Array<JSX.Element> | null,
    userRole: boolean, 
    adminRole: boolean, 
    userAdminRole: boolean
}

export default connect(mapStateToProps)(AddUser);