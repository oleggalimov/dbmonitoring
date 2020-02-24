import { OraclePointParser } from "./OraclePointParser";

export default class PointParserFactory {
    static getParser(json: any, type: string) {
        switch (type) {
            case "ORACLE": {
                return new OraclePointParser(json);
            };
            case "POSTGRES":
            case "MSSQL":
            case "MYSQL":
            default: return null;
        }
    }
}