import React = require("react");
import { connect } from "react-redux";
import { Container, Row, Spinner } from "reactstrap";
import { bindActionCreators, Dispatch } from "redux";
import SET_TOKEN_STRING from "../../actions/SetToken";
import SET_USER_NAME from "../../actions/SetUserName";
import ErrorComponentFactory from "../../utils/ErrorComponentFactory";
import MessageComponentFactory from "../../utils/MessageComponentFactory";
import UserComponentFactory from "../../utils/UserComponentFactory";
import Authorisation from "../common/Authorisation";
import ForbiddenMeesage from "../common/ForbiddenMeesage";
import LoadingErrorMessage from "../common/LoadingErrorMessage";


class ListUsers extends React.Component<Props, State>  {
    constructor(props: any) {
        super(props);
        this.state = {
            usersList: null,
            stateToken: props.propsToken,
            stateUserName: props.propsUserName,
            loading: true,
            instances: null,
            messages: null,
            errors: null,
            loadStatuses: false
        }

    }
    abortController = new AbortController();
    componentDidMount() {
        this.loadInstances();
    }
    componentWillUnmount() {
        this.abortController.abort();
    }
    async loadInstances(loadStatuses: boolean = false) {
        const token = this.state.stateToken;
        if (token == "" || token == null || token == undefined) {
            return;
        }
        const contextRoot = location.origin + location.pathname;
        let requestURL: string;
        this.setState({ loading: true });
        requestURL = `${contextRoot}rest/list/user/all`;
        const headers = new Headers();
        headers.append('Accept', 'application/json');
        headers.append('Content-Type', 'application/json');
        headers.append('Authorization', `Basic ${this.state.stateToken}`);
        
        console.log (JSON.stringify(headers));
        await fetch(
            requestURL, {
            signal: this.abortController.signal,
            method: 'GET',
            headers:headers

        }
        )
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
                    const usersList = UserComponentFactory(json['body']);
                    this.setState({
                        usersList: usersList
                    });
                }
                const messageList = MessageComponentFactory(json);
                const errorsList = ErrorComponentFactory(json);
                this.setState({
                    messages: messageList, errors: errorsList
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
            return (
                this.state.loading ?
                    <div className="d-flex justify-content-center">
                        <Spinner type="grow" color="primary" style={{ width: '8rem', height: '8rem' }} />
                    </div> :
                    <div>
                        <Container fluid={true}>
                            <Row>{this.state.usersList}</Row>
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

    usersList: Array<JSX.Element> | null,
    stateToken: string | null,
    stateUserName: string | null,
    loading: boolean,
    instances: Array<JSX.Element> | null,
    messages: Array<JSX.Element> | null,
    errors: Array<JSX.Element> | null
    loadStatuses: boolean
}

export default connect(mapStateToProps, mapDispatchToProps)(ListUsers);