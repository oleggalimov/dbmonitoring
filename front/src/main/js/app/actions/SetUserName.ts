const SET_USER_NAME = (newUserName:string|null=null) => {
    return {
        type:"SET_USER_NAME",
        userName:newUserName
    }
}
export default SET_USER_NAME;