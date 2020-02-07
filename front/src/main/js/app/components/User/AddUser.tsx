import React = require("react");
import { Button, Col, Container, Form, FormGroup, Input, Label, Row } from "reactstrap";
import { User } from "../../Interfaces/User";

export default class AddUser extends React.Component<{}, {
    user?: User,
    login: string, roles: Array<String>, firstName: string, lastName: string, personNumber: string, status: string, email: string, password: string,
    sendingData: boolean, gotResult: boolean, messages: Array<JSX.Element> | null, errors: Array<JSX.Element> | null
}> {
    constructor(props: any) {
        super(props);
        this.state = this.getInitState();
    }
    getInitState = () => {
        return {
            user: undefined,
            login: "",
            roles: new Array<String>(),
            firstName: "",
            lastName: ",",
            personNumber: "",
            status: "",
            email: "",
            password: "",
            sendingData: false,
            gotResult: false,
            messages: null,
            errors: null
        }
    }

    fieldChangeHandler = (event: any) => {
        const fieldId = event.target.id;
        const fieldValue = event.target.value;
        if ((fieldValue as string).length < 1) {
            return;
        }
        switch (fieldId) {
            case "userLogin": this.setState({ login: fieldValue });
                break;
            case "userPassword": this.setState({ password: fieldValue });
                break;
            case "email": this.setState({ email: fieldValue });
                break;
            default: console.debug("Field id is undefined!")
        }
    }
    clearButtonHandler = () => {
        this.setState(this.getInitState());
    }


    sendDataToServer = async () => {
        // const validationMessages: Array<JSX.Element> = InstanceInfoValidator(this.state);
        // if (validationMessages.length > 0) {
        //     this.setState({
        //         sendingData: false, gotResult: true, messages: validationMessages
        //     });

        //     return;
        // }

        // const contextRoot = location.origin + location.pathname;
        // // const requestURL = 'http://127.0.0.1:8887/statusList.json';
        // // const requestURL = `${contextRoot}rest/create/user`;
        // const requestURL = `http://127.0.0.1:8080/database_monitoring/rest/create/instance/`;
        // const { id, host, port, database, type, login, password } = this.state;
        // const postBody = {
        //     id, host, port, database, type, user: { login: login, password: password }
        // }
        // this.setState({ sendingData: true, gotResult: false, errors: null, messages: null });
        // await fetch(requestURL, {
        //     method: 'POST',
        //     headers: {
        //         'Accept': 'application/json',
        //         'Content-Type': 'application/json'
        //     },
        //     body: JSON.stringify(postBody)
        // }).then((response) => {
        //     if (response.status == 403) {
        //         this.setState({ sendingData: false, gotResult: true, messages: [<ForbiddenMeesage key={'forbiddenMessageBox'} />] });
        //         return null;
        //     }
        //     else if (response.status == 200) {
        //         return response.json();
        //     } else {
        //         throw Error(`Response status: ${response.status}`);
        //     }
        // })
        //     .then((json) => {
        //         if (json == null) {
        //             return;
        //         }
        //         const successFlag = json['success'] as boolean;
        //         if (successFlag) {
        //             this.setState(this.getInitState());
        //             this.setState({ sendingData: false, gotResult: true, messages: [<LoadingSuccessMessage key={'successMessageBox'} />] });
        //         } else {
        //             const messageList = MessageComponentFactory(json);
        //             const errorsList = ErrorComponentFactory(json);
        //             this.setState({
        //                 sendingData: false, gotResult: true, messages: messageList, errors: errorsList
        //             });
        //         }
        //     })
        //     .catch((error) => {
        //         console.debug(`Exception in request: ${error}`);
        //         this.setState({ sendingData: false, gotResult: true, errors: [<LoadingErrorMessage key={'errorMessageBox'} />] });
        //     });
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
        return (
            <>
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
                                        placeholder="Input user password..." value={this.state.password} autoComplete="on"/>
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
                        <Row >
                            <Col md={6}></Col>
                            {buttonColumn}
                        </Row>
                    </Form>
                </Container>
                <br />
                {/* <Container>
                    <Row>
                        <Col className="row justify-content-center" >{this.state.sendingData ? <Spinner color="secondary" type="grow" style={{ width: '8rem', height: '8rem' }} /> : null}</Col>
                    </Row>
                    <Row>
                        <Col>
                            {this.state.gotResult ? this.state.messages : null}
                            {this.state.gotResult ? this.state.errors : null}
                        </Col>
                    </Row>
                </Container> */}
            </>
        );
    }
}