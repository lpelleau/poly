var map;
var marker;

var initMap = function() {
	// Initialise la carte Google Map centrée sur Montréal
	map = new google.maps.Map(document.getElementById("map"), {
		center: { lat: 45.5, lng: -73.550003 }, 
		zoom: 5
	});
	
	// Ajout du listener pour changement du zoom afin de l'accorder avec le Slider
	map.addListener("zoom_changed", function() {	
		$("#slider").slider("value", map.getZoom());
	});

	// Création du Marker
	marker = new google.maps.Marker();
}

$(document).ready(function() {
	var langs = [ 'fr', 'en' ]; // Langues disponibles
	var lang = -1; // La première langue affichée sera la première du tableau
	var maxCitiesToDisplay = 5; // Limite le nombre de réponses dans les propositions de ville

	$("#selected").fadeOut(0);

	// Changement de langue
	var langsJSON = { 'fr' : undefined, 'en' : undefined };
	// Function servant de proxy à getJSON permettant 
	// d'éviter des requêtes au serveur inutilement
	function getLang(lang, callback) {
		if (langsJSON[lang] == undefined) {
			$.getJSON('lang/' + lang + '.json', function(data) {
				langsJSON[lang] = data;
				callback(langsJSON[lang]);
			});
		} else {
			callback(langsJSON[lang]);
		}
	};
	$("#lang button").click(function() {
		// On va chercher la langue suivante
		// retour au début si on a atteint la fin
		lang +=1;
		lang %= langs.length;

		// On applique la traduction avec le fichier json de langue sélectionnée
		getLang(langs[lang], translate);
	});

	// From https://github.com/dakk/jquery-multilang
	// Insère le texte de la langue sélectionnée sur les éléments à attribut "tkey"
	function translate(data) {
		$("[tkey]").each(function(index) {
			var strTr = data[$(this).attr('tkey')];
			$(this).text(strTr);
		});
	};

	// On simule le premier click sur le bouton changement de langue
	$("#lang button").trigger("click");

	var citiesData;
	// Function servant de proxy à getJSON permettant 
	// d'éviter des requêtes au serveur inutilement
	function getCities(callback) {
		if (citiesData == undefined) {
			$.getJSON('villes.json', function(data) {
				citiesData = data;
				callback(citiesData);
			});
		} else {
			callback(citiesData);
		}
	};

	// Initialisation du composant d'autocompletion
	$("#city").autocomplete({
		source: function(request, response) {
			// Lors d'un changement dans le champ de saisie

			var re = new RegExp("(-?[0-9]{1,3}(\.[0-9]+)?); *(-?[0-9]{1,3}\(.[0-9]+)?)$");
			var m = re.exec(request.term);
			
			// Si on recherche une latitude; longitude...
			if (m) {
				// On place directement la Map/Marker/Zoom à la bonne position
				map.panTo(
					new google.maps.LatLng(
						m[1],  
						m[3]));
				map.setZoom(10);
        			google.maps.event.trigger(map, "resize");
				marker.setMap(map);

				marker.setPosition(
					new google.maps.LatLng(
						m[1], 
						m[3]));
				
				$("#slider").slider("value", 10);

			// Sinon...
			} else {
				// On récupère les villes disponibles
				getCities(function(data) {
					var cities = [];
					$.map(data, function(prop, key) {
						// On les ajoute dans le tableau utilisé pour
						// l'affichage si la ville correspond à la saisie
						// et qu'on a pas dépassé le nombre de résultats
						if (new RegExp(request.term, "i").exec(key) && cities.length < maxCitiesToDisplay) {
							cities.push({ "label": key, "value": data[key] });
						}
					});
					response(cities);
				});
			}
		}, 
		select: function(event, ui) {
			// Lors de la sélection on place le label
			// et non la value (qui est lat; lng)
			$(this).val(ui.item.label);
			
			// On affiche la boite de confirmation avec la ville sélectionnée
			$("#selected strong").text(ui.item.label);
			$("#selected").fadeIn();
			// Qui disparait au bout de 2sc
			setTimeout(function() {
				$("#selected").fadeOut();
			}, 2000);

			// On place ensuite la Map/Marker/Zoom à la bonne position
			map.panTo(
				new google.maps.LatLng(
					ui.item.value.lat, 
					ui.item.value.lon));
			map.setZoom(10);
      			google.maps.event.trigger(map, "resize");
				
			$("#slider").slider("value", 10);

			marker.setMap(map);
			marker.setPosition(
				new google.maps.LatLng(
					ui.item.value.lat, 
					ui.item.value.lon));
			marker.setTitle(ui.item.label);

			// On retourne false pour arrêter le comportement par défaut
			return false;
		}, 
		focus: function(event, ui) {
			// Lors de la sélection avec le clavier on place le label
			// et non la value (qui est lat; lng)
			$(this).val(ui.item.label);

			// On retourne false pour arrêter le comportement par défaut
			return false;
		}
	});

	// Initialisation du Slider
	$("#slider").slider({
		min: 1, // Zoom min
		max: 20, // Zoom max
		step: 0.1, // Précision du Slider
		value: 5, // Valeur par défaut
		slide: function(event, ui) {
			// Lorsque l'utilisateur utlise le Slider
			// la valeur met à jour le zoom de la carte
			map.setZoom(ui.value);
      			google.maps.event.trigger(map, "resize");
		}
	});
});

