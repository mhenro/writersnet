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

export const addLeadZero = (n, width = 2, z = '') => {
    z = z || '0';
    n = n + '';
    return n.length >= width ? n : new Array(width - n.length + 1).join(z) + n;
};


export const getHost = () => {
    return 'https://' + window.location.hostname + ':8080/';
};