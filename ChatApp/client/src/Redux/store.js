

import { applyMiddleware, combineReducers, legacy_createStore } from "redux";
import { authReducer } from "./Auth/Reducer";
import { thunk } from "redux-thunk";
import { chatReducer } from "./Chat/Reducer";
import { messageReducer } from "./Message/Reducer";

const rootReducer = combineReducers({
    auth:authReducer,
    chat:chatReducer,
    message:messageReducer,
})

export const store = legacy_createStore(rootReducer, applyMiddleware(thunk))
