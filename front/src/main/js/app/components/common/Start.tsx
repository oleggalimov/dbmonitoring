import React = require("react");
import Container from "reactstrap/lib/Container";

export default (props:any) => {
    const {userName} = props;
    let message: JSX.Element;
    if (userName == "" || userName == undefined || userName == null) {
        message = <Container>
            <h1>
                Система мониторинга баз данных. <br/>
                Необходима авторизация
        </h1>
        </Container>
    } else {
        message = <Container>
            <h1>
                Система мониторинга баз данных.<br/>
                Авторизованный пользователь: {userName}
            </h1>
        </Container>
    }

    return (message);
}