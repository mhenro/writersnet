import {combineReducers} from 'redux';
import GlobalReducer from './GlobalReducer.jsx';
import AuthorReducer from './AuthorReducer.jsx';

const appReducer = combineReducers({
    GlobalReducer,
    AuthorReducer
});

export default appReducer;