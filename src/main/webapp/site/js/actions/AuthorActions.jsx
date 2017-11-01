import doFetch from './fetch';
import {getHost} from '../utils.jsx';

export const getAuthors = (page) => {
    return doFetch(getHost() + 'authors?page=' + page + '&size=20');
};

export const getAuthorDetails = (userId) => {
    return doFetch(getHost() + 'authors/' + userId);
};

export const saveAuthor = (author, token) => {
    return doFetch(getHost() + 'authors', author, token);
};

export const saveAvatar = (avatar, token) => {
    return doFetch(getHost() + 'avatar', avatar, token, 'multipart/form-data');
};

export const SET_AUTHORS = 'SET_AUTHORS';
export const SET_AUTHOR = 'SET_AUTHOR';

export const setAuthors = (authors) => {
    return {
        type: SET_AUTHORS,
        authors
    }
};

export const setAuthor = (author) => {
    return {
        type: SET_AUTHOR,
        author
    }
};