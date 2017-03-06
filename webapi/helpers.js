'use strict';

module.exports = {
    parseRequestHeader: function(request) {
        var cookie = request.headers['x-cookie'];
        var parsedCookie = JSON.parse(cookie);
        return parsedCookie;
    }
};
