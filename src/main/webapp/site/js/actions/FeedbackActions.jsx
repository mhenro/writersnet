import doFetch from './fetch';
import {getHost} from '../utils.jsx';

export const sendFeedback = (feedbackRequest) => {
    return doFetch(getHost() + 'feedback', feedbackRequest);
};