
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.IOException;
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
    private int score; // score of the game
  //  private Bullet bullet; //to use a bullet
    public enum gameState { normal, pause, gameOver} // enum for the game states
    private gameState gameState; // variable to store the current game state
    public static int GROUND; // to check when invaders hit the ground and invade us
    public static final int ALIEN_HEIGHT = 25; // invader's height
    public static final int ALIEN_WIDTH = 50;  // invader's width
    public static final int BORDERX = 25;     // horizontal space between invaders
    public static final int BORDERY = 25;     // verticla space between invaders
    public static final int PLAYER_WIDTH = 50; // player's width
    public static final int PLAYER_HEIGHT = 50; // player's height
    public int delay = 40;          // delay between invader's movements
    private LinkedList<Invader> invaders; // list to store the invaders
    private Projectile bullet;  // bullet shot by the player
    private boolean goingLeft; // variable to determine in what direction the invaders ar moving
    private boolean goingDown; // variable to indicate when to move the invaders down
    private int jumpTimer;     // timer to count between each invader jump
    
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
        goingLeft = false;
        goingDown = false;
        jumpTimer = delay;
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
     * Gets the current state of the game
     * @return the gameState the game is currently at
     */
    public gameState getGameState() {
        return gameState;
    }
    
    /**
     * Sets the current game state
     * @param gameState of the game
     */
    public void setGameState(gameState gameState) {
        this.gameState = gameState;
    }
    
    /**
     * Gets the bullet shot by the player
     * @return the bullet 
     */
    public Projectile getBullet() {
        return bullet;
    }  
    
    /**
     * Sets whether the bullet exists or not
     * @param bulletExists 
     */
    public void setBulletExists(boolean bulletExists) {
        this.bulletExists = bulletExists;
    }
    
    /**
     * Checks if the invaders are going left or not
     * @return whether the invaders are moving left or not
     */
    public boolean isGoingLeft() {
        return goingLeft;
    }
    
    /**
     * Sets if the invaders are going left or not
     * @param goingLeft 
     */
    public void setGoingLeft(boolean goingLeft) {
        this.goingLeft = goingLeft;
    }
    
    /**
     * Gets the int value of the timer between invader's movements
     * @return the int value of the timer
     */
    public int getJumpTimer() {
        return jumpTimer;
    }
    
    /**
     * Sets the timer for the invader's movements
     * @param jumpTimer 
     */
    public void setJumpTimer(int jumpTimer) {
        this.jumpTimer = jumpTimer;
    }
    
    /**
     * Checks whether the invaders are moving down or not
     * @return whether the invaders are moving down or not
     */
    public boolean isGoingDown() {
        return goingDown;
    }
    
    /**
     * Sets if the invaders are moving down or not
     * @param goingDown 
     */
    public void setGoingDown(boolean goingDown) {
        this.goingDown = goingDown;
    }   
        
    /**
     * Initializing the display window of the game
     */
    private void init() {
        display = new Display(title, width, height);
        bullet = null;
        score = 0;
        bulletExists = false;
        Assets.init();        
        player = new Player(getWidth()/2 - PLAYER_WIDTH/2, getHeight() - PLAYER_HEIGHT - BORDERY, 1, PLAYER_WIDTH, PLAYER_HEIGHT, this);
        bullet = new Projectile(getWidth()/2, -50, 50, 50, this);
        // Add pulpo invaders
        for (int j = 0; j < 11; j++) {
            int iPosX = (BORDERX + ALIEN_WIDTH)* j;
            invaders.add(new Invader(iPosX + BORDERX, BORDERY * 3, ALIEN_WIDTH, ALIEN_HEIGHT, Invader.Type.pulpo, this));            
        }
        // Add normal invaders
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 11; j++) {
                int iPosX = (BORDERX + ALIEN_WIDTH)* j;
                int iPosY = BORDERY * i + ALIEN_HEIGHT + i * ALIEN_HEIGHT;
                invaders.add(new Invader(iPosX + BORDERX, iPosY + BORDERY * 4, ALIEN_WIDTH, ALIEN_HEIGHT, Invader.Type.invader, this));            
            }
        }        
        // Add weird invaders
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 11; j++) {
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
        
        // Restarts the game at any moment if the player presser 'R'
        if (keyManager.getRestart()) {
            restartGame();
        }
        
        // "Normal" game behaviour
        if (getGameState() == gameState.normal) {
            // Makes the player lose if he runs out of lives
            if (getPlayer().getCurrentLives() <= 0 && getPlayer().getDeathTimer() <= 0) {
                setGameState(gameState.gameOver);
            }
            // If player clears enemies gets harder
            if (invaders.isEmpty()) {
                setJumpTimer(delay);
                delay = delay / 2;
                // Add pulpo invaders
                for (int j = 0; j < 11; j++) {
                    int iPosX = (BORDERX + ALIEN_WIDTH)* j;
                    invaders.add(new Invader(iPosX + BORDERX, BORDERY * 3, ALIEN_WIDTH, ALIEN_HEIGHT, Invader.Type.pulpo, this));            
                }
                // Add normal invaders
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 11; j++) {
                        int iPosX = (BORDERX + ALIEN_WIDTH)* j;
                        int iPosY = BORDERY * i + ALIEN_HEIGHT + i * ALIEN_HEIGHT;
                        invaders.add(new Invader(iPosX + BORDERX, iPosY + BORDERY * 4, ALIEN_WIDTH, ALIEN_HEIGHT, Invader.Type.invader, this));            
                    }
                }        
                // Add weird invaders
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 11; j++) {
                        int iPosX = (BORDERX + ALIEN_WIDTH)* j;
                        int iPosY = BORDERY * i + ALIEN_HEIGHT * 3 + i * ALIEN_HEIGHT;
                        invaders.add(new Invader(iPosX + BORDERX, iPosY + BORDERY * 6, ALIEN_WIDTH, ALIEN_HEIGHT, Invader.Type.monstro, this));            
                    }
                }
            }
            
            //Saves the game if the player presses "G"
            if (keyManager.getSave()) {
//                save();
            }            

            // advancing player with collision
            player.tick();
            if (getPlayer().getState() == Player.playerState.alive) {
                // Reduce the timer or reset it
                if (getJumpTimer() >= 0) setJumpTimer(getJumpTimer() - 1);
                else {                
                    setJumpTimer(delay);
                }
                // Shoots the player's bullet
                if (keyManager.getShoot() && !bulletExists) {
                    bullet = new Projectile(player.getX()+player.getWidth()/2 - 2, player.getY(), 5, 10, this);
                    bulletExists = true;
                    Assets.shootSound.play();
                }
                // Updates the player's bullet if it exists
                if (bullet != null) {
                    bullet.tick();
                }
                // Checks if the invaders need to move down or change direction
                if (getJumpTimer() == 0) {
                    for (int i = 0; i < invaders.size(); i++) {
                        Invader myInvader = invaders.get(i);  
                        if ((isGoingLeft() && myInvader.getX() - myInvader.getWidth() <= 0) || (!isGoingLeft() && myInvader.getX() + myInvader.getWidth() *2 >= getWidth())) {
                            setGoingDown(true);
                        }
                        if (myInvader.getX() - myInvader.getWidth() <= 0) {
                            setGoingLeft(false);
                        }
                        else if (myInvader.getX() + myInvader.getWidth() *2 >= getWidth()) {
                            setGoingLeft(true);
                        }
                    }
                }
                // ticking invaders
                for (int i = 0; i < invaders.size(); i++) {
                    Invader myInvader = invaders.get(i);
                    if (isGoingDown()) myInvader.setGoingDown(true);
                    myInvader.tick();
                    // Makes the player lose if the invaders reach the bottom of the screen
                    if (myInvader.getY() >= getPlayer().getY()) {
                        setGameState(gameState.gameOver);
                    }
                    // Destroys the invader if he was shot by the player and its animation is over
                    if (myInvader.getState() == Invader.status.destroyed && myInvader.getDestroyTimer() <= 0) {
                        invaders.remove(i);
                    }
                }

                if (isGoingDown()) setGoingDown(false);

                //Checks if there's a collison with the invader
                for(int i = 0; i < invaders.size(); i++){
                    Invader myInvader = invaders.get(i);
                    Bomb myBomb = myInvader.getBomb();
                    if (myBomb != null) {
                        myBomb.tick();
                        // Checks if the player is hit by a bomb
                        if (getPlayer().getState()!= Player.playerState.dead && myBomb.getHitbox().intersects(getPlayer().getHitbox())) {
                            getPlayer().setCurrentLives(getPlayer().getCurrentLives() - 1);
                            getPlayer().setState(Player.playerState.dead);
                            Assets.deathSound.play();
                            myInvader.setBomb(null);
                        }
                    }

                    boolean Intersects = bullet.intersecta(myInvader);
                    // Checks if the invader was shot by the player
                    if(myInvader.getState() != Invader.status.destroyed && Intersects){
                        myInvader.setState(Invader.status.destroyed);
                        Assets.destroySound.play();
                        Intersects = false;
                        bullet.setY(-bullet.getHeight());
                        myInvader.setDestroyTimer(15);
                        score += 100; 
                    }
                }
            }
            else {
                setJumpTimer(delay);
            }
            
        }

        //stops everything until the player presses "enter" if he loses
        if (getGameState() == gameState.gameOver) {             
            if (keyManager.enter) {
                restartGame();
            }
        }
    }
    
    /**
     * restartGame()
     * Function to restart the game
     */
    private void restartGame() {
        setGameState(gameState.normal);
            for (int i = 0; i < invaders.size(); i++) {
                invaders.remove(i);
            }
            invaders = new LinkedList<Invader>();
            goingLeft = false;
            goingDown = false;
            jumpTimer = delay;
            getPlayer().setCurrentLives(getPlayer().LIVES);
            bullet = null;
            score = 0;
            bulletExists = false;
            player = new Player(getWidth()/2 - PLAYER_WIDTH/2, getHeight() - PLAYER_HEIGHT - BORDERY, 1, PLAYER_WIDTH, PLAYER_HEIGHT, this);
            bullet = new Projectile(getWidth()/2, -50, 50, 50, this);
            // Add pulpo invaders
            for (int j = 0; j < 11; j++) {
                int iPosX = (BORDERX + ALIEN_WIDTH)* j;
                invaders.add(new Invader(iPosX + BORDERX, BORDERY * 3, ALIEN_WIDTH, ALIEN_HEIGHT, Invader.Type.pulpo, this));            
            }
            // Add normal invaders
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 11; j++) {
                    int iPosX = (BORDERX + ALIEN_WIDTH)* j;
                    int iPosY = BORDERY * i + ALIEN_HEIGHT + i * ALIEN_HEIGHT;
                    invaders.add(new Invader(iPosX + BORDERX, iPosY + BORDERY * 4, ALIEN_WIDTH, ALIEN_HEIGHT, Invader.Type.invader, this));            
                }
            }        
            // Add weird invaders
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 11; j++) {
                    int iPosX = (BORDERX + ALIEN_WIDTH)* j;
                    int iPosY = BORDERY * i + ALIEN_HEIGHT * 3 + i * ALIEN_HEIGHT;
                    invaders.add(new Invader(iPosX + BORDERX, iPosY + BORDERY * 6, ALIEN_WIDTH, ALIEN_HEIGHT, Invader.Type.monstro, this));            
                }
            }
            keyManager.setRestart(false);
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
                    Bomb myBomb = myInvader.getBomb();
                    if (myBomb != null) {
                        myBomb.render(g);
                    }
                } 
                // Draw the score
                g.setFont(new Font("Arial", Font.PLAIN, 40));
                g.setColor(Color.white);
                g.drawString("Score: ", 0, 40);
                g.drawString(String.valueOf(score), 120, 40);
                // Draw the lives
                g.drawImage(Assets.player, getWidth()-90, 10, 30, 30, null);
                g.drawString("x" + Integer.toString(getPlayer().getCurrentLives()), getWidth()-50, 40);
                //Draws the pause screen
                if (getGameState() == gameState.pause) {
                    g.drawImage(Assets.pause, 0, 0, getWidth(), getHeight(), null);
                }                
            }
            
            //Draws the gameover screen if the player lost
            if (getGameState() == gameState.gameOver) {
                g.drawImage(Assets.gameOver, 0, 0, getWidth(), getHeight(), null);
                g.setFont(new Font("Arial", Font.PLAIN, 100));
                g.setColor(Color.white);
                g.drawString("Score: ", getWidth()/3, getHeight()/5 * 4);
                g.drawString(Integer.toString(score), getWidth()/3 + 300, getHeight()/5 * 4);
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
