import doFetch from './fetch.jsx';
import { getHost } from '../utils.jsx';

export const getGiftDetails = (giftId) => {
    return doFetch(getHost() + 'gift/' + giftId);
};

export const getAllGifts = () => {
    return doFetch(getHost() + 'gifts');
};