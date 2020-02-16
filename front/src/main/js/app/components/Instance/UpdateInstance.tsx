import React = require("react");
import { connect } from "react-redux";
import { Button, Col, Container, Form, FormGroup, Input, Label, Row, Spinner } from "reactstrap";
import { Instance } from "../../Interfaces/Instance";
import ErrorComponentFactory from "../../utils/ErrorComponentFactory";
import InstanceInfoValidator from "../../utils/InstanceInfoValidator";
import MessageComponentFactory from "../../utils/MessageComponentFactory";
import ForbiddenMeesage from "../common/ForbiddenMeesage";
import LoadingErrorMessage from "../common/LoadingErrorMessage";
import Authorisation from "../common/Authorisation";

class UpdateInstance extends React.Component<Props, State> {
    constructor(props: any) {
        super(props);
        this.state = this.getInitState(props.propsToken);

    }

    getInitState(token: string | null) {
        return {
            stateToken: token,
            loading: false,
            instanceID: "",
            messages: null,
            errors: null,
            instanceLoaded: false,

            id: "",
            host: "",
            port: 0,
            database: "",
            type: "",
            login: "",
            password: ""
        }
    }
    abortController = new AbortController();
    componentWillUnmount() {
        this.abortController.abort();
    }

    fieldChangeHandler = (event: any) => {
        this.setState({
            instanceID: event.target.value
        });
    }
    clearButtonHandler = () => {
        this.setState({
            host: "",
            port: 0,
            database: "",
            type: "",
            login: "",
            password: ""
        });
    }
    instanceFormChangeHandler = (event: any) => {
        const fieldId = event.target.id;
        const fieldValue = event.target.value;
        switch (fieldId) {
            case "instanceHost": this.setState({ host: fieldValue });
                break;
            case "instancePort": this.setState({ port: fieldValue });
                break;
            case "dbName": this.setState({ database: fieldValue });
                break;
            case "instanceType": this.setState({ type: fieldValue });
                break;
            case "dbUserName": this.setState({
                login: fieldValue,
            });
                break;
            case "dbUserPass": this.setState({
                password: fieldValue
            });
                break;
            default: console.debug("Field id is undefined!")
        }
    }

    loadInstanceInfo = async () => {
        const contextRoot = location.origin + location.pathname;
        // const requestURL = 'http://127.0.0.1:8887/statusList.json';
        // const requestURL = `${contextRoot}rest/create/user`;
        const requestURL = `http://127.0.0.1:8080/database_monitoring/rest/list/instance/${this.state.instanceID}`;
        this.setState({ loading: true, errors: null, messages: null });
        const headers = new Headers();
        headers.append('Accept', 'application/json');
        headers.append('Content-Type', 'application/json');
        headers.append('Authorization', `Basic ${this.state.stateToken}`);
        await fetch(
            requestURL,
            {
                signal: this.abortController.signal,
                method: 'GET',
                headers: headers,
            }
        )
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
                    const instance: Instance = json['body']["INSTANCES"][0];
                    let { id, host, port, database, type, user } = instance;
                    this.setState({
                        loading: false, instanceLoaded: true,
                        id: id, host: host, port: port, database: database, type: type, login: user.login
                    });
                } else {
                    const messageList = MessageComponentFactory(json);
                    const errorsList = ErrorComponentFactory(json);
                    this.setState({ messages: messageList, errors: errorsList, loading: false, instanceID: "" });
                }
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
        const validationMessages: Array<JSX.Element> = InstanceInfoValidator(this.state);
        if (validationMessages.length > 0) {
            this.setState({
                messages: validationMessages
            });
            return;
        }

