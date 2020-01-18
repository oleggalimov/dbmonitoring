import { DataBaseUser } from "./DataBaseUser";

export interface Instance {
    readonly id:String;
    readonly host:String;
    readonly port:Number;
    readonly database:String;
    readonly user:DataBaseUser;
    readonly type:String;
    readonly status?:String;
}