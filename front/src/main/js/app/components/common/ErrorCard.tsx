import { Card, CardText, CardTitle } from "reactstrap";
import React = require("react");

export default (props: any) => {
    const title:String = props.title;
    const message:String = props.message;
    return (
        <div>
            <Card body outline color='danger'>
                <CardTitle>{title}</CardTitle>
                <CardText>{message}</CardText>
            </Card>
        </div>
    );
}