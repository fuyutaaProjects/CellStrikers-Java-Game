# Manuel des nouvelles cellules ajouté au projet

1. ![""](/src/resources/texture/flipper.png "Le switcher"). Le switcher:

Il échange s'il le peut les cellules en face et derrière lui si elles existent. Dans le cas où plusieurs switcher iraient s'échanger mutuellement, ce sera celui qui sera le plus proche du coin supérieur gauche de la grille de commencer. 

2. ![""](/src/resources/texture/pig.png "Le wanderer"). Le wanderer:

Un ennemi spécial qui est non-statique. Il se déplace sur la grille vers une direction, cette direction est déterminée comme étant la direction qui lui permettra de se déplacer le plus longtemps au cycle où elle a été calculé, si elle n'a pas été calculé c'est la direction du wanderer lors de sa création. <br>
Si dans une direction un mover fonce sur lui: il n'y va pas. Si ce mover se trouve en face de lui, et qu'au prochain tour il sera trop tard pour l'éviter, alors la cellule va sur une case adjacente libre peu importe laquelle tant que ce mover ne risque pas de la toucher. <br>
Le wanderer peut se faire éliminer par des cellules arrivant sur lui par les côtés ou en face de lui, un mover se situant en face de lui qui avance vers lui en poussant une grande chaîne de cellule peut aussi avoir raison du wanderer. <br>
La direction où la cellule va si elle ne se sent pas en danger peut être changer avec un spinner.

3. ![""](/src/resources/texture/nudge.png "Le nudge"). Le nudge:

Une cellule agissant comme un mover, mais elle ne peut pousser que les trash et les ennemis de ce fait : si deux nudge se croisent ils ne bougent pas

4. ![""](/src/resources/texture/repulse.png "Le repulse"). Le repulse:

Une cellule qui a chaque cycle repousse d'une case les cellules adjacentes à elle. Elle peut repousser toute une chaîne de cellule. Cette cellule ne repousse pas les murs, trash et ennemis. Des repulses peuvent se pousser mutuellement, le premier à pousser est celui le plus proche du coin supérieur gauche de la grille.

5. ![""](/src/resources/texture/hitman.png "Le Hitman"). Le hitman:

La cellule Hitman ne se déplace pas d'elle-même mais elle peut être poussée. C’est la seule Cellule qui peut détruire la Cellule Boss au contact. Elle ne peut être tuée que par une Trash.

5. ![""](/src/resources/texture/boss.png "Le Boss"). Le boss:

Cellule ennemie qui ne peut être éliminée qu’au contact d’un Hitman, ou d’une Trash (bien que cela n’est pas réalisable)

### Le nouvel ordre des cellules :

Le nouvel ordre des cellules est le suivant :

- repulse
- switcher
- generator
- spinner
- mover
- nudge
- wanderer
- hitman
- boss