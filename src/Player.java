
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

public class Player extends Item {
    private int direction; // Direction of the player
    private int width;      // Width of the player
    private int height;     // Height of the player
    private Game game;      // Game object
    private int speed;      // Speed of the player
    private Rectangle hitbox;// The player's hitbox
    public enum playerState {alive, dead } // Declares the player's possible states
    private playerState state; // To store the player's current state
    public static final int LIVES = 5; // Total number of lives
    private int currentLives; // To store the current number of lives
    private int deathTimer; // To store the death timer
    
    /**
     * Constructor with parameters
     * @param x position
     * @param y position
     * @param direction of the player
     * @param width of the player
     * @param height of the player
     * @param game where the player exists
     */
    public Player(int x, int y, int direction, int width, int height, Game game) {
        super(x, y);
        this.direction = direction;
        this.width = width;
        this.height = height;
        this.game = game;
        this.speed = 10;
        this.hitbox = new Rectangle(x, y, width, height/3);
        this.state = playerState.alive;
        this.currentLives = LIVES;
        deathTimer = 30;
    }
     /**
      * Returns the width of the player
      * @return int value of the player's width
      */
    public int getWidth() {
        return width;
    }
    /**
     * Returns the height of the player
     * @return int value of the player's height
     */
    public int getHeight() {
        return height;
    }
    /**
     * Sets the direction of the player
     * @param direction of the player
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }
    /**
     * Sets the width of the player
     * @param width of the player
     */
    public void setWidth(int width) {
        this.width = width;
    }
    /**
     * Sets the height of the player
     * @param height of the player
     */
    public void setHeight(int height) {
        this.height = height;
    }
    /**
     * Returns the speed of the player
     * @return int value of the speed
     */
    public int getSpeed() {
        return speed;
    }
    /**
     * Sets the speed of the player
     * @param speed of the player
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    /**
     * Returns the player's hitbox
     * @return Rectangle hitbox
     */
    public Rectangle getHitbox() {
        return hitbox;
    }
    /**
     * Returns the player's current state
     * @return playerState 
     */
    public playerState getState() {
        return state;
    }
    /**
     * Sets the player state
     * @param state is the new state of the player
     */
    public void setState(playerState state) {
        this.state = state;
    }
    /**
     * Returns the number of lives the player currently has
     * @return int value of the current lives
     */
    public int getCurrentLives() {
        return currentLives;
    }
    /**
     * Sets the current lives of the player
     * @param currentLives 
     */
    public void setCurrentLives(int currentLives) {
        this.currentLives = currentLives;
    }
    /**
     * Gets the int value of the timer for death animation
     * @return int value of the timer
     */
    public int getDeathTimer() {
        return deathTimer;
    }    
        
    /**
     * Function tick that gets called every frame and updates player logic
     */
    @Override
    public void tick() {
     
        // Normal game stuff
        if (getState() == playerState.alive) {
            
            // refresh hitbox location
            hitbox.setLocation(getX(), getY());
            
            // Move the bar based on input
            if (game.getKeyManager().left) {
                setX(getX() - getSpeed());
            }
            if (game.getKeyManager().right) {
                setX(getX() + getSpeed());
            }
            
            // Collision with walls
            if (getX() + getWidth() >= game.getWidth()) {
                setX(game.getWidth() - getWidth());
            }
            if (getX() <= 0) {
                setX(0);
            }
            
        }
        else if (getState() == playerState.dead) {
            if (deathTimer > -60) deathTimer--;
            if (deathTimer <= -60) {
                deathTimer = 30;
                setState(playerState.alive);
            }
        }
    }
    
    /**
     * Function render that draws the player
     * @param g for the graphics
     */    
    @Override
    public void render(Graphics g) {
        if (getState() == playerState.alive) {
            g.drawImage(Assets.player, getX(), getY(), getWidth(), getHeight(), null);
        }
        else if (deathTimer > 0){
            g.drawImage(Assets.playerDead, getX(), getY(), getWidth(), getHeight(), null);
        }
    }
}
