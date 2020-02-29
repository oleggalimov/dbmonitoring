import React = require("react");
import { Container, Label, Spinner } from "reactstrap";
import * as Uuidv4 from 'uuid/v4';
import { PointParser } from "../../Interfaces/PointsParser";
import ErrorComponentFactory from "../../utils/ErrorComponentFactory";
import MessageComponentFactory from "../../utils/MessageComponentFactory";
import PointParserFactory from "../../utils/points/PointParserFactory";
import OracleCharts from "../charts/oracle/OracleCharts";
import Authorisation from "../common/Authorisation";
import LoadingErrorMessage from "../common/LoadingErrorMessage";
import MsgCard from "../common/MsgCard";

export default class OracleMetrics extends React.Component<Props, State> {
    constructor(props: any) {
        super(props);
        this.state = this.getDefaultState(props.propsToken, props.propsInstanceId, props.propsTimePeriod);
    }

    getDefaultState = (stateToken: string | null, stateInstanceId: string | null, stateTimePeriod: string | null) => {
        const result = {
            stateToken: stateToken,
            instanceID: stateInstanceId,
            timePeriod: stateTimePeriod,
            systemMetrics_absolute: null,
            systemMetrics_relative: null,
            tableSpaceMetrics: null,
            waitEventsMetrics: null,
            waitClassMetrics: null,
            systemMetrics_absolute_loading_error: false,
            systemMetrics_relative_loading_error: false,
            tableSpaceMetrics_loading_error: false,
            waitEventsMetrics_loading_error: false,
            waitClassMetrics_loading_error: false,
            messages: null,
            errors: null
        }
        return result;
    }

