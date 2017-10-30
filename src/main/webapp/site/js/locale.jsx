/* supported languages */
let EN = {
    label: 'english', //reserved property

    /* genres */
    'SCI_FI': 'sci-fi',
    'FANTASY': 'fantasy'
};

let RU = {
    label: 'русский', //reserved property

    /* genres */
    'SCI_FI': 'научная фантастика',
    'FANTASY': 'фэнтези'
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