(function() {
    'use strict';

    angular
        .module('augustusApp')
        .controller('TaskDialogController', TaskDialogController);

    TaskDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Task', 'Phase'];

    function TaskDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Task, Phase) {
        var vm = this;

        vm.task = entity;
        vm.clear = clear;
        vm.save = save;
        vm.phases = Phase.query();
        vm.cost = 0;
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.task.id !== null) {
                Task.update(vm.task, onSaveSuccess, onSaveError);
            } else {
                Task.save(vm.task, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('augustusApp:taskUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

         $scope.updateCost = function(cost) {
            vm.cost = cost;
        }

         $scope.updatehours = function(hours) {
                    vm.updatehours = hours;
        }

         $scope.updatenresources = function(resources) {
            vm.task.subTotal = resources * vm.cost * vm.updatehours;
            console.log("setting subtotal to "+ vm.task.subTotal);
        }

    }
})();
