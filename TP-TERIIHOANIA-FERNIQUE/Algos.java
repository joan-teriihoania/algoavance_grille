import java.util.*;

public class Algos {

    public static boolean egalEnsembliste(ArrayList<?> a1, ArrayList<?> a2){
        //retourn vrai ssi les a1 à les même éléments que a2 (peut importe l'ordre)
        return a1.containsAll(a2) && a2.containsAll(a1);
    }


    public static Solution greedySolver(Instance i) {

        //calcule la solution obtenue en allant chercher à chaque étape la pièce restante la plus proche
        //(si plusieurs pièces sont à la même distance, on fait un choix arbitraire)

        return i.calculerSol(i.greedyPermut());
    }


    public static Solution algoFPT1(InstanceDec id) {
        //algorithme qui décide id (c'est à dire si opt(id.i) >= id.c) en branchant (en 4^k) dans les 4 directions pour chacun des k pas
        //retourne une solution de valeur >= c si une telle solution existe, et null sinon
        //Ne doit pas modifier le paramètre
        //Rappel : si c==0, on peut retourner la solution égale au point de départ puisque l'on est pas obligé d'utiliser les k pas
        // (on peut aussi retourner une solution plus longue si on veut)
        //Remarque : quand vous aurez codé la borneSup, pensez à l'utiliser dans cet algorithme pour ajouter un cas de base

        //à compléter

        //case de base si c == 0
        if (id.c == 0) {
            return new Solution(id.i.getStartingP());
        }

        return rechercheOptFPT(id, new Solution());
    }

    /**
     * @param id     nouvelle instance qui a éte parcouru par le chemin
     * @param chemin chemin déja parcouru
     * @return la solution qui satisfait c ou sinon null si k==0
     */
    private static Solution rechercheOptFPT(InstanceDec id, Solution chemin) {
        Solution nextChemin = new Solution();

        nextChemin.addAll(chemin);
        nextChemin.add(id.i.getStartingP());

        InstanceDec nextInstanceDec = new InstanceDec(new Instance(id.i), id.c);

        if (nextInstanceDec.i.piecePresente(nextInstanceDec.i.getStartingP())) {
            nextInstanceDec.c--;
            nextInstanceDec.i.retirerPiece(nextInstanceDec.i.getStartingP());
        }

        //cas de base
        if (nextInstanceDec.i.getK() == 0) {
            if (nextInstanceDec.c <= 0) {

                return nextChemin;
            } else {
                return null;
            }
        }
        nextInstanceDec.i.setK(id.i.getK() - 1);

        //Recupérer les coord des case dans les 4 directions
        Coord left = new Coord(id.i.getStartingP().getL(), id.i.getStartingP().getC() - 1);
        Coord up = new Coord(id.i.getStartingP().getL() - 1, id.i.getStartingP().getC());
        Coord right = new Coord(id.i.getStartingP().getL(), id.i.getStartingP().getC() + 1);
        Coord down = new Coord(id.i.getStartingP().getL() + 1, id.i.getStartingP().getC());

        Solution leftSol;
        Solution upSol;
        Solution downSol;
        Solution rightSol;
        nextInstanceDec.i.setK(id.i.getK() - 1);

        if (left.estDansPlateau(nextInstanceDec.i.getNbL(), nextInstanceDec.i.getNbC())) {
            nextInstanceDec.i.setStartingP(left);
            leftSol = rechercheOptFPT(nextInstanceDec, nextChemin);
        }
        if (up.estDansPlateau(nextInstanceDec.i.getNbL(), nextInstanceDec.i.getNbC())) {
            nextInstanceDec.i.setStartingP(up);
            upSol = rechercheOptFPT(nextInstanceDec, nextChemin);
        }
        if (right.estDansPlateau(nextInstanceDec.i.getNbL(), nextInstanceDec.i.getNbC())) {
            nextInstanceDec.i.setStartingP(right);
            rightSol = rechercheOptFPT(nextInstanceDec, nextChemin);
        }
        if (down.estDansPlateau(nextInstanceDec.i.getNbL(), nextInstanceDec.i.getNbC())) {
            nextInstanceDec.i.setStartingP(down);
            downSol = rechercheOptFPT(nextInstanceDec, nextChemin);
        }
        
        Solution bestSol = leftSol;
        if(bestSol == null || id.i.evaluerSolution(upSol) > id.i.evaluerSolution(bestSol)){
            bestSol = upSol;
        }
        if(bestSol == null || id.i.evaluerSolution(downSol) > id.i.evaluerSolution(bestSol)){
            bestSol = downSol;
        }
        if(bestSol == null || id.i.evaluerSolution(rightSol) > id.i.evaluerSolution(bestSol)){
            bestSol = rightSol;
        }
        return bestSol;
    }

