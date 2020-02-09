import React = require("react");
import { Button, Col, Container, Form, FormGroup, Input, Label, Row, Spinner } from "reactstrap";
import ErrorComponentFactory from "../../utils/ErrorComponentFactory";
import MessageComponentFactory from "../../utils/MessageComponentFactory";
import ForbiddenMeesage from "../common/ForbiddenMeesage";
import LoadingErrorMessage from "../common/LoadingErrorMessage";


export default class RemoveUser extends React.Component<{}, {
    loading: boolean, userLogin: string, messages: Array<JSX.Element> | null, errors: Array<JSX.Element> | null
}> {
    constructor(props: any) {
        super(props);
        this.state = this.getInitState();

    }

    getInitState() {
        return {
            loading: false,
            userLogin: "",
            messages: null,
            errors: null
        }
    }

    fieldChangeHandler = (event: any) => {
        this.setState({
            userLogin: event.target.value
        });
    }

    loadUserInfo = async () => {
        const contextRoot = location.origin + location.pathname;
        // const requestURL = 'http://127.0.0.1:8887/statusList.json';
        // const requestURL = `${contextRoot}rest/create/user`;
        const requestURL = `http://127.0.0.1:8080/database_monitoring/rest/delete/user/${this.state.userLogin}`;
        this.setState({ loading: true, errors: null, messages: null });
        await fetch(requestURL, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
            .then((response) => {
                if (response.status == 403 || response.status == 401)  {
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
                    this.setState({ userLogin: "" });
                }
                const messageList = MessageComponentFactory(json);
                const errorsList = ErrorComponentFactory(json);
                this.setState({ messages: messageList, errors: errorsList, loading: false });

            })
            .catch((error) => {
                console.debug(`Exception: ${error}`);
                this.setState({ loading: false, errors: [<LoadingErrorMessage key={'errorMessageBox'} />] });
            });
    }

    render() {
        if (this.state.loading) {
            return <>
                <Container>
                    <Row>
                        <Col className="row justify-content-center" >
                            <Spinner color="primary" type="grow" style={{ width: '8rem', height: '8rem' }} />
                        </Col>
                    </Row>
                </Container>
            </>
        } else {
            return <>
                <Container>
                    <Form onKeyPress={event => {
                        if (event.key === "Enter") {
                            this.loadUserInfo();
                        }
                    }}>
                        <Row form>
                            <Col>
                                <FormGroup>
                                    <Label for="instanceId"><b>USER LOGIN FOR DELETE: </b></Label>
                                    <Input onChange={this.fieldChangeHandler} type="text" id="userLogin" placeholder="Input user login here..." />
                                </FormGroup>
                            </Col>
                        </Row>
                        {this.state.userLogin == "" ?
                            <Row>
                                <Col className="row justify-content-end" >
                                    <Button onClick={this.loadUserInfo} color="success" disabled>Delete</Button>&nbsp;
                            </Col>
                            </Row>
                            :
                            <Row>
                                <Col className="row justify-content-end" >
                                    <Button onClick={this.loadUserInfo} color="success">Delete</Button>&nbsp;
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
        }
    }
}