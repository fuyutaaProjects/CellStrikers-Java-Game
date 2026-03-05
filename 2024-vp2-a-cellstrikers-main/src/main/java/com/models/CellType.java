package src.main.java.com.models;

import java.util.ArrayList;
import src.main.java.com.views.AnimaCell;

/**
 * Enumération représentant les différents types de cellules et leurs comportements.
 * Chaque type de cellule a des comportements spécifiques définis dans la méthode update.
 */
public enum CellType {
    REPULSE{
        @Override
        public void update(Position pos, Cell[][] grid, Cell[][] newGrid, ArrayList<AnimaCell> animaQueue){
            Cell[] neighboorCells = Grid.GridCheckers.checkNeighbor(pos, grid);

            for(Direction dir : Direction.values()){
                Cell cell = neighboorCells[dir.ordinal()];

                //Répulsion des cellules
                if(cell != null){
                    pushCell(pos, dir, grid, newGrid, animaQueue);
                }
            }
        
        }
    },
    SWITCHER{
        @Override
        public void update(Position pos, Cell[][] grid, Cell[][] newGrid, ArrayList<AnimaCell> animaQueue){
            Direction dir = pos.getCell(grid).getDirection();

            Position frontPos = pos.getFrontPosition(dir);
            Position backPos = pos.getBackPosition(dir);

            Cell front = frontPos.getCell(grid);
            Cell back = backPos.getCell(grid);

            if( (front != null && front.getCellType() == WALL) || (back != null && back.getCellType() == WALL)){
                return;
            }

            //Si les cellules en face et à l'arrière de la cellule sont à des positions légales on les échanges
            if(frontPos.isValid(grid.length, grid[0].length) && backPos.isValid(grid.length, grid[0].length)){
                newGrid[frontPos.getI()][frontPos.getJ()] = back;
                newGrid[backPos.getI()][backPos.getJ()] = front;
                if(front != null){
                    AnimaCell.addToAnimaList(animaQueue, new AnimaCell("switch",front,frontPos.getJ(), frontPos.getI(),backPos.getJ(),backPos.getI()));
                    front.setChanged(true);
                }
                if(back != null){
                    AnimaCell.addToAnimaList(animaQueue, new AnimaCell("switch",back,backPos.getJ(), backPos.getI(), frontPos.getJ(),frontPos.getI()));
                    back.setChanged(true);
                }
            }
        }
    },
    GENERATOR{
        @Override
        public void update(Position pos, Cell[][] grid, Cell[][] newGrid, ArrayList<AnimaCell> animaQueue){
            // Récupère les informations nécessaire qui concerne l'entourage proche du generator.
            Cell actual = pos.getCell(grid);
            // On récupère la cellule derrière le generator
            Position backPos = pos.getBackPosition(actual.getDirection());
            Direction oppositeDirection = actual.getOpposite();
            Cell copy = backPos.getCell(grid);

            if (copy != null){
                Position frontPos = pos.getFrontPosition(actual.getDirection());
                Cell front = frontPos.getCell(grid);

                // Si la case devant est vide on place la copie
                if (frontPos.isValid(grid.length, grid[0].length) && front == null){
                    newGrid[frontPos.getI()][frontPos.getJ()] = copy.copy();
                    AnimaCell.addToAnimaList(animaQueue, new AnimaCell("gen", pos.getCell(grid), newGrid[frontPos.getI()][frontPos.getJ()] , pos.getJ(), pos.getI(), frontPos.getJ(), frontPos.getI()));
                    newGrid[frontPos.getI()][frontPos.getJ()].setChanged(true);
                    newGrid[pos.getI()][pos.getJ()] = actual.copy();
                    return;
                }
                // Sinon on pousse les cellules devant le generator
                if (frontPos.isValid(grid.length, grid[0].length) && pushCell(pos,actual.getDirection(),grid,newGrid, animaQueue)){
                    AnimaCell.addToAnimaList(animaQueue, new AnimaCell("gen", pos.getCell(grid), copy, pos.getJ(), pos.getI(), frontPos.getJ(), frontPos.getI()));
                    newGrid[frontPos.getI()][frontPos.getJ()] = copy.copy();
                    newGrid[frontPos.getI()][frontPos.getJ()].setChanged(true);
                    newGrid[pos.getI()][pos.getJ()] = actual.copy();
                    return;
                }
                // Si on ne peut pas pousser devant le generator on pousse derrière sinon on fait rien
                if (frontPos.isValid(grid.length, grid[0].length) && pushCell(pos,oppositeDirection,grid,newGrid, animaQueue)){
                    AnimaCell.addToAnimaList(animaQueue, new AnimaCell("gen", pos.getCell(grid), copy, pos.getJ(), pos.getI(), pos.getJ()+oppositeDirection.getI(), pos.getI()+oppositeDirection.getJ()));
                    newGrid[pos.getI()][pos.getJ()] = copy.copy();
                    newGrid[frontPos.getI()][frontPos.getJ()].setChanged(true);
                    newGrid[backPos.getI()][backPos.getJ()] = actual.copy();
                }
            }
        }
    },
    SPINNERLEFT{
        @Override
        public void update(Position pos, Cell[][] grid, Cell[][] newGrid, ArrayList<AnimaCell> animaQueue){
            // On regarde les cases autours du spinner
            Cell[] slice = Grid.GridCheckers.checkNeighbor(pos, grid);
            
            for (int i = 0; i < slice.length; i++) {

                // On regarde si il y a une cellule dans les cases adjacentes si oui on les fait tourner
                if(slice[i]!=null){
                    if(slice[i].getCellType() != WALL && slice[i].getCellType() != TRASH && slice[i].getCellType() != ENEMY){
                        AnimaCell.addToAnimaList(animaQueue, new AnimaCell("spin-left", slice[i], pos.getJ() + Direction.values()[i].getI(), pos.getI() + Direction.values()[i].getJ(), slice[i].getDirection()));
                        slice[i].setChanged(true);
                    }
                    slice[i].turn(-1);
                }
            }
        }
    },
    SPINNERRIGHT{
        @Override
        public void update(Position pos, Cell[][] grid, Cell[][] newGrid, ArrayList<AnimaCell> animaQueue){
            // On regarde les cases autours du spinner
            Cell[] slice = Grid.GridCheckers.checkNeighbor(pos, grid);

            for (int i = 0; i < slice.length; i++) {
                // On regarde si il y a une cellule dans les cases adjacentes si oui on les fait tourner
                if(slice[i]!=null){
                    if(slice[i].getCellType() != WALL && slice[i].getCellType() != TRASH && slice[i].getCellType() != ENEMY){
                        AnimaCell.addToAnimaList(animaQueue, new AnimaCell("spin-right", slice[i], pos.getJ() + Direction.values()[i].getI(), pos.getI() + Direction.values()[i].getJ(), slice[i].getDirection()));
                        slice[i].setChanged(true);
                    }
                    slice[i].turn(1);
                }
           }
        }
    },
    MOVER{
        @Override
        // pi et pj sont les positions de la cellule dans la grille
        public void update(Position pos, Cell[][] grid, Cell[][] newGrid, ArrayList<AnimaCell> animaQueue) {
            Cell actual = pos.getCell(grid);
            Direction direction = actual.getDirection();
            //On récupère l'ensemble des cellules en face du mover
            Cell[] slice = Grid.GridCheckers.checkDirection(pos, direction, grid);

            if(canPush(pos, grid, slice)){
                //Déplacement des cellules
                pushCell(pos, direction, grid, newGrid, animaQueue);
                Grid.move(pos, direction, grid, newGrid, animaQueue);
            }
        }

        /**
         * Méthode permettant d'informer si le Mover peut pousser la rangée de cellule
         * @param pos Position sa position sur la grille
         * @param grid Cell[][]La grille de cellules
         * @param slice Cell[] la chaîne de cellule
         * @return boolean true si il peut, false sinon
         */
        private boolean canPush(Position pos, Cell[][] grid, Cell[] slice){
            int count = 0;
            int count2 = 0;
            Cell actual = pos.getCell(grid);
            Direction direction = actual.getDirection();

            //Pour vérifier s'il y a des mover allant dans la même direction à l'arrière le mover ne bouge pas car il a déjà bougé
            Cell[] rearCells = Grid.GridCheckers.checkDirection(pos, direction.getOpposite(), grid);
            for (int i = 1; i < rearCells.length; i++) {

                Cell cell = rearCells[i];
                Position cellPos = new Position(pos.getI() + i* cell.getOpposite().getJ(), pos.getJ() + i* cell.getOpposite().getI());

                if(cell.getCellType() == MOVER && cell.getDirection() == direction &&
                canPush(cellPos, grid, Grid.GridCheckers.checkDirection(cellPos, direction, grid))){
                    return false;
                }
            }

            //Détecter si on a la puissance nécéssaire pour déplacer les cellules:
            for (int i = 0; i < slice.length; i++) {
                
                CellType type = slice[i].getCellType();

                if(type == MOVER){

                    //Si on en rencontre un qui va dans la même direction la puissance montre
                    if(slice[i].getDirection() == grid[pos.getI()][pos.getJ()].getDirection()){
                        count++;
                    }
                    //Si on en rencontre un qui va dans le sens oposé l'autre puissance monte
                    else if(slice[i].getDirection() == direction.getOpposite()){
                        count2++;
                    }else if(type == ENEMY || type == TRASH || type == WANDERER){
                        break;
                    }
                }
                if(count2 > count){//Si le mover n'a pas la puissance requise il ne peut pas déplacer
                    return false;
                }
            }
            if (count2 == count && count > 0) {//On check que le mover n'a pas la même puissance que celui en face, ne sert que pour les animations
                return false;
            }
            return canImpel(pos, grid, slice);
        }

        /**
         * Méthode permettant de savoir si les cellules dans la chaîne sont déplaçables par le mover
         * @param pos sa position en i sur la grille
         * @param grid la grille de cellules
         * @param slice la chaîne de cellule
         * @return true si la chaîne est déplaçable, false sinon
         */
        private boolean canImpel(Position pos, Cell[][] grid, Cell[] slice){
            Direction direction = pos.getCell(grid).getDirection();
            return Grid.canMove(pos, direction, grid);
        }
    },
    NUDGE{
        @Override
        public void update(Position pos, Cell[][] grid, Cell[][] newGrid, ArrayList<AnimaCell> animaQueue){

            Direction dir = grid[pos.getI()][pos.getJ()].getDirection();
            Cell[] rearCells = Grid.GridCheckers.checkDirection(pos, dir.getOpposite(), grid);

            //Gérer le cas ou des nudges sont à l'arrière
            for (int i = 1; i < rearCells.length; i++){
                Cell cell = rearCells[i];

                if(cell.getCellType() == NUDGE && cell.getDirection() == dir){
                    return;
                }// La cellules ne peut pas être poussé par un nudge donc la cellule courante ne risquent pas d'être poussé
                if(!cond(cell.getCellType())){
                    break;
                }
            }
            Position frontPos = pos.getFrontPosition(dir);

            if(!frontPos.isValid(grid.length, grid[0].length)){
                return;
            }
            Cell front = frontPos.getCell(grid);

            if(front != null){
                CellType type = front.getCellType();

                if(cond(type)){

                    if(canPush(frontPos, dir, grid)){
                        
                        Cell[] slice = Grid.GridCheckers.checkDirection(pos, dir, grid);
                        //Déplacement des cellules:
                        for (int i = slice.length - 1; i >= 0; i--) {
                            Position cellPos = pos.getFrontPosition(dir, i);
                            Position animationPos = pos.getFrontPosition(dir, i+1);
                            newGrid[cellPos.getFrontPosition(dir).getI()][cellPos.getFrontPosition(dir).getJ()] = cellPos.getCell(grid).copy();
                            newGrid[cellPos.getI()][pos.getJ()] = null;
                            AnimaCell.addToAnimaList(animaQueue, new AnimaCell("move", newGrid[cellPos.getFrontPosition(dir).getI()][cellPos.getFrontPosition(dir).getJ()], cellPos.getJ(), cellPos.getI(), animationPos.getJ(), animationPos.getI()));
                            newGrid[cellPos.getFrontPosition(dir).getI()][cellPos.getFrontPosition(dir).getJ()].setChanged(true);
                        }
                    }
                }
            }else{
                Grid.move(pos, dir, grid, newGrid, animaQueue);
            }
        }
        /**
         * Méthode permettant de vérifier si le nudge peut pousser le type spécifier
         * @param type le type de la cellule à pousser
         * @return true si il peut la pousser, false sinon
         */
        private boolean cond(CellType type){
            return type == WANDERER || type == TRASH || type == ENEMY || type == NUDGE;
        }
        /**
         * Méthode permettant d'informer si le nudge peut pousser les cellules devant lui
         * @param pos Position la position du nudge
         * @param dir Direction la direction du nudge
         * @param grid Cell[][] la grille de cellule
         * @return true si il peut, false sinon
         */
        private boolean canPush(Position pos, Direction dir, Cell[][] grid){
            if(pos.isValid(grid.length, grid[0].length)){
                if(pos.getCell(grid) == null){
                    return true;
                }
                if(cond(pos.getCell(grid).getCellType())){
                    return canPush(pos.getFrontPosition(dir), dir, grid);
                }
            }
            return false;
        }
    },
    WANDERER{
        @Override
        public void update(Position pos, Cell[][] grid, Cell[][] newGrid, ArrayList<AnimaCell> animaQueue) {

            Direction direction = pos.getCell(grid).getDirection();
            Position frontPos = pos.getFrontPosition(direction);

            //Cas ou elle peut bouger devant
            if(frontPos.isValid(grid.length, grid[0].length) && frontPos.getCell(grid) == null){

                if(isMover(pos, direction, frontPos.getFrontPosition(direction).getCell(grid), grid, newGrid, animaQueue)){
                    return;
                }
                Grid.move(pos, direction, grid, newGrid, animaQueue);
            }else{
                
                //Recherche d'une direction viable qui est le chemin le plus long:
                int[] toGo = new int[4];
                Direction[] allDir = Direction.values();

                for (Direction dir : allDir) {

                    frontPos = pos.getFrontPosition(dir);
                    int len = 0; //Longueur du chemin
    
                    int count = 1;
                    while(frontPos.isValid(grid.length, grid[0].length)){
                        Cell cell = frontPos.getCell(grid);
    
                        //Cas ou un Mover dans cette direction fonce sur la cellule
                        if(cell != null){
                            if(cell.getCellType() == MOVER && cell.getDirection().getOpposite() == dir){
                                //Cas ou il est près d'elle:
                                if(count == 1){
                                    if(isMover(pos, dir, cell, grid, newGrid, animaQueue)){
                                        return;
                                    }
                                }
                                len = 0;//On ignore ce chemin
                            }break;
                        }else{
                            len++;
                            frontPos = frontPos.getFrontPosition(dir);
                        }
                        count++;
                    }
                    toGo[dir.ordinal()] = len;
                }
                //Sélection du chemin de longueur maximum
                int max = toGo[0];
                int idMax = 0;
    
                for (int i = 1; i < toGo.length; i++) {
                    if(max < toGo[i]){
                        max = toGo[i];
                        idMax = i;
                    }
                }
                pos.getCell(grid).setDirection(Direction.values()[idMax]);
                Grid.move(pos, Direction.values()[idMax], grid, newGrid, animaQueue);
            }
        }
        /**
         * Méthode permettant de détecter si un mover fonce sur la cellule
         * @param pos Position la position de la cellule
         * @param dir Direction la direction ou la cellule se rend
         * @param cell Cell le mover riquant de lui foncer dessus
         * @param grid Cell[][] la grille de cellule actuelle
         * @param newGrid Cell[][] la grille de cellule du prochain état
         * @return true si c'est le cas, false sinon
         */
        public boolean isMover(Position pos, Direction dir, Cell cell, Cell[][] grid, Cell[][] newGrid, ArrayList<AnimaCell> animaQueue){

            if((cell != null && cell.getCellType() == MOVER && cell.getDirection().getOpposite() == dir)){

                //Recherche d'une échappatoire peu importe laquelle:
                for (Direction dir2 : Direction.values()) {

                    if(dir2 != dir){
                        if(pos.getFrontPosition(dir2).isValid(grid.length, grid[0].length)){

                            if(pos.getFrontPosition(dir2).getCell(grid) == null){
                                pos.getCell(grid).setDirection(dir2);
                                Grid.move(pos, dir2, grid, newGrid, animaQueue);
                                return true;
                            }
                        }
                    }
                }
            }return false;
        }
    },
    PUSH{

    },
    SLIDE{
        @Override
        public boolean canMove(Direction dir, Direction toGo) {
            return dir.isVertical() != toGo.isVertical();
        }
    },
    DIRECTIONAL{
        @Override
        public boolean canMove(Direction dir, Direction toGo) {
            return dir == toGo;
        }
    },
    ENEMY{

    },
    TRASH{

    },
    WALL{
        @Override
        public boolean canMove(Direction dir, Direction toGo) {
            return false;
        }
    },
    HITMAN {

    },
    BOSS{
        // La cellule BOSS est utilisée dans le mode additionnel "HITMAN CELL.". 
        // Elle est tuable seulement par la celulle HITMAN. Elle peut être poussée par d'autres cellules.
        @Override
        public boolean canMove(Direction dir, Direction toGo) {
            return false;
        }
    };

