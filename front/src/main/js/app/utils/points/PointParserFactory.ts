import { InstanceTypes } from "../../enumerations/InstanceTypes";
import { OraclePointParser } from "./OraclePointParser";

export default class PointParserFactory {
    static getParser(json: any, type: InstanceTypes) {
        switch (type) {
            case InstanceTypes.ORACLE: {
                return new OraclePointParser(json);
            };
            case InstanceTypes.POSTGRES:
            case InstanceTypes.MSSQL:
            case InstanceTypes.MYSQL:
            default: return null;
        }
    }
}