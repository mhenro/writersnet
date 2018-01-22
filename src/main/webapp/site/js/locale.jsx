import { RU } from './languages/lang-RU.jsx';
import { EN } from './languages/lang-EN.jsx';

/* locale */
export const locale = {
    EN,
    RU
};

/* get locale method */
export const getLocale = (language) => {
    return locale[language];
};