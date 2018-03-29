(function() {
    'use strict';

    angular
        .module('augustusApp')
        .controller('PhaseController', PhaseController);

    PhaseController.$inject = ['Phase'];

    function PhaseController(Phase) {

        var vm = this;

        vm.phases = [];

        loadAll();

        function loadAll() {
            Phase.query(function(result) {
                vm.phases = result;
                vm.searchQuery = null;
            });
        }
    }
})();
