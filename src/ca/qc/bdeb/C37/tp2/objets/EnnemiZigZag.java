package ca.qc.bdeb.C37.tp2.objets;

import ca.qc.bdeb.C37.tp2.window.Vue;
import static ca.qc.bdeb.C37.tp2.window.Vue.splitImage;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Danmasta97, jerome
 */
public class EnnemiZigZag extends ObjetJeu {

    public static final int L = 50, H = 55, V = 5;
    
    private final int CADENCE = 50;

    public BufferedImage[] enemySprite;
    private int frame = 4;
    private int timer = 0;
    private int ready;
    

    public EnnemiZigZag(float x, IdObjet id) {
        super(x, -25, id);
        setImg();
        ready = 0;
        velX = V;
    }

    @Override
    public void tick(ControlleurObjets controlleur) {
        if(ready > 0){
            --ready;
        }
        y += V;
        
        if (y > Vue.H) {
            y = -H;
        }
        
        x += velX;
        gererCollision(controlleur);
    }

    @Override
    public void render(Graphics g) {
        if (++timer >= 3) {
            ++frame;
            timer = 0;
        }
        if (frame >= 8) {
            frame -= 4;
        }
        
        this.img = enemySprite[frame];
        g.drawImage(img, (int) x, (int) y, L, H, null);
    }

    @Override
    public Rectangle contact() {
        return new Rectangle((int) x, (int) y + 10, L, H - 10);
    }

    @Override
    public void setImg() {
        BufferedImage enemyImg;
        try {
            enemyImg = ImageIO.read(new File("res/baddy.png"));
        } catch (IOException e) {
            enemyImg = null;
        }
        enemySprite = splitImage(enemyImg, 4, 2);

        this.img = enemySprite[frame];
    }
    
    /**
     * 
     * @param objets 
     */
    private void gererCollision(ControlleurObjets controlleur) {
        for (int i = 0; i < controlleur.objets.size(); i++) {
            ObjetJeu temp = controlleur.objets.get(i);
            
            if (temp.getId() == IdObjet.TirNormal) {
                if (contact().intersects(temp.contact())) {
                    controlleur.enleverObjet(temp);
                    controlleur.enleverObjet(this);
                }
            } else if (temp.id == IdObjet.Joueur){
                if (getX() + 50f >= temp.getX() && getX() - 50f 
                         <= temp.getX() && ready == 0) {
                     
                    controlleur.ajouterObjet(new TirEnnemi(getX() + 20f,
                        getY() + 50f, IdObjet.TirEnnemi));
                    
                    ready = CADENCE;
                        
                }
                
                if (getX() <= 0 || getX() >= Vue.L - L) {
                    velX = -velX;
                }
            }
        }
    }
}

