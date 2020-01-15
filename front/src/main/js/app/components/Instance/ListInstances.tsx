import React = require("react");

export default class ListInstance extends React.Component {
    componentDidMount() {
        console.log("Компонент загрузился");
    }
    render() {
        return (<div>Список инстансов</div>);
    }
}