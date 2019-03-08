
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
    private Player player; // to use a player
    private LinkedList<Brick> bricks ;// to use the bricks 
    private Projectile ball; //to use the projectile
    private KeyManager keyManager; // to manage the keyboard
    private WriteFile saveFile; //to store the saveFile
    private enum gameState { normal, gameOver, pause, victory }
    private gameState gameState; //to have the gameStates
    private int brickTimer; //invulnerability frames for bricks
    private SoundClip loseSound; //Sound if the player losses
    private long tiempoActual; //tiempo de la animacion 
    private String[] arr; //aux for the reading file
    private LinkedList<PowerUp> drops; //PowerUps Drops
    private int powerTimer; //timer for the powerUps Drops
    
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
        this.gameState = gameState.normal;
        bricks = new LinkedList<Brick>();
        brickTimer = 0;
        drops = new LinkedList<PowerUp>();
        powerTimer = 0;
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
    
    /**
     * Gets the projectile that is used in the game
     * @return the projectile object
     */
    public Projectile getBall() {
        return ball;
    }

    /**
     * Gets the actual game state of the game
     * @return the actual game state of the game
     */
    public gameState getGameState() {
        return gameState;
    }

    /**
     * Changes the state of the game
     * @param gameState  that is the new state of the game
     */
    public void setGameState(gameState gameState) {
        this.gameState = gameState;
    }
        
    /**
     * Initializing the display window of the game
     */
    private void init() {
        display = new Display(title, width, height);
        Assets.init();        
        loseSound = Assets.loseSound;
        player = new Player(getWidth()/2 - 113, getHeight() - 75, 1, 226, 50, this);
        ball = new Projectile(player.getX() + player.getWidth()/2 - 25, player.getY() - 51, 50, 50, this);

        int azarX = (int) (Math.random() * (9 - 1 + 1)) + 1;
        int azarY = (int) (Math.random() * (4 - 0 + 1)) + 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 9; j++) {
                int iPosX = j * 141;
                int iPosY = 158 - i * 47;
                bricks.add(new Brick(iPosX, iPosY, 155, 55, this));
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
    private void save() {
        try {
            saveFile = new WriteFile("save.txt", false);
            saveFile.writeToFile(String.valueOf(player.getX()));
            saveFile.setAppend(true);
            saveFile.writeToFile(String.valueOf(bricks.size()));
            //Guarda los bloques en el juego 
            for (int i = 0; i < bricks.size(); i++) {
                Brick myBrick = bricks.get(i);
                saveFile.writeToFile(String.valueOf(myBrick.getX()));
                saveFile.writeToFile(String.valueOf(myBrick.getY()));
                saveFile.writeToFile(String.valueOf(myBrick.getState()));
            }
            //Guarda valores de la bola
            saveFile.writeToFile(String.valueOf(ball.getX()));
            saveFile.writeToFile(String.valueOf(ball.getY()));
            saveFile.writeToFile(String.valueOf(ball.getXSpeed()));
            saveFile.writeToFile(String.valueOf(ball.getYSpeed()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Loads a game if it has a save file;
     * @throws IOException 
     */
    public void leeArchivo() throws IOException {
        BufferedReader fileIn;
        try {
            fileIn = new BufferedReader(new FileReader("save.txt"));
        } catch (FileNotFoundException e) {
            File puntos = new File("save.txt");
            PrintWriter fileOut = new PrintWriter(puntos);
            fileOut.println("100,demo");
            fileOut.close();
            fileIn = new BufferedReader(new FileReader("save.txt"));
        }
        String dato = fileIn.readLine();
        
        int num = (Integer.parseInt(dato));
        int iPosX, iPosY;
        player.setX(num);
        
        int numBlocks = Integer.parseInt(fileIn.readLine());
        bricks.clear();
        for (int i = 0; i < numBlocks; i++) {
             iPosX = Integer.parseInt(fileIn.readLine());
             iPosY = Integer.parseInt(fileIn.readLine());
            bricks.add(new Brick(iPosX, iPosY, 155, 55, this));
            
            Brick myBrick = bricks.get(i);
            String actualState = fileIn.readLine();
            
            if ("normal".equals(actualState)) {
             myBrick.setState(Brick.status.normal);    
            }
            if ("hit".equals(actualState)) {
             myBrick.setState(Brick.status.hit);    
            }
            if ("destroyed ".equals(actualState)) {
             myBrick.setState(Brick.status.destroyed);    
            }
        }
        
        iPosX = Integer.parseInt(fileIn.readLine());
        iPosY = Integer.parseInt(fileIn.readLine());
        
        ball.setX(iPosX);
        ball.setY(iPosY);
        
        double speedX = Double.parseDouble(fileIn.readLine());
        double speedY = Double.parseDouble(fileIn.readLine());
        
        ball.setXSpeed(speedX);
        ball.setYSpeed(speedY);
        
        dato = fileIn.readLine();
        while (dato != null) {

            arr = dato.split(",");
           // player.setX(arr);
            num = (Integer.parseInt(dato));
            
            //String nom = arr[1];
            //vec.add(new Puntaje(nom, num));
            dato = fileIn.readLine();
        }
        fileIn.close();
    }
    
    /**
     * Ticks every time to have the game moving
     * @throws IOException 
     */
    private void tick() throws IOException {
        brickTimer--;
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
            leeArchivo();
        }
        
        if (getGameState() == gameState.normal) {
            if (getPlayer().getState() == Player.playerState.dead) {
                setGameState(gameState.gameOver);
                loseSound.play();
            }
            
            //Saves the game if the player presses "G"
            if (keyManager.getSave()) {
                save();
            }
            
            if (bricks.isEmpty()) {
                setGameState(gameState.victory);
            }
            
            // advancing player with collision
            player.tick();
            // Returning the player to normal size when timer is done
            if (powerTimer <= 0) {
                resizeBar(226);
            }
            else {
                powerTimer--;
            }
            // ticking the ball
            ball.tick();
            // ticking bricks
            for (int i = 0; i < bricks.size(); i++) {
                Brick myBrick = bricks.get(i);
                myBrick.tick();
            }
            // ticking powerUps
            for (int i = 0; i < drops.size(); i++) {
                PowerUp myPower = drops.get(i);
                myPower.tick();
                // Delete from game if power up isnt caught by player
                if (myPower.getY() >= getHeight()) {
                    drops.remove(i);
                }
                if (myPower.getHitbox().intersects(getPlayer().getHitbox())) {
                    if (myPower.getType() == PowerUp.Type.good) {
                        resizeBar(226 * 2);
                        drops.remove(i);                        
                    }
                    else if (myPower.getType() == PowerUp.Type.bad) {
                        resizeBar(226 / 2);
                        drops.remove(i);
                    }
                }
            }
            //Checks if there's a collison with the bricks
            for(int j = 0; j < bricks.size(); j++){
                Brick myBrick = bricks.get(j);

                boolean Intersects = ball.intersecta(myBrick);

                if(myBrick.getState() != Brick.status.destroyed && Intersects && brickTimer <= 0){
                    if (myBrick.getState() == Brick.status.normal) myBrick.setState(Brick.status.hit);
                    else if (myBrick.getState() == Brick.status.hit) myBrick.setState(Brick.status.destroyed);
                    Intersects = false;
                    brickTimer = 10;
                    ball.setYSpeed(ball.getYSpeed() * -1);
                    ball.setXSpeed(ball.getXSpeed() * -1);
                }
                
                if (myBrick.getState() == Brick.status.destroyed) {
                    
                    if (!myBrick.isAlreadyDropped() && myBrick.getDropProb() <= myBrick.getGoodDropChance()) {
                        drops.add(new PowerUp(myBrick.getX() + myBrick.getWidth() / 2 - 25, myBrick.getY(), 50, 50, PowerUp.Type.good, this));
                        myBrick.setAlreadyDropped(true);
                    }
                    else if (!myBrick.isAlreadyDropped() && myBrick.getDropProb() >= myBrick.getBadDropChance()) {
                        drops.add(new PowerUp(myBrick.getX() + myBrick.getWidth() / 2 - 25, myBrick.getY(), 50, 50, PowerUp.Type.bad, this));
                        myBrick.setAlreadyDropped(true);
                    }
                    if (myBrick.isAnimOver())   bricks.remove(j);
                }
            }
        }
        //Stops everything if the player wins 
        if (getGameState() == gameState.victory) {
            if (keyManager.enter) {
                setGameState(gameState.normal);
                keyManager.setStart(false);
                player = new Player(getWidth()/2 - 113, getHeight() - 75, 1, 226, 50, this);
                ball = new Projectile(player.getX() + player.getWidth()/2 - 25, player.getY() - 51, 50, 50, this);
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 9; j++) {
                        int iPosX = j * 141;
                        int iPosY = 158 - i * 47;
                        bricks.add(new Brick(iPosX, iPosY, 155, 55, this));
                    }
                }
            }
        }

        //stops everything until the player presses "enter" if the loses
        if (getGameState() == gameState.gameOver) {
             for (int i = 0; i < bricks.size(); i++) {
                 bricks.remove(i);
             }
             for (int i = 0; i < drops.size(); i++) {
                 drops.remove(i);
             }
            if (keyManager.enter) {
                setGameState(gameState.normal);
                keyManager.setStart(false);
                player = new Player(getWidth()/2 - 113, getHeight() - 75, 1, 226, 50, this);
                ball = new Projectile(player.getX() + player.getWidth()/2 - 25, player.getY() - 51, 50, 50, this);
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 9; j++) {
                        int iPosX = j * 141;
                        int iPosY = 158 - i * 47;
                        bricks.add(new Brick(iPosX, iPosY, 155, 55, this));
                    }
                }
            }
        }
    }
    
    /**
     * Changes the size of the bar
     * @param width is the new size of the bar
     */
    private void resizeBar(int width) {
        getPlayer().setWidth(width);
        getPlayer().getHitbox().setSize(width, getPlayer().getHeight());
        powerTimer = 300;
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
                ball.render(g);
                //Prints the bricks in the screen
                for (int i = 0; i < bricks.size(); i++) {
                    Brick myBrick = bricks.get(i);
                    myBrick.render(g);
                }
                //Prints the powerUps if there's any
                for (int i = 0; i < drops.size(); i++) {
                    PowerUp myPower = drops.get(i);
                    myPower.render(g);
                }
                //Prints the pause screen
                if (getGameState() == gameState.pause) {
                    g.drawImage(Assets.pause, 0, 0, getWidth(), getHeight(), null);
                }
                
            }
            
            //Prints the gameover screen if the player lost
            if (getGameState() == gameState.gameOver) {
                g.drawImage(Assets.gameOver, 0, 0, getWidth(), getHeight(), null);
            }
            
            //Prints the victory screen if the player won
            if (getGameState() == gameState.victory) {
                g.drawImage(Assets.victory, 0, 0, getWidth(), getHeight(), null);
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
