
// Import du script comptant les jetons
self.importScripts("CompteurJetons.js");
var compteur = new CompteurJetons();

// À la réception d'un text sur lequel travailler
self.addEventListener('message', function(e) {
	// Données à renvoyer à l'appelant
	var data = [];
	data["done"] = false;

	// Appel de la fonction
	var g = compteur.compterJetons(e.data);

	// À chaque "yield" rencontré
	while(!g.next().done) {
		// Mise à jour et envoi à l'appelant des données
		data["jetons"] = compteur.getJetons();
		data["progress"] = compteur.getProgress();
		postMessage(data);
	}

	// Notification de l'appelant que le travail est terminé
	data["done"] = true;
	postMessage(data);
}, false);
