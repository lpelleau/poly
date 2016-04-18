---
papersize: letter
geometry: margin=1.3in
documentclass: scrartcl
title: INF4410 - Lab3
lang: french
abstract: "Résumé : Réponse aux questions et compte-rendu du lab 3 sur OpenStack."
author:
 - Loïc Pelleau 1810978
 - Quentin Dufour 1811002

---

# Question 1 - Description de composants OpenStack
## 1. Heat
*HEAT* est le composant de *OpenStack* permettant d'instancier à partir d'un script hautement paramétrable des machines virtuelles. Ce script s'appel HOT (**H**eat **O**rchestration **T**emplate) et doit être écrit au format yaml.  

Ce fichier décrit la relation entre chaque composants du groupe qui sera créé.

Ce script peut demander certaines valeurs à l'utilisateur lors de son execution, comme les paires de clés à ajouter aux serveurs.

## 2. Neutron

*Neutron* sert à gérer le(s) réseau(x). Neutron permet entre autre d'assigner des adresses IP statiques ou dynamiques, et possède aussi un service d'adresse IP flottante (pour assigner des adresses IP publiques).

De plus, Neutron met à disposition des utilisateurs des groupe de sécurité et leur permet de contrôller le traffic au sein des réseaux créés.

## 3. Nova

Nova gère les ressources de calcul des serveurs. Il sert à répartir la charge des machines virtuelles sur le matériel. C'est donc Nova qui gère les hyperviseurs comme KVM, Xen, Hyper-V, etc.

# Question 2 - Description des ressources utilisées

## 1. OS::Heat::RessourceGroup

Nous avons utilisé *RessourceGroup* afin de lancer plusieurs instances de serveur avec la même configuration.

## 2. OS::Neutron::HealthMonitor
La ressource *HealthMonitor* a été utile afin de paramétrer la vérification de l'état de nos serveurs.

## 3. OS::Neutron::Pool

*Pool* permet de choisir l'algorithme de sélection qui sera utilisé sur notre *RessourceGroup*.

## 4. OS::Neutron::LoadBalancer

Le *LoadBalancer* fait le lien entre notre *Pool* (paramétrage de requête) et notre *RessourceGroup* (ensemble de serveurs). Ainsi le load balancer va appliquer le comportement indiqué par le Pool sur notre ResourceGroup.

## 5. OS::Nova::Server

*Server* nous permets de déclarer des instances qui fonctionneront dans des machines virtuels à partir d'images existantes configurées selon nos souhaits.

Nous avons pu lui assigner un réseau, ajouter un script de configuration au premier démarrage, ainsi que de nombreux autres paramètres propre au serveur.

# Question 3 - Allocation dynamique de ressources

1. Le nom du composant permettant de modifier dynamiquement le nombre d'instances de serveur est appelé `OS::AutoScale::ScalingGroup`.

2. `OS::Ceilometer::Alarm` permets de déclencher des évènements en fonction de l'utilisation des ressources.  
   `OS::Heat::ScalingPolicy` permet d'ajuster le nombre de machines en fonction de ces alertes.
