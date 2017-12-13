import doFetch from './fetch';
import { getHost } from '../utils.jsx';

export const getNews = (token, page) => {
    return doFetch(getHost() + 'news?page=' + page + '?size=20', null, token);
};