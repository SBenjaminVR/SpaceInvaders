
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
    public enum ballStatus{ // Declares the possible states of the projectile
        idle, base, fallen
    }
    private ballStatus state; // To store the projectile's current state
    private double xSpeed; // To store the horizontal speed
    private double ySpeed; // To store the vertical speed
    private double speed; // To store the general speed used to calculate horizontal and vertical speed
    private boolean collision; // boolean for collisions
    private SoundClip bounceSound; // to store the sound used when the projectile bounces on the player's bar
    
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
        this.state = ballStatus.idle;
        this.speed = 8;
        this.xSpeed = 0;
        this.ySpeed = -8;
        this.hitbox = new Rectangle(x, y, width, height);  
        this.anim = new Animation(Assets.grenade, 100);
        this.collision = false;
        this.bounceSound = Assets.bounceSound;
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
    /**
     * Sets the projectile current state
     * @param state of the projectile
     */
    public void setState(ballStatus state) {
        this.state = state;
    }
    /**
     * Returns the current state of the projectile
     * @return current state of the projectile
     */
    public ballStatus getState() {
        return state;
    }
    /**
     * Returns the horizontal speed of the projectile
     * @return the horizontal speed
     */
    public double getXSpeed() {
        return xSpeed;
    }
    /**
     * Returns the vertical speed of the projectile
     * @return the vertical speed
     */
    public double getYSpeed() {
        return ySpeed;
    }
    /**
     * Sets the projectile's horizontal speed
     * @param speed horizontal speed
     */
    public void setXSpeed(double speed) {
        this.xSpeed = speed;
    }
    /**
     * Sets the projectile's vertical speed
     * @param speed vertical speed
     */
    public void setYSpeed(double speed) {
        this.ySpeed = speed;
    }
    /**
     * Returns the general speed
     * @return the general speed
     */
    public double getSpeed() {
        return speed;
    }
    /**
     * Returns the projectiles's hitbox
     * @return Rectangle for the hitbox
     */
    public Rectangle getHitbox() {
        return hitbox;
    }
    /**
     * Returns collision boolean value
     * @return boolean for collision
     */
    public boolean isCollision() {
        return collision;
    }
    /**
     * Sets the collision boolean
     * @param collision boolean 
     */
    public void setCollision(boolean collision) {
        this.collision = collision;
    }
    
    /**
     * Function tick called every frame to update logic
     */        
    @Override
    public void tick() {
        // refresh hitbox location
        hitbox.setLocation(getX(), getY());
        // Start moving
        if (getState() == ballStatus.idle) {
            if (game.getKeyManager().isStart()) {
                setState(ballStatus.base);
            }
        }
        else if (getState() == ballStatus.base) { 
            anim.tick();
            // player loses if ball falls
            if (getY() + getHeight() >= game.getHeight()) {
              setState(ballStatus.fallen);
            }
            // Collision with walls
            if (getX() + getWidth() >= game.getWidth()) {
                
                setXSpeed(getXSpeed() * -1);
            }
            else if (getX() <= 0) {
                setX(0);
                setXSpeed(getXSpeed() * -1);
            }
            else if (getY() <= 0) {
                setYSpeed(getYSpeed() * -1);
            }

            // Collision with player
            if (hitbox.intersects(game.getPlayer().getHitbox())) {
                bounceSound.play();
                int position = (getX() + getWidth() / 2) - (int)game.getPlayer().getHitbox().getX();
                double angle;
                boolean right = false;
                if (position < 0) {
                    position = 0;
                }
                else if (position > game.getPlayer().getHitbox().getWidth()) {
                    position = (int)game.getPlayer().getHitbox().getWidth();
                }
                
                if (position <= game.getPlayer().getHitbox().getWidth()/2) {
                    angle = (position / (game.getPlayer().getHitbox().getWidth() / 2.0) ) * 40 + 30;
                    right = false;
                }
                else {
                    position -= game.getPlayer().getHitbox().getWidth() / 2;
                    angle = ((game.getPlayer().getHitbox().getWidth() / 2 - position) / (game.getPlayer().getHitbox().getWidth() / 2)) * 40 + 30;
                    right = true;
                }
                angle = Math.toRadians(angle);
                setXSpeed((right ? 1 : -1) * getSpeed() * Math.cos(angle));
                setYSpeed(-1 * getSpeed() * Math.sin(angle));
            }                      
            setX((int) (getX() + getXSpeed()));
            setY((int) (getY() + getYSpeed()));
            setCollision(false);
        }
    }
    /**
     * Function to check intersections with the projectile
     * @param obj that intersects the projectile
     * @return whether the projectile waa intersected by a brick
     */
    public boolean intersecta(Object obj) {
        return obj instanceof Brick && hitbox.intersects(((Brick) obj).getPerimetro());
        
    }
    /**
     * Function render that draws the projectile
     * @param g for the graphics 
     */
    @Override
    public void render(Graphics g) {
       g.drawImage(anim.getCurrentFrame(), getX(), getY(), getWidth(), getHeight(), null);
    }
}
