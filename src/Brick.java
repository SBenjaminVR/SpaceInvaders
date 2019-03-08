
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Humberto Gonzalez 
 * @author Benjamin Valdez
 */

public class Brick extends Item {
    
    private int width; //width of the brick
    private int height; //height of the brick
    private Game game; 
    private Animation destroyEffect; // to store the animation for being destroyed
    public enum status { normal, hit, destroyed } //status that the block can have
    private status state ; //state
    private int lives; //Number of hits that the brick can receive
    private int bTimer; // Invulnerability frames
    private boolean animOver;  //Boolean to know if the animetion is over
    private boolean destroySndDone; //Boolean to know if the block has been broken
    private SoundClip destroySound; //Sound of the block when it's destroyed 
    private SoundClip hitSound; //Sound of the block when it receives a hit
    private boolean hitSndDone; //Boolean to know if the block has received a hit
    private int dropProb; // variable to check if brick can drop powerup
    private int goodDropChance; // Chance to drop good powerup
    private int badDropChance; // Chance to drop bad powerup
    private boolean alreadyDropped; //Boolean to know if the block has already dropped a powerUp
    
    public Brick(int x, int y, int width, int height, Game game) {
        super(x, y);
        this.width = width;
        this.height = height;
        this.game = game;
        this.destroyEffect = new Animation(Assets.destroyEffect, 100);
        this.state = status.normal;
        this.lives = 2;
        this.animOver = false;
        this.destroySound = Assets.destroySound;
        this.destroySndDone = false;
        this.hitSound = Assets.hitSound;
        this.hitSndDone = false;
        this.dropProb = (int) (Math.random() * 100); // random number from 0 to 100
        this.goodDropChance = 10; // if dropProb is <= goodDropChance drop it
        this.badDropChance = 90; // if dropProb is >= badDropChance drop it
        this.alreadyDropped = false;
    }
    
    /**
     * To get the width of the brick 
     * @return the width of the brick 
     */
     
    public int getWidth() {
        return width;
    }
    
    /**
     * To get the height of the brick 
     * @return the height of the brick 
     */
    public int getHeight() {
        return height;
    }
        
    /**
     * To set the new Width of the brick
     * @param width is the new widht of the brick
     */
    public void setWidth(int width) {
        this.width = width;
    }
    
    /**
     * To set the new height of the brick
     * @param height is the new height of the brick
     */
    public void setHeight(int height) {
        this.height = height;
    }

    
    /**
     * To get the state of the brick 
     * @return the state of the brick 
     */
    public status getState() {
        return state;
    }

    /**
     * To set the new state of the brick
     * @param state is the new state of the brick
     */
    public void setState(status state) {
        this.state = state;
    } 

    /**
     * To get a bool that says if the destroyed brick animation is over
     * @return true if the animation is over
     */
    public boolean isAnimOver() {
        return animOver;
    }

    /**
     * To get the invulnerability frames of the brick
     * @return the timer of invulnerability
     */
    public int getbTimer() {
        return bTimer;
    }

    /**
     * Changes the time for the invulnerability frames
     * @param bTimer is the new time for invulnerability
     */
    public void setbTimer(int bTimer) {
        this.bTimer = bTimer;
    }

    /**
     * Gets the chance that has a powerUp of spawn
     * @return the chance that a powerUP has of spawn
     */
    public int getDropProb() {
        return dropProb;
    }

    /**
     * Gets the chance that has a powerUp of spawn a good powerUp
     * @return the chance that a powerUP has of spawn a good powerUp
     */
    public int getBadDropChance() {
        return badDropChance;
    }

    /**
     * Gets the chance that has a powerUp of spawn a bad powerUP
     * @return the chance that a powerUP has of spawn a bad powerUp
     */
    public int getGoodDropChance() {
        return goodDropChance;
    }

    /**
     * Gets if the brick has already spawned a powerUp
     * @return true if the brick has already spawned a powerUP 
     */
    public boolean isAlreadyDropped() {
        return alreadyDropped;
    }

    /**
     * Changes the boolean that knows if the brick has already spawned a powerUp
     * @param alreadyDropped that says if the brick has already spawned a powerUp
     */
    public void setAlreadyDropped(boolean alreadyDropped) {
        this.alreadyDropped = alreadyDropped;
    }    
        
    @Override
    public void tick() {
        //If the brick gets hit, it produces a sound 
        if (getState() == status.hit) {
            if (!hitSndDone) {
                hitSound.play();
                hitSndDone = true;
            }
        }
        //If the brick gets destroyed it plays an animation
        if (getState() == status.destroyed) {
            if (!destroySndDone) {
                destroySound.play();
                destroySndDone = true;
            }            
            destroyEffect.tick();
            if (destroyEffect.getIndex() == 5) {
                animOver = true;
            }
        }
    }
    
    /**
     * To get the perimeter of the brick
     * @return the perimeter of the brick
     */
    public Rectangle getPerimetro() {

            return new Rectangle(getX(), getY(), getWidth(), getHeight());
        }
    
    @Override
    public void render(Graphics g) {
       if (getState() == status.normal) {
            g.drawImage(Assets.drug, getX(), getY(), getWidth(), getHeight(), null);
       }
       if (getState() == status.hit) {
            g.drawImage(Assets.damagedBrick, getX(), getY(), getWidth(), getHeight(), null);
       }
       if (getState() == status.destroyed) {
           g.drawImage(destroyEffect.getCurrentFrame(), getX(), getY(), getWidth(), getHeight(), null);
       }
       
    }
}
