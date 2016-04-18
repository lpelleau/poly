angular.module("app", []) // JavaScript application

  .controller("MenuController", function($scope, $http, $sce) { // Controller managing the menu and it's items
		$http.get("menu.json").then(function(response) {
      $scope.items = response.data.Navigation; // Just recopy the HTTP response in the variable items

      // Create Menu and sub menu (used in bonus page)
      $scope.items2 = [];
      $scope.subitems2 = [];
      var lastItem = null;
      angular.forEach(response.data.Navigation, function (item, index, menuTab) {
        if (item.Lien == 0) { // If it's a category
          lastItem = item;
          $scope.items2.push(item); // add item to the list
        } else {
          if (lastItem == null) { // The firsts links are categories
            $scope.items2.push(item);
          } else { // Add sub items
            if ($scope.subitems2[lastItem.Nom] == null) {
              $scope.subitems2[lastItem.Nom] = [];
            }
            $scope.subitems2[lastItem.Nom].push(item);
          }
        }
      });
      $scope.items2.shift();
    });
  })

  .controller("SeancesController", function($scope, $http, $sce) { // Controller managing the seance tab
		$http.get("seances.json").then(function(response) {
      $scope.seances = angular.forEach(response.data, function (seance, index, seancesTab) {
				seancesTab[index] = angular.forEach(seance, function(valeur, cle, valeursTab) {
					valeursTab[cle] = $sce.trustAsHtml(valeur.trim()); // Add value in array as HTML and without spaces at start and end
				});
			});
    });
  });
