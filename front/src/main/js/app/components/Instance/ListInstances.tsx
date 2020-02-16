import React = require("react");
import Switch from '@material-ui/core/Switch';
import { Container, Row, Spinner } from "reactstrap";
import ErrorComponentFactory from "../../utils/ErrorComponentFactory";
import InstanceComponentFactory from "../../utils/InstanceComponentFactory";
import MessageComponentFactory from "../../utils/MessageComponentFactory";
import LoadingErrorMessage from "../common/LoadingErrorMessage";
import { bindActionCreators, Dispatch } from "redux";
import SET_TOKEN_STRING from "../../actions/SetToken";
import SET_USER_NAME from "../../actions/SetUserName";
import { connect } from "react-redux";
import Authorisation from "../common/Authorisation";
import ForbiddenMeesage from "../common/ForbiddenMeesage";


class ListInstance extends React.Component<Props, State> {
    constructor(props: any) {
        super(props);
        this.state = {
            loading: true,
            instances: null,
            messages: null,
            errors: null,
            loadStatuses: false,
            stateToken: props.propsToken,
            stateUserName: props.propsUserName
        }

    }
    controller = new AbortController(); //allows to not update component's state when on stage componentWillUnmount

    componentDidMount() {
        this.loadInstances();
    }
    componentWillUnmount() {
        this.controller.abort();
    }

    handleSwitch = () => {
        const switcher = !this.state.loadStatuses;
        this.setState({
            errors: null,
            messages: null,
            loadStatuses: switcher
        });
        this.loadInstances(switcher);
    }

    async loadInstances(loadStatuses: boolean = false) {
        const token = this.state.stateToken;
        if (token == "" || token == null || token == undefined) {
            return;
        }
        const contextRoot = location.origin + location.pathname;
        let requestURL: string;
        this.setState({ loading: true, errors:null, messages:null,instances:null });
        if (loadStatuses) {
            // requestURL = `${contextRoot}rest/check/instance/all`;
            requestURL = `http://localhost:8080/database_monitoring/rest/check/instance/all`;
        } else {
            // requestURL = `${contextRoot}rest/list/instance/all`;
            requestURL = `http://localhost:8080/database_monitoring/rest/list/instance/all`;
        }
        const headers = new Headers();
        headers.append('Accept', 'application/json');
        headers.append('Content-Type', 'application/json');
        headers.append('Authorization', `Basic ${this.state.stateToken}`);

        await fetch(
            requestURL, { 
            signal: this.controller.signal, 
            method: 'GET', 
            headers: headers 
        })
            .then((response) => {
                if (response.status == 403 || response.status == 401) {
                    this.setState({ loading: false, messages: [<ForbiddenMeesage key={'forbiddenMessageBox'} />] });
                    return null;
                } else
                    if (response.status == 200) {
                        return response.json();
                    } else {
                        throw Error(`Response status: ${response.status}`);
                    }
            })
            .then((json) => {
                if (json == null) {
                    return;
                }
                this.setState({ loading: false });
                const successFlag = json['success'] as boolean;
                if (successFlag) {
                    const instanceList = InstanceComponentFactory(json['body'], this.state.loadStatuses);
                    this.setState({
                        instances: instanceList
                    });
                } else {
                    const messageList = MessageComponentFactory(json);
                    const errorsList = ErrorComponentFactory(json);
                    this.setState({
                        messages: messageList, errors: errorsList
                    });
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
    render() {
        const token = this.state.stateToken;
        if (token == "" || token == null || token == undefined) {
            return <Authorisation />
        } else {
            return (
                this.state.loading ?
                    <div className="d-flex justify-content-center"><Spinner type="grow" color="primary" style={{ width: '8rem', height: '8rem' }} /> </div> :
                    <div>
                        <Container fluid={true}>
                            Загружать статусы <Switch color="primary" checked={this.state.loadStatuses} onChange={this.handleSwitch} />
                            <Row>{this.state.instances}</Row>
                        </Container>
                        <Container>
                            <div>
                                {this.state.messages}
                            </div>
                            <div>
                                {this.state.errors}
                            </div>
                        </Container >
                    </div>
            );
        }
    }
}

const mapStateToProps = (state: any) => ({
    propsToken: state.token,
    propsUserName: state.userName
});

interface DispatchProps {
    SET_TOKEN_STRING: typeof SET_TOKEN_STRING,
    SET_USER_NAME: typeof SET_USER_NAME
}

const mapDispatchToProps = (dispatch: Dispatch): DispatchProps => ({
    ...bindActionCreators({ SET_TOKEN_STRING, SET_USER_NAME }, dispatch),
});

interface Props extends State, DispatchProps {
    token: string,
    userName: string
}
interface State {
    stateToken: string | null,
    stateUserName: string | null,
    loading: boolean,
    instances: Array<JSX.Element> | null,
    messages: Array<JSX.Element> | null,
    errors: Array<JSX.Element> | null
    loadStatuses: boolean
}
export default connect(mapStateToProps, mapDispatchToProps)(ListInstance);