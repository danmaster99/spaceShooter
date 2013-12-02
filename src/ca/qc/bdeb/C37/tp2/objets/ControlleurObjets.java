package ca.qc.bdeb.C37.tp2.objets;

import ca.qc.bdeb.C37.tp2.window.Jeu;
import ca.qc.bdeb.C37.tp2.window.Vue;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author jerome
 */
public class ControlleurObjets {
    
    public LinkedList<ObjetJeu> objets = new LinkedList<>();
    
    private ObjetJeu objetTemp;
    
    private Random rand = new Random();
    private int conteurMobs;
    private int mobsToSpawn;
    private int mobTimer = 1;
        
    private boolean pUpSpawned = false;
    
    /**
     * 
     */
    public void tick() {
            
        conteurMobs = 0;
        
        for (int i = 0; i < objets.size(); i++) {
            objetTemp = objets.get(i);
            objetTemp.tick(objets);
        
            if (objetTemp.getId() == IdObjet.TirNormal && objetTemp.y < 0) {
                enleverObjet(objetTemp);
            }
            else if (objetTemp.getId() ==
                    IdObjet.Ennemi && objetTemp.y > Vue.H) {
                
                objets.get(i).y = 20;
            }
            if(objetTemp.getId() == IdObjet.Ennemi){
                Ennemi ennemi = (Ennemi) objetTemp;
                
                ++conteurMobs; 
            }
        }
        
        if (!TirJoueur.isReady()) {
            TirJoueur.decrementerReady();
        }
        
        gererMobs();
        
        spawnPowerUps();
        
    }
    
    /**
     * 
     * @param g 
     */
    public void render(Graphics g) {
        for (int i = 0; i < objets.size(); i++) {
            objetTemp = objets.get(i);
            objetTemp.render(g);
        }
    }
    
    /**
     * 
     * @param objet 
     */
    public void ajouterObjet(ObjetJeu objet) {
        this.objets.add(objet);
    }
    
    /**
     * 
     * @param objet 
     */
    public void enleverObjet(ObjetJeu objet) {
        if (objet.getId() == IdObjet.Ennemi) {
            Jeu.incrementerScore(10 + Jeu.getNiveau());
        }
        this.objets.remove(objet);
    }
    
    /**
     * 
     */
    public void dessinerFrontieres() {
        // Haut
        ajouterObjet(new Frontiere(0, Vue.H*2/3, this,
                IdObjet.Frontiere, Vue.L, 2));
        
        // Bas
        ajouterObjet(new Frontiere(0, Vue.H-2, this,
                IdObjet.Frontiere, Vue.L, 2));
        
        // Gauche
        ajouterObjet(new Frontiere(0, Vue.H*2/3, this,
                IdObjet.Frontiere, 2, Vue.H/3));
        
        // Droite
        ajouterObjet(new Frontiere(Vue.L-2, Vue.H*2/3, this,
                IdObjet.Frontiere, 2, Vue.H/3));
    }
    
    /**
     * 
     */
    public void gererMobs(){
        
        if (mobsToSpawn <= 0 && conteurMobs <= 0) {
            monterNiveau();
            mobTimer = 1;
            mobsToSpawn = Jeu.getNiveau() * 3;
        }
        
        if(mobsToSpawn > 0 && mobTimer <= 0){
            this.ajouterObjet(new Ennemi(rand.nextFloat() * 
                    (Vue.L - 50f) + 25f, this, IdObjet.Ennemi));
            --mobsToSpawn;
            mobTimer = rand.nextInt(150) + 100 / Jeu.getNiveau();
        }
        
        --mobTimer;
    }
    
    /**
     * 
     */
    public void spawnPowerUps() {
        
        if (!pUpSpawned) {
            if (Jeu.getNiveau() % 5 == 0) {
                pUpSpawned = true;
                this.ajouterObjet(new PowerUp(Vue.L/2,0, IdObjet.PowerUpRouge));
            }
            else if (Jeu.getNiveau() % 3 == 0) {
                pUpSpawned = true;
                this.ajouterObjet(new PowerUp(Vue.L/2, 0, IdObjet.PowerUpVert));
            }
            else if (Jeu.getNiveau() % 2 == 0) {
                pUpSpawned = true;
                this.ajouterObjet(new PowerUp(Vue.L/2, 0, IdObjet.PowerUpBleu));
            }
        }
    }
    
    /**
     * 
     */
    private void monterNiveau() {
        Jeu.monterNiveau();
        pUpSpawned = false;
    }
} 
