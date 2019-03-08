
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

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

public class PowerUp extends Item {
    
    private int width; // To store the width of the power Up
    private int height; // To store the height of the power Up
    private Game game; // To store the game object
    private Rectangle hitbox; // To store the hitbox of the power Up
    public enum Type { good, bad } // To define the 2 types of power Up we have
    private Type type; // To store they type of power Up of each instance
    private BufferedImage sprite; // To store the sprite of each instance corresponding to the type
    private int fallSpeed; // To store the speed at which the power Ups fall
    
    /**
     * Constructor with parameters for power Ups
     * @param x position
     * @param y position
     * @param width of the power up
     * @param height of the power up
     * @param type of the power up
     * @param game where the power up exists
     */
    public PowerUp(int x, int y, int width, int height, Type type, Game game) {
        super(x, y);
        this.width = width;
        this.height = height;
        this.game = game;
        this.hitbox = new Rectangle(x, y, width, height);        
        this.type = type;
        if (type == Type.good) {
            this.sprite = Assets.goodPwrUp;
        }
        else if (type == Type.bad) {
            this.sprite = Assets.badPwrUp;
        }
        this.fallSpeed = 5;
    }    
    
    /**
     * Returns the width of the power up
     * @return the int value of the power up's widht
     */
    public int getWidth() {
        return width;
    }
    /**
     * Returns the height of the power up
     * @return int value of the power up's height
     */
    public int getHeight() {
        return height;
    }
    /**
     * Sets the width of the power up
     * @param width of the power up
     */
    public void setWidth(int width) {
        this.width = width;
    }
    /**
     * Sets height of the power up
     * @param height of the power up
     */
    public void setHeight(int height) {
        this.height = height;
    }
    /**
     * Returns the type of the power up
     * @return Type of the power up
     */
    public Type getType() {
        return type;
    }
    /**
     * Set the type of the power up
     * @param type of the power up
     */
    public void setType(Type type) {
        this.type = type;
    }
    /**
     * Returns the x position of the power up
     * @return the x position of the power up
     */
    public int getX() {
        return x;
    }
    /**
     * Returns the y position of the power up
     * @return the y position of the power up
     */
    public int getY() {
        return y;
    }
    /**
     * Sets the x position of the power up
     * @param x position
     */
    public void setX(int x) {
        this.x = x;
    }
    /**
     * Sets the y position of the power up
     * @param y position
     */
    public void setY(int y) {
        this.y = y;
    }
    /**
     * Returns the speed at which the power up falls
     * @return int value for the speed of the power up
     */
    public int getFallSpeed() {
        return fallSpeed;
    }
    /**
     * Returns the hitbox of the power up
     * @return Rectangle hitbox
     */
    public Rectangle getHitbox() {
        return hitbox;
    }    
    
    /**
     * Function tick that updates the location of the hitbox and moves the power up
     */
    @Override
    public void tick() {
        // Refreshes the location of the hitbox
        getHitbox().setLocation(getX(), getY());
        // Makes the power up fall
        setY(getY() + getFallSpeed());
    }
    /**
     * Function render that draws the sprite of the power up
     * @param g for the graphics
     */
    @Override
    public void render(Graphics g) {
        g.drawImage(sprite, getX(), getY(), getWidth(), getHeight(), null);
    }
    
}
