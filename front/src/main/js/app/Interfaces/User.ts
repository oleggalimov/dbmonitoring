export interface User {
    readonly login:string;
    readonly roles:Array<String>;
    readonly firstName:string;
    readonly lastName:string;
    readonly personNumber:string;
    readonly status:string;
    readonly email:string;
    readonly password?:string
}