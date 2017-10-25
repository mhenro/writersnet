/* supported languages */
let EN = {
    label: 'english' //reserved property
};

let RU = {
    label: 'русский' //reserved property
};

/* locale */
export const locale = {
    EN,
    RU
};

/* get locale method */
export const getLocale = (language) => {
    return locale[language];
};