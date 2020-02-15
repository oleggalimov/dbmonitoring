import { Col } from "reactstrap";
import * as UUIDv4 from 'uuid/v4';
import InstanceCard from "../components/Instance/InstanceCard";
import { Instance } from "../Interfaces/Instance";
import React = require("react");

export default (json: any, withStatus: boolean = false) => {
    const result: Array<JSX.Element> = new Array();
    if (json==null || json == undefined || json == []) {
        return result;
    }
    const instanceList = json['INSTANCES'] as Array<Instance>;
    instanceList.forEach(
        dbInstance => {
            result.push(<Col xs="12" sm="4" md ="3" lg="2"  key={UUIDv4()}><InstanceCard instance={dbInstance} withStatus={withStatus} /></Col>);
        }
    );
    return result;
}