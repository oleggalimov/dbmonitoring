import React = require("react");
import Switch from '@material-ui/core/Switch';
import { Container, Row, Spinner } from "reactstrap";
import ErrorComponentFactory from "../../utils/ErrorComponentFactory";
import InstanceComponentFactory from "../../utils/InstanceComponentFactory";
import MessageComponentFactory from "../../utils/MessageComponentFactory";
import LoadingErrorMessage from "../common/LoadingErrorMessage";


export default class ListInstance extends React.Component<
    { loading: boolean },
    {
        loading: boolean,
        instances: Array<JSX.Element> | null,
        messages: Array<JSX.Element> | null,
        errors: Array<JSX.Element> | null
        loadStatuses: boolean
    }
    > {
    constructor(props: any) {
        super(props);
        this.state = {
            loading: true,
            instances: null,
            messages: null,
            errors: null,
            loadStatuses: false
        }

    }

    handleSwitch = () => {
        const switcher = !this.state.loadStatuses;
        this.setState({
            loadStatuses: switcher
        });
        this.loadInstances(switcher);
    }

    async loadInstances(loadStatuses:boolean=false) {
        const contextRoot = location.origin + location.pathname;
        let requestURL: string;
        this.setState({ loading: true });
        if (loadStatuses) {
            // requestURL = `${contextRoot}rest/check/instance/all`;
            requestURL = 'http://127.0.0.1:8887/statusList.json';
        } else {
            // requestURL = `${contextRoot}rest/list/instance/all`;
            requestURL = 'http://127.0.0.1:8887/list.json#';
        }
        await fetch(requestURL)
            .then((response) => {
                if (response.status == 200) {
                    return response.json();
                } else {
                    throw Error(`Response status: ${response.status}`);
                }
            })
            .then((json) => {
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
                <div className="d-flex justify-content-center"><Spinner color="secondary" type="grow" style={{ width: '8rem', height: '8rem' }} /> </div> :
                <div>
                    <Container fluid={true}>
                        Загружать статусы <Switch color="secondary" checked={this.state.loadStatuses} onChange={this.handleSwitch} />
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