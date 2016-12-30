/**
 * Created by Nicholas_Wang on 2016/11/23.
 */
app = angular.module('testApp',[]);
app.controller('testCtrl', function ($scope, $http) {

    var url = rootUrl + 'getDodgeTollAuditInfo?count=10';
    $http.get(url).success(function (response) {
        $scope.carInfoArr = response;
    });

    $scope.$on('ngRepeatFinished', function (ngRepeatFinishedEvent) {
        //下面是在table render完成后执行的js

        $(function(){
            $('.dowebok').vTicker({
                showItems: 4,
                pause: 3000
            });
        });

    });


});
app.directive('onFinishRenderFilters', function ($timeout) {
    console.log('invoke directive');
    return {
        restrict: 'A',
        link: function (scope, element, attr) {
            console.log('scope last: ' + scope.$last);
            if (scope.$last === true) {
                $timeout(function () {
                    scope.$emit('ngRepeatFinished');
                });
            }
        }
    };
});

