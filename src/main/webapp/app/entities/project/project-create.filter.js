//var chunk = require('lodash-node/modern/array/chunk');
//var memoize = require('lodash-node/modern/function/memoize');
(function() {
    'use strict';

    angular
        .module('griphookApp')
        .filter('chunk', function() {
            return memoize(chunk);
        });
})();
