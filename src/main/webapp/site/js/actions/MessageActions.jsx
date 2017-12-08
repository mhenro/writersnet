import doFetch from './fetch';
import {getHost} from '../utils.jsx';

export const getMessagesByGroup = (userId, groupId, token, page) => {
    return doFetch(getHost() + userId + '/messages/' + groupId + '?page=' + page + '&size=20', null, token);
};

export const addMessageToGroup = (userId, groupId, text, token) => {
    let messageRequest = {
        creator: userId,
        text: text,
        groupId: groupId
    };
    return doFetch(getHost() + '/messages/add', messageRequest, token);
};