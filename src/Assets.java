
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
public class Assets {
    public static BufferedImage background; // to store background image
    public static BufferedImage barSprites; // to store the sprites
    public static BufferedImage playerBar[]; // pictures to go up    
    public static BufferedImage drug; // to store the sprite for the meth bricks
    public static BufferedImage damagedBrick; // to store the sprite for the damaged meth bricks 
    public static BufferedImage grenadeSprites; // to store the spritesheet for the grenade animation
    public static BufferedImage grenade[]; // to store the sprites of the grenade animation
    public static BufferedImage gameOver; // to store game over image
    public static BufferedImage pause; //to store the pause image
    public static BufferedImage destroySprites; // to store the sprites for the destroy animation
    public static BufferedImage destroyEffect[]; // sprites for being destroyed animation
    public static BufferedImage victory; // To store the victory image
    public static SoundClip destroySound; // to store the destroy brick sound effect
    public static SoundClip loseSound; // to store the game over sound effect
    public static SoundClip hitSound; // to store the sound of hitting bricks
    public static SoundClip bounceSound; // to store the bar bounce sound
    public static BufferedImage goodPwrUp; // to store the sprite of the good power up
    public static BufferedImage badPwrUp;  // to store the sprite of the bad power up
    
    /**
     * initializing the images of the game
     */
    public static void init() {
        // Assign image file to the corresponding variable
        background = ImageLoader.loadImage("/images/Background.jpg"); // background image
        gameOver = ImageLoader.loadImage("/images/gameoverscreen.jpg"); // game over image
        drug = ImageLoader.loadImage("/images/methSprite.png");  // meth brick image
        damagedBrick = ImageLoader.loadImage("/images/damagedSprite.png"); // damaged brick image

        victory = ImageLoader.loadImage("/images/victoryScreen.jpg"); // victory screen image

        pause = ImageLoader.loadImage("/images/Pausa.png"); // pause screen image
        
        barSprites = ImageLoader.loadImage("/images/BarritaSprite.png"); // player bar image
        SpreadSheet barSpritesheet = new SpreadSheet(barSprites);         // spritesheet of the bar
        playerBar = new BufferedImage[18];                              // the sprites of the bar animation
        
        grenadeSprites = ImageLoader.loadImage("/images/granadaSprites.png"); // the spritesheet of the grenade
        SpreadSheet grenadeSpritesheet = new SpreadSheet(grenadeSprites); // spritesheet for the animation of the grenade
        grenade = new BufferedImage[35];                                  // the sprites of the grenade aniimation
        
        destroySprites = ImageLoader.loadImage("/images/destroyedAnimationSprite.png"); // the spritesheet of the destroyed bricks
        SpreadSheet destroySpritesheet = new SpreadSheet(destroySprites);       // spritesheet for the animation of the destroy effect
        destroyEffect = new BufferedImage[6];                               // the sprites of the destroy animation
        
        destroySound = new SoundClip("/sounds/destroy.wav"); // Bricks destroy sound
        loseSound = new SoundClip("/sounds/boom.wav");      // Game over sound
        hitSound = new SoundClip("/sounds/hit.wav");        // Brick hit sound
        bounceSound = new SoundClip("/sounds/bounce.wav");  // Bar bounce sound
        
        goodPwrUp = ImageLoader.loadImage("/images/br.png");    // Good power up sprite
        badPwrUp = ImageLoader.loadImage("/images/brMalo.png"); // Bad poser up sprite
        
        // cropping the pictures from the player bar sheet into the array
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 6; j++) {
                playerBar[count] = barSpritesheet.crop(i * 226, j * 50, 226, 50);
                count++;
            }
        }
         // cropping the pictures from the grenade sheet into the array
        count = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (count > 34) break;
                grenade[count] = grenadeSpritesheet.crop(i * 50, j * 50, 50, 50);
                count++;
            }
        }
         // cropping the pictures from the destroy animation sheet into the array
        count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                destroyEffect[count] = destroySpritesheet.crop(i * 155, j * 55, 155, 55);
                count++;
            }
        }
    }
}
