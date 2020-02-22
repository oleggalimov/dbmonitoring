import React = require("react");
import { LineChart, Line, CartesianGrid, XAxis, YAxis, Tooltip } from "recharts";
import { connect } from "react-redux";
import MessageComponentFactory from "../../../utils/MessageComponentFactory";
import ErrorComponentFactory from "../../../utils/ErrorComponentFactory";
import ForbiddenMeesage from "../../common/ForbiddenMeesage";
import LoadingErrorMessage from "../../common/LoadingErrorMessage";
import Authorisation from "../../common/Authorisation";
import { Container, Form, FormGroup, Label, Input, Col, Row, Button } from "reactstrap";
import PointParserFactory from "../../../utils/points/PointParserFactory";
import { InstanceTypes } from "../../../enumerations/InstanceTypes";
import { PointParser } from "../../../Interfaces/PointsParser";
import uuid = require("uuid");

class SystemMetrics extends React.Component<Props, State> {
    constructor(props: any) {
        super(props);
        this.state = this.getDefaultState(props.propsToken);
    }

    getDefaultState = (stateToken: string | null) => {
        return {
            stateToken: stateToken,
            loading: false,
            instanceID: "",
            instanseType: "ORACLE",
            messages: null,
            errors: null,
            points:null
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

    loadInstanceInfo = async () => {
        if (this.state.instanceID == "") {
            return;
        }
        const contextRoot = location.origin + location.pathname;
        // const requestURL = 'http://127.0.0.1:8887/statusList.json';
        // const requestURL = `${contextRoot}rest/create/user`;
        const requestURL = `http://127.0.0.1:8887/TableSpace.json`;
        this.setState({ loading: true, errors: null, messages: null });
        const headers = new Headers();
        // headers.append('Accept', 'application/json');
        // headers.append('Content-Type', 'application/json');
        // headers.append('Authorization', `Basic ${this.state.stateToken}`);
        await fetch(
            requestURL,
            {
                signal: this.abortController.signal,
                method: 'GET',
                headers: headers,
                // body: JSON.stringify({})
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
                    console.debug("Null json body was recieved from ${requestURL}");
                    return;
                }
                
                const successFlag = json.success as boolean;
                if (successFlag) {
                    const points = json.body.POINTS[0];
                    if (points==undefined ) {
                        //TODO: нет данных
                        return;
                    }
                    const series = points.results[0];
                    if (series == null || series == undefined) {
                        console.debug(`No data in series:  ${series}.`);
                        return;
                    }
                    const parser: PointParser | null =  PointParserFactory.getParser(series, InstanceTypes.ORACLE);
                    if (parser==null) {
                        return;
                    }
                    const parsedPoints = parser.parse();
                    this.setState({
                        points:parser.parse()
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

    mapPointsToJSX(points: Array<any>|null): Array<JSX.Element>|null{
        if (points==null) {
            return null;
        }
        const row:object = points[0];
        console.log(`type is : ${typeof(row)}`);
        const result = new Array<JSX.Element>();
        for (const field in row) {
            if (field!="name"){
                result.push(<Line key={uuid.v4()}type="monotone" dataKey={field} stroke={this.getRandomColor()} />);
            }
        }
        return result;
    }

    getRandomColor() {
        return `#${(Math.round(Math.random() * 0XFFFFFF)).toString(16)}`;
    }

    render() {
        // const token = this.state.stateToken;
        const token = 1;
        let charts;
        if (/*token == "" ||*/ token == null || token == undefined) {
            charts = <Authorisation />
        } else {
            const instanceID = this.state.instanceID;
            const points = this.state.points;
            if (points==null) {
                console.log(JSON.stringify(this.state.points));
                charts=  <>
                    <Container>
                        <Form>
                            <Row form>
                                <Col>
                                    <FormGroup>
                                        <Label for="instanceId"><b>INSTANCE ID: </b></Label>
                                        <Input onChange={this.fieldChangeHandler} type="text" name="instanceIdentifier" id="instanceId" placeholder="Input instance id..." />
                                    </FormGroup>
                                </Col>
                            </Row>
                            {this.state.instanceID == "" ?
                                <Row>
                                    <Col className="row justify-content-end" >
                                        <Button onClick={this.loadInstanceInfo} color="success" disabled>LOAD INFO</Button>&nbsp;
                            </Col>
                                </Row>
                                :
                                <Row>
                                    <Col className="row justify-content-end" >
                                        <Button onClick={this.loadInstanceInfo} color="success">LOAD INFO</Button>&nbsp;
                            </Col>
                                </Row>
                            }
                        </Form>
                    </Container>
                    <br/>
                    <Container>
                        {this.state.messages}
                        {this.state.errors}
                    </Container>
                </>
            } else {
                    charts = <>
                    <Container>
                        <LineChart width={600} height={300} data={points} margin={{ top: 5, right: 20, bottom: 5, left: 0 }}>
                            {this.mapPointsToJSX(points)}
                            <CartesianGrid stroke="#ccc" strokeDasharray="5 5" />
                            <XAxis dataKey="name" />
                            <YAxis />
                            <Tooltip />
                        </LineChart>
                    </Container>
                    <br/>
                        <Container>
                            {this.state.messages}
                            {this.state.errors}
                        </Container>
                    </>        
            }

        }
        return (charts);
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
    points: Array<any> | null,
    messages: Array<JSX.Element> | null,
    errors: Array<JSX.Element> | null
}
export default connect(mapStateToProps)(SystemMetrics);