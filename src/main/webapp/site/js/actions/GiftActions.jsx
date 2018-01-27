import doFetch from './fetch';
import { getHost } from '../utils.jsx';

export const getGiftDetails = (giftId) => {
    return doFetch(getHost() + 'gift/' + giftId);
};