    /**
     * Méthode qui permet de définir les comportements des cellules, les comportements sont redéfinis pour les cellules ayant
     * un comportement important.
     *
     * @param pos Position la position de la cellule
     * @param grid Cell[][] la grille de l'état courant
     * @param newGrid Cell[][] la grille du prochain état
     */
    public void update(Position pos, Cell[][] grid, Cell[][] newGrid, ArrayList<AnimaCell> animaQueue){}

    /**
     * Permet de pousser une ligne ou colonne de cellule dans une direction donner pour laisser la place à une cellule
     * qui veut aller dans la même direction.
     *
     * @param pos Position la position ou commencer le déplacement
     * @param dir Direction la direction du déplacement
     * @param grid Cell[][] la grille de l'état courant
     * @param newGrid Cell[][] la grille du prochain état
     * @return boolean true si les cellules ont pu bougé, false sinon
     */
    public boolean pushCell(Position pos, Direction dir, Cell[][] grid, Cell[][] newGrid, ArrayList<AnimaCell> animaQueue) {
        Position nextPos = pos.getFrontPosition(dir);

        if (!Grid.isValid(nextPos, grid)) {
            return false;
        }
        if (grid[nextPos.getI()][nextPos.getJ()] == null) {
            return true;
        }
        CellType type = grid[nextPos.getI()][nextPos.getJ()].getCellType();
        if(type == ENEMY || type == TRASH || type == WANDERER || type == BOSS){
            return true;
        }
        if(type == WALL){
            return false;
        }
        if (pushCell(nextPos, dir, grid, newGrid, animaQueue)){
            Grid.move(nextPos, dir, grid, newGrid, animaQueue);
            return true;
        }
        return false;
    }

    /**
     * Méthode permettant d'informer si la cellule peut se déplacer
     * vers une direction donnée sans prendre en compte son environnement. 
     * Cette méthode est prévu pour être redéfinie dans les cellules qui ont besoin.
     *
     * @param dir Direction la direction actuelle de la cellule
     * @param toGo Direction la direction où la cellule doit aller
     * @return boolean true si elle peut se déplacer, false sinon
     */
    public boolean canMove(Direction dir, Direction toGo){
        return true;
    }
}

