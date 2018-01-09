import doFetch from './fetch';
import { getHost } from '../utils.jsx';

export const getCaptcha = () => {
    return doFetch(getHost() + 'captcha', null, null, 'image/jpeg');
};