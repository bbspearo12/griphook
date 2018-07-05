(function() {
    'use strict';

    angular
        .module('griphookApp')
        .controller('TaskDialogController', TaskDialogController);

    TaskDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Task', 'Phase', 'Project'];

    function TaskDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Task, Phase, Project) {
        var vm = this;

        vm.task = entity;
        vm.clear = clear;
        vm.save = save;
        vm.phases = Phase.query();
        vm.projects = Project.query();
        vm.updatedPhases = [];
        vm.cost = 0;
        vm.delimiter = " - ";
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
            $scope.$emit('griphookApp:taskUpdate', result);
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

        $scope.updatePhases = function(project) {
             Phase.query(function(result) {
                            if (result != null ) {
                                result.forEach(function(element) {
                                    console.log("processing phase "+element);
                                    if (element.project.id == project.id) {
                                        //console.log("adding phase "+element.id);
                                        vm.updatedPhases.push(element);
                                        //console.log("jsondata: "+JSON.stringify(vm.viewData));
                                    }
                                })
                            }
                            vm.searchQuery = null;
                        });
            console.log("project phases "+ vm.updatedPhases);
        }

    }
})();
