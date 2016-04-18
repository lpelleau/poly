$(function() {
	var w; // Variable stoquant le worker
	var free = true; // Possibilité de changer l'état du boutton compter

	// Change l'état du bontton Compter en fonction du contenu tu textarea
	var etatBtncompter = function(e) {
		if (!free) {
			return;
		}
		if (e.val().trim() == "") {
			$("#btnCompter").attr("disabled", "");
		} else {
			$("#btnCompter").removeAttr("disabled");
		}
	};

	// Appelé à chaque changement dans le textarea
	$('#saisieText').bind('input propertychange', function() {
		etatBtncompter($('#saisieText'));
	});

	var initPage = function(jetons, pourcentage) {
		// Réinitialisation de la progress-bar et du compteur
		$(".progress-bar").attr("aria-valuenow", pourcentage);
		$(".progress-bar").attr("style", "width:" + pourcentage + "%");
		$("#compteur").text(jetons);
		$("#progress").text(pourcentage);
	};

	// Lors d'un click sur le boutton Compter
	$("#btnCompter").on("click", function() {
		// Suppression des espaces inutiles, si vide : on ne fait rien
		var text = $("#saisieText").val().trim();
		if (text == "") {
			return;
		}

		// Contrôles sur le worker
		if (typeof(Worker) !== "undefined") {
			if (typeof(w) == "undefined") {
				w = new Worker("worker.js");
			}

			initPage(0, 0);
			// Réinitialisation des boutton
			$(".progress-bar").addClass("active");
			$("#btnCompter").attr("disabled", "");
			$("#btnAnnuler").text("Arrêter");
			$("#btnAnnuler").removeAttr("disabled");
			free = false;

			// Si le texte à évaluer est un URL, on récupère le contenu de la cible
			if (/http:\/\/(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/.test(text)) {
				// Donne un warning (deprecated) mais fonctionne
				var req = new XMLHttpRequest();
				req.open("GET", text, false);
				req.send(null);
				text = req.responseText;

				// Ne donne pas de warning mais "data" n'est pas correctement parsé
				//$.ajax(text)
				//		.done(function(data) {
				//			text = data;
				//		});
			}

			// Appel du worker avec le texte à parser
			w.postMessage(text);

			// Sur réception d'un message provenant du worker
			w.onmessage = function(event) {
				initPage(event.data.jetons, event.data.progress);

				// À la fin du traitement, réinitialisation du boutton
				if (event.data.done) {
					$(".progress-bar").removeClass("active");
					$("#btnAnnuler").text("Réinitialiser");
				}
			};

		// Workers non supportés
		} else {
			alert("Sorry! No Web Worker support.");
		}
	});

	// Lors d'un click sur le boutton Compter
	$("#btnAnnuler").on("click", function() {
		// Si le worker n'a pas fini son travail
		if ($("#btnAnnuler").text() == "Arrêter") {
			// Terminaison du worker
			w.terminate();
			w = undefined;

			// Réinitialisation du boutton
			$(".progress-bar").removeClass("active");
			$("#btnAnnuler").text("Réinitialiser");

		// Si le worker a fini son travail
		} else {
			w = undefined;
			initPage(0, 0);
			free = true;

			// Réinitialisation des boutton
			$(".progress-bar").removeClass("active");
			etatBtncompter($('#saisieText'));
			$("#btnAnnuler").text("Arrêter");
			$("#btnAnnuler").attr("disabled", "");
		}
	});
});
