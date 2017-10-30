import fetch from 'isomorphic-fetch';

const doFetch = (url, request, token, contentType = 'application/json', responseType = 'json') => {
    let header = {
        method: request ? 'POST' : 'GET',
        headers: {
            'Accept': 'application/json'
        }
    };

    if (request === 'DELETE') {
        header.method = 'DELETE';
    }

    if (contentType !== 'multipart/form-data') {
        header.headers['Content-Type'] = contentType;
    }

    if (request) {
        header.body = contentType === 'application/json' ? JSON.stringify(request) : request;
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