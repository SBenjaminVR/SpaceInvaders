
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
public class KeyManager implements KeyListener {
    
    public boolean left;  // flag to move left the player
    public boolean right; // flag to move right the player
    public boolean enter; // flag to restart game
    
    private boolean keys[]; // to store the flags for every key
    private boolean start; //flag to start the game
    private boolean pause; //flag to pause the game
    private boolean load; //flag to load the game
    private boolean save; //flag to save the game
    private boolean lastPause; //flag to know the last Pause 
    private boolean lastSave; //flag to know the last Save 
    private boolean lastLoad;//flag to know the last Load 

    
    public KeyManager() {
        keys = new boolean[256];
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        // set true every key pressed
         keys[e.getKeyCode()] = true;
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        // set false to every key released
        keys[e.getKeyCode()] = false;
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            start = true;
        }
    }

    /**
     * Gets true if the game has already started 
     * @return true if the game has already started  
     */
    public boolean isStart() {
        return start;
    }

    /**
     * Changes if the game has already started or not
     * @param start to change if the game has already started or not
     */
    public void setStart(boolean start) {
        this.start = start;
    }
    
    /**
     * Knows if the game is paused
     * @return true if the game is paused
     */
    public boolean getPause() {
        return pause;
    }
    
    /**
     * Knows if the game is saved
     * @return true if the game is saved
     */
    public boolean getSave() {
        return save;
    }
    
    /**
     * Knos if the game has just loaded a save file
     * @return true if the game has loaded a save file
     */
    public boolean getLoad() {
        return load;
    }
    
    /**
     * to enable or disable moves on every tick
     */
    public void tick() {
        //Doesn't change the pause until the 'P' is pressed again
        if (lastPause && !keys[KeyEvent.VK_P]) {
            pause = true;
        }
        else {
            pause = false; 
        }
        //Doesn't change the save until the 'G' is pressed again
        if (lastSave && !keys[KeyEvent.VK_G]) {
            save = true;
        }
        else {
            save = false; 
        }
        //Doesn't change the pause until the 'C' is pressed again
        if (lastLoad && !keys[KeyEvent.VK_C]) {
            load = true;
        }
        else {
            load = false; 
        }
        
        left = keys[KeyEvent.VK_LEFT];
        right = keys[KeyEvent.VK_RIGHT];
        enter = keys[KeyEvent.VK_ENTER];
        lastPause = keys[KeyEvent.VK_P];
        lastLoad = keys[KeyEvent.VK_C];
        lastSave = keys[KeyEvent.VK_G];
    }
}
