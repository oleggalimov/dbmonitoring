import { DataBaseUser } from "./DataBaseUser";

export interface Instance {
    readonly id:string;
    readonly host:string;
    readonly port:number;
    readonly database:string;
    readonly user:DataBaseUser;
    readonly type:string;
    readonly status?:string;
}