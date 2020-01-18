import { Alert } from "reactstrap";
import React = require("react");

export default (props: any) => {
    return (
        <div>
            <Alert  color={(props.cardType as String).toLowerCase()}>
                <div>
                    {props.title}
                </div>
                <p>
                    {props.message}
                </p>
            </Alert >
        </div>
    );
}