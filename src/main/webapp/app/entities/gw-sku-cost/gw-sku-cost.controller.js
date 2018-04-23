(function() {
    'use strict';

    angular
        .module('griphookApp')
        .controller('GW_SKU_COSTController', GW_SKU_COSTController);

    GW_SKU_COSTController.$inject = ['GW_SKU_COST'];

    function GW_SKU_COSTController(GW_SKU_COST) {

        var vm = this;

        vm.gW_SKU_COSTS = [];

        loadAll();

        function loadAll() {
            GW_SKU_COST.query(function(result) {
                vm.gW_SKU_COSTS = result;
                vm.searchQuery = null;
            });
        }
    }
})();
