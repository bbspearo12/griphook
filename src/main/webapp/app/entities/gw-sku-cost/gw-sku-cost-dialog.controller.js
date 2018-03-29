(function() {
    'use strict';

    angular
        .module('augustusApp')
        .controller('GW_SKU_COSTDialogController', GW_SKU_COSTDialogController);

    GW_SKU_COSTDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'GW_SKU_COST'];

    function GW_SKU_COSTDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, GW_SKU_COST) {
        var vm = this;

        vm.gW_SKU_COST = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.gW_SKU_COST.id !== null) {
                GW_SKU_COST.update(vm.gW_SKU_COST, onSaveSuccess, onSaveError);
            } else {
                GW_SKU_COST.save(vm.gW_SKU_COST, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('augustusApp:gW_SKU_COSTUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
