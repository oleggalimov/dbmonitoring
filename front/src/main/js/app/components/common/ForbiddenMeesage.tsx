import { Alert } from "reactstrap";
import React = require("react");
import * as Uuidv4 from 'uuid/v4';

export default (props: any) => {
    return (
        <div>
            <Alert  color='warning' key={Uuidv4()}>
                <div>
                    You have no permission for this operation. Please sign in or contact your administrator.
                </div>
            </Alert >
        </div>
    );
}