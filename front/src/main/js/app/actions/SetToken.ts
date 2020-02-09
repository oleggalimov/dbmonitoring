const SET_TOKEN_STRING = (newToken:string|null=null) => {
    return {
        type:"SET_TOKEN",
        token:newToken
    }
}
export default SET_TOKEN_STRING;