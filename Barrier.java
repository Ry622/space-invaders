/**
 * @(#)Barrier.java
 *
 *
 * @Shahir Chowdhury 
 * @version 1.00 2015/2/11
 */

/* This program is used to make Barrier Objects, the sole defense for the player in the Space Invaders game. */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Barrier {
	private boolean alive=true;
	private int barrx;
	private int barry;
	private int barrw;
	private int barrh;
	private ArrayList<BarrierBlock> barrBList= new ArrayList<BarrierBlock>();

    public Barrier(int barrx,int barry,Image barrierPic) {
    	ImageIcon Ipic= new ImageIcon(barrierPic); //get the imageicon of the picture (to get the width and height)
    	this.barrx=barrx; //x coordinate
    	this.barry=barry; //y coordinate
		barrw= Ipic.getIconWidth(); //width
    	barrh= Ipic.getIconHeight(); //height
    	barrBMaker(5,5); //create the blocks that will make up the barrier
    }
    
    public int getx(){ 
    	return barrx;
    }
    public int gety(){
    	return barry;
    }
    public int getw(){
    	return barrw;
    }
    public int geth(){
    	return barrh;
    }
    public ArrayList<BarrierBlock> getb(){
    	return barrBList;
    }
    public void barrBMaker(int rowNum, int columnNum){  //creates a grid of barrier blocks 
    	for (int i=0;i<rowNum;i++){
    		for (int j=0;j<columnNum;j++){
    			BarrierBlock barrblock= new BarrierBlock(barrx+i*(barrw/rowNum),barry+j*(barrh/rowNum),barrw/rowNum,barrh/columnNum);
    			barrBList.add (barrblock);
    		}
    	}
    } 
}

