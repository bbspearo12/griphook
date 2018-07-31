(function() {
    'use strict';

    angular
        .module('griphookApp')
        .controller('ProjectCloneController', ProjectCloneController);

    ProjectCloneController.$inject = ['$timeout', '$scope', '$rootScope', '$stateParams', '$uibModalInstance', 'entity', 'Project', 'Phase', 'Task', '$http'];

    function ProjectCloneController ($timeout, $scope, $rootScope, $stateParams, $uibModalInstance, entity, Project, Phase, Task, $http) {
        var vm = this;

        vm.project = entity;
        vm.clear = clear;
        vm.save = save;
        vm.phases = [];

        var oldProjectId = vm.project.id;
        vm.project.id = "";

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
        	console.log("cloning project ", oldProjectId);
        	var url = '/api/projects/'+oldProjectId+'/clone/'+vm.project.name;
            $http.post(url).success(function(data, status, headers, config) {
            	onSaveSuccess(data);
            }).error(function(err, status) {
            	console.log(err);
            	console.log(status);
            	$scope.returned_data='Failed to clone project, Error from server: '+JSON.stringify(err);
            	return JSON.stringify(err);
            });

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
        };
    }
})();
