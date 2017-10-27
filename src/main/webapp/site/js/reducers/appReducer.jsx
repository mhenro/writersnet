import {combineReducers} from 'redux';
import GlobalReducer from './GlobalReducer.jsx';
import AuthorReducer from './AuthorReducer.jsx';
import BookReducer from './BookReducer.jsx';

const appReducer = combineReducers({
    GlobalReducer,
    AuthorReducer,
    BookReducer
});

export default appReducer;