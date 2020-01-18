import { Alert } from "reactstrap";
import React = require("react");
import * as Uuidv4 from 'uuid/v4';

export default (props: any) => {
    return (
        <div>
            <Alert  color='danger' key={Uuidv4()}>
                <div>
                    Loading error:
                </div>
                <p>
                    Can't load data from server.
                </p>
            </Alert >
        </div>
    );
}