(function() {
    'use strict';

    angular
        .module('augustusApp')
        .controller('ProjectDetailController', ProjectDetailController);

    ProjectDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Project', 'Phase', 'Task'];

    function ProjectDetailController($scope, $rootScope, $stateParams, previousState, entity, Project, Phase, Task) {
        var vm = this;

        vm.project = entity;
        vm.previousState = previousState.name;
        vm.phases = [];
        vm.viewData = [];
        loadTasks();
        loadProjectPhases();
        vm.engTotal = 0;
        vm.pmTotal = 0;
        vm.TotalHours = 0;

        function getTasksByPhaseId (id) {
            console.log("getting tasks for phase id  "+id)
            return vm.phaseTaskMap[id];
        }

        async function loadProjectPhases() {
            await sleep(500);
            Phase.query(function(result) {
                if (result != null ) {
                    result.forEach(function(element) {
                        console.log("processing phase "+element);
                        if (element.project.id == vm.project.id) {
                            console.log("adding phase "+element.id);
                            var phasetasks = vm.phaseTaskMap[element.id];
                            console.log("tasks: "+ phasetasks)
                            var phaseHours = getPhaseHours(phasetasks);
                            var pst = getPhaseSubtotal(phasetasks);
                            vm.viewData.push({'id': element.id, 'name': element.name, 'subTotal': pst, 'subTotalWithMargin': element.subTotalWithMargin, 'phaseEstHours': phaseHours, 'tasks': vm.phaseTaskMap[element.id]});
                            vm.phases.push(element);
                            console.log("jsondata: "+JSON.stringify(vm.viewData));
                        }
                    })
                }
                vm.searchQuery = null;
            });

        }
        vm.phaseTaskMap = {};
        async function loadTasks() {
            Task.query(function(result) {
                if (result != null ) {
                    result.forEach(function(element) {
                        if (!vm.phaseTaskMap[element.phase.id]) {
                            vm.phaseTaskMap[element.phase.id] = [];
                        }
                         console.log("adding task for phase "+element.phase.id);

                        console.log("adding task "+element);
                        vm.phaseTaskMap[element.phase.id].push(element);
                    })
                }
            });
            console.log("phase task map "+vm.phaseTaskMap)
        }


        function sleep(ms) {
          return new Promise(resolve => setTimeout(resolve, ms));
        }

        function getPhaseHours(tasks) {
            var hours = 0;
            angular.forEach(tasks, function (task) {
                hours = hours + task.estimatedHours;
            })
            console.log("phase hours: "+hours);
            return hours;
        }

        function getPhaseSubtotal(tasks) {
            var pst = 0;
            angular.forEach(tasks, function (task) {
                pst = pst + task.subTotal;
                if (task.resource.indexOf('ENG') > 0 ) {
                    vm.engTotal = vm.engTotal + task.subTotal;
                } else if (task.resource.indexOf('PM') > 0 ) {
                    vm.pmTotal = vm.pmTotal + task.subTotal;
                }
            })
            console.log("phase pst: "+pst);
            return pst;
        }

        var unsubscribe = $rootScope.$on('augustusApp:projectUpdate', function(event, result) {
            vm.project = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
