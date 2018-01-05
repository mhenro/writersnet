import doFetch from './fetch';
import { getHost } from '../utils.jsx';

export const getNews = (token, page = 0, size = 20) => {
    return doFetch(getHost() + 'news?page=' + page + '&size=' + size, null, token);
};