import { createStore, combineReducers } from "redux";
import { TOKEN_REDUCER } from "../reducers/TokenReducer";

export default () => {
    const store = createStore(
        combineReducers({
            token: TOKEN_REDUCER
        }));
    return store;
}