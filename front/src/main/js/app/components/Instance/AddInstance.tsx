import React = require("react");
import { Form, FormGroup, Label, Input, Container, Col, Row } from "reactstrap";

export default class AddInstance extends React.Component {
    async sendDataToServer() {
        const requestURL = "http://localhost";
        console.debug("Sending request");
    }
    render() {
        return (
            <Container>
                <Form>
                    <Row form>
                        <Col md={4}>
                            <FormGroup>
                                <Label for="instanceId"><b>INSTANCE ID: </b></Label>
                                <Input type="text" name="instanceIdentifier" id="instanceId" placeholder="Input instance id..." />
                            </FormGroup>
                        </Col>
                        <Col md={4}>
                            <FormGroup>
                                <Label for="instanceHost"><b>HOST: </b></Label>
                                <Input type="text" name="instanceHostIdentifier" id="instanceHost" placeholder="Input hostname or ip-address..." />
                            </FormGroup>
                        </Col>
                        <Col md={4}>
                            <FormGroup>
                                <Label for="instancePort"><b>PORT: </b></Label>
                                <Input type="number" name="instancePortIdentifier" id="instancePort" placeholder="Input instance port..." />
                            </FormGroup>
                        </Col>
                    </Row>

                    <Row form>
                        <Col md={6}>
                            <FormGroup>
                                <Label for="instanceDataBase"><b>DATABASE NAME (SID): </b></Label>
                                <Input type="text" name="dtabaseNameIdentifier" id="dbName" placeholder="Input database name or SID..." />
                            </FormGroup>
                        </Col>
                        <Col md={6}>
                            <FormGroup>
                                <Label for="instanceTypeSelector"><b>INSTANCE TYPE: </b></Label>
                                <Input type="select" name="instanceSelectorIdentifier" id="instanceType">
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
                                <Input type="text" name="dbUserNameIdentifier" id="dbUserName" placeholder="Input database user name..." />
                            </FormGroup>
                        </Col>
                        <Col md={6}>
                            <FormGroup>
                                <Label for="dbUserPassword"><b>DATABASE USER PASSWORD: </b></Label>
                                <Input type="password" name="dbUserPasswordIdentifier" id="dbUserPass" placeholder="Input database user password..." />
                            </FormGroup>
                        </Col>
                    </Row>

                </Form>
            </Container>
        );
    }
}