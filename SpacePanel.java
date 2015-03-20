/**
 * @(#)SpacePanel.java
 *
 *
 * @Shahir Chowdhury
 * @version 1.00 2015/1/17
 */

/* This program creates a SpacePanel object that oversees all objects in the game */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import javax.swing.*;
import javax.swing.text.*;
import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import javax.sound.sampled.AudioSystem;
import java.applet.*;

class SpacePanel extends JPanel implements KeyListener{
	private Image playerPic,barrierPic,playerbulletPic,enemybulletPic,leonPic,denisPic1,denisPic2,frankPic1,frankPic2,tristhalPic1,tristhalPic2;
	private Image playerdeathPic,enemydeathPic,sEnemydeathPic,bulletdeathPic,barrBdeathPic; 
	private AudioClip playershoot,enemyshoot,playerdeath,sEnemyspawn,gameoversound;
	private Font spacefontS,spacefontM,spacefontL;
	private File scorefile= new File ("HIGHSCORES.txt");

	private boolean []keys;
	private boolean movedown=false; //flag to tell enemies to move down one row
	private boolean gotlifeflag=false; //flag to prevent player from getting more than one life per 1000 points
	private boolean successfulhigh=false; //flag that allows player to input a name if they managed to place on the highscores board
	private boolean pauseflag=false; //flag to run the game unless the game is paused (false is unpaused, true is paused)
	private boolean respawnpause=false; //flag to temporarily pause the game so player may respawn safely
	private boolean levelpause=false; //flag to temporarily pause the game so player can mentally prepare for next level
	public boolean overpause=false; //flag to temporarily pause the game once the player has lost for dramatic effect
	private boolean gameoverflag=false;  
		
	private String currentscreen="Menu"; //current screen the player is viewing
	private String playername="Frank"; //the player's default name
	
	private int playerstartx=50; //player starting position values
	private int playerstarty=592;
	private int enemystartx=32; //enemy starting position values 
	private int enemystarty=176;
	private int sEnemystarty=130; //special enemy starting y value (x doesn't matter as long as special enemy starts offscreen)
	private int pbulletstarty=-1; //player bullet starting y value (x doesn't matter as long as player bullet starts offscreen)
	private int barrierstartx=50;//barrier starting position values
	private int barrierstarty=512;	
	private int barriernum=4; //number of barriers
	private int enemyrownum=5; //number of rows of enemies
	private int enemyperrow=11;	//enemies per row
	private int enemytotal=enemyrownum*enemyperrow;
	private int movex=3; //player horizontal move speed
	private int emovex=10; //enemy horizontal move speed
	private int emovey=16; //enemy vertical move speed
	private int sEmovex=2; //special enemy move speed	
	private int edeathtotal=0; //counter for number of enemies killed 
	private int sSpawntime=2000; //threshhold for when special enemy can be spawned
	private int ebulletspeed=4; //enemy bullet speed
	private int pbulletspeed=6; //player bullet speed	
	private int score=0;
	private int lowesthigh; //lowest high score
	private int nextscore=1000; //score required to gain a life
	private int lives=3;
	private int time=0; //counter used to set intervals for enemy actions
	private int defaultthresh=90; //default threshhold
	private int threshhold=defaultthresh; //time intervals for enemy actions
	private int loadtimer=0; //timer to add suspense inbetween game starting and player selecting the play button
	private int maxload=300; //time required for game to start
	private int pausetimer=0; //timer to avoid case in which pausing breaks by holding down the pause key (P) a bit too long
	private int maxptime=50; //time required to pause/unpause again	
	private int respawntimer=0; //timer to pause game when player dies
	private int maxrespawntime=200; //unpauses the game once respawntimer reaches this value	
	private int level=1; 
	private int leveltimer=0; //timer to pause game when player completes a level
	private int maxleveltime=200; //unpauses the game once leveltimer reaches this value	
	private int gameovertimer=0; ///timer to pause the game when the player loses
	private int maxovertime=300; //unpauses the game once gameovertimer reaches this value
	private int windoww=700; //window width
	private int windowh=750; //window height
	
	private ArrayList<SpaceBullet> enemybullets= new ArrayList<SpaceBullet>(); //list of all enemy bullet objects
	private ArrayList<Enemy> enemylist= new ArrayList<Enemy>(); //list of all enemy objects
	private ArrayList<Barrier>barrierlist= new ArrayList<Barrier>(); //list of all barrier objects
	private ArrayList<SpaceBullet> explosionlist= new ArrayList<SpaceBullet>(); //list of all dead bullets that have collided with another bullet
	private ArrayList<String> namelist=new ArrayList<String>(); //list of all the highscore names
	private ArrayList<String> scorelist=new ArrayList<String>(); //list of all the highscore scores
	
