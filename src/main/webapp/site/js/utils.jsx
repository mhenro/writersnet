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

export const getHost = () => {
    return 'http://' + window.location.hostname + ':8080/';
};