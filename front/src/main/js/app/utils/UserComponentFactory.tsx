import { Col } from "reactstrap";
import * as UUIDv4 from 'uuid/v4';
import React = require("react");
import { User } from "../Interfaces/User";
import UserCard from "../components/User/UserCard";

export default (json: any, withStatus: boolean = false) => {
    const instanceList = json['USERS'] as Array<User>;
    const result: Array<JSX.Element> = new Array();
    instanceList.forEach(
        userObject => {
            result.push(
                <Col xs="12" sm="4" md="3" lg="2" key={UUIDv4()}>
                    <UserCard userObject={userObject} />
                </Col>
            );
        }
    );
    return result;
}