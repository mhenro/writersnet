import doFetch from './fetch.jsx';
import {getHost} from '../utils.jsx';

export const sendFeedback = (feedbackRequest) => {
    return doFetch(getHost() + 'feedback', feedbackRequest);
};