import React = require("react");
import { Message } from "../../Interfaces/Message";
import MsgCard from "../MsgCard";
import { Container } from "reactstrap";

export default class ListInstance extends React.Component<{ loading: boolean }, { loading: boolean,messages: Array<JSX.Element> | null }> {
    constructor(props: any) {
        super(props);
        this.state = {
            loading: true,
            messages: null
        }
    }
    async loadInstances() {
        const contextRoot = location.origin + location.pathname;
        let response = await fetch(`${contextRoot}rest/list/instance/allш`)
            .then((response) => {
                if (response.status == 200) {
                    return response.json();
                } else {
                    throw Error(`Статус ответа: ${response.status}`);
                }
            })
            .then((json) => {
                this.setState({ loading: false });
                const jsonString = JSON.stringify(json);
                console.log(`Json is: ${jsonString}`);
                const successFlag = json['success'] as boolean;
                if (successFlag) {
                    console.log(`Body is: ${json['body']}`);

                } else {
                    console.log(`Errors is: ${json['errors']}`);
                    const messageList = json['messages'] as Array<Message>;
                    console.log(`messageList: ${messageList.length}`);
                    const msgElements: Array<JSX.Element> = new Array();
                    messageList.forEach(msg => {
                        msgElements.push(
                            <MsgCard title={msg.title} cardType={CardType.WARNING} message={msg.message} />
                        );
                    });
                    this.setState({messages:msgElements})
                    console.log(`Messages is: ${json['messages']}`);
                }
            })
            .catch((error) => {
                console.log(`Error: ${error}`);
            });
    }
    componentDidMount() {
        this.loadInstances();
    }
    render() {
        return (
            this.state.loading ? <div>Загрузка...</div> : <Container>{this.state.messages}</Container>
        );
    }
}