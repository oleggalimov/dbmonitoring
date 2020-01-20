import React = require("react");
import { Form, FormGroup, Label, Input, Container, Col, Row, Button, Spinner } from "reactstrap";
import MessageComponentFactory from "../../utils/MessageComponentFactory";
import ErrorComponentFactory from "../../utils/ErrorComponentFactory";
import LoadingErrorMessage from "../common/LoadingErrorMessage";

export default class AddInstance extends React.Component<{}, {
    instanceId: string, host: string, port: number, databaseName: string, type: string, userName: string,
    password: string, sendingData: boolean, gotResult: boolean, messages: Array<JSX.Element> | null,
    errors: Array<JSX.Element> | null
}> {
    constructor(props: any) {
        super(props);
        this.state = this.getInitState();
    }
    getInitState = () => {
        return {
            instanceId: "",
            host: "",
            port: 0,
            databaseName: "",
            type: "",
            userName: "",
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
        if (fieldId != "instancePort" && (fieldValue as String).length < 0) {
            return;
        }
        switch (fieldId) {
            case "instanceId": this.setState({ instanceId: fieldValue });
                break;
            case "instanceHost": this.setState({ host: fieldValue });
                break;
            case "instancePort": this.setState({ port: fieldValue });
                break;
            case "dbName": this.setState({ databaseName: fieldValue });
                break;
            case "instanceType": this.setState({ type: fieldValue });
                break;
            case "dbUserName": this.setState({ userName: fieldValue });
                break;
            case "dbUserPass": this.setState({ password: fieldValue });
                break;
            default: console.debug("Field id is undefined!")
        }
    }
    clearButtonHandler = () => {
        this.setState(this.getInitState());
    }


    sendDataToServer = async () => {
        const requestURL = "http://localhost";
        console.debug("Sending request");
        this.setState({ sendingData: true, gotResult: false, errors: null, messages: null });
        await fetch(requestURL, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(this.state)
        }).then((response) => {
            if (response.status == 200) {
                return response.json();
            } else {
                throw Error(`Response status: ${response.status}`);
            }
        })
            .then((json) => {
                this.setState({ sendingData: false });
                const successFlag = json['success'] as boolean;
                if (successFlag) {
                    alert("Инстанс добавлен");
                } else {
                    const messageList = MessageComponentFactory(json);
                    const errorsList = ErrorComponentFactory(json);
                    this.setState({
                        messages: messageList, errors: errorsList
                    });
                }
            })
            .catch((error) => {
                console.debug(`${error}`);
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
        return (
            <>
                <Container>
                    <Form>
                        <Row form>
                            <Col md={4}>
                                <FormGroup>
                                    <Label for="instanceId"><b>INSTANCE ID: </b></Label>
                                    <Input
                                        onChange={this.fieldChangeHandler} type="text" name="instanceIdentifier" id="instanceId"
                                        placeholder="Input instance id..." value={this.state.instanceId} />
                                </FormGroup>
                            </Col>
                            <Col md={4}>
                                <FormGroup>
                                    <Label for="instanceHost"><b>HOST: </b></Label>
                                    <Input onChange={this.fieldChangeHandler} type="text" name="instanceHostIdentifier" id="instanceHost"
                                        placeholder="Input hostname or ip-address..." value={this.state.host} />
                                </FormGroup>
                            </Col>
                            <Col md={4}>
                                <FormGroup>
                                    <Label for="instancePort"><b>PORT: </b></Label>
                                    <Input onChange={this.fieldChangeHandler} type="number" name="instancePortIdentifier" id="instancePort"
                                        placeholder="Input instance port..." value={this.state.port} />
                                </FormGroup>
                            </Col>
                        </Row>

                        <Row form>
                            <Col md={6}>
                                <FormGroup>
                                    <Label for="instanceDataBase"><b>DATABASE NAME (SID): </b></Label>
                                    <Input onChange={this.fieldChangeHandler} type="text" name="dtabaseNameIdentifier" id="dbName"
                                        placeholder="Input database name or SID..." value={this.state.databaseName} />
                                </FormGroup>
                            </Col>
                            <Col md={6}>
                                <FormGroup>
                                    <Label for="instanceTypeSelector"><b>INSTANCE TYPE: </b></Label>
                                    <Input onChange={this.fieldChangeHandler} type="select" name="instanceSelectorIdentifier"
                                        id="instanceType" value={this.state.type} >
                                        <option>Select type...</option>
                                        <option>ORACLE</option>
                                        <option>POSTGRES</option>
                                        <option>MS SQL</option>
                                        <option>MY SQL</option>
                                    </Input>
                                </FormGroup>
                            </Col>
                        </Row>
                        <Row form>
                            <Col md={6}>
                                <FormGroup>
                                    <Label for="dbUserName"><b>DATABASE USER NAME: </b></Label>
                                    <Input onChange={this.fieldChangeHandler} type="text" name="dbUserNameIdentifier" id="dbUserName"
                                        placeholder="Input database user name..." value={this.state.userName} />
                                </FormGroup>
                            </Col>
                            <Col md={6}>
                                <FormGroup>
                                    <Label for="dbUserPassword"><b>DATABASE USER PASSWORD: </b></Label>
                                    <Input onChange={this.fieldChangeHandler} autoComplete="on" type="password" name="dbUserPasswordIdentifier"
                                        id="dbUserPass" placeholder="Input database user password..." value={this.state.password} />
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
        );
    }
}