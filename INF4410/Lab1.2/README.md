### Instructions pour le lancement du projet

1. Lancer le script `./launch.sh`    
    Ce script compile le projet (`ant`)    
    Il lance `rmiregistery &` depuis le dossier bin/ s'il n'est pas deja lancé    
    Il lance le server (`./server`)    

2. Lancer le client `./client`    
    Une aide sera ensuite affichée avec toutes les commandes disponibles   

Voici les commandes disponibles :    

* ./client create <file_name>    
* ./client list    
* ./client syncLocalDir    
* ./client get <file_name>    
* ./client lock <file_name>    
* ./client push <file_name>    
* ./client  help    

Un lock n'est effectif que 3 minutes.    
Si le délai est dépassé et que l'utilisateur effectue un nouveau lock, le contenu du fichier local est remplacé par celui du serveur.    
L'utilisateur aura donc perdu les modifications faites entre les deux lock.

L'identifiant du client (un entier unique) est stocke dans le fichier .duf dans le repertoire courrant.

