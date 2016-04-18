Pour executer les commandes ci-dessous, il faut se trouver dans le dossier du rendu.  

# Lancement de tests sécurisés
Ce teste lance les serveurs en q1=2, q2=4, q3=8.  
Pour lancer d'autres, il faut changer le 1 paramètre des 'slaves'.  

Depuis L4712-01.info.polymtl.ca:  
  ant
  cd bin/
  rmiregistry 5001 &

  cd ..
  ./master ressources/donnees-4172.txt L4712-01.info.polymtl.ca:5001 secured

Depuis L4712-02.info.polymtl.ca:  
  ./slave 2 0 L4712-01.info.polymtl.ca:5001

Depuis L4712-03.info.polymtl.ca:  
  ./slave 4 0 L4712-01.info.polymtl.ca:5001

Depuis L4712-04.info.polymtl.ca:  
  ./slave 8 0 L4712-01.info.polymtl.ca:5001

# Lancement de tests non-sécurisés
Ce teste lance les serveurs en q1=2, q2=4, q3=8.  
Pour lancer d'autres, il faut changer le 1 paramètre des 'slaves'.  
Ici, c'ets le 1er serveur qui est malicieux à 50%. Pour changer cela, il faut modifier le second paramètre des slaves.  

Depuis L4712-01.info.polymtl.ca:  
  ant
  cd bin/
  rmiregistry 5001 &

  cd ..
  ./master ressources/donnees-4172.txt L4712-01.info.polymtl.ca:5001 unsecured

Depuis L4712-02.info.polymtl.ca:  
  ./slave 2 50 L4712-01.info.polymtl.ca:5001

Depuis L4712-03.info.polymtl.ca:  
  ./slave 4 0 L4712-01.info.polymtl.ca:5001

Depuis L4712-04.info.polymtl.ca:  
  ./slave 8 0 L4712-01.info.polymtl.ca:5001
