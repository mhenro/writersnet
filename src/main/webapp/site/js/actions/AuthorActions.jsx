import doFetch from './fetch';

export const getAuthors = (page) => {
    return doFetch('http://localhost:8080/authors?page=' + page + '&size=20');
};

export const getAuthorDetails = (userId) => {
    return doFetch('http://localhost:8080/authors/' + userId);
};

export const saveAuthor = (author, token) => {
    return doFetch('http://localhost:8080/authors', author, token);
};

export const saveAvatar = (avatar, token) => {
    return doFetch('http://localhost:8080/avatar', avatar, token, 'multipart/form-data');
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