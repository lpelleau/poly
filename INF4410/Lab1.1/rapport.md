---
papersize: letter
geometry: margin=1.3in
documentclass: scrartcl
title: INF4410 - Lab1
lang: french
abstract: "Résumé : Réponse aux questions et compte-rendu du lab 1 sur Java RMI."
author:
 - Loïc Pelleau 1810978
 - Quentin Dufour 1811002

---

# Partie 1
## Question 1

![Graphique généré à partir des données](graph.png)

| Taille    | Appel local | RMI local | RMI distant |
|-----------|-------------|-----------|-------------|
| 10        | 8620        | 2278324   | 152318256   |
| 100       | 9697        | 2236265   | 657823240   |
| 1000      | 9825        | 2165823   | 550674703   |
| 10000     | 10035       | 2345424   | 444599684   |
| 100000    | 10471       | 2318399   | 552795819   |
| 1000000   | 21897       | 2738688   | 1080150108  |
| 10000000  | 144785      | 5691092   | 3758459549  |
| 100000000 | 4749853     | 34119395  | 10661048272 |


On constate que les appels RMI sont plus lents, particulièrement les appels distants. Pour un argument de 10 octets, un appel RMI local est 1 000 fois plus lent qu'un appel local, et un appel RMI distant est un million de fois plus lent que le même appel local.

Qui plus est, le temps augmente significativement lors de l'augmentation de la taille des paquets. Le dernier appel RMI distant est très lent par exemple (10 secondes).

RMI ou un autre système RPC semblable est intéressant quand il s'agit de partager la charge de calcul sur plusieurs machines, que les données transférées restent en quantité mesurée, et enfin qu'une réponse instantanée n'est pas nécessaire, comme du calcul distribué.

A contrario, un système faisant transiter beaucoup de données, et ayant de fortes contraintes temporelles, comme un ordinateur au sein d'un avion, Java RMI pourrait alors être une moins bonne solution.

## Question 2

Le diagramme de séquence présentant les différents échanges à partir du lancement du serveur est présenté en Figure 2.

![Diagramme de séquence entre les différents acteurs](mcsolaar-rmi.png)

\newpage

# Partie 2

## Directory

Cette classe représente un dossier virtuel dans le serveur.    
Un `hashMap` stocke les fichiers virtuels avec pour clé le nom du fichier et comme contenu associé le contenu de la classe `FileDir`.

## FileDir

`FileDir` contient toutes les informations nécessaires sur un fichier : son nom, son contenu, le verrou...

## Server

La classe `Server` contient un `Directory` afin de garder en mémoire tous les fichiers virtuels.    
Une liste des identifiants des clients est aussi gardée à jour afin que aucun client n'ait le même identifiant.

## Protection des variables partagées

Protection seulement pour les actions en écriture : GenerateClientId, Lock et Push.
