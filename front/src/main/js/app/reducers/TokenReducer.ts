export const TOKEN_REDUCER = (state: string | null = null, action: any) => {
    switch (action.type) {
        case "SET_TOKEN":
            return action.token;
        default:
            return state;
    }
}