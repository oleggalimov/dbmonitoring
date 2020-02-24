import React = require("react");
import { Container, Label } from "reactstrap";
import { CartesianGrid, LineChart, Tooltip, XAxis, YAxis } from "recharts";
import * as Uuidv4 from 'uuid/v4';
import { PointParser } from "../../Interfaces/PointsParser";
import ErrorComponentFactory from "../../utils/ErrorComponentFactory";
import MessageComponentFactory from "../../utils/MessageComponentFactory";
import PointParserFactory from "../../utils/points/PointParserFactory";
import OracleSystemMetrics from "../charts/oracle/OracleSystemMetrics";
import Authorisation from "../common/Authorisation";
import LoadingErrorMessage from "../common/LoadingErrorMessage";
import MsgCard from "../common/MsgCard";

export default class OracleMetrics extends React.Component<Props, State> {
    constructor(props: any) {
        super(props);
        this.state = this.getDefaultState(props.propsToken, props.propsInstanceId, props.propsTimePeriod);
    }

    getDefaultState = (stateToken: string | null, stateInstanceId: string | null, stateTimePeriod: string | null) => {
        return {
            stateToken: stateToken,
            instanceID: stateInstanceId,
            timePeriod: stateTimePeriod,
            systemMetrics: null,
            tableSpaceMetrics: null,
            waitEventsMetrics: null,
            waitClassMetrics: null,
            messages: null,
            errors: null
        }

    }

    componentDidMount() {
        this.loadMetrics("SYSTEM");
        // this.loadMetrics("TABLESPACE");
        // this.loadMetrics("WAIT_CLASS");
        // this.loadMetrics("WAIT_EVENT");
    }

