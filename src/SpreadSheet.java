
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
public class SpreadSheet {
    private BufferedImage sheet; // to store the spritesheet
    
    /**
     * Create a new spritesheet
     * @param sheet the <code>image</code> with the sprites
     */
    public SpreadSheet (BufferedImage sheet) {
        this.sheet = sheet;
    }
    
    /**
     * Crop a sprite from the spritesheet
     * @param x an <code>int</code> value with the x coordinate
     * @param y an <code>int</code> value with the y coordinate
     * @param width an <code>int</code> value with the width of the sprite
     * @param height an <code>int</code> value with the height of the sprite
     * @return 
     */
    public BufferedImage crop(int x, int y, int width, int height) {
        return sheet.getSubimage(x, y, width, height);
    }
}
