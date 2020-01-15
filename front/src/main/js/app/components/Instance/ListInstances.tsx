import React = require("react");

export default class ListInstance extends React.Component<{ loading: boolean }, { loading: boolean }> {
    constructor(props: any) {
        super(props);
        this.state = {
            loading: true
        }
    }
    async loadInstances() {
        const contextRoot = location.origin + location.pathname;
        let response = await fetch(`${contextRoot}rest/list/instance/all`)
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
                if (successFlag === true) {
                    console.log(`Body is: ${json['body']}`);
                } else {
                    console.log(`Errors is: ${json['errors']}`);
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
            this.state.loading ? <div>Загрузка...</div> : <div>Данные загружены</div>
        );
    }
}