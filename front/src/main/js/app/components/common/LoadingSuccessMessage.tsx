import { Alert } from "reactstrap";
import React = require("react");
import * as Uuidv4 from 'uuid/v4';

export default (props: any) => {
    return (
        <div>
            <Alert  color='success' key={Uuidv4()}>
                <div>
                    Data sucessfuly updated
                </div>
            </Alert >
        </div>
    );
}