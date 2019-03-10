
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

public class Invader extends Item {
    
    private int width; //width of the invader
    private int height; //height of the invader
    private Game game; 
    private Animation anim; // to store the animation for the invader
    private BufferedImage destroyEffect; // to store the image for being destroyed
    public enum status { normal, destroyed } //status that the block can have
    public enum Type { invader, monstro, pulpo }
    private status state ; //state
    private Type type; // type of invader
    private boolean animOver;  //Boolean to know if the animetion is over
    private SoundClip destroySound; // to play when invader is destroyed
    private boolean destroySndDone;
    private int destroyTimer;
    private boolean goingDown;
    private Bomb bomb; //To store if the alien has a bomb
    
    public Invader(int x, int y, int width, int height, Type type , Game game) {
        super(x, y);
        this.width = width;
        this.height = height;
        this.type = type;
        this.game = game;
        if (this.type == Type.invader) {
            this.anim = new Animation (Assets.invaders, 200);
            this.destroyEffect = Assets.invaderDestroyEffect;
        }
        else if (this.type == Type.monstro) {
            this.anim = new Animation (Assets.monstros, 200);
            this.destroyEffect = Assets.monstroDestroyEffect;
        }
        else if (this.type == Type.pulpo) {
            this.anim = new Animation (Assets.pulpos, 200);
            this.destroyEffect = Assets.pulpoDestroyEffect;
        }        
        this.state = status.normal;
        this.animOver = false;
        this.destroySound = Assets.destroySound;
        this.destroySndDone = false;
        this.destroyTimer = 0;
        this.goingDown = false;
        this.bomb = null;
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

    public Type getType() {
        return type;
    }  

    public int getDestroyTimer() {
        return destroyTimer;
    }

    public void setDestroyTimer(int destroyTimer) {
        this.destroyTimer = destroyTimer;
    }   
    
     public boolean isGoingDown() {
        return goingDown;
    }

    public void setGoingDown(boolean goingDown) {
        this.goingDown = goingDown;
    }
    
    public Bomb getBomb() {
        return bomb;

    }
        
    @Override
    public void tick() {
        //If the invader gets destroyed it plays an animation
        if (getState() == status.destroyed) {
            if (getDestroyTimer() > 0) setDestroyTimer(getDestroyTimer() - 1);
            if (!destroySndDone) {
                //destroySound.play();
                destroySndDone = true;
            }  
            if (bomb != null) {
                if (bomb.getCollision()) {
                    bomb = null;
                }
            } 
        }
        else if (getState() == status.normal) {
            anim.tick();

            if (game.getJumpTimer() == 0) {
                if (isGoingDown()) {
                    setY(getY() + getHeight());
                    setGoingDown(false);
                }
                else if (game.isGoingLeft()) {
                    setX(getX() - getWidth());

                }
                else {
                    setX(getX() + getWidth());                
                }
            }
            
            if (bomb != null) {
                if (bomb.getCollision()) {
                    bomb = null;
                }
            } 
            else {
                int azar = (int) (Math.random() * (10000-1+1) ) + 1;
                if (azar == 1) {
                    bomb = new Bomb(x + width/2-3, y+height, 12, 24, game);
                }
            }            
        }
    }
    
    /**
     * To get the perimeter of the invader
     * @return the perimeter of the invader
     */
    public Rectangle getPerimetro() {

            return new Rectangle(getX(), getY(), getWidth(), getHeight());
        }
    
    @Override
    public void render(Graphics g) {
        if (getState() == status.normal) {
             g.drawImage(anim.getCurrentFrame(), getX(), getY(), getWidth(), getHeight(), null);
        }
        if (getState() == status.destroyed) {
            g.drawImage(destroyEffect, getX(), getY(), getWidth(), getHeight(), null);
        }     
       
    }
}
