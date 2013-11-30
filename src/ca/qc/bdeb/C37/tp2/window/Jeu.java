package ca.qc.bdeb.C37.tp2.window;

import ca.qc.bdeb.C37.tp2.objets.*;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 *
 * @author jerome
 */
public class Jeu extends Canvas implements Runnable {

    /**
     * État du jeu
     */
    public boolean running = false, paused = false;
    /**
     * Thread du jeu
     */
    private Thread thread;

    /**
     * Choix de contrôles
     */
    public static IdCtrl ctrl;
    private IdCtrl memCtrl;
    
    /**
     * Objets
     */
    ControlleurObjets controlleur;

    /**
     * Image de fond
     */
    Image fond;
    
    
    /**
     * 
     */
    public static Menu menu;
    
    /**
     * Innitialisation du jeu
     */
    private void init() {

        ctrl = IdCtrl.CLAVIER;

        setFond();

        Random rand = new Random();

        controlleur = new ControlleurObjets();

        // Limites de déplacement
        controlleur.dessinerFrontieres();

        // Ajouter le joueur
        controlleur.ajouterObjet(new Joueur((float)Vue.L/2-25, (float)Vue.H*3/4,
                controlleur, IdObjet.Joueur));
        
        // Ajouter le pointeur
        controlleur.ajouterObjet(new Pointeur(0, 0, IdObjet.Pointeur));

        // Ajoute des ennemis (test)
        controlleur.spawnMobs();
        

        addKeyListener(new CtrlClavier(controlleur, this));

        CtrlSouris souris = new CtrlSouris(controlleur, this);
        addMouseMotionListener(souris);
        addMouseListener(souris);

    }

    /**
     * Démarage de la partie
     */
    public synchronized void start() {
        if (running)
            return;
        
        thread = new Thread(this);
        thread.start();
        running = true;
        pause();
    }
    
    public void pause() {
        memCtrl = ctrl;
        ctrl = null;
        paused = true;
        
        menu = new Menu(this);
    }
    
    public void resume() {
        menu.dispose();
        ctrl = memCtrl;
        if (ctrl == null)
            ctrl = IdCtrl.CLAVIER;
        
        paused = false;
    }

    /**
     * Game Loop Source :
     * http://www.mediafire.com/download/in0truhx4fzoerf/game_loop.txt
     */
    @Override
    public void run() {
        init();
        requestFocus();

        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        while (running) {
            if (!paused) {
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;
                while (delta >= 1) {
                    tick();
                    updates++;
                    delta--;
                }
                render();
                frames++;

                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    System.out.println("FPS: " + frames + " TICKS: " + updates);
                    frames = 0;
                    updates = 0;
                }
            }
            else {
                lastTime = System.nanoTime();
                timer = System.currentTimeMillis();
            }
        }
    }

    /**
     *
     */
    private void tick() {
        controlleur.tick();
    }

    /**
     *
     */
    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        // ***Début affichage***
        g.drawImage(fond, 0, 0, null);

        controlleur.render(g);

        // ***Fin affichage*****
        g.dispose();
        bs.show();
    }

    /**
     *
     */
    public void setFond() {
        File fichier;
        Image image;

        fichier = new File("res/8bitBackground.bmp");

        try {
            image = ImageIO.read(fichier);

            this.fond = image;
        } catch (IOException ex) {
            System.out.println("Erreur : Image non-chargée.");
        }
    }

}
