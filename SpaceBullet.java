/**
 * @(#)SpaceBullet.java
 *
 *
 * @Shahir Chowdhury
 * @version 1.00 2015/1/17
 */
 
 /* This program is used to make SpaceBullet objects and controls both enemy and player bullets and 
  * the direction they are travelling */
 
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SpaceBullet {
	private boolean exploded=false;
	private int bulletx;
	private int bullety;
	private int bulletw;
	private int bulleth;
	private int explodetime=0;
	
    public SpaceBullet(int bulletx,int bullety,Image bulletPic){
    	ImageIcon IbulletPic= new ImageIcon(bulletPic); //temporarily create an image icon to get the bullet's height and width
    	this.bulletx=bulletx; //x coordinate
    	this.bullety=bullety; //y coordinate
    	bulletw= IbulletPic.getIconWidth(); //width
    	bulleth= IbulletPic.getIconHeight();//height
    }
    public int getx(){ 
    	return bulletx;
    }
    public int gety(){
    	return bullety;
    }
    public int getw(){
    	return bulletw;
    }
    public int geth(){
    	return bulleth;
    }
    public int gett(){ //gets the time of explosion
    	return explodetime;
    }
    public void explode(int time){ // explodes the bullet (kills it for colliding with another bullet)
    	exploded=true;
    	explodetime=time;
    }
    public boolean exploded(){ //checks if bullet has exploded (hasn't been shot by another a bullet)
    	return exploded;
    }	
    public void ptravel(int speed){ //moves the bullet object upwards (fired from a player)
    	for (int i=0;i<speed;i++){
    		bullety-=1;
    	}	
    }
    public void pReset(){ //creates the condition for the player to shoot a bullet again
    	bullety= -1;
    }
    public void etravel(int speed){ //moves the bullet object downwards (fired from an enemy)
   		bullety+=speed;	
    }
    public Boolean collision(SpaceBullet bullet){ //checks if a bullet has collided with another bullet
		int otherbulletx= bullet.getx();
		int otherbullety= bullet.gety();  
		int otherbulletw= bullet.getw();  
		int otherbulleth= bullet.geth();      
    
        if (new Rectangle(bulletx, bullety, bulletw, bulleth).intersects(new Rectangle(otherbulletx, otherbullety, otherbulletw, otherbulleth))){
			return true;
		}
        return false;
    }   
}