	private Player player; 
	private SpaceBullet playerbullet; //the player only has one bullet on screen at a time
	private SpecialEnemy special; //only one special enemy on screen at a time
	private SpaceInvaders mainFrame;
	
	public SpacePanel(SpaceInvaders m){ 
		keys = new boolean[KeyEvent.KEY_LAST+1];
		mainFrame = m;
		
		playerPic= new ImageIcon("Space Invader Sprites/Pics/Player.PNG").getImage(); //pictures
		playerdeathPic=new ImageIcon("Space Invader Sprites/Pics/Player Explosion.PNG").getImage();
		
		denisPic1= new ImageIcon("Space Invader Sprites/Pics/Denis1.PNG").getImage();
		denisPic2= new ImageIcon("Space Invader Sprites/Pics/Denis2.PNG").getImage();
		frankPic1= new ImageIcon("Space Invader Sprites/Pics/Frank1.PNG").getImage();
		frankPic2= new ImageIcon("Space Invader Sprites/Pics/Frank2.PNG").getImage();
		tristhalPic1= new ImageIcon("Space Invader Sprites/Pics/Tristhal1.PNG").getImage();
		tristhalPic2= new ImageIcon("Space Invader Sprites/Pics/Tristhal2.PNG").getImage();
		enemydeathPic= new ImageIcon("Space Invader Sprites/pics/Enemy Explosion.PNG").getImage();
		
		leonPic= new ImageIcon("Space Invader Sprites/Pics/Leon.PNG").getImage();
		sEnemydeathPic= new ImageIcon("Space Invader Sprites/Pics/Special Enemy Explosion.PNG").getImage();
		
		playerbulletPic= new ImageIcon("Space Invader Sprites/Pics/Player Bullet.PNG").getImage();
		enemybulletPic= new ImageIcon("Space Invader Sprites/Pics/Enemy Bullet.PNG").getImage();
		bulletdeathPic= new ImageIcon("Space Invader Sprites/Pics/Bullet Explosion.PNG").getImage();
		
		barrierPic= new ImageIcon("Space Invader Sprites/Pics/Barrier.PNG").getImage();
		barrBdeathPic= new ImageIcon("Space Invader Sprites/Pics/Destroyed Barrier Block.PNG").getImage();

		playershoot=Applet.newAudioClip(getClass().getResource("Space Invader Sounds/Player Shoot.wav")); //sound effects
		enemyshoot= Applet.newAudioClip(getClass().getResource("Space Invader Sounds/Enemy Shoot.wav"));
		playerdeath=Applet.newAudioClip(getClass().getResource("Space Invader Sounds/Player Death Sound.wav"));
		sEnemyspawn=Applet.newAudioClip(getClass().getResource("Space Invader Sounds/Special Enemy Spawned.wav"));
		gameoversound=Applet.newAudioClip(getClass().getResource("Space Invader Sounds/Game Over Sound.wav"));
		
		try{ //fonts sizes
			spacefontS=Font.createFont(Font.TRUETYPE_FONT,new FileInputStream(new File("Space Invaders Font.TTF"))).deriveFont(Font.PLAIN,12);
			spacefontM=Font.createFont(Font.TRUETYPE_FONT,new FileInputStream(new File("Space Invaders Font.TTF"))).deriveFont(Font.PLAIN,24);
			spacefontL=Font.createFont(Font.TRUETYPE_FONT,new FileInputStream(new File("Space Invaders Font.TTF"))).deriveFont(Font.PLAIN,36);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		player= new Player (playerstartx,playerstarty,playerPic); //player 
		playerbullet=new SpaceBullet (-300,pbulletstarty,playerbulletPic); //creates the one and only player bullet	
		special=new SpecialEnemy (-300,sEnemystarty,leonPic,sEnemydeathPic); //special enemy
		makeEnemies(enemylist,enemyrownum,enemyperrow,enemystartx,enemystarty); //enemies
		makeBarriers(barrierlist,barriernum,barrierstartx,barrierstarty); //barriers
		readscorelist(); //get all of the highscores
		lowesthigh= Integer.parseInt(scorelist.get(0));
				
		setSize(windoww,windowh);
        addKeyListener(this);
	}
	
    public void addNotify() {
        super.addNotify();
        requestFocus();
        mainFrame.start();
    }
	public void input(){//gets all user input and updates the player
		if (currentscreen=="Menu"){
			if (keys[KeyEvent.VK_A]){
				currentscreen="Loading";			
			}
			if (keys[KeyEvent.VK_B]){
				currentscreen="Help";
			}
			if (keys[KeyEvent.VK_X]){
				currentscreen="Highscores";
			}
		}
		if (currentscreen=="Loading"){
			loadtimer+=1; 
			if (loadtimer>maxload){ //move to game screen once load timer hits a certain value
				loadtimer=0;
				currentscreen="Game";
			}
		}
		if (currentscreen=="Help"){
			if (keys[KeyEvent.VK_Y]){
				currentscreen="Menu";
			}
		}
		if (currentscreen=="Highscores"){
			if (keys[KeyEvent.VK_Y]){
				currentscreen="Menu";
			}
		}
		if (currentscreen=="Entering Highscores"){
			if (keys[KeyEvent.VK_A]){
				playername=randomname();
			}
			if (keys[KeyEvent.VK_Y]){
				enterhighscore();
				currentscreen="Menu";
			}
		}
		if (currentscreen=="Game"){	
			if (player.alive() && respawnpause==false && levelpause==false && gameoverflag==false){ //do not allow player input if a non-player pause event is taking place
				if (keys[KeyEvent.VK_P]){ //player pause and unpause
					if (pauseflag==false && pausetimer>maxptime){ //pause
						pause();
						pausetimer=0; //reset pause timer
					}
					if (pauseflag==true && pausetimer>maxptime){ //unpause
						unpause();
						pausetimer=0; //reset pause timer
					}
				}
	
				if (pauseflag==false){ //game only allows player input if unpaused
					if(keys[KeyEvent.VK_RIGHT] ){ // moves player right
						for (int i=0;i<movex;i++){
							player.move(1,getWidth()-50);
						}
					}
					if(keys[KeyEvent.VK_LEFT] ){ //moves player left
						for (int i=0;i<movex;i++){
							player.move(-1,getWidth());
						}
					}
					if(keys[KeyEvent.VK_SPACE] ){ //shoot
						player.shoot(playerbullet.gety(),this);
					}	
				}
			}
		}	
	}
	public void update(){ //updates the rest of the game if in the game screen
		if (currentscreen=="Game"){
			if (pauseflag==false && respawnpause==false && levelpause==false && gameoverflag==false){ //only updates the game if unpaused
				time+=1;	//time counter for enemy movements		
				if (time!=0 && time%sSpawntime==0){ //create a special enemy after a set number of frames
					special.specialCommand(windoww,sEnemystarty,this);
				}
				if (special.alive() && special.getx()+special.getw()>=0 && special.getx()<=windoww){ //special enemy
					for (int m=0;m<sEmovex;m++){ //move all special enemy incrementally so collision checks are done every pixel
						special.move(windoww,1);
						if (special.alive() && special.collision(playerbullet)){ //special enemy on player bullet collision
							special.die(time);
							playerbullet.pReset(); //reset player bullet
							score+=special.getp(windoww); //add special enemy's point value to score
							addlife(); //if score has increased by 1000 points, give the player an extra life
						}
					}
				}
				if (time%threshhold==0){//enemy only acts if the threshhold has been reached
					for (int i=0;i<enemylist.size();i++){ //enemies
						if (enemylist.get(i).alive()){ 
							enemylist.get(i).shoot(this);
							enemylist.get(i).move(windoww,windowh-101,emovex,movedown);
							if (enemylist.get(i).gety()>playerstarty+16){ //player loses the game if enemies  manage to get behind him/her
								special.reset();
								gameover();
							}
							if	(player.alive() && enemylist.get(i).playerCollision(player)){ //enemy on player collision
								playerdeath.play();
								player.die();
								lives-=lives; //player loses the game if enemies manage to touch him/her
								respawntimer=0; //restart spawn timer
								respawn(); //check if player has the lives to respawn, otherwise game over
							}
							for (int b=0;b<barrierlist.size();b++){ //check through barriers to see if enemies are touching any barrier blocks 
								Barrier barr=barrierlist.get(b);
								for (int bb=barr.getb().size();bb>0;bb--){ //iterate backwards through the arraylist to avoid errors
									BarrierBlock barrB=barr.getb().get(bb-1);
									if (barrB.alive() && enemylist.get(i).barrierCollision(barrB)){ //enemy on barrier block collision
										barrB.die();
										break;
									}
								}
							}
						}
					}			
				}
				for (int i=0;i< enemybullets.size();i++){ //enemy bullets
					if (enemybullets.get(i).gety()<=windowh-101){
						for (int m=0;m<ebulletspeed;m++){ //incremental for proper collision checks
							enemybullets.get(i).etravel(1);
							if (player.alive() && player.collision (enemybullets.get(i))){ //enemy bullet on player collision
								enemybullets.remove(i);
								playerdeath.play();
								player.die();
								lives-=1;
								respawntimer=0; //restart spawn timer
								respawn();  //check if player has the lives to respawn, otherwise game over
								break;
							}
						}
					}
				}
				for (int b=0;b<barrierlist.size();b++){ //barriers
					Barrier barr=barrierlist.get(b);
					for (int bb=barr.getb().size();bb>0;bb--){ //barrier blocks
						BarrierBlock barrB=barr.getb().get(bb-1);
						if (barrB.alive() && barrB.collision(playerbullet)){ //barrier block on player bullet collision
							barrB.die();
							playerbullet.pReset();
							break;
						}
						for (int j=0;j<enemybullets.size();j++){
							if (barrB.alive() && barrB.collision(enemybullets.get(j))){ //barrier block on enemy bullet collision
								barrB.die();
								enemybullets.remove(j);
								break;
							}
						}			
					}
				}						
				if (playerbullet.gety()>=0){ //player bullet
					for (int m=0;m<pbulletspeed;m++){ //player bullet travels faster than enemy's for game balance
						playerbullet.ptravel(1); 
						for (int i=0;i<enemylist.size();i++){
							if (enemylist.get(i).alive() && enemylist.get(i).collision(playerbullet)){ //player bullet on enemy collision
								playerbullet.pReset();
								enemylist.get(i).die(time); //kill the enemy and get their time of death
								edeathtotal+=1;
								score+=enemylist.get(i).getp(); //increase score by the enemy's point value
								addlife(); //check if player can be awarded another life
								threshhold-=1; //decrease the time interval required for the enemy to move
								threshhold= threshhold<=2? 2:threshhold; //decrease the frames required for the enemy to move
								if (edeathtotal==enemytotal){ //procced to next level if all enemies have been wiped out from current stage
									leveltimer=0; //restart level timer   
									nextlevel();
								}
							}	
						}
						for (int i=0;i<enemybullets.size();i++){ //player bullet on enemy bullet collision
							if (playerbullet.collision(enemybullets.get(i))){
								playerbullet.pReset();
								enemybullets.get(i).explode(time); //record the time at which the enemy bullet exploded
								explosionlist.add(enemybullets.get(i)); //add exploded bullets to the list of bullet explosions to draw
								enemybullets.remove(i);
								break;
							}
						}
					}	 
				}					
				if (time%threshhold==0){  //check if any enemies hit the wall after moving
					Enemy.wallBounce(windoww,this);
				}
				if (movedown==true){ //if enemies were told to move down
					movedown=false;
					for(int i=0;i<enemylist.size();i++){ //move down one row
						enemylist.get(i).goDown(emovey,emovex);		
					}
					threshhold-=2; //decrease the time interval required for the enemy to move	
					threshhold= threshhold<=2? 2:threshhold; //decrease the time interval required for the enemy to move
				}			
			}
			if (respawnpause==false && levelpause==false){ //increase the pausetimer if no non-player pause related event is taking place
				pausetimer+=1; //pausing and unpausing can only be done if pausetimer has reached a certain value after every update frame	
			}
			else if (respawnpause==true){ //game will unpause once respawntimer has reached a certain value after every update frame
				respawn();
				respawntimer+=1;
			}
			else if (levelpause==true){ //game will unpause once leveltimer has reached a certain value after every update frame
				nextlevel();
				leveltimer+=1;
			}
		}
		if (overpause==true){
			gameovertimer+=1;
			gameover();
		} 
	}
	
	public void pause(){ //pause
		pauseflag=true;
	}
	public void unpause(){ //unpause
		pauseflag=false;	
	}
	
	public void downCommand(){ //signals enemies to move down a row
		movedown=true;
	}
	public void addlife(){ //gives the player an addtional life
		if (gotlifeflag==false && score>=nextscore && score!=0){ //player can only recieve life if they have earned 1000 points 
			lives+=1;
			nextscore=score-(score%1000)+1000; //set the value of the next 1000 points required to recieve an extra life
			gotlifeflag=true; //flag to prevent player from earning multiple lives per 1000 points
		}
		else{
			gotlifeflag=false;
		}
	}
	public void respawn(){ //respawns the player after dying
		for (int i=0;i<enemybullets.size();i++){ //clear the screen of bullets
			enemybullets.remove(i);
		}
		playerbullet.pReset();
		if (respawntimer<maxrespawntime){ //check if it is time to unpause
			respawnpause=true;
		}
		else{ 
			respawnpause=false;
		}
		if (lives>0 && respawnpause==false){ //allow player to respawn if they have 1 or more lives remaining
			player= new Player (playerstartx,playerstarty,playerPic);
		}
		else if (lives==0){ //ends the game if player is out of lives
			gameover();
		}
	}
	public void nextlevel(){ //begins the next level in the game
		if (leveltimer<maxleveltime){ //check if it is time to unpause
			levelpause=true;
		}
		else{
			levelpause=false;
		}
		if (levelpause==false){
			edeathtotal=0;
			level+=1;
			time+=100; //increase time to remove remaining enemy explosion pictures 
			threshhold=defaultthresh-(2*level)+1; //decrease starting threshhold to make enemies act faster 
			if (time%threshhold==0){ //reduce time by threshhold to start next wave of enemies on a frame where they will not act  
				time-=threshhold;
			}
			for (int i=0;i<enemybullets.size();i++){ //clean screen of bullets
				enemybullets.remove(i);
			}
			special.reset();
			playerbullet.pReset();
			makeEnemies(enemylist,enemyrownum,enemyperrow,enemystartx,enemystarty); //spawn a new wave of enemies
			for (int i=0;i<barrierlist.size();i++){ //respawn all dead barrier blocks
				Barrier barr= barrierlist.get(i);
				for (int j=barr.getb().size();j>0;j--){ 
					BarrierBlock barrB= barr.getb().get(j-1);
					if (barrB.alive()==false){
						barrB.revive();
					} 
				}
			}	
		}
	}
	public void gameover(){ //Ends the game and checks if the player managed to place in the highscores board
		gameoverflag=true;
		if (gameovertimer<maxovertime){ //check if it is time to unpause
			overpause=true;
		}
		else{
			overpause=false;
		}
		if(overpause==false){
			if (score>lowesthigh){ //player will submit a name if they made it on the highscores boad
				successfulhigh=true;
				currentscreen="Entering Highscores";
				
			}
			else if (successfulhigh==false){ //proceed to menu otherwise
				reset();
				currentscreen="Menu";
			}
		}
	}
	public void reset(){ //reset all game variables to their default values
		score=0;
		level=1;
		lives=3;
		time=0;
		threshhold=defaultthresh;
		special.reset();
		player=	player= new Player (playerstartx,playerstarty,playerPic);
		playerbullet.pReset();
		for (int i=0;i<enemylist.size();i++){
			enemylist.get(i).die(time);
		}
		edeathtotal=0;
		time+=100;
		makeEnemies(enemylist,enemyrownum,enemyperrow,enemystartx,enemystarty);
		for (int i=0;i<enemybullets.size();i++){ //clean screen of bullets
			enemybullets.remove(i);
		}  
		for (int i=0;i<barrierlist.size();i++){ //respawn all dead barrier blocks
			Barrier barr= barrierlist.get(i);
			for (int j=barr.getb().size();j>0;j--){ 
				BarrierBlock barrB= barr.getb().get(j-1);
				if (barrB.alive()==false){
					barrB.revive();
				} 
			}
		}
		gameovertimer=0;
		playername="Frank";
		gameoverflag=false;	
	}
	public String randomname(){ //generates a random name for the player
		String finalname="";
		for (int i=0;i<5;i+=1){ 
    		finalname+= (char)(Math.random()*26+'A');   
    	}
    	return finalname;
	}
	public void enterhighscore(){ //enters the player's score into the highscores list
		for (int i=scorelist.size();i>0;i--){
			int currentscore=Integer.parseInt(scorelist.get(i-1)); //current score value in position on the top 5 scores
			if (score>currentscore){ //if the player's score is greater than the current score
				String newscore= Integer.toString(score);
				scorelist.add(i,newscore); //the player's score now replaces the score in that position
				namelist.add(i,playername);
				scorelist.remove(0); //shift all the scores down
				namelist.remove(0);
				lowesthigh= Integer.parseInt(scorelist.get(0));
				successfulhigh=false;
				reset();
				break;
			}	
		}
		FileWriter writer= null; 
		try{
			writer= new FileWriter(scorefile,false); //rewrite a new highscores file
			for (int i=0;i<scorelist.size();i++){
				writer.write(namelist.get(i)+","+scorelist.get(i)+"\n");	
			}
			writer.close();
		}
		catch (IOException ex){
			System.out.println(ex);
		}
	}	
	public void readscorelist(){ //gets the names and score avlues from the highscores text file
		Scanner infile= null;
		try{
    		infile= new Scanner(new File ("HIGHSCORES.txt"));
    	}
    	catch (IOException ex){
    		System.out.println(ex);
    	}
    	for (int i=0;i<5;i++){ //max of 5 names on the highscore list (magical numbers)
    		String scorestring=infile.nextLine(); 
    		String[]namescore= scorestring.split(",");
    		namelist.add(namescore[0]);
    		scorelist.add(namescore[1]);
    	}
	}	
		
	public void makePBullet(int x, int y){ //make player bullets that fly upwards
		playerbullet=new SpaceBullet(x,y,playerbulletPic);
		playershoot.play();	//sound effect
	}
	public void makeEBullet(int x, int y){ //make enemy bullets that fly downwards 
		SpaceBullet enemybullet=new SpaceBullet(x,y,enemybulletPic);
		enemybullets.add(enemybullet);
		enemyshoot.play(); //sound effect
	}
	public void makeBarriers(ArrayList<Barrier> barrierlist,int barrPerRow, int startx,int starty){ //method to create all barrier objects
		for (int i=0;i<barrPerRow;i++){		
			Barrier barrier=new Barrier(startx+(i*175),starty,barrierPic);
			barrierlist.add(barrier);
		}
	}
	public void makeSpecial(int x,int y){ //makes the special enemy
		special=new SpecialEnemy(x,y,leonPic,sEnemydeathPic);
		sEnemyspawn.play();
	}	
	public void makeEnemies(ArrayList<Enemy> enemylist,int enemyrows,int enemyPerRow,int startx,int starty){ //method to create all enemy objects
		for (int i=0;i<enemyrows;i++){ //number of enemy rows
			int x=startx; //x value that can be reset to the original
			for (int j=0;j<enemyPerRow;j++){ 
				if (i<1){//enemy type 1
					Enemy enemy= new Enemy (x+(int)2.5+(j*48),starty,30,denisPic1,denisPic2,enemydeathPic); //x+2.5 to centre the skinny enemy type 1
					enemylist.add(enemy);
				}
				else if (i>=1 && i<3){//enemy type 2
					Enemy enemy= new Enemy (x+(j*48),starty,20,frankPic1,frankPic2,enemydeathPic);
					enemylist.add(enemy);
				}
				else if (i>=3){//enemy type 3
					Enemy enemy= new Enemy (x+(j*48),starty,10,tristhalPic1,tristhalPic2,enemydeathPic);
					enemylist.add(enemy);
				}
			}
			x=startx; //reset the x value to go back to the beginning of the row
			starty+=48;	 //move down to make next row;
		}		
	}	
	
    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
    public void paintComponent(Graphics g){ //paints the window
		String stringScore= Integer.toString(score);
		String highscore= Integer.toString(lowesthigh);
		String lifestring= (" x "+Integer.toString(lives));
		String levelstring=(Integer.toString(level));
    	if (currentscreen=="Menu"){
    		g.setColor(new Color(0,0,0));
	    	g.fillRect(0,0,getWidth(),getHeight()); //background
	    	g.setColor(new Color(0,252,0)); 
	    	g.fillRect(windoww*1/2-135,250,270,70); //play button
	    	g.fillRect(windoww*1/2-135,350,270,70); //help button
	    	g.setColor(new Color(237,28,36));
	    	g.fillRect(windoww*1/2-135,550,270,70); //highscores button
	    	
	    	g.setColor(new Color(255,255,255));
	    	g.setFont(spacefontL);
	    	g.drawString("SPACE INVADERS",windoww*1/2-170,150); //title
	    	g.drawString("PLAY",windoww*1/2-50,300); 
	    	g.drawString("HELP",windoww*1/2-50,400);
	    	g.drawString("HIGHSCORES",windoww*1/2-130,600);
	    	
	    	g.setFont(spacefontM);
	    	g.drawString("[A]",windoww*1/2-200,300); //play button
	    	g.drawString("[A]",windoww*1/2+161,300);
	    	g.drawString("[B]",windoww*1/2-200,400); //help button
	    	g.drawString("[B]",windoww*1/2+161,400);
	    	g.drawString("[X]",windoww*1/2-200,600); //highscores button
	    	g.drawString("[X]",windoww*1/2+161,600);
	    	g.drawString("USE YOUR KEYBOARD",windoww*1/2-142,680); //helpful text
	    	
	    	g.drawImage(playerPic,600,600,this); //art
	    	g.drawImage(playerbulletPic,610,450,this);
	    	g.drawImage(playerbulletPic,630,500,this);
	    	g.drawImage(playerbulletPic,610,550,this);
	    	g.drawImage(enemydeathPic,595,300,this);
	    	g.drawImage(enemydeathPic,605,360,this);
	    	g.drawImage(enemydeathPic,620,330,this);
	    	g.drawImage(denisPic1,100,300,this);
	    	g.drawImage(tristhalPic2,70,360,this);
	    	g.drawImage(frankPic1,40,330,this);
	    	g.drawImage(enemybulletPic,85,450,this);
	    	g.drawImage(enemybulletPic,110,550,this);
	    	g.drawImage(enemybulletPic,50,500,this);
	    	g.drawImage(playerdeathPic,85,600,this);
	    	g.drawImage(leonPic,windoww*1/2+20,75,this);
	    	g.drawImage(leonPic,320,60,this);
	    	g.drawImage(leonPic,270,70,this);
    	}
    	if (currentscreen=="Highscores"){
     		g.setColor(new Color(0,0,0));
	    	g.fillRect(0,0,getWidth(),getHeight()); //background
     		g.setColor(new Color(255,255,255));
			g.setFont(spacefontL);
     		g.drawString("HIGHSCORES",windoww*1/2-125,100);
     		g.setFont(spacefontM);
     		g.drawString("NAME",150,175);
     		g.drawString("SCORE",450,175);
     		for (int i=scorelist.size()-1;i>=0;i--){
				g.drawString(namelist.get(i),150,440-(i*45));
				g.drawString(scorelist.get(i),450,440-(i*45));
     		}
	    	g.setColor(new Color(237,28,36));
	    	g.fillRect(windoww*1/2-135,550,270,70);
	    	g.setColor(new Color(255,255,255));
	    	g.setFont(spacefontL);
	    	g.drawString("MENU",windoww*1/2-53,600);
	    	g.setFont(spacefontM);
	    	g.drawString("[Y]",windoww*1/2-200,600); //menu button
	    	g.drawString("[Y]",windoww*1/2+161,600);
    	}
    	if (currentscreen=="Help"){
    		g.setColor(new Color(0,0,0));
	    	g.fillRect(0,0,getWidth(),getHeight()); //background
	    	g.setColor(new Color(255,255,255));
	    	g.setFont(spacefontL);
	    	g.drawString("HELP",windoww*1/2-50,100);
	    	g.setFont(spacefontM);
	    	g.drawString("Controls",windoww*1/2-70,200);
	    	g.setFont(spacefontS);
	    	g.drawString("Use the left and right arrow keys to move and use the spacebar to shoot",windoww*1/2-275,300);
	    	g.drawString("Press P to pause",windoww*1/2-61,350);
	    	
	    	
	    	g.setColor(new Color(237,28,36));
	    	g.fillRect(windoww*1/2-135,550,270,70);
	    	g.setColor(new Color(255,255,255));
	    	g.setFont(spacefontL);
	    	g.drawString("MENU",windoww*1/2-53,600);
	    	g.setFont(spacefontM);
	    	g.drawString("[Y]",windoww*1/2-200,600); //menu button
	    	g.drawString("[Y]",windoww*1/2+161,600);
    	}
    	if (currentscreen=="Entering Highscores"){
    		g.setColor(new Color(0,0,0));
	    	g.fillRect(0,0,getWidth(),getHeight()); //background
	    	g.setColor(new Color(255,255,255));
	    	g.setFont(spacefontL);
	    	g.drawString("CONGRATULATIONS",155,100);
	    	g.setFont(spacefontM);
	    	g.drawString("You beat someone's high score!",113,200); 
	    	g.setFont(spacefontS);
	    	g.drawString("Please submit a random name to mark your success",155,250);
	    	g.drawString("Or leave it as Frank to be Frank",225,300);
	    	
	    	g.setColor(new Color(0,252,0));
	    	g.fillRect(windoww*1/2-90,400,170,50);
	    	g.setFont(spacefontM);
	    	g.setColor(new Color(255,255,255));
	    	g.drawString("Generate",windoww*1/2-76,437);
	    	g.drawString("[A]",windoww*1/2-158,437); //menu button
	    	g.drawString("[A]",windoww*1/2+110,437);
	    	g.drawString("Your Name: "+playername,windoww*1/2-125,500);
	    	
	    	g.setColor(new Color(237,28,36));
	    	g.fillRect(windoww*1/2-135,550,270,70);
	    	g.setColor(new Color(255,255,255));
	    	g.setFont(spacefontL);
	    	g.drawString("FINISH",windoww*1/2-70,600);
	    	g.setFont(spacefontM);
	    	g.drawString("[Y]",windoww*1/2-200,600); //menu button
	    	g.drawString("[Y]",windoww*1/2+161,600);
    	}
    	if ( currentscreen=="Loading"){
    		g.setColor(new Color(0,0,0));
	    	g.fillRect(0,0,getWidth(),getHeight()); //background
	    	g.setFont (spacefontM); 
			g.setColor(new Color(255,255,255));
	    	g.drawString ("PLAY<PLAYER1>",windoww/2-110,windowh/2-20);
	    	g.drawString("SCORE<1>",50,50);
			g.drawString(stringScore,50,75);
			g.drawString("HIGHSCORE",windoww*4/10,50);
			g.drawString(highscore,windoww*4/10,75);
			g.drawString("LEVEL",windoww-140,50);
			g.drawString(levelstring,windoww-140,75);
	    	
	    	if (loadtimer!=0 && loadtimer%5==0){
	    		g.setColor(new Color(0,0,0));
	    		g.fillRect(windoww/2-110,windowh/2-70,250,75);
	    		g.fillRect(50,50,100,30);
	    	}
    	}     	
    	if (currentscreen=="Game"){
	    	g.setColor(new Color(0,0,0));
	    	g.fillRect(0,0,getWidth(),getHeight()); //background
	    	g.setColor(new Color(0,252,0));
	    	g.fillRect(0,windowh-100,windoww,1);
	    	
	    	if (player.alive()){ //player
	    		g.drawImage(playerPic,player.getx(),player.gety(),this);
	    	}
	    	else if(player.alive()==false && respawntimer<maxrespawntime){ //draw dead player for a limited amount of time
	    		g.drawImage(playerdeathPic,player.getx(),player.gety(),this);
	    	}
	    	for (int i=0;i<barrierlist.size();i++){ //barriers
				g.drawImage(barrierPic,barrierlist.get(i).getx(),barrierlist.get(i).gety(),this);
				Barrier barr= barrierlist.get(i);
				for (int j=barr.getb().size();j>0;j--){ //barrier blocks
					BarrierBlock barrB= barr.getb().get(j-1);
					if (barrB.alive()==false){ //cover dead barrier blocks 
						g.drawImage(barrBdeathPic,barrB.getx(),barrB.gety(),this);
					}
				}
			}
			if (playerbullet.gety()>0){ //only draw player bullet if it is on screen
	        	g.drawImage(playerbulletPic,playerbullet.getx()-4,playerbullet.gety()-9,this); //-4 and -9 are for to centre the bullet launch point (magical numbers)
			}
			if (special.alive() && special.getx()+special.getw()>=0 && special.getx()<=windoww){ //special enemy
				g.drawImage(special.geti(),special.getx(),special.gety(),this);
			}
			else if (special.alive()==false && time-threshhold*2<special.gett()){ //draw an explosion at the special enemy's death
				g.drawImage(special.geti(),special.getx()-special.getw()/2,special.gety(),this); 
				
				String specialscore= Integer.toString(special.getp(windoww));
				g.setFont(spacefontS);
				g.setColor(new Color(237,28,36));
				if (special.getp(windoww)==50){ //draw the special enemy's point value depending on where it died on screen
					g.drawString(specialscore,special.getx()+2,special.gety()+20);	//+2 and +20 for centering (magical numbers)
				}
				else if (special.getp(windoww)==150){
					g.drawString(specialscore,special.getx(),special.gety()+20); //+20 for centering(magical numbers)
				}
				else{
					g.drawString(specialscore,special.getx()-2,special.gety()+20); //-2 and +20 for centering (magical numbers)
				}
			}
			for (int i=0;i<enemylist.size();i++){ //enemies
				if (enemylist.get(i).alive()){ 
					g.drawImage(enemylist.get(i).geti(),enemylist.get(i).getx(),enemylist.get(i).gety(),this);
				}
				else if (enemylist.get(i).alive()==false && time-threshhold<enemylist.get(i).gett()){ //draw an explosion at the enemy's death
					g.drawImage(enemylist.get(i).geti(),enemylist.get(i).getx(),enemylist.get(i).gety(),this);
				} 
			}
			for (int i=0;i<enemybullets.size();i++){ //enemy bullets
				if (enemybullets.get(i).gety()<=windowh-101){ //only draw enemy bullets if they are on screen
					g.drawImage(enemybulletPic,enemybullets.get(i).getx()-4,enemybullets.get(i).gety()-9,this); //-4 and -9 are for to centre the bullet launch point (magical numbers)
				}
				else{ //remove enemy bullets that are off screen
					enemybullets.remove(i);
				}	
			}
			for (int i=0;i<explosionlist.size();i++){ //draw explosions where the player bullet and enemy bullet have collided
				if (explosionlist.get(i).exploded()==true && time-threshhold<explosionlist.get(i).gett()){ //draw for a brief period of time
					g.drawImage(bulletdeathPic, explosionlist.get(i).getx()-10,explosionlist.get(i).gety(),this); //-10 is to centre the explosion (magical numbers)
				}
				else{
					explosionlist.remove(i); //remove explosions after a brief period of time
				}
			}
			g.setFont (spacefontM); 
			g.setColor(new Color(255,255,255));
			g.drawString("SCORE<1>",50,50);
			g.drawString(stringScore,50,75);
			g.drawString("HIGHSCORE",windoww*4/10,50);
			g.drawString(highscore,windoww*4/10,75);
			g.drawString("LEVEL",windoww-140,50);
			g.drawString(levelstring,windoww-140,75);
			g.drawString("LIVES",50,windowh-60);
			g.drawImage(playerPic,150,windowh-75,this);
			g.drawString(lifestring,180,windowh-60);
			
			if (gameoverflag==true){
				g.setFont (spacefontL);
				g.drawString("GAMEOVER",windoww*1/2-100,120);
			}
    	}
    }
}