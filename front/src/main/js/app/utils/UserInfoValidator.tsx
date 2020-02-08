import * as Uuidv4 from 'uuid/v4';
import MsgCard from "../components/common/MsgCard";
import React = require("react");
const stringRegexp = '^[a-zA-Z0-9]+$';
const passwordgRegexp = '(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$';
const eMailRegexp = '/\S+@\S+\.\S+/';

const validateField = (fieldName: string, fieldValue: String, regExpression: string) => {
    if (!fieldValue.match(stringRegexp)) {
        return <MsgCard cardType="warning" message={`Field ${fieldName} must contains only characters and numbers!`} key={Uuidv4()} />;
    } else {
        return null;
    }
}

export default (userInfo: any): Array<JSX.Element> => {
    const result: Array<JSX.Element> = new Array();
    let hasNoRoles = true;
    for (let element in userInfo) {
        let error: JSX.Element | null = null;
        switch (element) {
            case "login":
                error = validateField("LOGIN", userInfo[element], stringRegexp);
                break;
            case "firstName":
                error = validateField("FIRST NAME", userInfo[element], stringRegexp);
                break;
            case "lastName":
                error = validateField("LAST NAME", userInfo[element], stringRegexp);
                break;
            case "personNumber":
                error = validateField("PERSON NUMBER", userInfo[element], stringRegexp);
                break;
            case "userPassword":
                error = validateField("PASSWORD", userInfo[element], passwordgRegexp);
                break;
            case "userRole":
                if (hasNoRoles) {
                    hasNoRoles = userInfo[element] == true ? false : true;
                }
                break;
            case "adminRole":
                if (hasNoRoles) {
                    hasNoRoles = userInfo[element] == true ? false : true;
                }
                break;
            case "userAdminRole":
                if (hasNoRoles) {
                    hasNoRoles = userInfo[element] == true ? false : true;
                }
                break;
        }
        if (error != null) {
            result.push(error);
        }
    };
    if (hasNoRoles) {
        result.push(
            <MsgCard cardType="warning" message={`User must have at least one role!`} key={Uuidv4()} />
        );
    }
    return result;
}