/**
 * @(#)BarrierBlock.java
 *
 *
 * @Shahir Chowdhury
 * @version 1.00 2015/2/20
 */

/* This program is used to create BarrierBlock objects, the components of the Barrier object.
 * These "blocks" act as parts of the Barrier and will intercept SpaceBullets, while being destroyed in the process */ 

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BarrierBlock {
	private boolean alive=true;
	private int barrBx;
	private int barrBy;
	private int barrBw;
	private int barrBh;
	
    public BarrierBlock(int barrBx,int barrBy,int barrBw,int barrBh) {
    	this.barrBx=barrBx; //x coordinate
    	this.barrBy=barrBy; //y coordinate
    	this.barrBw= barrBw; 
    	this.barrBh= barrBh; 
    }
    
    public int getx(){ 
    	return barrBx;
    }
    public int gety(){
    	return barrBy;
    }
    public int getw(){
    	return barrBw;
    }
    public int geth(){
    	return barrBh;
    }
    public boolean alive(){ //checks if the barrier block is alive
    	return alive;
    }
    public void die(){ //kills the barrier block
    	alive=false;
    }
    public void revive(){ //brings the barrier block back to life to fight another day
    	alive=true;
    }
    public boolean collision(SpaceBullet bullet){ //checks if the barrier block has collided with a space bullet
    	int bulletx=bullet.getx();
    	int bullety=bullet.gety();
    	int bulletw=bullet.getw();
    	int bulleth=bullet.geth();
    	
    	if (new Rectangle(barrBx,barrBy,barrBw,barrBh).intersects(new Rectangle(bulletx,bullety,bulletw,bulleth))){
    		return true;
    	}
    	return false; 
    }   
}