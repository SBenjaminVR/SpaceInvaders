
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
public class Game implements Runnable {
    
    private BufferStrategy bs; //to have several buffers when displaying
    private Graphics g; //to paint objects
    private Display display; //to display in the game
    String title; //title of the window
    private int width; //width of the window
    private int height; //height of the window
    private Thread thread; //thread to create the game
    private boolean running; //to set the game
    private KeyManager keyManager; // to manage the keyboard
    private WriteFile saveFile; //to store the saveFile
    private Player player; // to use a player
    private boolean bulletExists; //to see if a bulletExists;
  //  private Bullet bullet; //to use a bullet
    public enum gameState { normal, pause, gameOver}
    private gameState gameState;
    public static int GROUND; // to check when invaders hit the ground and invade us
    public static final int BOMB_HEIGHT = 5;
    public static final int ALIEN_HEIGHT = 25;
    public static final int ALIEN_WIDTH = 50;
    public static final int BORDERX = 50;
    public static final int BORDERY = 25;
    public static final int GO_DOWN = 15;
    public static final int NUMBER_OF_ALIENS_TO_DESTROY = 24;
    public static final int CHANCE = 5;
    public static final int DELAY = 17;
    public static final int PLAYER_WIDTH = 50;
    public static final int PLAYER_HEIGHT = 50;
    private LinkedList<Invader> invaders;
    private Projectile bullet;
    
    /**
     * to create title, width and height and set the game is still not running
     * @param title to set the title of the window
     * @param width to set the width of the window
     * @param height to set the height of the window
     */
    public Game(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        running = false;
        keyManager = new KeyManager();
        gameState = gameState.normal;
        GROUND = height / 8 *7;
        invaders = new LinkedList<Invader>();
    }

    /**
     * Gets the Width of the Screen of the game 
     * @return Width of the Screen of the game 
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the Height of the Screen of the game 
     * @return Height of the Screen of the game 
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the bar that is the player of the game
     * @return the bar object for the player 
     */
    public Player getPlayer() {
        return player;
    }

    public gameState getGameState() {
        return gameState;
    }

    public void setGameState(gameState gameState) {
        this.gameState = gameState;
    }

    public Projectile getBullet() {
        return bullet;
    }  
    
    public void setBulletExists(boolean bulletExists) {
        this.bulletExists = bulletExists;
    }
        
    /**
     * Initializing the display window of the game
     */
    private void init() {
        display = new Display(title, width, height);
        bullet = null;
        bulletExists = false;
        Assets.init();        
        player = new Player(getWidth()/2 - PLAYER_WIDTH/2, getHeight() - PLAYER_HEIGHT - BORDERY, 1, PLAYER_WIDTH, PLAYER_HEIGHT, this);
        bullet = new Projectile(getWidth()/2, -50, 50, 50, this);
        // Add pulpo invaders
        for (int j = 0; j < 12; j++) {
            int iPosX = (BORDERX + ALIEN_WIDTH)* j;
            invaders.add(new Invader(iPosX + BORDERX, BORDERY * 3, ALIEN_WIDTH, ALIEN_HEIGHT, Invader.Type.pulpo, this));            
        }
        // Add normal invaders
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 12; j++) {
                int iPosX = (BORDERX + ALIEN_WIDTH)* j;
                int iPosY = BORDERY * i + ALIEN_HEIGHT + i * ALIEN_HEIGHT;
                invaders.add(new Invader(iPosX + BORDERX, iPosY + BORDERY * 4, ALIEN_WIDTH, ALIEN_HEIGHT, Invader.Type.invader, this));            
            }
        }        
        // Add weird invaders
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 12; j++) {
                int iPosX = (BORDERX + ALIEN_WIDTH)* j;
                int iPosY = BORDERY * i + ALIEN_HEIGHT * 3 + i * ALIEN_HEIGHT;
                invaders.add(new Invader(iPosX + BORDERX, iPosY + BORDERY * 6, ALIEN_WIDTH, ALIEN_HEIGHT, Invader.Type.monstro, this));            
            }
        }
        display.getJframe().addKeyListener(keyManager);
    }
    
    /**
     * Gets the KeyManager that handles the Keyboard
     * @return the KeyManager that handles the Keyboard
     */
    public KeyManager getKeyManager() {
        return keyManager;
    }
    
    @Override
    public void run() {
        init();
        // frames per second
        int fps = 60;
        // time for each tick in nanoseconds
        double timeTick = 1000000000 / fps;
        // initializing delta
        double delta  = 0;
        // define now to use inside the loop
        long now;
        // initializing last time to the computer time in nanosecs
        long lastTime = System.nanoTime();
        while (running) {
            // setting the time now to the actual time
            now = System.nanoTime();
            // acumulating to delta the difference between times in timeTick units
            delta += (now - lastTime) / timeTick;
            // updating the last time
            lastTime = now;
            
            // if delta is positive we tick the game
            if (delta >= 1) {
                try {
                    tick();
                } catch (IOException ex) {
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                }
                render();
                delta--;
            }
        }
        stop();
    }
    
    /**
     * Saves the game in "save.txt" that can bea loaded later
     */
