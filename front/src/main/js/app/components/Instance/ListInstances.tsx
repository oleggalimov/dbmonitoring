import React = require("react");
import { Container } from "reactstrap";
import MessageTransformer from "../../utils/MessageTransformer";
import ErrorsTransformer from "../../utils/ErrorsTransformer";
import LoadingErrorMessage from "../common/LoadingErrorMessage";

export default class ListInstance extends React.Component<
    { loading: boolean },
    {
        loading: boolean,
        instances: Array<JSX.Element> | null,
        messages: Array<JSX.Element> | null,
        errors: Array<JSX.Element> | null
    }
    > {
    constructor(props: any) {
        super(props);
        this.state = {
            loading: true,
            instances: null,
            messages: null,
            errors: null
        }
    }
    async loadInstances() {
        const contextRoot = location.origin + location.pathname;
        // await fetch(`${contextRoot}rest/list/instance/all`)
        await fetch(`http://127.0.0.1:8887/list.json#`)
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
                    console.log(`Body is: ${json['body']}`);

                } else {
                    const messageList = MessageTransformer(json);
                    const errorsList = ErrorsTransformer(json);
                    this.setState({ messages: messageList, errors: errorsList });
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
                <div>Загрузка...</div> :
                <Container>
                    <div>
                        {this.state.instances}
                    </div>
                    <div>
                        {this.state.messages}
                    </div>
                    <div>
                        {this.state.errors}
                    </div>
                </Container>
        );
    }
}