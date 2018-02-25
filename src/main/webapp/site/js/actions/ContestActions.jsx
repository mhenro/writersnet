import doFetch from './fetch';
import {getHost} from '../utils.jsx';

export const getAllContests = (page = 0, size = 20) => {
    return doFetch(getHost() + 'contests?page=' + page + '&size=' + size);
};