import * as Uuidv4 from 'uuid/v4';
import MsgCard from "../components/common/MsgCard";
import React = require("react");
import { ErrorElement } from '../Interfaces/ErrorElement';

export default (json: any): Array<JSX.Element> => {
    const errorList = json['errors'] as Array<ErrorElement>;
    const result: Array<JSX.Element> = new Array();
    errorList.forEach(err => {
        result.push(
            <MsgCard title={err.title} cardType={"danger"} message={err.message} key={Uuidv4()} />
        );

    });
    return result;
}