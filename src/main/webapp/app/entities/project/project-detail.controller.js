(function() {
    'use strict';

    angular
        .module('griphookApp')
        .controller('ProjectDetailController', ProjectDetailController);

    ProjectDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Project', 'Phase', 'Task', '$http'];

    function ProjectDetailController($scope, $rootScope, $stateParams, previousState, entity, Project, Phase, Task, $http) {
        var vm = this;

        vm.project = entity;
        vm.previousState = previousState.name;
        vm.phases = [];
        vm.viewData = [];
        loadTasks();
        //loadProjectPhases();
        vm.engTotal = 0;
        vm.pmTotal = 0;
        vm.TotalHours = 0;

        function getTasksByPhaseId (id) {
            //console.log("getting tasks for phase id  "+id)
            return vm.phaseTaskMap[id];
        }

//        async function loadProjectPhases() {
//            await sleep(500);
//            Phase.query(function(result) {
//                if (result != null ) {
//                    result.forEach(function(element) {
//                        //console.log("processing phase "+element);
//                        if (element.project.id == vm.project.id) {
//                            //console.log("adding phase "+element.id);
//                            var phasetasks = vm.phaseTaskMap[element.id];
//                            //console.log("tasks: "+ phasetasks)
//                            var phaseHours = getPhaseHours(phasetasks);
//                            var pst = getPhaseSubtotal(phasetasks);
//                            vm.viewData.push({'id': element.id, 'name': element.name, 'subTotal': pst, 'subTotalWithMargin': element.subTotalWithMargin, 'phaseEstHours': phaseHours, 'tasks': vm.phaseTaskMap[element.id]});
//                            vm.phases.push(element);
//                            //console.log("jsondata: "+JSON.stringify(vm.viewData));
//                        }
//                    });
//                }
//                vm.searchQuery = null;
//            });
//
//        }
        vm.phaseTaskMap = {};
//        async function loadTasks() {
//            Task.query(function(result) {
//                if (result != null ) {
//                    result.forEach(function(element) {
//                        if (!vm.phaseTaskMap[element.phase.id]) {
//                            vm.phaseTaskMap[element.phase.id] = [];
//                        }
//                         //console.log("adding task for phase "+element.phase.id);
//
//                        //console.log("adding task "+element);
//                        vm.phaseTaskMap[element.phase.id].push(element);
//                    });
//                }
//            });
//            //console.log("phase task map "+vm.phaseTaskMap)
//        }


        function sleep(ms) {
            return new Promise(resolve => setTimeout(resolve, ms));
        }

        function getPhaseHours(tasks) {
            var hours = 0;
            angular.forEach(tasks, function (task) {
                hours = hours + task.estimatedHours;
            });
            //console.log("phase hours: "+hours);
            return hours;
        }

        function getPhaseSubtotal(tasks) {
            var pst = 0;
            angular.forEach(tasks, function (task) {
                pst = pst + task.subTotal;
                if (task.resource.indexOf('PROJMNGMT') > 0 ) {
                    vm.pmTotal = vm.pmTotal + task.subTotal;
                } else {
                    vm.engTotal = vm.engTotal + task.subTotal;
                }
            });
            //console.log("phase pst: "+pst);
            return pst;
        }
        $scope.exp = function(id) {
            console.log("getting project report for "+id);
            var url = '/api/projects/'+id+'/report';
            $http.get(url).success(function(data, status, headers, config) {
                //console.log(data);
                var contentDispositionHeader = headers('Content-Disposition');
                var result = contentDispositionHeader.split(';')[1].trim().split('=')[1];
                var filename = result.replace(/"/g, '');
                //console.log(data);
                var csvfile = document.createElement('a');
                csvfile.href = 'data:attachment/csv;charset=utf-8,' + encodeURI(data);
                csvfile.target = '_blank';
                csvfile.download = filename;
                csvfile.click();
                $scope.returned_data=JSON.stringify(data);
                return JSON.stringify(data);
            }).error(function(err, status) {
                //console.log(err);
                //console.log(status);
                $scope.returned_data='Failed to export to csv, Error from server: '+JSON.stringify(err);
                return JSON.stringify(err);
            });
        };
        var unsubscribe = $rootScope.$on('griphookApp:projectUpdate', function(event, result) {
            vm.project = result;
        });
        $scope.$on('$destroy', unsubscribe);

        function loadTasks() {
            var taskUrl = '/api/tasks';
            var phaseUrl = 'api/phases';
            var taskPromise = $http.get(taskUrl);
            var phasePromise = $http.get(phaseUrl);
            taskPromise.then(function(result) {
                                             if (result.data != null ) {
                                                 //console.log("result from all tasks: ", result.data);
                                                 result.data.forEach(function(element) {
                                                     if (!vm.phaseTaskMap[element.phase.id]) {
                                                         vm.phaseTaskMap[element.phase.id] = [];
                                                     }
                                                      //console.log("adding task for phase "+element.phase.id);

                                                     //console.log("adding task "+element);
                                                     vm.phaseTaskMap[element.phase.id].push(element);
                                                 });
                                                 phasePromise.then(function(phaseResult) {
                                                                                   if (phaseResult.data != null ) {
                                                                                       //console.log("result from all phases: ", phaseResult.data);
                                                                                       phaseResult.data.forEach(function(element) {
                                                                                           //console.log("processing phase "+element);
                                                                                           if (element.project.id == vm.project.id) {
                                                                                               //console.log("adding phase "+element.id);
                                                                                               var phasetasks = vm.phaseTaskMap[element.id];
                                                                                               //console.log("tasks: "+ phasetasks)
                                                                                               var phaseHours = getPhaseHours(phasetasks);
                                                                                               var pst = getPhaseSubtotal(phasetasks);
                                                                                               vm.viewData.push({'id': element.id, 'name': element.name, 'subTotal': pst, 'subTotalWithMargin': element.subTotalWithMargin, 'phaseEstHours': phaseHours, 'tasks': vm.phaseTaskMap[element.id]});
                                                                                               vm.phases.push(element);
                                                                                               //console.log("jsondata: "+JSON.stringify(vm.viewData));
                                                                                           }
                                                                                       });
                                                                                   }
                                                                                   vm.searchQuery = null;
                                                                               });
                                             }
                                         });
        }
    }
})();
