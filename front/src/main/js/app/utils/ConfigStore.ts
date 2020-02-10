import { createStore, combineReducers } from "redux";
import { TOKEN_REDUCER } from "../reducers/TokenReducer";
import { USER_NAME_REDUCER } from "../reducers/UserNameReducer";

export default () => {
    const store = createStore(
        combineReducers({
            token: TOKEN_REDUCER,
            userName: USER_NAME_REDUCER
        }));
    return store;
}