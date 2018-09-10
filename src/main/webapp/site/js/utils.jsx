export const getQueryParams = (qs) => {
    qs = qs.split('+').join(' ');

    var params = {},
        tokens,
        re = /[?&]?([^=]+)=([^&]*)/g;

    while (tokens = re.exec(qs)) {
        params[decodeURIComponent(tokens[1])] = decodeURIComponent(tokens[2]);
    }

    return params;
};

export const formatBytes = (a, b) => {
    if( 0 == a) {
        return '0 Bytes';
    }
    let c = 1024,
        d = b || 2,
        e = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
        f = Math.floor(Math.log(a) / Math.log(c));

    return parseFloat((a / Math.pow(c, f)).toFixed(d)) + ' ' + e[f];
};

export const formatDate = (date, formatter = 'D/M/Y h:m:s') => {
    let year = date.getFullYear() + '',
        month = (date.getMonth() + 1) + '',
        day = date.getDate() + '',
        hour = date.getHours() + '',
        min = date.getMinutes() + '',
        sec = date.getSeconds() + '';

    return formatter.replace('D', addLeadZero(day)).replace('M', addLeadZero(month)).replace('Y', year).replace('h', addLeadZero(hour)).replace('m', addLeadZero(min)).replace('s', addLeadZero(sec));
};

export const formatTimeInterval = (timeInterval, formatter = 'h:m:s') => {  //'y:d:h:m:s'
    let years = Math.trunc(timeInterval / 1000 / 60 / 60 / 24 / 365),
        days = Math.trunc((timeInterval / 1000 / 60 / 60 / 24 / 365 - years) * 365),
        hours = Math.trunc((timeInterval / 1000 / 60 / 60 / 24 - years * 365 - days) * 24),
        minutes = Math.trunc((timeInterval / 1000 / 60 / 60  - years * 365 * 24 - days * 24 - hours) * 60),
        seconds = Math.trunc((timeInterval / 1000 / 60 - years * 365 * 24 * 60 - days * 24 * 60 - hours * 60 - minutes) * 60);

    return formatter.replace('Y', years).replace('D', days).replace('H', hours).replace('M', minutes).replace('S', seconds);
};

export const addLeadZero = (n, width = 2, z = '') => {
    z = z || '0';
    n = n + '';
    return n.length >= width ? n : new Array(width - n.length + 1).join(z) + n;
};


export const getHost = () => {
    return 'https://' + window.location.hostname + '/api/';
};

export const clone = (source) => {
    return JSON.parse(JSON.stringify(source));
};

export const isSubscriber = (login, author) => {
    return author.subscribers.some(subscriber => subscriber.subscriptionId === login);
};

export const isSubscription = (login, author) => {
    return author.subscriptions.some(subscription => subscription.subscriberId === login);
};

export const isFriend = (login, author) => {
    return isSubscriber(login, author) && isSubscription(login, author);
};

export const OperationType = {
    'PREMIUM_ACCOUNT': 0,
    'BOOK': 1,
    'MEDAL': 2,
    'GIFT': 3,
    'BALANCE_RECHARGE': 4,
    'CONTEST_DONATE': 5,
    'CONTEST_AWARD': 6
};

export const GiftCategory = {
    'EVERYDAY': 0,
    'BIRTHDAY': 1,
    'RESPECT': 2,

    valueOf: (value) => {
        switch(value) {
            case '0': return 'EVERYDAY';
            case '1': return 'BIRTHDAY';
            case '2': return 'RESPECT';
        }
    }
};