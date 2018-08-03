(function() {
    'use strict';

    angular
        .module('griphookApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('gw-sku-cost', {
            parent: 'entity',
            url: '/gw-sku-cost',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'GW_SKU_COSTS'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/gw-sku-cost/gw-sku-costs.html',
                    controller: 'GW_SKU_COSTController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('gw-sku-cost-detail', {
            parent: 'gw-sku-cost',
            url: '/gw-sku-cost/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'GW_SKU_COST'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/gw-sku-cost/gw-sku-cost-detail.html',
                    controller: 'GW_SKU_COSTDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'GW_SKU_COST', function($stateParams, GW_SKU_COST) {
                    return GW_SKU_COST.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'gw-sku-cost',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('gw-sku-cost-detail.edit', {
            parent: 'gw-sku-cost-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gw-sku-cost/gw-sku-cost-dialog.html',
                    controller: 'GW_SKU_COSTDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GW_SKU_COST', function(GW_SKU_COST) {
                            return GW_SKU_COST.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('gw-sku-cost.new', {
            parent: 'gw-sku-cost',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gw-sku-cost/gw-sku-cost-dialog.html',
                    controller: 'GW_SKU_COSTDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                sku: null,
                                skill: null,
                                cost: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('gw-sku-cost', null, { reload: 'gw-sku-cost' });
                }, function() {
                    $state.go('gw-sku-cost');
                });
            }]
        })
        .state('gw-sku-cost.edit', {
            parent: 'gw-sku-cost',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gw-sku-cost/gw-sku-cost-dialog.html',
                    controller: 'GW_SKU_COSTDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GW_SKU_COST', function(GW_SKU_COST) {
                            return GW_SKU_COST.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('gw-sku-cost', null, { reload: 'gw-sku-cost' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('gw-sku-cost.delete', {
            parent: 'gw-sku-cost',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gw-sku-cost/gw-sku-cost-delete-dialog.html',
                    controller: 'GW_SKU_COSTDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['GW_SKU_COST', function(GW_SKU_COST) {
                            return GW_SKU_COST.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('gw-sku-cost', null, { reload: 'gw-sku-cost' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