//    private void save() {
//        try {
//            saveFile = new WriteFile("save.txt", false);
//            saveFile.writeToFile(String.valueOf(player.getX()));
//            saveFile.setAppend(true);
//            saveFile.writeToFile(String.valueOf(bricks.size()));
//            //Guarda los bloques en el juego 
//            for (int i = 0; i < bricks.size(); i++) {
//                Invader myBrick = bricks.get(i);
//                saveFile.writeToFile(String.valueOf(myBrick.getX()));
//                saveFile.writeToFile(String.valueOf(myBrick.getY()));
//                saveFile.writeToFile(String.valueOf(myBrick.getState()));
//            }
//            //Guarda valores de la bola
//            saveFile.writeToFile(String.valueOf(ball.getX()));
//            saveFile.writeToFile(String.valueOf(ball.getY()));
//            saveFile.writeToFile(String.valueOf(ball.getXSpeed()));
//            saveFile.writeToFile(String.valueOf(ball.getYSpeed()));
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//    }
    
    /**
     * Loads a game if it has a save file;
     * @throws IOException 
     */
//    public void leeArchivo() throws IOException {
//        BufferedReader fileIn;
//        try {
//            fileIn = new BufferedReader(new FileReader("save.txt"));
//        } catch (FileNotFoundException e) {
//            File puntos = new File("save.txt");
//            PrintWriter fileOut = new PrintWriter(puntos);
//            fileOut.println("100,demo");
//            fileOut.close();
//            fileIn = new BufferedReader(new FileReader("save.txt"));
//        }
//        String dato = fileIn.readLine();
//        
//        int num = (Integer.parseInt(dato));
//        int iPosX, iPosY;
//        player.setX(num);
//        
//        int numBlocks = Integer.parseInt(fileIn.readLine());
//        bricks.clear();
//        for (int i = 0; i < numBlocks; i++) {
//             iPosX = Integer.parseInt(fileIn.readLine());
//             iPosY = Integer.parseInt(fileIn.readLine());
//            bricks.add(new Invader(iPosX, iPosY, 155, 55, this));
//            
//            Invader myBrick = bricks.get(i);
//            String actualState = fileIn.readLine();
//            
//            if ("normal".equals(actualState)) {
//             myBrick.setState(Invader.status.normal);    
//            }
//            if ("hit".equals(actualState)) {
//             myBrick.setState(Invader.status.hit);    
//            }
//            if ("destroyed ".equals(actualState)) {
//             myBrick.setState(Invader.status.destroyed);    
//            }
//        }
//        
//        iPosX = Integer.parseInt(fileIn.readLine());
//        iPosY = Integer.parseInt(fileIn.readLine());
//        
//        ball.setX(iPosX);
//        ball.setY(iPosY);
//        
//        double speedX = Double.parseDouble(fileIn.readLine());
//        double speedY = Double.parseDouble(fileIn.readLine());
//        
//        ball.setXSpeed(speedX);
//        ball.setYSpeed(speedY);
//        
//        dato = fileIn.readLine();
//        while (dato != null) {
//
//            arr = dato.split(",");
//           // player.setX(arr);
//            num = (Integer.parseInt(dato));
//            
//            //String nom = arr[1];
//            //vec.add(new Puntaje(nom, num));
//            dato = fileIn.readLine();
//        }
//        fileIn.close();
//    }
    
    /**
     * Ticks every time to have the game moving
     * @throws IOException 
     */
    private void tick() throws IOException {
        keyManager.tick();
        //Pauses the game if the player presses 'P'
        if (keyManager.getPause() && getGameState() == gameState.normal) {
                setGameState(gameState.pause);
            
        }
        else {
            //Unpauses the game if the game is already paused
            if (keyManager.getPause() && getGameState() == gameState.pause) {
                setGameState(gameState.normal);
            }
        }
        
        //Loads a savefile
        if (keyManager.getLoad()) {
//            leeArchivo();
        }
        if (keyManager.getShoot() && !bulletExists) {
            bullet = new Projectile(player.getX()+player.getWidth()/2 - 2, player.getY(), 5, 10, this);
            bulletExists = true;
            
        }
        if (bullet != null) {
            bullet.tick();
        }
        
        if (getGameState() == gameState.normal) {
            if (getPlayer().getState() == Player.playerState.dead) {
                setGameState(gameState.gameOver);
            }
            
            //Saves the game if the player presses "G"
            if (keyManager.getSave()) {
//                save();
            }
            if (keyManager.getRestart()) {
                setGameState(gameState.gameOver);
            }
            
            // advancing player with collision
            player.tick();
            
            // ticking invaders
            for (int i = 0; i < invaders.size(); i++) {
                Invader myInvader = invaders.get(i);
                myInvader.tick();
            }
            
            //Checks if there's a collison with the invader
            for(int i = 0; i < invaders.size(); i++){
                Invader myInvader = invaders.get(i);

                boolean Intersects = bullet.intersecta(myInvader);

                if(myInvader.getState() != Invader.status.destroyed && Intersects){
                    myInvader.setState(Invader.status.destroyed);
                    Intersects = false;
                    bullet.setY(-bullet.getHeight());
                }
                
                if (myInvader.getState() == Invader.status.destroyed) {                    
                    if (myInvader.isAnimOver())   invaders.remove(i);
                }
            }
        }

        //stops everything until the player presses "enter" if he loses
        if (getGameState() == gameState.gameOver) {
             for (int i = 0; i < invaders.size(); i++) {
                 invaders.remove(i);
            }
            if (keyManager.enter) {
                setGameState(gameState.normal);
                keyManager.setStart(false);
                player = new Player(getWidth()/2 - 113, getHeight() - 75, 1, 226, 50, this);
                // Add pulpo invaders
                for (int j = 0; j < 12; j++) {
                    int iPosX = j * ALIEN_WIDTH;
                    invaders.add(new Invader(iPosX + BORDERX, BORDERY * 3, ALIEN_WIDTH, ALIEN_HEIGHT, Invader.Type.pulpo, this));            
                }
                // Add weird invaders
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 12; j++) {
                        int iPosX = j * ALIEN_WIDTH;
                        int iPosY = ALIEN_HEIGHT + i * ALIEN_HEIGHT;
                        invaders.add(new Invader(iPosX + BORDERX, iPosY + BORDERY * 4, ALIEN_WIDTH, ALIEN_HEIGHT, Invader.Type.monstro, this));            
                    }
                }        
                // Add normal invaders
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 12; j++) {
                        int iPosX = j * ALIEN_WIDTH;
                        int iPosY = (ALIEN_HEIGHT * 3) + i * ALIEN_HEIGHT;
                        invaders.add(new Invader(iPosX + BORDERX, iPosY + BORDERY * 6, ALIEN_WIDTH, ALIEN_HEIGHT, Invader.Type.invader, this));            
                    }
                }
            }
        }
    }
    
    /**
     * Prints the graphics on the screen 
     */
    private void render() {
        //get the buffer strategy from the display
        bs = display.getCanvas().getBufferStrategy();
        /*if it is null, we define one with 3 buffers to display images of
        the game, if not null, then we display every image of the game but
        after clearing the Rectangle, getting the graphic object from the
        buffer strategy element.
        show the graphic and dispose it to the trash system
        */
        if (bs == null) {
            display.getCanvas().createBufferStrategy(3);
        }
        else {
            g = bs.getDrawGraphics();
            if (getGameState() == gameState.normal || getGameState() == gameState.pause) {
                g.drawImage(Assets.background, 0, 0, width, height, null);
                player.render(g);   
                bullet.render(g);
                //Draws the invaders in the screen
                for (int i = 0; i < invaders.size(); i++) {
                    Invader myInvader = invaders.get(i);
                    myInvader.render(g);
                }
                //Draws the pause screen
                if (getGameState() == gameState.pause) {
                    g.drawImage(Assets.pause, 0, 0, getWidth(), getHeight(), null);
                }                
            }
            
            //Draws the gameover screen if the player lost
            if (getGameState() == gameState.gameOver) {
                g.drawImage(Assets.gameOver, 0, 0, getWidth(), getHeight(), null);
            }            
            
            bs.show();
            g.dispose();
        }
    }
    
    /**
     * setting the thread for the game
     */
    public synchronized void start() {
        if (!running) {
            running = true;
            thread = new Thread(this);
            thread.start();
        }
    }
    
    /**
     * stopping the thread
     */
    public synchronized void stop() {
        if (running) {
            running = false;
            try {
                thread.join();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }    
}
