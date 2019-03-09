
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
    public static BufferedImage gameOver; // to store game over image
    public static BufferedImage pause; //to store the pause image
    public static BufferedImage player; //to store the image of the player
    public static BufferedImage bullet; //to store the image of the bullet;
    
    public static BufferedImage invaderSprites; // to store the sprites
    public static BufferedImage invaders[]; // pictures to go up
    
    public static BufferedImage monstroSprites; // to store the spritesheet for the monstro
    public static BufferedImage monstros[]; // to store the sprites of the monstro animation
    
    public static BufferedImage pulpoSprites; // to store the spritesheet for the pulpo
    public static BufferedImage pulpos[]; // to store the sprites of the pulpo animation
        
    public static BufferedImage invaderDestroyEffect; // sprites for being destroyed animation
    
    public static BufferedImage monstroDestroyEffect; // sprites for being destroyed animation
    
    public static BufferedImage pulpoDestroyEffect; // sprites for being destroyed animation
    
    public static SoundClip destroySound; // to store the destroy sound effect
    public static SoundClip loseSound; // to store the game over sound effect
    public static SoundClip hitSound; // to store the sound of hitting invaders
    public static SoundClip shootSound; // to store the shooting sound
    
    /**
     * initializing the images of the game
     */
    public static void init() {
        // Assign image file to the corresponding variable
        background = ImageLoader.loadImage("/images/Background.jpg"); // background image
        gameOver = ImageLoader.loadImage("/images/gameOver.jpg"); // game over image
        pause = ImageLoader.loadImage("/images/Pausa.png"); // pause screen image
        player = ImageLoader.loadImage("/images/player.png"); //player image
        bullet = ImageLoader.loadImage("/images/bullet.png"); //bullet image
        
        invaderSprites = ImageLoader.loadImage("/images/invader.png"); // invaders sprites
        SpreadSheet invaderSpritesheet = new SpreadSheet(invaderSprites);// spritesheet of the invaders
        invaders = new BufferedImage[2];                              // the sprites for the invaders
        invaderDestroyEffect = ImageLoader.loadImage("/images/invaderDeath.png"); // Sprite for dying
        
        monstroSprites = ImageLoader.loadImage("/images/monstro.png"); // monstros sprites
        SpreadSheet monstroSpritesheet = new SpreadSheet(monstroSprites);// spritesheet of the monstros
        monstros = new BufferedImage[2];                              // the sprites for the monstros
        monstroDestroyEffect = ImageLoader.loadImage("/images/monstroDeath.png"); // Sprite for dying
        
        pulpoSprites = ImageLoader.loadImage("/images/pulpo.png"); // monstros sprites
        SpreadSheet pulpoSpritesheet = new SpreadSheet(pulpoSprites);// spritesheet of the monstros
        pulpos = new BufferedImage[2];                              // the sprites for the monstros
        pulpoDestroyEffect = ImageLoader.loadImage("/images/pulpoDeath.png"); // Sprite for dying
//        
//        destroySprites = ImageLoader.loadImage("/images/destroyedAnimationSprite.png"); // the spritesheet of the destroyed bricks
//        SpreadSheet destroySpritesheet = new SpreadSheet(destroySprites);       // spritesheet for the animation of the destroy effect
//        destroyEffect = new BufferedImage[6];                               // the sprites of the destroy animation
//        
//        destroySound = new SoundClip("/sounds/destroy.wav"); // Bricks destroy sound
//        loseSound = new SoundClip("/sounds/boom.wav");      // Game over sound
//        hitSound = new SoundClip("/sounds/hit.wav");        // Brick hit sound
//        bounceSound = new SoundClip("/sounds/bounce.wav");  // Bar bounce sound
//        
//        goodPwrUp = ImageLoader.loadImage("/images/br.png");    // Good power up sprite
//        badPwrUp = ImageLoader.loadImage("/images/brMalo.png"); // Bad poser up sprite
        
        // cropping the pictures for the invaders of all 3 types
        for (int i = 0; i < 2; i++) {
            invaders[i] = invaderSpritesheet.crop(i * 220, 0, 220, 160);
            monstros[i] = monstroSpritesheet.crop(i * 218, 0, 218, 160);
            pulpos[i] = pulpoSpritesheet.crop(i * 159, 0, 159, 160);           
        }
    }
}
