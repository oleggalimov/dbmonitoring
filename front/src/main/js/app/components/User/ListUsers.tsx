import React = require("react");
import { Container, Row, Spinner } from "reactstrap";
import ErrorComponentFactory from "../../utils/ErrorComponentFactory";
import MessageComponentFactory from "../../utils/MessageComponentFactory";
import UserComponentFactory from "../../utils/UserComponentFactory";
import LoadingErrorMessage from "../common/LoadingErrorMessage";
import ForbiddenMeesage from "../common/ForbiddenMeesage";


export default class ListUsers extends React.Component<
    { token: string },
    {
        loading: boolean,
        usersList: Array<JSX.Element> | null,
        messages: Array<JSX.Element> | null,
        errors: Array<JSX.Element> | null,
        token:string
    }
    > {
    constructor(props: any) {
        super(props);
        this.state = {
            loading: true,
            usersList: null,
            messages: null,
            errors: null,
            token: props.token
        }

    }

    async loadInstances(loadStatuses: boolean = false) {
        const contextRoot = location.origin + location.pathname;
        let requestURL: string;
        this.setState({ loading: true });
        // requestURL = `${contextRoot}rest/check/instance/all`;
        requestURL = `http://127.0.0.1:8080/database_monitoring/rest/list/user/all`;
        await fetch(
            requestURL, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorisation': `Base ${this.state.token}`
            },
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
                console.debug(`${error}`);
                this.setState({ loading: false, errors: [<LoadingErrorMessage key={'errorMessageBox'} />] });
            });
    }

    componentDidMount() {
        this.loadInstances();
    }
    render() {
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