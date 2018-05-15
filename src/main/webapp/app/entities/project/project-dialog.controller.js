(function() {
    'use strict';

    angular
        .module('griphookApp')
        .controller('ProjectDialogController', ProjectDialogController);

    ProjectDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Project', 'Phase'];

    function ProjectDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Project, Phase) {
        var vm = this;

        vm.project = entity;
        vm.clear = clear;
        vm.save = save;
        vm.phases = Phase.query();
        vm.project.defaultProjectMargin = 40;
        vm.project.defaultProjectMargin = 40;
        vm.project.subcontractProjectMargin = 20;
        vm.project.pmpercentage = 20;
        vm.project.risk = 3;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.project.id !== null) {
                Project.update(vm.project, onSaveSuccess, onSaveError);
            } else {
                Project.save(vm.project, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('griphookApp:projectUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
        $scope.newPhases = [{id: '1', name: ''}];
        $scope.addNewPhase = function () {
            var newPhase = $scope.newPhases.length+1;
            $scope.newPhases.push({name: ''});
        }
    }
})();
