/**
 * @(#)Player.java
 *
 *
 * @Shahir Chowdhury
 * @version 1.00 2015/1/17
 */
 
 /* This prorgam creates Player objects. It contains all methods regarding to moving and shooting with the player. 
  *	It is used to relay information back to the SpacePanel where all of the player object's calculations are made use of */ 
 
 
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Player {
	private boolean alive=true; //flag for if the player is alive
	private int playx;
	private int playy;
	private int playw;
	private int playh;
		
    public Player(int playx,int playy,Image playerPic){
    	ImageIcon IplayerPic= new ImageIcon(playerPic);//temporarily create an image icon to get the player's height and width
    	this.playx=playx; //x coordinate
    	this.playy=playy; //y coordinate
    	playw= IplayerPic.getIconWidth(); //width 
    	playh= IplayerPic.getIconHeight();//height 
    }
    
    public int getx(){ 
    	return playx;
    }
    public int gety(){
    	return playy;
    }
    public int getw(){
    	return playw;
    }
    public int geth(){
    	return playh;
    }
    public boolean alive(){ //checks if player is alive
    	return alive;
    }
    public void die(){ //kills the player
    	alive=false;
    }
    public void shoot(int bulletdist,SpacePanel spacepanel){ //tells the SpacePanel to create a player bullet object at the player's location
    	if (bulletdist<0){ //only shoot if the player's bullet object is off the screen
    		spacepanel.makePBullet(playx+playw/2,playy);
    	}
    }  
    public int move(int x,int windoww){ //moves the player
    	playx+=x;
    	playx=playx+playw>650 ? 650-playw:playx; //reposition player on screen if they attempt to go off the right side of the screen 
    	playx=playx<50? 50:playx;  //reposition the player on screen if they attempt to go off the left side of the screen
    	return playx;
    }
    public Boolean collision(SpaceBullet bullet){ //checks if a bullet has collided with the player
    	int bulletx=bullet.getx(); 
    	int bullety=bullet.gety();
    	int bulletw=bullet.getw();
    	int bulleth=bullet.geth();
    	
        if (new Rectangle(playx,playy,playw,playh).intersects(new Rectangle(bulletx,bullety,bulletw,bulleth))){
			return true;
		}
        return false;
    }   
}