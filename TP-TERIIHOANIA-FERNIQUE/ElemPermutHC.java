import javax.swing.text.Element;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class ElemPermutHC implements IElemHC {

    private Instance i;
    private ArrayList<Integer> permut; //permutation de {0,..,i.getListePieces().size()-1} représentant l'ordre dans lequel on souhaite ramasser les pièces
    private static int dist = 1; //distance à laquelle on génère voisinage

    public ElemPermutHC(Instance i, ArrayList<Integer> p){
        this.i = i;
        permut = p;
    }

    public ElemPermutHC(ElemPermutHC s){
        this.i = new Instance(s.i);
        this.permut = new ArrayList<Integer>();
        permut.addAll(s.permut);
    }

    public static void setDist(int d){
        dist = d;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElemPermutHC)) return false;
        ElemPermutHC that = (ElemPermutHC) o;
        return i.equals(that.i) && permut.equals(that.permut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, permut);
    }

    public int getVal(){


        //retourne nbCases * valSol - nbStepsTotal, où :
        //- nbCases est le nombre de cases du plateau
        //- valSol est la valeur de la solution associée à this
        //- nbStepsTotal est le nombre de pas total qu'il faudrait pour ramasser toutes les pièces dans l'ordre de permut

        // à compléter
         return (i.getNbL() * i.getNbC()) + i.evaluerSolution(getSol()) - i.nbStepsToCollectAll(permut);
    }

    public Solution getSol(){
        return i.calculerSol(permut);
    }


    public ArrayList<ElemPermutHC> getVoisinsImmediats() {


        //retourne l'ensemble des voisins à dist <= 1 (et donc this fait partie du résultat car à distance 0)
        //voisins = toutes les permutations que l'on peut atteindre en repoussant un élément de permut à la fin
        //ex pour permut = (0,1,2), doit retourner {(1,2,0),(0,2,1),(0,1,2)}
        //les objets retournés doivent être indépendant de this, et cette méthode ne doit pas modifier this

        //ne dois pas modifier this

        //à compléter
        ArrayList<ElemPermutHC> voisins = new ArrayList<>();
        for (int i = 0; i < this.permut.size(); i++) {
            ElemPermutHC voisin = new ElemPermutHC(this);
            voisin.permut = new ArrayList<>(this.permut);
            voisin.permut.remove(i);
            voisin.permut.add(this.permut.get(i));
            voisins.add(voisin);
        }

        return voisins;
    }



    public ArrayList<ElemPermutHC> getVoisins(){

        //retourne voisins (sans doublons) à une distance <= dist
        //pour dist = 1, doit retourner getVoisinsImmediats();

        //à compléter
        ArrayList<ElemPermutHC> voisins = getVoisinsRecurs(dist);
        ArrayList<ElemPermutHC> uniqueVoisins = new ArrayList<>();
        for (ElemPermutHC voisin :voisins) {
            int nb_sim = 0;
            for (ElemPermutHC voisin_ :uniqueVoisins) {
                if(voisin.permut.equals(voisin_.permut)){
                    nb_sim += 1;
                }
            }

            if(nb_sim == 0){
                uniqueVoisins.add(voisin);
            }
        }
        return uniqueVoisins;
    }

    private ArrayList<ElemPermutHC> getVoisinsRecurs(int dist){
        ArrayList<ElemPermutHC> voisinsImmediats = getVoisinsImmediats();
        if (dist == 1) return voisinsImmediats;

        ArrayList<ElemPermutHC> voisins = new ArrayList<>();

        for (ElemPermutHC voisin :voisinsImmediats) {
            voisins.addAll(voisin.getVoisinsRecurs(dist - 1));
        }

        return voisins;
    }

}