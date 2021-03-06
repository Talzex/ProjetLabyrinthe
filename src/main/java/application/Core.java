package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import labyrinthe.ILabyrinthe;
import labyrinthe.ISalle;
import personnages.IPersonnage;
import vue2D.IVue;
import vue2D.sprites.HerosSprite;
import vue2D.sprites.ISprite;
import vue2D.sprites.MonstreSprite;

/**
 *
 * @author INFO Professors team
 */
public class Core {

    ArrayList<ISprite> AllPerso = new ArrayList<>();
    ILabyrinthe labyrinthe;

    /**
     * Initialisation du labyrinthe
     */
    protected void initLabyrinthe() {
        // creation du labyrinthe
        labyrinthe = new labyrinthe.Labyrinthe();
        chargementLaby("labys/level3.txt");
    }

    /**
     * Initialisation des sprites 
     * @param vue, la vue
     */
    protected void initSprites(IVue vue) {
        // creation du heros 
        IPersonnage h = new personnages.Heros(labyrinthe.getEntree());
        ISprite heros = new HerosSprite(h, labyrinthe);
        vue.add(heros);
        AllPerso.add(heros);
        
        for (int i = 0; i < 10; i++) {
            IPersonnage m = new personnages.Monstre(labyrinthe.getSortie());
            ISprite monstre = new MonstreSprite(m, labyrinthe);
            vue.add(monstre);
            AllPerso.add(monstre);

        }
    }

    /**
     * Boucle de jeu
     * @param vue, la vue
     */
    protected void jeu(IVue vue) {
        // boucle principale
        ISalle destination = null;
        ISprite heros = AllPerso.get(0);
        while (!labyrinthe.getSortie().equals(heros.getPosition())) {
            // choix et deplacement
            for (IPersonnage p : vue) {
                Collection<ISalle> sallesAccessibles = labyrinthe.sallesAccessibles(p);
                destination = p.faitSonChoix(sallesAccessibles); // on demande au personnage de faire son choix de salle
                p.setPosition(destination); // deplacement
            }


            // detection des collisions
            boolean collision = false;
            ISprite monstre = null;
            for (ISprite p : vue) {
                if (p != heros) {
                    if (p.getPosition() == heros.getPosition()) {
                        System.out.println("Collision !!");
                        collision = true;
                        monstre = p;
                    }
                }
            }
            if (collision) {
                vue.remove(monstre);
                vue.remove(heros);
                System.out.println("Perdu !");
                System.out.println("Plus que " + vue.size() + " personnages ...");
            }

            temporisation(5);
        }

        System.out.println("Gagn??!");
    }

    private void chargementLaby(String fic) {
        try {
            labyrinthe.creerLabyrinthe(fic);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * M??thode pour temporiser
     * @param nb, le temps de temporisation
     */
    protected void temporisation(int nb) {
        try {
            Thread.sleep(nb); // pause de nb millisecondes
        } catch (InterruptedException ie) {
        };
    }
}