        const contextRoot = location.origin + location.pathname;
        // const requestURL = 'http://127.0.0.1:8887/statusList.json';
        // const requestURL = `${contextRoot}rest/create/user`;
        const requestURL = `http://127.0.0.1:8080/database_monitoring/rest/update/instance`;
        const { id, host, port, database, type, login, password } = this.state;
        const postBody = {
            id, host, port, database, type, user: { login: login, password: password }
        }
        this.setState({ loading: true, errors: null, messages: null });
        const headers = new Headers();
        headers.append('Accept', 'application/json');
        headers.append('Content-Type', 'application/json');
        headers.append('Authorization', `Basic ${this.state.stateToken}`);
        await fetch(requestURL, {
            method: 'PUT',
            headers: headers ,
            body: JSON.stringify(postBody)
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
                    loading: false, messages: messageList, errors: errorsList, instanceLoaded: false
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
        const token = this.state.stateToken;
        if (token == "" || token == null || token == undefined) {
            return <Authorisation />
        } else {
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
            } else if (this.state.instanceLoaded == false) {
                return <>
                    <Container>
                        <Form>
                            <Row form>
                                <Col>
                                    <FormGroup>
                                        <Label for="instanceId"><b>INSTANCE ID FOR UPDATE: </b></Label>
                                        <Input onChange={this.fieldChangeHandler} type="text" name="instanceIdentifier" id="instanceId" placeholder="Input instance id..." />
                                    </FormGroup>
                                </Col>
                            </Row>
                            {this.state.instanceID == "" ?
                                <Row>
                                    <Col className="row justify-content-end" >
                                        <Button onClick={this.loadInstanceInfo} color="success" disabled>Load data</Button>&nbsp;
                            </Col>
                                </Row>
                                :
                                <Row>
                                    <Col className="row justify-content-end" >
                                        <Button onClick={this.loadInstanceInfo} color="success">Load data</Button>&nbsp;
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
                return <>
                    <Container>
                        <Form>
                            <Row form>
                                <Col md={4}>
                                    <FormGroup>
                                        <Label for="instanceId"><b>INSTANCE ID: </b></Label>
                                        <Input type="text" name="instanceIdentifier" id="instanceId"
                                            placeholder="Input instance id..." value={this.state.id} disabled />
                                    </FormGroup>
                                </Col>
                                <Col md={4}>
                                    <FormGroup>
                                        <Label for="instanceHost"><b>HOST: </b></Label>
                                        <Input onChange={this.instanceFormChangeHandler} type="text" name="instanceHostIdentifier" id="instanceHost"
                                            placeholder="Input hostname or ip-address..." value={this.state.host} />
                                    </FormGroup>
                                </Col>
                                <Col md={4}>
                                    <FormGroup>
                                        <Label for="instancePort"><b>PORT: </b></Label>
                                        <Input onChange={this.instanceFormChangeHandler} type="number" name="instancePortIdentifier" id="instancePort"
                                            placeholder="Input instance port..." value={this.state.port} />
                                    </FormGroup>
                                </Col>
                            </Row>
                            <Row form>
                                <Col md={6}>
                                    <FormGroup>
                                        <Label for="instanceDataBase"><b>DATABASE NAME (SID): </b></Label>
                                        <Input onChange={this.instanceFormChangeHandler} type="text" name="dtabaseNameIdentifier" id="dbName"
                                            placeholder="Input database name or SID..." value={this.state.database} />
                                    </FormGroup>
                                </Col>
                                <Col md={6}>
                                    <FormGroup>
                                        <Label for="instanceTypeSelector"><b>INSTANCE TYPE: </b></Label>
                                        <Input onChange={this.instanceFormChangeHandler} type="select" name="instanceSelectorIdentifier"
                                            id="instanceType" value={this.state.type} >
                                            <option>Select type...</option>
                                            <option>ORACLE</option>
                                            <option>POSTGRES</option>
                                            <option>MSSQL</option>
                                            <option>MYSQL</option>
                                        </Input>
                                    </FormGroup>
                                </Col>
                            </Row>
                            <Row form>
                                <Col md={6}>
                                    <FormGroup>
                                        <Label for="dbUserName"><b>DATABASE USER NAME: </b></Label>
                                        <Input onChange={this.instanceFormChangeHandler} type="text" name="dbUserNameIdentifier" id="dbUserName"
                                            placeholder="Input database user name..." value={this.state.login} />
                                    </FormGroup>
                                </Col>
                                <Col md={6}>
                                    <FormGroup>
                                        <Label for="dbUserPassword"><b>DATABASE USER PASSWORD: </b></Label>
                                        <Input onChange={this.instanceFormChangeHandler} autoComplete="on" type="password" name="dbUserPasswordIdentifier"
                                            id="dbUserPass" placeholder="Enter database user password here..." value={this.state.password} />
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
    }
}
const mapStateToProps = (state: any) => ({
    propsToken: state.token,
    propsUserName: state.userName
});



interface Props extends State {
    token: string,
    userName: string
}
interface State {
    stateToken: string | null,
    loading: boolean,
    instanceID: string,
    messages: Array<JSX.Element> | null,
    errors: Array<JSX.Element> | null,
    instanceLoaded: boolean,
    id: string,
    host: string,
    port: number,
    database: string,
    type: string,
    login: string,
    password: string
}
export default connect(mapStateToProps)(UpdateInstance);