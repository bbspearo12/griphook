(function() {
    'use strict';

    angular
        .module('griphookApp')
        .controller('GW_SKU_COSTDeleteController',GW_SKU_COSTDeleteController);

    GW_SKU_COSTDeleteController.$inject = ['$uibModalInstance', 'entity', 'GW_SKU_COST'];

    function GW_SKU_COSTDeleteController($uibModalInstance, entity, GW_SKU_COST) {
        var vm = this;

        vm.gW_SKU_COST = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            GW_SKU_COST.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
