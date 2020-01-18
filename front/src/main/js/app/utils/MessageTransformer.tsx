import { MessageElement } from "../Interfaces/MessageElement";
import * as Uuidv4 from 'uuid/v4';
import MsgCard from "../components/common/MsgCard";
import React = require("react");

export default (json: any): Array<JSX.Element> => {
    const messageList = json['messages'] as Array<MessageElement>;
    const result: Array<JSX.Element> = new Array();
    messageList.forEach(msg => {
        result.push(
            <MsgCard title={msg.title} cardType={msg.type} message={msg.message} key={Uuidv4()} />
        );

    });
    return result;
}