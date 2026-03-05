# Fonctionnement de la lecture des interactions

Pour la lecture d'interactions, le code fonctionne de deux manières différentes
dans le code :
1. La première manière est la plus naturelle, elle gère l'interaction avec la grille en jeu 
    et permet de lire les interactions de manière classique sans besoin précis. On passe en paramètre de GridController un 
    objet Interaction sans être dans le mode éditeur. On va donc lire l'interaction normalement avec un traitement des coordonnées
    pour placer et gérer le drag and drop.
2. La deuxième manière est plus complexe et en plusieurs étapes, elle concerne l'éditeur de niveau. La vue de l'éditeur 
    possède un écouteur sur le panel qui gère chaque bouton de cellule ou d'interaction. Lorsqu'un bouton est pressé le 
    controller va alors récupérer les informations de la vue qui ont changé pour en gros récupérer l'interaction qui est 
    demandé par le joueur. Lorsqu'il a récupéré les informations le controller va appeler la fonction selectCell qui va 
    donc changer les informations que l'interaction doit transmettre (si une cellule doit être placé, tourné, supprimé ect...).
    Ensuite, on peut changer les interactions et simuler un jeu si on le veut.