import React = require("react");
import { Button, Col, Container, Form, FormGroup, Input, Label, Row, Spinner } from "reactstrap";
import ErrorComponentFactory from "../../utils/ErrorComponentFactory";
import MessageComponentFactory from "../../utils/MessageComponentFactory";
import ForbiddenMeesage from "../common/ForbiddenMeesage";
import LoadingErrorMessage from "../common/LoadingErrorMessage";
import { connect } from "react-redux";
import Authorisation from "../common/Authorisation";


class RemoveUser extends React.Component<Props, State> {
    constructor(props: any) {
        super(props);
        this.state = this.getInitState(props.propsToken);
    }

    getInitState(token: string | null) {
        return {
            stateToken: token,
            loading: false,
            userLogin: "",
            messages: null,
            errors: null
        }
    }

    abortController = new AbortController();
    componentWillUnmount() {
        this.abortController.abort();
    }

    fieldChangeHandler = (event: any) => {
        this.setState({
            userLogin: event.target.value
        });
    }

    loadUserInfo = async () => {
        const contextRoot = location.origin + location.pathname;
        const requestURL = `${contextRoot}rest/delete/user/${this.state.userLogin}`;
        this.setState({ loading: true, errors: null, messages: null });
        const headers = new Headers();
        headers.append('Accept', 'application/json');
        headers.append('Content-Type', 'application/json');
        headers.append('Authorization', `Basic ${this.state.stateToken}`);
        await fetch(requestURL, {
            signal: this.abortController.signal,
            method: 'DELETE',
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
                    this.setState({ userLogin: "" });
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

    render() {
        let container: JSX.Element;
        const token = this.state.stateToken;
        if (token == "" || token == null || token == undefined) {
            container = <Authorisation />
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
            } else {
                container = <>
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
    loading: boolean,
    userLogin: string,
    messages: Array<JSX.Element> | null,
    errors: Array<JSX.Element> | null
}
export default connect(mapStateToProps)(RemoveUser);