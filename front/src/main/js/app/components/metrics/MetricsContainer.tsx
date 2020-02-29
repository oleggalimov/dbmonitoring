import React = require("react");
import { Button, Container, Form, FormGroup, Input, Label } from "reactstrap";
import InstanceType from "../../enumerations/InstanceType";
import TimePeriod from "../../enumerations/TimePeriod";
import Authorisation from "../common/Authorisation";
import OracleMetrics from "./OracleMetrics";
import { connect } from "react-redux";

class MetricsContainer extends React.Component<Props, State> {
    constructor(props: any) {
        super(props);
        this.state = this.getDefaultState(props.propsToken);
    }

    getDefaultState = (stateToken: string | null) => {
        return {
            stateToken: stateToken,
            loading: false,
            instanceID: "",
            instanceType: "",
            timePeriod: "",
            metricsComponent: null
        }

    }
    clearState = () => {
        this.setState(
            this.getDefaultState(this.state.stateToken)
        );
    }

    fieldChangeHandler = (event: any) => {
        const targetId = event.target.id;
        const targetvalue = event.target.value;
        if (targetId == null || targetvalue == null) {
            return;
        }
        if (this.state.metricsComponent) {
            this.setState({
                metricsComponent:null,
                loading:false
            });
        }
        
        switch (targetId) {
            case "instanceId": {
                this.setState({
                    instanceID: targetvalue
                });
                break;
            }
            case "instanceType": {
                this.setState({
                    instanceType: targetvalue
                });
                break;
            }
            case "timePeriod": {
                this.setState({
                    timePeriod: targetvalue
                });
                break;
            }
            default: {
                console.debug(`Can't get id for ${targetId}, value: ${targetvalue}`);
            }
        }
    }

    loadData = () => {
        const period = TimePeriod[this.state.timePeriod];
        const type = InstanceType[this.state.instanceType];
        if (period == undefined || type == undefined) {
            console.debug(`Time period (${period}) or instance type (${type}) is undefined.`);
            return;
        }
        this.setState({ loading: true });
        console.debug(`Mounting component for type: ${type} with instance id: ${this.state.instanceID},  and period: ${period}`);
        switch (type) {
            case "ORACLE": {
                this.setState({
                    metricsComponent: <OracleMetrics propsToken={this.state.stateToken} propsInstanceId={this.state.instanceID} propsTimePeriod={period}/>
                });
                break;
            }
            case "POSTGRES" || "MSSQL" || "MYSQL": {
                ;
            }
            default: {
                console.debug(`View for instance type ${type} is undefined`);
                this.setState({ loading: false });
            }
        }

    }


    render() {
        const token = this.state.stateToken;
        let component;
        if (token == "" ||  token == null || token == undefined) {
            component = <Authorisation />
        } else {
            const loadButtonDisabled = (
                this.state.instanceID == "" || this.state.instanceType == ""
                || this.state.timePeriod == "" || this.state.loading
            );
            component = <>
                <Container>
                    <Form>
                        <FormGroup inline>
                            <Label><b>INSTANCE ID: </b>
                                <Input onChange={this.fieldChangeHandler} type="text" id="instanceId" placeholder="Input instance id..." value={this.state.instanceID} />
                            </Label>{' '}
                            <Label ><b>INSTANCE TYPE:</b>
                                <Input onChange={this.fieldChangeHandler} type="select" id="instanceType" value={this.state.instanceType as string} >
                                    <option>Select type...</option>
                                    <option>Oracle</option>
                                    <option>Postgres</option>
                                    <option>MySQL</option>
                                    <option>MS SQL</option>
                                </Input>
                            </Label> {' '}
                            <Label for="timePeriod"><b>TIME PERIOD</b>
                                <Input onChange={this.fieldChangeHandler} type="select" id="timePeriod" value={this.state.timePeriod}>
                                    <option>Select period...</option>
                                    <option>Last 5 minutes</option>
                                    <option>Last 10 minutes</option>
                                    <option>Last 15 minutes</option>
                                    <option>Last 30 minutes</option>
                                    <option>Last 1 hour</option>
                                </Input>
                            </Label>{' '}
                            {loadButtonDisabled ?
                                <Button color="success" disabled>LOAD INFO</Button> :
                                <Button color="success" onClick={this.loadData}>LOAD INFO</Button>
                            }{' '}
                            <Button color="warning" onClick={this.clearState}>
                                CLEAR
                            </Button>
                        </FormGroup>
                    </Form>
                </Container>
                <Container>
                    {this.state.metricsComponent}
                </Container>
            </>;
        }
        return (component);
    }
}

const mapStateToProps = (state: any) => {
    return {
        propsToken: state.token
    }
};

interface Props  {
    propsToken: string
}
interface State {
    stateToken: string | null,
    loading: boolean,
    instanceID: string,
    instanceType: string,
    timePeriod: string,
    metricsComponent: JSX.Element | null
}
export default connect(mapStateToProps)(MetricsContainer);