    loadMetrics = async (metricsType: string) => {
        if (this.state.instanceID == "") {
            const newErrors = this.addJSXToArray(
                this.state.errors,
                <MsgCard title={`Error on loading ${metricsType} metrics`} cardType={"danger"} message={"Instance ID is undefined!"} key={Uuidv4()} />
            );
            this.setState({ errors: newErrors });
            return null;
        }
        const contextRoot = location.origin + location.pathname;
        // const requestURL = 'http://127.0.0.1:8887/statusList.json';
        // const requestURL = `${contextRoot}rest/create/user`;
        let requestURL: string;

        const headers = new Headers();
        // headers.append('Accept', 'application/json');
        // headers.append('Content-Type', 'application/json');
        // headers.append('Authorization', `Basic ${this.state.stateToken}`);
        switch (metricsType) {
            case "SYSTEM": requestURL = `http://127.0.0.1:8887/System.json`;
                break;
            case "TABLESPACE": requestURL = `http://127.0.0.1:8887/TableSpace.json`;
                break;
            case "WAIT_CLASS": requestURL = `http://127.0.0.1:8887/WaitClass.json`;
                break;
            case "WAIT_EVENT": requestURL = `http://127.0.0.1:8887/WaitEvent.json`;
                break;
            default: requestURL = `http://127.0.0.1:8887/`;
        }
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
                    const newMessages = this.addJSXToArray(
                        this.state.messages,
                        <MsgCard title={`Loading ${metricsType} metrics warning`} cardType={"warning"} message={"You have no permission for this operation. Please sign in or contact your administrator!"} key={Uuidv4()} />
                    );
                    this.setState({ messages: newMessages });
                    return null;
                } else if (response.status == 200) {
                    return response.json();
                } else {
                    throw Error(`Response status: ${response.status}`);
                }
            })
            .then((json) => {
                if (json == null) {
                    console.debug(`Null json body was recieved from ${requestURL}`);
                    return null;
                }
                const successFlag = json.success as boolean;
                const messageList = MessageComponentFactory(json);
                const errorsList = ErrorComponentFactory(json);
                this.setState({
                    errors: this.addJSXElementsToArray(this.state.errors, errorsList),
                    messages: this.addJSXElementsToArray(this.state.messages, messageList),
                });
                if (successFlag) {
                    const points = json.body.POINTS[0];
                    if (points == undefined) {
                        const newMessages = this.addJSXToArray(
                            this.state.messages,
                            <MsgCard title={`Loading ${metricsType} metrics warning`} cardType={"warning"} message={"No data points in responce!"} key={Uuidv4()} />
                        );
                        this.setState({ messages: newMessages });
                        return null;
                    }
                    const series = points.results[0];
                    if (series == null || series == undefined) {
                        console.debug(`No data in series:  ${series}.`);
                        const newMessages = this.addJSXToArray(
                            this.state.messages,
                            <MsgCard title={`Loading ${metricsType} metrics warning`} cardType={"warning"} message={"No data series in responce!"} key={Uuidv4()} />
                        );
                        this.setState({ messages: newMessages });
                        return null;
                    }
                    const parser: PointParser | null = PointParserFactory.getParser(series, "ORACLE");
                    if (parser == null) {
                        console.debug(`Can't get parser for metrics type: ${metricsType}!.`);
                        return null;
                    }
                    const parsedPoints: Array<any> | null = parser.parse();
                    if (parsedPoints != null) {
                        switch (metricsType) {
                            case "SYSTEM": {
                                console.debug(`Setting metrics: ${metricsType}.`);
                                this.setState({ systemMetrics: parsedPoints });
                                break;
                            }
                            case "TABLESPACE": {
                                console.debug(`Setting metrics: ${metricsType}.`);
                                this.setState({ tableSpaceMetrics: parsedPoints });
                                break;
                            }
                            case "WAIT_CLASS": {
                                console.debug(`Setting metrics: ${metricsType}.`);
                                this.setState({ waitClassMetrics: parsedPoints });
                                break;
                            }
                            case "WAIT_EVENT": {
                                console.debug(`Setting metrics: ${metricsType}.`);
                                this.setState({ waitEventsMetrics: parsedPoints });
                                break;
                            }
                        }
                    }
                }
            })
            .catch((error) => {
                console.debug(`Exception on request to ${requestURL}: ${error}`);
                if (error.name == 'AbortError') {
                    return null;
                };
                const newMessages = this.addJSXToArray(
                    this.state.messages,
                    <LoadingErrorMessage key={'errorMessageBox'} />
                );
                this.setState({ messages: newMessages });
            });
    }

    addJSXToArray(stateArray: Array<JSX.Element> | null, element: JSX.Element) {
        let result = stateArray == null ? new Array<JSX.Element>() : stateArray;
        result.push(element);
        return result;
    };

    addJSXElementsToArray(stateArray: Array<JSX.Element> | null, elements: Array<JSX.Element> | null) {
        if (elements == null || elements.length == 0) {
            return stateArray;
        }
        let result = stateArray == null ? new Array<JSX.Element>() : stateArray;
        elements.forEach(element => result.push(element));
        return result;
    };

    abortController = new AbortController();

    componentWillUnmount() {
        this.abortController.abort();
    }


    render() {
        // const token = this.state.stateToken;
        const token = 1;
        let charts;
        if (/*token == "" ||*/ token == null || token == undefined) {
            charts = <Authorisation />
        } else if (this.state.instanceID == null || this.state.timePeriod == null) {
            return (
                <div>Instance id or time period is undefined!</div>
            );
        }

        else {
            return <>
                <Container>
                    {this.state.systemMetrics == null ?
                        "No system metrics data..." :
                        <OracleSystemMetrics points = {this.state.systemMetrics} />
                    }
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

interface Props {
    propsToken: string | null,
    propsInstanceId: string | null,
    propsTimePeriod: string | null
}
interface State {
    stateToken: string | null,
    instanceID: string | null,
    timePeriod: string | null,
    systemMetrics: Array<any> | null,
    tableSpaceMetrics: Array<any> | null,
    waitEventsMetrics: Array<any> | null,
    waitClassMetrics: Array<any> | null,
    messages: Array<JSX.Element> | null,
    errors: Array<JSX.Element> | null
}