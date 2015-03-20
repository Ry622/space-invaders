/**
 * @(#)Enemy.java
 *
 *
 * @Shahir Chowdhury
 * @version 1.00 2015/1/23
 */
 
 /* This program is used to make Enemy objects and controls their shooting and movement */
 
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Enemy {
	private Image pic1; //sprite 1
	private Image pic2; //sprite 2
	private Image deathpic; //death image
	private Image displayedimage; //currently displayed image
	private boolean alive=true; //flag for if the enemy is alive
	private int enemyx;
	private int enemyy;
	private int enemyw;
	private int enemyh;
	private int pointval;
	private int deathtime=0; //time of the enemy's death
	
	private static int whereIHitTheWall=0; //the position where an enemy hits the wall to determine if the direction to change to should be right or left 
	private static boolean wallFlag=false; //flag for when an enemy hits the wall
	private static boolean direction=true; //flag for which direction enemy is currently moving in (true is right, false is left) 
	
	
    public Enemy(int enemyx,int enemyy,int pointval,Image pic1,Image pic2,Image deathpic) {
    	ImageIcon Ipic= new ImageIcon(pic1); //hit box will be constantly based off of the first image regardless of current image
    	this.enemyx=enemyx; //x coordinate
    	this.enemyy=enemyy; //y coordinate
    	enemyw= Ipic.getIconWidth(); //width
    	enemyh= Ipic.getIconHeight(); //height
    	this.pointval=pointval;
    	this.pic1=pic1; 
    	this.pic2=pic2;
    	this.deathpic=deathpic; 
    	displayedimage=pic1; //sets the current image to the first sprite
    }
   
    public int getx(){ 
    	return enemyx;
    }
    public int gety(){
    	return enemyy;
    }
    public int getw(){
    	return enemyw;
    }
    public int geth(){
    	return enemyh;
    }
    public Image geti(){ //gets current image
    	return displayedimage;
    }
    public int gett(){ //gets time of death
    	return deathtime;
    }
    public int getp(){ //gets enemy's point value
    	return pointval;
    }
    public boolean alive(){ //checks if enemy is alive
    	return alive;
    }
    public void die(int time){ //kills the enemy
    	displayedimage=deathpic;
    	deathtime= time;
    	alive=false;
    }
    public boolean chance(int potential){ //chance method controls chance of shooting 
    	int chance=(int)(Math.random()*100);
    	if (chance<potential){
    		return true;
    	}
    	return false;
    }
    public void shoot(SpacePanel spacepanel){ //enemy tells SpacePanel to make an enemy bullet object at its location 
    	if (chance(2)){ //only shoot if chance returns true and if it is time to make an action
    		spacepanel.makeEBullet(enemyx+enemyw/2,enemyy);
    	}
    }
    public static void wallBounce(int windoww,SpacePanel spacepanel){ 
    /* This method that detects when an enemy has collided with the sides of the wall,
     * signaling to the rest of the enemys to change direction and move down */
      
		if(wallFlag){ //if someone has the sides of the window
			if(whereIHitTheWall>windoww-100){ //right side
				direction = false;
			}
			else if(whereIHitTheWall<50){ //left side
				direction = true;
			}
			spacepanel.downCommand();//tell spacepanel to tell everyone to move down
	    	wallFlag=false;
		}
    }
    public void goDown(int movey,int movex){
    /* Moves the enemy down, and moves the enemy back one movement instance to avoid 
     * moving horizontally and vertically at the same time */
    	enemyy+=movey;
    	if (direction==true){
    		enemyx+=movex;
    	}
    	else if (direction==false){
    		enemyx-=movex;
    	}		
    }
    public void move(int windoww,int windowy,int movex,boolean movedown){ //moves the enemy 
		if ((direction==true && enemyx+enemyw>windoww-50) || (direction==false && enemyx<50) && movedown==false){ //if the enemy hits the wall in the direction they are moving 	
			wallFlag = true; 
			whereIHitTheWall=enemyx; //position of the wall
		}
		if (direction==true){ //move right
			enemyx+=movex;
		}
		else if(direction==false){ //move left
			enemyx-=movex;	
		}
		if (displayedimage== pic1){ //alternates sprites
			displayedimage=pic2;	
		}
		else if (displayedimage==pic2){ //alternates sprites
			displayedimage=pic1;
		}		
    }
    public boolean collision (SpaceBullet bullet){ //checks if the enemy has collided with a bullet
    	int bulletx=bullet.getx();
    	int bullety=bullet.gety();
    	int bulletw=bullet.getw();
    	int bulleth=bullet.geth();
    	
    	if (new Rectangle(enemyx,enemyy,enemyw,enemyh).intersects(new Rectangle(bulletx,bullety,bulletw,bulleth))){
			return true;
		}
        return false;
    }
    public Boolean playerCollision(Player player){ //checks if the enemy has collided with the player
    	int playx=player.getx();
    	int playy=player.gety();
    	int playw=player.getw();
    	int playh=player.geth();
    	
		if (new Rectangle(enemyx,enemyy,enemyw,enemyh).intersects(new Rectangle(playx,playy,playw,playh))){
			return true;
		}
        return false;
    }
    public boolean barrierCollision(BarrierBlock barrB){ //checks if the enemy has collided with a barrier block
    	int barrBx=barrB.getx();
    	int barrBy=barrB.gety();
    	int barrBw=barrB.getw();
    	int barrBh=barrB.geth();
    	if (new Rectangle(enemyx,enemyy,enemyw,enemyh).intersects(new Rectangle(barrBx,barrBy,barrBw,barrBh))){
			return true;
		}
        return false;
    } 
}

