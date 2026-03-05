## Explication du Processus d'Affichage

1. Point d'entrée: Game
Quand le jeu se lance, c'est Game.java le point d'entrée. Il va créer une variable MainMenu (qui est un extend de JPanel), ainsi que les autres menus.
Il va ensuite afficher le mainMenu:
```java
        card.show(mainPanel, "mainMenu");
```

2. MainMenu
Sur MainMenu on un bouton pour aller sur le LevelSelectionMenu:
```java
        JButton start = new JButton("Start");
        start.setBounds(...);
        start.addActionListener(e -> Game.card.show(Game.mainPanel, "levelSelectionMenu"));
```
On utilise donc un CardLayout et change de menus par card.show, au lieu de devoir utiliser des toggles. On optimise au maximum le MVC.

3. LevelSelectionMenu: Sélection du Niveau
Pareil que MainMenu, on aura plusieurs buttons mais cette fois c'est les levels. Ils font ce call quand on clique:
```java
level1Button.addActionListener(e -> loadAndShowLevel(1));
```
loadAndShowLevel sert juste à formatter et éviter d'avoir des lignes difficiles à lire, car on aura beaucoup de buttons.
```java
    private void loadAndShowLevel(int level) {
        LevelLoader.loadLevel(level);
    }
```

4. LevelLoader
Pour le moment il ne contient pas une multitude de levels. Dans le futur, il lira les levels depuis un fichier .json, et affichera le level correspondant au nombre n passé en paramètre. mais actuellement il contient juste une grille pour tester, et renvoie dans tous les cas peut importe le n, cette grille. 
```java
        Grid gameGrid = new Grid(grid, interactGridDisplayTest);
        Game.showGamePanel(gameGrid);
```

5. Affichage de la grille depuis Game
On a besoin d'afficher depuis Game car on veut accéder à screen pour loader la grille dedans puis lancer le jeu. On ne va pas rendre screen public, ça serait de la mauvaise pratique.
```java
public static void showGamePanel(Grid gameGrid) {
    screen.loadLevel(gameGrid);
    screen.launchGameThread();
    card.show(mainPanel, "screen");
}
```

6. Loading de la grille dans Screen
Screen.java possède un paramètre gameGrid, la grille provenant de LevelLoader et correspondant au level qu'on veut jouer.
```java
    public void loadLevel(Grid gameGrid) {
        this.gameGrid = gameGrid;
        repaint();
    }
```

```java
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (gameGrid != null) {
            Render.renderGrid(g2, gameGrid);
        }

        g2.dispose();
    }
```