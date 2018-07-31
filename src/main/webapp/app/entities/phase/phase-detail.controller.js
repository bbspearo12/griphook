(function() {
    'use strict';

    angular
        .module('griphookApp')
        .controller('PhaseDetailController', PhaseDetailController);

    PhaseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Phase', 'Project', 'Task'];

    function PhaseDetailController($scope, $rootScope, $stateParams, previousState, entity, Phase, Project, Task) {
        var vm = this;

        vm.phase = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('griphookApp:phaseUpdate', function(event, result) {
            vm.phase = result;
        });
        $scope.$on('$destroy', unsubscribe);

        function doTheBack() {
            window.history.back();
        }
    }
})();
