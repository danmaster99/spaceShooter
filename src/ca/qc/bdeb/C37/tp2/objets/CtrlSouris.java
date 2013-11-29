package ca.qc.bdeb.C37.tp2.objets;

import ca.qc.bdeb.C37.tp2.window.Jeu;
import ca.qc.bdeb.C37.tp2.window.Vue;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author jerome
 */
public class CtrlSouris implements MouseMotionListener, MouseListener {

    ControlleurObjets controlleur;
    Jeu jeu;
    
    public CtrlSouris (ControlleurObjets controlleur, Jeu jeu) {
        this.controlleur = controlleur;
        this.jeu = jeu;
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        ObjetJeu temp;
        
        for (int i = 0; i < controlleur.objets.size(); i++) {
            temp = controlleur.objets.get(i);

            if (temp.id == IdObjet.Joueur && jeu.ctrl == IdCtrl.SOURIS) {
                
                if (e.getX() > 8 && e.getX() < Vue.L - 8) {
                    temp.setVelY(e.getY() - (temp.getY() + Joueur.H/2));
                }
                else {
                    temp.setVelY(0);
                }
                if (e.getY() > Vue.H/2 && e.getX() < Vue.H - 15) {
                    temp.setVelX(e.getX() - (temp.getX() + Joueur.L/2));
                }
                else {
                    temp.setVelX(0);
                }
            }
            else if (temp.id == IdObjet.Pointeur) {
                temp.setX(e.getX());
                temp.setY(e.getY());
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        ObjetJeu temp;
         
        for (int i = 0; i < controlleur.objets.size(); i++) {
            temp = controlleur.objets.get(i);

            if (temp.id == IdObjet.Joueur && jeu.ctrl == IdCtrl.SOURIS) {
                
                controlleur.ajouterObjet(new TirJoueur((int)temp.getX(), 
                                (int)temp.getY(), IdObjet.TirNormal));
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
}
