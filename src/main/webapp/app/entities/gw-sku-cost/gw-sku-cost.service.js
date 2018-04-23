(function() {
    'use strict';
    angular
        .module('griphookApp')
        .factory('GW_SKU_COST', GW_SKU_COST);

    GW_SKU_COST.$inject = ['$resource'];

    function GW_SKU_COST ($resource) {
        var resourceUrl =  'api/gw-sku-costs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
