import doFetch from './fetch';
import {getHost} from '../utils.jsx';

export const getMessagesByGroup = (userId, groupId, token, page) => {
    return doFetch(getHost() + userId + '/messages/' + groupId + '?page=' + page + '&size=20', null, token);
};

export const getGroupName = (groupId, userId, token) => {
    return doFetch(getHost() + 'groups/' + groupId + '/' + userId, null, token);
};

export const addMessageToGroup = (userId, groupId, recipientId, text, token) => {
    let messageRequest = {
        creator: userId,
        text: text,
        primaryRecipient: recipientId,
        groupId: groupId
    };
    return doFetch(getHost() + '/messages/add', messageRequest, token);
};

export const getGroupIdByRecipient = (recipientId, userId, token) => {
    let messageRequest = {
        creator: userId,
        primaryRecipient: recipientId
    };
    return doFetch(getHost() + '/groups/get', messageRequest, token);
};