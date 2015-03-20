/**
 * @(#)Spaceinvaders.java
 *
 *
 * @Shahir Chowdhury
 * @version 1.00 2015/1/14
 */

/* This program creates the Spaceinvader's game loop and is used to run the game */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//import java.awt.MouseInfo;

public class SpaceInvaders extends JFrame implements ActionListener{
	Timer myTimer;   
	SpacePanel game;
		
    public SpaceInvaders() {
		super("Space Invaders");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700,750);

		myTimer = new Timer(10, this);	 // trigger every 10 ms
		this.setTitle(title());

		game = new SpacePanel(this);
		add(game);

		setResizable(false);
		setVisible(true);
    }
	public void start(){
		myTimer.start();
	}
	public void actionPerformed(ActionEvent evt){
		game.input(); //takes in all player input
		game.update(); //move all objects in the game
		game.repaint(); //updates the game
	}
    public static void main(String[] arguments) {
		SpaceInvaders frame = new SpaceInvaders();		
    }
    public String title(){ //changes the title of the window to something creative
    	int chance=(int)(Math.random()*100);
    	if (chance<25){
    		return "Space Invaders, Defenders of the Green Line";
    	}
    	else if (chance>25 && chance<50){
    		return "Space Invaders (They use bows and arrows!)";
    	}
    	else if (chance>50 && chance<75){
    		return "Space Invaders, Avengers of the Fallen Walls";
    	}
    	else if (chance>75 && chance<99){
    		return "Space Invaders";
    	}
    	else{ //the 1 percent
    		return "Space Invaders 2, Return of the Frank Gu";
    	}
    }
}