    componentDidMount() {
        this.loadMetrics("ABSOLUTE_SYSTEM_CUSTOM");
        this.loadMetrics("RELATIVE_SYSTEM");
        this.loadMetrics("TABLESPACE");
        this.loadMetrics("WAIT_CLASS");
        this.loadMetrics("WAIT_EVENT");
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
        const requestURL = `${contextRoot}rest/points`;
        const body = {
            instanceId: this.state.instanceID,
            databaseInstanceType: "ORACLE",
            measurement: metricsType,
            timePeriod: this.state.timePeriod
        }
        const headers = new Headers();
        headers.append('Accept', 'application/json');
        headers.append('Content-Type', 'application/json');
        headers.append('Authorization', `Basic ${this.state.stateToken}`);
        await fetch(
            requestURL,
            {
                signal: this.abortController.signal,
                method: 'POST',
                headers: headers,
                body: JSON.stringify(body)
            }
        )
            .then((response) => {
                if (response.status == 403 || response.status == 401) {
                    this.setPointsOrError(null, metricsType, <MsgCard title={`Loading ${metricsType} metrics warning`} cardType={"warning"} message={"You have no permission for this operation. Please sign in or contact your administrator!"} key={Uuidv4()} />);
                    return null;
                } else if (response.status == 200) {
                    return response.json();
                } else {
                    throw Error(`Response status: ${response.status}`);
                }
            })
            .then((json) => {
                if (json == null) {
                    console.debug(`Null json body was recieved from ${requestURL} with request body: ${JSON.stringify(body)}`);
                    return null;
                }
                const successFlag = json.success as boolean;
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
                    const error = series.error;
                    if (error != undefined) {
                        this.setPointsOrError(null,metricsType,<MsgCard cardType="warning" title="Request metrics error" message={error} key={Uuidv4()} />);
                        return;
                    }
                    if (series.series == null || series.series == undefined) {
                        console.debug(`No data in series:  ${JSON.stringify(series)}.`);
                        this.setPointsOrError(null,metricsType,<MsgCard title={`Loading ${metricsType} metrics warning`} cardType={"warning"} message={"No data series in responce!"} key={Uuidv4()} />);
                        return;
                    }
                    
                    const parser: PointParser | null = PointParserFactory.getParser(series, "ORACLE");
                    if (parser == null) {
                        console.debug(`Can't get parser for metrics type: ${metricsType}!.`);
                        return null;
                    }
                    const parsedPoints: Array<any> | null = parser.parse();
                    this.setPointsOrError(parsedPoints, metricsType, null);

                } else {
                    const errorsList = ErrorComponentFactory(json);
                    this.setPointsOrErrorsList(null, metricsType, errorsList);
                }
            })
            .catch((error) => {
                console.debug(`Exception on request to ${requestURL}: ${error}`);
                if (error.name == 'AbortError') {
                    return null;
                };
                this.setPointsOrError(null, metricsType, <LoadingErrorMessage additionalMessage={`Can't get : ${body.measurement}`} key={Uuidv4()} />);
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

    setPointsOrError = (points: Array<any> | null, metricsType: string | null, error: JSX.Element | null) => {
        const arrayArg = error == null ? null : [error];
        this.setPointsOrErrorsList(points, metricsType, arrayArg)
    }

    setPointsOrErrorsList = (points: Array<any> | null, metricsType: string | null, errorsList: Array<JSX.Element> | null) => {
        switch (metricsType) {
            case "ABSOLUTE_SYSTEM_CUSTOM": {
                console.debug(`Setting metrics: ${metricsType}.`);
                points != null ?
                    this.setState({ systemMetrics_absolute: points }) :
                    this.setState({
                        systemMetrics_absolute_loading_error: true,
                        systemMetrics_absolute: errorsList
                    })
                break;
            }
            case "RELATIVE_SYSTEM": {
                console.debug(`Setting metrics: ${metricsType}.`);
                points != null ?
                    this.setState({ systemMetrics_relative: points }) :
                    this.setState({
                        systemMetrics_relative_loading_error: true,
                        systemMetrics_relative: errorsList
                    })
                break;
            }
            case "TABLESPACE": {
                console.debug(`Setting metrics: ${metricsType}.`);
                points != null ?
                    this.setState({ tableSpaceMetrics: points }) :
                    this.setState({
                        tableSpaceMetrics_loading_error: true,
                        tableSpaceMetrics: errorsList
                    })
                break;
            }
            case "WAIT_CLASS": {
                console.debug(`Setting metrics: ${metricsType}.`);
                points != null ?
                    this.setState({ waitClassMetrics: points }) :
                    this.setState({
                        waitClassMetrics_loading_error: true,
                        waitClassMetrics: errorsList
                    })
                break;
            }
            case "WAIT_EVENT": {
                console.debug(`Setting metrics: ${metricsType}.`);
                points != null ?
                    this.setState({ waitEventsMetrics: points }) :
                    this.setState({
                        waitEventsMetrics_loading_error: true,
                        waitEventsMetrics: errorsList
                    })
                break;
            }
            default: return;
        }
    }

    abortController = new AbortController();

    componentWillUnmount() {
        this.abortController.abort();
    }


    render() {
        const token = this.state.stateToken;
        if (token == "" || token == null || token == undefined) {
            return <Authorisation />
        } else if (this.state.instanceID == null || this.state.timePeriod == null) {
            return (
                <div>Instance id or time period is undefined!</div>
            );
        }
        else {
            return <>
                <Container>
                    <Label><b>Absolute system metrics</b></Label>
                    {this.state.systemMetrics_absolute == null ?
                        <div className="d-flex justify-content-center">
                            <Spinner type="grow" color="primary" style={{ width: '8rem', height: '8rem' }} />
                        </div>
                        : ""
                    }
                    {this.state.systemMetrics_absolute != null && this.state.systemMetrics_absolute_loading_error == false ?
                        <OracleCharts points={this.state.systemMetrics_absolute} /> :
                        this.state.systemMetrics_absolute
                    }
                </Container>
                <br />
                <Container>
                    <Label><b>Relative system metrics</b></Label>
                    {this.state.systemMetrics_relative == null ?
                        <div className="d-flex justify-content-center">
                            <Spinner type="grow" color="primary" style={{ width: '8rem', height: '8rem' }} />
                        </div>
                        : ""
                    }
                    {this.state.systemMetrics_relative != null && this.state.systemMetrics_relative_loading_error == false ?
                        <OracleCharts points={this.state.systemMetrics_relative} /> :
                        this.state.systemMetrics_relative
                    }
                </Container>
                <br />
                <Container>
                    <Label><b>Tablespace</b></Label>
                    {this.state.tableSpaceMetrics == null ?
                        <div className="d-flex justify-content-center">
                            <Spinner type="grow" color="primary" style={{ width: '8rem', height: '8rem' }} />
                        </div>
                        : ""
                    }
                    {this.state.tableSpaceMetrics != null && this.state.tableSpaceMetrics_loading_error == false ?
                        <OracleCharts points={this.state.tableSpaceMetrics} /> :
                        this.state.tableSpaceMetrics
                    }
                </Container>
                <br />
                <Container>
                    <Label><b>Wait classes</b></Label>
                    {this.state.waitClassMetrics == null ?
                        <div className="d-flex justify-content-center">
                            <Spinner type="grow" color="primary" style={{ width: '8rem', height: '8rem' }} />
                        </div>
                        : ""
                    }
                    {this.state.waitClassMetrics != null && this.state.waitClassMetrics_loading_error == false ?
                        <OracleCharts points={this.state.waitClassMetrics} /> :
                        this.state.waitClassMetrics
                    }
                </Container>
                <br />
                <Container>
                    <Label><b>Wait events</b></Label>
                    {this.state.waitEventsMetrics == null ?
                        <div className="d-flex justify-content-center">
                            <Spinner type="grow" color="primary" style={{ width: '8rem', height: '8rem' }} />
                        </div>
                        : ""
                    }
                    {this.state.waitEventsMetrics != null && this.state.waitEventsMetrics_loading_error == false ?
                        <OracleCharts points={this.state.waitEventsMetrics} /> :
                        this.state.waitEventsMetrics
                    }
                </Container>
                <br />
                <Container>
                    {this.state.messages == null ? "" : this.state.messages.map((msg) => msg)}
                    {this.state.errors == null ? "" : this.state.errors.map((msg) => msg)}
                </Container>
                <br />
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
    systemMetrics_absolute: Array<any> | null,
    systemMetrics_relative: Array<any> | null,
    tableSpaceMetrics: Array<any> | null,
    waitEventsMetrics: Array<any> | null,
    waitClassMetrics: Array<any> | null,
    systemMetrics_absolute_loading_error: boolean,
    systemMetrics_relative_loading_error: boolean,
    tableSpaceMetrics_loading_error: boolean,
    waitEventsMetrics_loading_error: boolean,
    waitClassMetrics_loading_error: boolean,
    messages: Array<JSX.Element> | null,
    errors: Array<JSX.Element> | null
}