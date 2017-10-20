import fetch from 'isomorphic-fetch';

const doFetch = (url, request, token, responseType = 'json') => {
    let header = {
        method: request ? 'POST' : 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    };

    if (request) {
        header.body = JSON.stringify(request);
    }
    if (token) {
        header.headers.Authorization = 'Bearer ' + token;
    }
    if (responseType === 'blob') {
        header.responseType = 'blob';
    }

    return fetch(url, header)
        .then(response => Promise.all([response, responseType === 'json' ? response.json() : response.blob()]))
        .catch(error => {
            console.log('Error requesting ' + url + ' : ' + error.message);
            throw error;
        });
};

export default doFetch;