---
papersize: letter
geometry: margin=1.3in
documentclass: scrartcl
title: INF4710 - Lab2
lang: french
abstract: "Résumé : Analyse du pipeline JPEG."
author:
 - Loïc Pelleau 1810978
 - Quentin Dufour 1811002

---

# DCT / IDCT

La perte d'information est négligeable. La différence apparait noire.
Pour aller plus loin, nous avons compter les pixels différents entre l'image d'origine et l'image encodée
pour chaque image du jeu de test, et on obtient : `1057, 458, 752, 803, 0, 191`.

On constate en effet, que le nombre de pixels différents est très faible par rapport à la taille de l'image.
Cela est du au fait que nous n'avons pas réalisé de compression, et que la parte d'information se situe au niveau des différentes
approximations réalisées : cosinus, PI, et conversions (particulièrement float).

# Quantification

La compression est réalisée lors de la quantification.
Pour mesurer les pertes de qualité, nous avons calculé la matrice de différence entre les deux images, puis avons sommé ses valeurs.
Ainsi, si le point (x=5, y=6) a une différence de 2 pixels entre l'image de départ et d'arrivée, nous avons ajouté +2 à notre indicateur.

Voici les valeurs pour les différentes valeurs de compression.

| n | 90%  | 50%  | 10%   | 
|---|------|------|-------| 
| 1 | 1057 | 3171 | 6342  | 
| 2 | 2082 | 4737 | 10142 | 
| 3 | 1636 | 3412 | 7373  | 
| 4 | 803  | 1606 | 4818  | 
| 5 | 0    | 0    | 0     | 
| 6 | 323  | 643  | 997   | 


# Méthodologie de calcul de la compression

Nous avons multiplié le nombre de canaux par le nombre de pixels par canaux pour obtenir le nombre totaux de pixels de l'image.
Nous avons multiplié le tout par 8 pour obtenir la taille en bit théorique de l'image, car un char non signé a une taille théorique de 8 bits.
 
Pour ce qui est de l'image encodée, pour chaque bloc, nous avons :

  * récupéré la taille du code
  * récupéré la taille de la map

Nous avons multiplié ces valeurs par 1, car un booléen peut être encodé sur 1 bit.

On notera que ces calculs ne représentent pas vraiment la taille réellement occupée en mémoire. 
En effet, pour des raisons matériel, notre implémentation oblige l'ordinateur à stocker ces valeurs sur des tailles plus grandes.
Cependant, cette analyse est plus pertinente, car dans le cas d'une implémentation réelle, il serait facilement possible de préciser la taille occupée par nos booléens.

# Analyse de la compression

## Compression avec une quantification à 90%

| n |  taille originale |  taille compressée |  ratio   | 
|---|-------------------|--------------------|----------| 
| 1 |  2.23027e+06      |  240537            |  9.27205 | 
| 2 |  819200           |  299914            |  2.73145 | 
| 3 |  6.29146e+06      |  1.02067e+06       |  6.16406 | 
| 4 |  3.84e+06         |  650079            |  5.90697 | 
| 5 |  1.57286e+06      |  123068            |  12.7804 | 
| 6 |  1.57286e+06      |  176353            |  8.91884 | 

## Compression avec une quantification à 50%

| n |  taille originale |  taille compressée |  ratio   | 
|---|-------------------|--------------------|----------| 
| 1 | 2.23027e+06       |  113648            |  19.6244 | 
| 2 | 819200            |  135435            |  6.04866 | 
| 3 | 6.29146e+06       |  380540            |  16.533  | 
| 4 | 3.84e+06          |  304173            |  12.6244 | 
| 5 | 1.57286e+06       |  68872             |  22.8375 | 
| 6 | 1.57286e+06       |  76051             |  20.6817 | 

## Compression avec une quantification à 10%

| n |  taille originale |  taille compressée |  ratio   | 
|---|-------------------|--------------------|----------| 
| 1 | 2.23027e+06       |  51429             |  43.366  | 
| 2 | 819200            |  46051             |  17.789  | 
| 3 | 6.29146e+06       |  171824            |  36.6157 | 
| 4 | 3.84e+06          |  130455            |  29.4354 | 
| 5 | 1.57286e+06       |  35238             |  44.6355 | 
| 6 | 1.57286e+06       |  35033             |  44.8966 | 

## Conclusion

Plus la compression est forte, plus la diminution de la taille de l'image est importante.
Ainsi, si on a un gain de l'ordre de 7X pour une qualité de 90%, on grimpe à 15X pour 50% et même 40X pour 10%.
Toutes les images ne sont pas égales face à la compression. Particulièrement l'image 2, qui contient beaucoup de contours.
Lors de la DCT, les hautes fréquences ne sont plus alors négligeables, et donc amoindrissent la compression.

