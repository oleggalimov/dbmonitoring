export const USER_NAME_REDUCER = (state: string | null = null, action: any) => {
    switch (action.type) {
        case "SET_USER_NAME":
            return action.userName;
        default:
            return state;
    }
}