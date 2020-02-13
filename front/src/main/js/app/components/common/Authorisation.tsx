import React = require("react");
import { NavLink } from "react-router-dom";
import Container from "reactstrap/lib/Container";

export default (props:any)=> {
    const token=props.token;
    const userName=props.userName;
        let container: JSX.Element;
        if (token == "" || token == null || token == undefined) {
            container = <Container>
                <h1>
                    You are not authorized. Please <NavLink to={`login`} > sign in </NavLink>
                </h1>

            </Container>
        } else {
            container = <Container>
                <h1>
                    Welcome, {userName}
                </h1>

            </Container>
        }
        return (container);
}