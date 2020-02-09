export default (username:string, password:string)=>{
    return btoa(`${username}:${password}`);
}