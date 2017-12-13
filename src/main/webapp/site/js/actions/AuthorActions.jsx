import doFetch from './fetch';
import { getHost } from '../utils.jsx';

export const getAuthors = (page) => {
    return doFetch(getHost() + 'authors?page=' + page + '&size=20');
};

export const getAuthorDetails = (userId) => {
    return doFetch(getHost() + 'authors/' + userId);
};

export const getAuthorChatGroups = (userId, token, page) => {
    return doFetch(getHost() + 'authors/' + userId + '/groups?page=' + page + '&size=20', null, token);
};

export const getFriends = (userId, matcher, token, page) => {
    return doFetch(getHost() + '/friends/' + userId + '/' + matcher + '?page=' + page + '&size=20', null, token);
};

export const saveAuthor = (author, token) => {
    return doFetch(getHost() + 'authors', author, token);
};

export const saveAvatar = (avatar, token) => {
    return doFetch(getHost() + 'avatar', avatar, token, 'multipart/form-data');
};

export const subscribeOn = (authorName, token) => {
    return doFetch(getHost() + 'authors/subscribe', authorName, token);
};

export const removeSubscription = (authorName, token) => {
    return doFetch(getHost() + 'authors/unsubscribe', authorName, token);
};

export const SET_AUTHORS = 'SET_AUTHORS';
export const SET_AUTHOR = 'SET_AUTHOR';

export const SET_NEW_FRIENDS = 'SET_NEW_FRIENDS';

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

export const setNewFriends = (newFriends) => {
    return {
        type: SET_NEW_FRIENDS,
        newFriends
    }
};