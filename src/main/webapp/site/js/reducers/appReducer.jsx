import {combineReducers} from 'redux';
import GlobalReducer from './GlobalReducer.jsx';
import AuthorReducer from './AuthorReducer.jsx';
import BookReducer from './BookReducer.jsx';
import ReviewReducer from './ReviewReducer.jsx';
import ContestReducer from './ContestReducer.jsx';

const appReducer = combineReducers({
    GlobalReducer,
    AuthorReducer,
    BookReducer,
    ReviewReducer,
    ContestReducer
});

export default appReducer;