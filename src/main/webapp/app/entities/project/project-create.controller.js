(function() {
    'use strict';

    angular
        .module('griphookApp')
        .controller('ProjectCreateController', ProjectCreateController);

    ProjectCreateController.$inject = ['$window', '$timeout', '$scope', '$stateParams', 'entity', 'Project', 'Phase', 'Task'];

    function ProjectCreateController($window, $timeout, $scope, $stateParams, entity, Project, Phase, Task) {
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
            Project.save(vm.project, onSaveSuccess, onSaveError);
        }

        function onSaveSuccess (result) {
//            $scope.$emit('griphookApp:projectUpdate', result);
//            vm.isSaving = false;
              //var projectid = result.id;
              console.log("saved project id: "+result.id +" with name: "+ result.name);
              savePhase(result);
        }

        function onSaveError () {
            vm.isSaving = false;
        }
        $scope.phases = [{id: 'project_kickoff', name: 'Project Kickoff', Tasks: [{id: '1'}]},
                            {id: 'discovery', name: 'Discovery', Tasks: [{id: '1'}]},
                            {id: 'staging', name: 'Staging', Tasks: [{id: '1'}]},
                            {id: 'deployment', name: 'Deployment', Tasks: [{id: '1'}]},
                            {id: 'installation', name: 'Installation', Tasks: [{id: '1'}]},
                            {id: 'go_live_support', name: 'Go Live Support', Tasks: [{id: '1'}]},
                            {id: 'documentation', name: 'Documentation / KT', Tasks: [{id: '1'}]}];
        $scope.addTask = function (phaseid) {
//            console.log("adding task to phase ", phaseid);
//            var i;
//            for (i = 0; i < $scope.phases.length; i++) {
//                if ($scope.phases[i].id == phaseid) {
//                    console.log("phase: "+$scope.phases[i].name+" tname: "+taskname+" ehours: "+ehours+" resource: "+ resource+" nofr: "+ nofr);
//                    //var st = resource * cost * ehours;
//                    $scope.phases[i].Tasks.unshift({id: $scope.phases[i].Tasks.length+1, tname: taskname, estimatedHours: ehours, resource: resource, numberOfResources: nofr, cost: cost, subTotal: st});
//                }
//            }
             var i;
             for (i = 0; i < $scope.phases.length; i++) {
                 if ($scope.phases[i].id == phaseid) {
                    var newTask = $scope.phases[i].Tasks.length+1;
                    $scope.phases[i].Tasks.push({'id': newTask})
                 }
             }
        }
         $scope.removeTask = function(phaseid) {
            for (i = 0; i < $scope.phases.length; i++) {
                if ($scope.phases[i].id == phaseid) {
                    var lastItem = $scope.phases[i].Tasks.length-1;
                    $scope.phases[i].Tasks.splice(lastItem);
                }
            }
          };

        function savePhase(project) {
            var i;
            for (i = 0; i < $scope.phases.length; i++) {
                if ($scope.phases[i].Tasks.length > 0) {
                    var phasevm = getPhaseEntity($scope.phases[i].name, project)
                    console.log("working on phase: "+ $scope.phases[i].name);
                    Phase.save(phasevm, onPhaseSaveSuccess, onPhaseSaveError);
                }
            }

        }

        function getPhaseEntity(phasename, projectentity) {
          return {
              name: phasename,
              project: projectentity
          };
        }

        function onPhaseSaveSuccess(phaseresult) {
            console.log("saved phase, id: "+phaseresult.id);
            saveTasks(phaseresult);
        }

        function onPhaseSaveError() {
            console.log("could not save phase");
        }

        function saveTasks(phase) {
            var i;
            for (i = 0; i < $scope.phases.length; i++) {
                if ($scope.phases[i].name == phase.name) {
                    console.log("saving tasks for phase: "+ phase.name);
                    var j;
                    for (j = 0; j< $scope.phases[i].Tasks.length; j++) {
                        console.log("working on task: "+$scope.phases[i].Tasks[j].tname);
                        if ($scope.phases[i].Tasks[j].tname != null) {
                            console.log("task name not null: "+$scope.phases[i].Tasks[j].tname);
                            var t = $scope.phases[i].Tasks[j];
                            var taskentity = getTaskEntity(t.tname, t.ehours, t.resource, t.cost, t.nofr, phase);
                            Task.save(taskentity, onTaskSaveSuccess, onTaskSaveError);
                        }
                    }
                }
            }
            $window.location.href = '/#/project';
        }
        function getTaskEntity(tname, ehours, resource, cost, nofr, phaseEntity) {
            return {
                name: tname,
                estimatedHours: ehours,
                resource: resource,
                cost: cost,
                numberOfResources: nofr,
                subTotal: nofr * cost * ehours,
                phase: phaseEntity
            };
        }


        function onTaskSaveSuccess(taskresult) {
            console.log("saved task id: "+taskresult.id+" name: "+taskresult.name);
            return;
        }

        function onTaskSaveError() {
            console.log("failed to save task");
        }


        $scope.updateCost = function(resource) {
            return resourceCost[resource];
        }
        var resourceCost = {
             GW_PS_NET_ENG3_SR_ARCHITECT :  185,
             GW_PS_NET_ENG2_ARCHITECT :  145,
             GW_PS_NET_ENG1_ENGINEER :  100,
             GW_PS_STORAGE_ENG3_SR_ARCHITECT :  185,
             GW_PS_STORAGE_ENG2_ARCHITECT :  145,
             GW_PS_STORAGE_ENG1_ENGINEER :  100,
             GW_PS_VIRT_ENG3_SR_ARCHITECT :  185,
             GW_PS_VIRT_ENG2_ARCHITECT :  145,
             GW_PS_VIRT_ENG1_ENGINEER :  100,
             GW_PS_MS_ENG3_SR_ARCHITECT :  185,
             GW_PS_MS_ENG2_ARCHITECT :  145,
             GW_PS_MS_ENG1_ENGINEER :  100,
             GW_PS_SEC_ENG3_SR_ARCHITECT :  185,
             GW_PS_SEC_ENG2_ARCHITECT :  145,
             GW_PS_SEC_ENG1_ENGINEER :  100,
             GW_PS_CLD_ENG3_SR_ARCHITECT :  185,
             GW_PS_CLD_ENG2_ARCHITECT :  145,
             GW_PS_CLD_ENG1_ENGINEER :  100,
             GW_PS_INT_LEVEL3 :  185,
             GW_PS_INT_LEVEL2 :  100,
             GW_PS_INT_LEVEL1 :  50,
             GW_PS_PM_LEVEL2_PROJECT_MANAGER :  185,
             GW_PS_PM_LEVEL1_PROJECT_MANAGER :  150,
             GW_PS_PC_PROJECT_COORDINATOR :  100
        }
    }
})();
