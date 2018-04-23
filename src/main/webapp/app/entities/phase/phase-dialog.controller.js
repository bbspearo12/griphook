(function() {
    'use strict';

    angular
        .module('griphookApp')
        .controller('PhaseDialogController', PhaseDialogController);

    PhaseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Phase', 'Project', 'Task'];

    function PhaseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Phase, Project, Task) {
        var vm = this;

        vm.phase = entity;
        vm.clear = clear;
        vm.save = save;
        vm.projects = Project.query();
        vm.tasks = Task.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.phase.id !== null) {
                Phase.update(vm.phase, onSaveSuccess, onSaveError);
            } else {
                Phase.save(vm.phase, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('griphookApp:phaseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
