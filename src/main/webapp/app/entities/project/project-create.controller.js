(function() {
    'use strict';

    angular
        .module('griphookApp')
        .controller('ProjectCreateController', ProjectCreateController);

    ProjectCreateController.$inject = ['$timeout', '$scope', '$stateParams', 'entity', 'Project'];

    function ProjectCreateController($timeout, $scope, $stateParams, entity, Project) {
        var vm = this;

        vm.project = entity;
        vm.clear = clear;
        vm.save = save;
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
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
        $scope.phases = [{id: 'project_kickoff', name: 'Project Kickoff', Tasks: [{}]},
                            {id: 'discovery', name: 'Discovery', Tasks: [{}]},
                            {id: 'staging', name: 'Staging', Tasks: [{}]},
                            {id: 'deployment', name: 'Deployment', Tasks: [{}]},
                            {id: 'installation', name: 'Installation', Tasks: [{}]},
                            {id: 'go_live_support', name: 'Go Live Support', Tasks: [{}]},
                            {id: 'documentation', name: 'Documentation / KT', Tasks: [{}]}];
        $scope.addTask = function (phaseid) {
            console.log("adding task to phase ", phaseid);
            var i;
            for (i = 0; i < $scope.phases.length; i++) {
                if ($scope.phases[i].id == phaseid ) {
                    console.log("should add task to this phase");
                    $scope.phases[i].Tasks.push({name: ""});
                }
            }
//            var newTask = $scope.newPhases.length+1;
//            $scope.newPhases.push({name: ''});
        }
    }
})();
