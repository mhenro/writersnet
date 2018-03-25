import doFetch from './fetch';
import { getHost } from '../utils.jsx';

export const getAuthors = (name, page = 0, size = 5, sort = 'firstName') => {
    if (name) {
        return doFetch(getHost() + 'authors/name/' + name + '?page=' + page + '&size=' + size + '&sort=' + sort);
    } else {
        return doFetch(getHost() + 'authors?page=' + page + '&size=' + '&size=' + size + '&sort=' + sort);
    }
};

export const getAuthorsCount = () => {
    return doFetch(getHost() + 'count/authors');
};

export const getAuthorDetails = (userId) => {
    return doFetch(getHost() + 'authors/' + userId);
};

export const getAuthorChatGroups = (userId, token, page = 0, size = 20) => {
    return doFetch(getHost() + 'authors/' + userId + '/groups?page=' + page + '&size=' + size, null, token);
};

export const getFriends = (userId, matcher, token, page = 0, size = 200) => {
    return doFetch(getHost() + 'friendship/friends/' + userId + '/' + matcher + '?page=' + page + '&size=' + size, null, token);
};

export const getNewFriendsCount = (userId, token) => {
    return doFetch(getHost() + 'friendship/new/friends/' + userId, null, token);
};

export const getNotAcceptedCountestCount = (userId) => {
    return doFetch(getHost() + 'contests/not-accepted/' + userId);
};

export const getAllFriends = (userId, token, page = 0, size = 20) => {
    return doFetch(getHost() + 'friendship/friends/' + userId + '?page=' + page + '&size=' + size, null, token);
};

export const getAllSubscribers = (userId, token, page = 0, size = 20) => {
    return doFetch(getHost() + 'friendship/subscribers/' + userId + '?page=' + page + '&size=' + size, null, token);
};

export const getAllSubscriptions = (userId, token, page = 0, size = 20) => {
    return doFetch(getHost() + 'friendship/subscriptions/' + userId + '?page=' + page + '&size=' + size, null, token);
};

export const isFriendOf = (authorId, token) => {
    return doFetch(getHost() + 'friends/' + authorId, null, token);
};

export const isSubscriberOf = (authorId, token) => {
    return doFetch(getHost() + 'subscribers/' + authorId, null, token);
};

export const isSubscriptionOf = (authorId, token) => {
    return doFetch(getHost() + 'subscriptions/' + authorId, null, token);
};

export const checkFriendshipWith = (authorId, token) => {
    return doFetch(getHost() + 'friendship/' + authorId, null, token);
};

export const saveAuthor = (author, token) => {
    return doFetch(getHost() + 'authors', author, token);
};

export const saveAvatar = (avatar, token) => {
    return doFetch(getHost() + 'avatar', avatar, token, 'multipart/form-data');
};

export const changePassword = (changePasswordRequest, token) => {
    return doFetch(getHost() + 'authors/password', changePasswordRequest, token);
};

export const restoreDefaultAvatar = (token) => {
    return doFetch(getHost() + 'avatar/restore', null, token);
};

export const subscribeOn = (authorName, token) => {
    return doFetch(getHost() + 'authors/subscribe', authorName, token);
};

export const removeSubscription = (authorName, token) => {
    return doFetch(getHost() + 'authors/unsubscribe', authorName, token);
};

export const getAuthorGifts = (userId, page = 0, size = 8) => {
    return doFetch(getHost() + 'gifts/authors/' + userId + '?page=' + page + '&size=' + size);
};



export const SET_AUTHORS = 'SET_AUTHORS';
export const SET_AUTHOR = 'SET_AUTHOR';

export const SET_NEW_FRIENDS = 'SET_NEW_FRIENDS';

export const SHOW_AUTHOR_GIFTS_FORM = 'SHOW_AUTHOR_GIFTS_FORM';
export const CLOSE_AUTHOR_GIFTS_FORM = 'CLOSE_AUTHOR_GIFTS_FORM';

export const SET_NOT_ACCEPTED_CONTESTS = 'SET_NOT_ACCEPTED_CONTESTS';

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

export const showAuthorGiftsForm = () => {
    return {
        type: SHOW_AUTHOR_GIFTS_FORM
    }
};

export const closeAuthorGiftsForm = () => {
    return {
        type: CLOSE_AUTHOR_GIFTS_FORM
    }
};

export const setNotAcceptedContests = (notAcceptedContests) => {
    return {
        type: SET_NOT_ACCEPTED_CONTESTS,
        notAcceptedContests
    }
};