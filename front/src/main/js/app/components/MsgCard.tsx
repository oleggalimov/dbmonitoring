import { Card, CardText, CardTitle } from "reactstrap";
import React = require("react");

export default (props: any) => {
    const cardType: CardType = props.cardType;
    const title:String = props.title;
    const message:String = props.message;
    console.debug(`Resolved props: cardType=${cardType}, title=${title}, message=${message}`);
    return (
        <div>
            <Card body outline color={cardType}>
                <CardTitle>{title}</CardTitle>
                <CardText>{message}</CardText>
            </Card>
        </div>
    );
}