    /**
     * @param id     nouvelle instance qui a éte parcouru par le chemin
     * @param chemin chemin déja parcouru
     * @return la solution qui satisfait c ou sinon null si k==0
     */
    private static Solution rechercheOptFPTDP(InstanceDec id, Solution chemin, HashMap<InstanceDec, Solution> table) {
        if(table.containsKey(id)){
            return table.get(id);
        }

        Solution nextChemin = new Solution();

        nextChemin.addAll(chemin);
        nextChemin.add(id.i.getStartingP());

        InstanceDec nextInstanceDec = new InstanceDec(new Instance(id.i), id.c);

        if (nextInstanceDec.i.piecePresente(nextInstanceDec.i.getStartingP())) {
            nextInstanceDec.c--;
            nextInstanceDec.i.retirerPiece(nextInstanceDec.i.getStartingP());
        }

        //cas de base
        if (nextInstanceDec.i.getK() == 0) {
            if (nextInstanceDec.c <= 0) {
                table.put(id, nextChemin);
            } else {
                table.put(id, null);
            }
            return table.get(id);
        }
        nextInstanceDec.i.setK(id.i.getK() - 1);

        //Recupérer les coord des case dans les 4 directions
        Coord left = new Coord(id.i.getStartingP().getL(), id.i.getStartingP().getC() - 1);
        Coord up = new Coord(id.i.getStartingP().getL() - 1, id.i.getStartingP().getC());
        Coord right = new Coord(id.i.getStartingP().getL(), id.i.getStartingP().getC() + 1);
        Coord down = new Coord(id.i.getStartingP().getL() + 1, id.i.getStartingP().getC());

        Solution leftSol;
        Solution upSol;
        Solution downSol;
        Solution rightSol;
        nextInstanceDec.i.setK(id.i.getK() - 1);

        if (left.estDansPlateau(nextInstanceDec.i.getNbL(), nextInstanceDec.i.getNbC())) {
            nextInstanceDec.i.setStartingP(left);
            leftSol = rechercheOptFPTDP(nextInstanceDec, nextChemin, table);
            if (leftSol != null) {
                table.put(id, leftSol);
                return table.get(id);
            }
        }
        if (up.estDansPlateau(nextInstanceDec.i.getNbL(), nextInstanceDec.i.getNbC())) {
            nextInstanceDec.i.setStartingP(up);
            upSol = rechercheOptFPTDP(nextInstanceDec, nextChemin, table);
            if (upSol != null) {
                table.put(id, upSol);
                return table.get(id);
            }
        }
        if (right.estDansPlateau(nextInstanceDec.i.getNbL(), nextInstanceDec.i.getNbC())) {
            nextInstanceDec.i.setStartingP(right);
            rightSol = rechercheOptFPTDP(nextInstanceDec, nextChemin, table);
            if (rightSol != null) {
                table.put(id, rightSol);
                return table.get(id);
            }
        }
        if (down.estDansPlateau(nextInstanceDec.i.getNbL(), nextInstanceDec.i.getNbC())) {
            nextInstanceDec.i.setStartingP(down);
            downSol = rechercheOptFPTDP(nextInstanceDec, nextChemin, table);
            if (downSol != null) {
                table.put(id, downSol);
                return table.get(id);
            }
        }

        table.put(id, null);
        return null;
    }




    public static Solution algoFPT1DP(InstanceDec id, HashMap<InstanceDec, Solution> table) {
        //même spécification que algoFPT1, si ce n'est que
        // - si table.containsKey(id), alors id a déjà été calculée, et on se contente de retourner table.get(id)
        // - sinon, alors on doit calculer la solution s pour id, la ranger dans la table (table.put(id,res)), et la retourner

        //Remarques
        // - ne doit pas modifier l'instance id en param (mais va modifier la table bien sûr)
        // - même si le branchement est le même que dans algoFPT1, ne faites PAS appel à algoFPT1 (et donc il y aura de la duplication de code)
        if (table.containsKey(id)) {
            return table.get(id);
        }

        if (id.c == 0) {
            table.put(id, new Solution(id.i.getStartingP()));
            return table.get(id);
        }

        table.put(id, rechercheOptFPTDP(id, new Solution(), table));
        return table.get(id);
    }


    public static Solution algoFPT1DPClient(InstanceDec id){
        HashMap<InstanceDec, Solution> table = new HashMap<>();
        //si il est possible de collecter >= id.c pièces dans id.i, alors retourne une Solution de valeur >= c, sinon retourne null
        //doit faire appel à algoFPT1DP

        //à completer
        return algoFPT1DP(id, table);
    }



}
