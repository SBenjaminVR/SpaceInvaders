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
public class VideoGame {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //creating a Game object
        Game g = new Game("Juego", 1280, 720);

        //initializing the game
        g.start();
    }
}
