import * as Uuidv4 from 'uuid/v4';
import MsgCard from "../components/common/MsgCard";
import React = require("react");

export default (instance: any): Array<JSX.Element> => {
    const result: Array<JSX.Element> = new Array();
    const { id, host, database, type, login, password } = instance;
    if (id == "" || id == undefined || id == null) {
        result.push(
            <MsgCard cardType="warning" message="ID is incorrect!" key={Uuidv4()} />
        );
    }
    if (host == "" || host == undefined || host == null) {
        result.push(
            <MsgCard cardType="warning" message="HOST is incorrect!" key={Uuidv4()} />
        );
    }
    if (database == "" || database == undefined || database == null) {
        result.push(
            <MsgCard cardType="warning" message="DATABASE NAME is incorrect!" key={Uuidv4()} />
        );
    }
    if (type == "" || type == undefined || type == null || type == "Select type...") {
        result.push(
            <MsgCard cardType="warning" message="TYPE is incorrect!" key={Uuidv4()} />
        );
    }
    if (login == "" || login == undefined || login == null) {
        result.push(
            <MsgCard cardType="warning" message="USER LOGIN is incorrect!" key={Uuidv4()} />
        );
    }
    if (password == "" || password == undefined || password == null || (password as string).length < 8) {
        result.push(
            <MsgCard cardType="warning" message="USER PASSWORD is too short or empty!" key={Uuidv4()} />
        );
    }
    return result;
}