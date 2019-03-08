
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
    private Animation bar; // to store the animation for the bar
    public enum playerState { init, alive, dead } // Declares the player's possible states
    private playerState state; // To store the player's current state
    
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
        this.bar = new Animation(Assets.playerBar, 100);
        this.state = playerState.init;
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
     * Function tick that gets called every frame and updates player logic
     */
    @Override
    public void tick() {
        
        if (state == playerState.init && game.getKeyManager().isStart()) {
            // Start the game
            state = playerState.alive;            
        }
        // Normal game stuff
        if (state == playerState.alive) {
            // refresh animation
            bar.tick();
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
            if (game.getBall().getState() == Projectile.ballStatus.fallen) {
                setState(playerState.dead);
            }
        }       
    }
    
    /**
     * Function render that draws the player
     * @param g for the graphics
     */    
    @Override
    public void render(Graphics g) {
       g.drawImage(bar.getCurrentFrame(), getX(), getY(), getWidth(), getHeight(), null);
    }
}
