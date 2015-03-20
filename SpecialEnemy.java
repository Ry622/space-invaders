/**
 * @(#)SpecialEnemy.java
 *
 *
 * @Shahir Chowdhury
 * @version 1.00 2015/2/16
 */
 
 /* This program is used to make SpecialEnemy objects. It contains methods to control the special enemy's movements
  * as well as the chances of it appearing on the left or right side of the screen */
 
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SpecialEnemy {
	private Image pic;
	private Image deathpic;
	private Image displayedimage; //currently displayed image
	private boolean alive=true; //flag for if the special enemy is alive
	private boolean direction; 
	private int sEnemyx;
	private int sEnemyy;
	private int sEnemyw;
	private int sEnemyh;
	private int maxpointval=300; //maximum point value the special can give
	private int midpointval=150;
	private int minpointval=50; //minimum point value the special can give
	private int deathtime=0; //time of the special enemy's death
	
	
	
    public SpecialEnemy(int sEnemyx,int sEnemyy,Image pic,Image deathpic){
    	ImageIcon Ipic= new ImageIcon(pic); //hit box will be constantly based off of the first image regardless of current image
    	this.sEnemyx=sEnemyx; //x coordinate
    	this.sEnemyy=sEnemyy; //y coordinate
    	sEnemyw= Ipic.getIconWidth(); //width
    	sEnemyh= Ipic.getIconHeight(); //height
    	this.pic=pic;
    	this.deathpic=deathpic; 
    	displayedimage=pic;
    	if (sEnemyx<=0){ //special moves right if starting on the left side
    		direction=true;
    	}
    	else{ //special moves left if starting on the right side
    		direction=false;
    	}
    }
    public int getx(){ 
    	return sEnemyx;
    }
    public int gety(){
    	return sEnemyy;
    }
    public int getw(){
    	return sEnemyw;
    }
    public int geth(){
    	return sEnemyh;
    }
    public Image geti(){ //gets current image
    	return displayedimage;
    }
    public int gett(){ //gets time of death
    	return deathtime;
    }
    public int getp(int windoww){ 
    /* This method is used to get the special enemy's point value. The special's  point value changes depending on
     * if it is in the first quarter of the screen(going horizontally), inbetween the first and third quarters, or in
     * the third quarter. The point value for these quarters also changes depending on if the special started on the left
     * or on the right */
    	if (direction==true){ //starting left
    		if (sEnemyx+sEnemyw<windoww*1/4){ //max point value at starting location
    			return maxpointval;
    		}
    		else if (sEnemyx+sEnemyw>=windoww*1/4 && sEnemyx+sEnemyw<=windoww*3/4){
    			return midpointval;
    		}
    		else if (sEnemyx+sEnemyw>windoww*3/4){ 
    			return minpointval;
    		}
    	}
    	if (direction==false){ //starting right
    		if (sEnemyx<windoww*1/4){ //min point value at ending location
    			return minpointval;
    		}
    		else if (sEnemyx>=windoww*1/4 && sEnemyx<=windoww*3/4){
    			return midpointval;
    		}
    		else if (sEnemyx>windoww*3/4){//max point value at starting location
    			return maxpointval;
    		}
    	}
    	return 0;
    }
    public boolean alive(){ //checks if special enemy is alive
    	return alive;
    }
    public void die(int time){ //kills the special enemy
    	displayedimage=deathpic;
    	deathtime= time;
    	alive=false;
    }
    public void reset(){ //reset the special's position, moving them off the screen and not giving the player any poitns
    	if (direction==true){
    		sEnemyx=1000;	
    	}
    	else if (direction==false){
    		sEnemyx=-1000;
    	}
    }
    public boolean chance(int potential){ //chance method controls chance of shooting 
    	int chance=(int)(Math.random()*100);
    	if (chance<potential){
    		return true;
    	}
    	return false;
    }
    public void specialCommand(int windoww,int starty,SpacePanel spacepanel){ //tells the SpacePanel to create a special enemy object 
    	boolean startside=chance(50);
    	if (startside==true){
    		spacepanel.makeSpecial(-sEnemyw,starty);
    	}
    	else if (startside==false){
    		spacepanel.makeSpecial(windoww,starty);
    	}
    }  
    public void move(int windoww,int movex){ //moves the special enemy
    	if (direction==true){
    		sEnemyx+=movex;
    	}
    	else if (direction==false){
    		sEnemyx-=movex;
    	}
    }
     public boolean collision (SpaceBullet bullet){ //checks if a bullet has collided with the special enemy
    	int bulletx=bullet.getx();
    	int bullety=bullet.gety();
    	int bulletw=bullet.getw();
    	int bulleth=bullet.geth();
    	
    	if (new Rectangle(sEnemyx,sEnemyy,sEnemyw,sEnemyh).intersects(new Rectangle(bulletx, bullety, bulletw, bulleth))){
			return true;
		}
        return false;
    }
}