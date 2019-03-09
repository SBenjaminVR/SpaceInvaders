
import java.awt.Graphics;
import java.awt.Rectangle;
import static java.lang.Math.cos;

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

public class Projectile extends Item {
    
    private int width; // projectile's width
    private int height; // projectile's height
    private Game game; // game object where the projectile exists
    private Rectangle hitbox; // projectile's hitbox
    private Animation anim; // to store the animation for the grenade
//    public enum ballStatus{ // Declares the possible states of the projectile
//        idle, base, fallen
//    }
//    private ballStatus state; // To store the projectile's current state
    private double speed; // To store the general speed used to calculate horizontal and vertical speed
    
    /**
     * Constructor with parameters
     * @param x position
     * @param y position
     * @param width of the projectile
     * @param height of the projectile
     * @param game where the projectile exists
     */
    public Projectile(int x, int y, int width, int height, Game game) {
        super(x, y);
        this.width = width;
        this.height = height;
        this.game = game;
//        this.state = ballStatus.idle;
        this.speed = 8;
        this.hitbox = new Rectangle(x, y, width, height);  
        //this.anim = new Animation(Assets.grenade, 100);
    }
    
    /**
     * Returns the projectile's width
     * @return the int value for the projectile's width
     */ 
    public int getWidth() {
        return width;
    }
    /**
     * Returns the projectile's height
     * @return the int value for the projectile's height
     */
    public int getHeight() {
        return height;
    }
   /**
    * Sets the projectile's width
    * @param width of the projectile
    */     
    public void setWidth(int width) {
        this.width = width;
    }
    /**
     * Sets the projectiles's height
     * @param height of the projectile
     */
    public void setHeight(int height) {
        this.height = height;
    }
//    /**
//     * Sets the projectile current state
//     * @param state of the projectile
//     */
//    public void setState(ballStatus state) {
//        this.state = state;
//    }
//    /**
//     * Returns the current state of the projectile
//     * @return current state of the projectile
//     */
//    public ballStatus getState() {
//        return state;
//    }
    
    /**
     * Returns the speed
     * @return the general speed
     */
    public double getSpeed() {
        return speed;
    }
    /**
     * Sets the speed
     * @param speed 
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Returns the projectiles's hitbox
     * @return Rectangle for the hitbox
     */
    public Rectangle getHitbox() {
        return hitbox;
    }
    
    /**
     * Function tick called every frame to update logic
     */        
    @Override
    public void tick() {
        // refresh hitbox location
        hitbox.setLocation(getX(), getY());
        // Start moving        
        anim.tick();
        // Collision with walls
        if (getY() <= -getHeight()) {
            setSpeed(0);
        }
    }
    /**
     * Function to check intersections with the projectile
     * @param obj that intersects the projectile
     * @return whether the projectile waa intersected by a brick
     */
    public boolean intersecta(Object obj) {
        return obj instanceof Invader && hitbox.intersects(((Invader) obj).getPerimetro());
        
    }
    /**
     * Function render that draws the projectile
     * @param g for the graphics 
     */
    @Override
    public void render(Graphics g) {
       //g.drawImage(anim.getCurrentFrame(), getX(), getY(), getWidth(), getHeight(), null);
    }
}
