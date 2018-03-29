(function() {
    'use strict';

    angular
        .module('augustusApp')
        .controller('GW_SKU_COSTDetailController', GW_SKU_COSTDetailController);

    GW_SKU_COSTDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'GW_SKU_COST'];

    function GW_SKU_COSTDetailController($scope, $rootScope, $stateParams, previousState, entity, GW_SKU_COST) {
        var vm = this;

        vm.gW_SKU_COST = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('augustusApp:gW_SKU_COSTUpdate', function(event, result) {
            vm.gW_SKU_COST = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
