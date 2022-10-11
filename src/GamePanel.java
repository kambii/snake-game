
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


import static java.awt.Color.green;
import static java.awt.Color.red;

public class GamePanel extends JPanel implements ActionListener {




    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    // how big we want the object in the game
    static final int UNIT_SIZE = 25;
    // I want to calculate how many objects I can feet on the screen
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;

    // delay for the timer
    static final int DELAY = 75;

    // holds all the coordinates for all the bodyParts of our snake including the head of the snake
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];

    // set amount of bodyParts for the snake
    int bodyParts = 6;

    // apples eaten
    int applesEaten;

    // x coordinate where the apple is located
    int appleX;

    // for the y positioning
    int appleY;

    // witch direction the snake is taking
    char direction = 'R';

    boolean running = false;
    Timer timer;
    Random random;


    //constructor method
    GamePanel(){
        random = new Random();
        // preferred size for the game panel
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

        // set background color
        this.setBackground(Color.black);

        //set the focusability
        this.setFocusable(true);

        // add a key listener
        this.addKeyListener(new MyKeyAdepter());

        // call the start game method
        startGame();
    }

    // we have void in these methods that means int won't return anything
    // start game function
    public void startGame(){
        // call the new apple method to create a new apple on the screen
        newApple();

        // boolean
        running = true;

        // instance of our timer
        timer = new Timer(DELAY,this);
        timer.start();
    }
    // paint component function
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    // draw function
    public void draw(Graphics g) {
        // using this for loop to draw lines across the game panel

        if (running){
            /*
        for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);

        }

             */

        // color for the apple
        g.setColor(Color.red);
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

        // head of the snake and the body
        for (int i = 0; i < bodyParts;i++) {
            if (i == 0) {
                g.setColor(Color.green);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            } else {
                g.setColor(new Color(45, 180, 0));
                g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

            }
        }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
        }
        else{
            gameOver(g);
        }
    }

    // new apple method
    public void newApple(){
        //  generates the coordinates av a new apple whenever this method is called
        appleX = random.nextInt((int) (SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE);
        appleY = random.nextInt((int) (SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE);

    }

    // move method
    // going to move the snake with this method
    public void move(){
        // created a for loop to repeat through all the body parts of the snake
        for (int i = bodyParts; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        // change the direction where the snake is heded
        switch (direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;

        }
    }
    // check apple method
    public void checkApple(){
        if ((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    // we want to check for collisions
    public void checkCollisions(){
        // checks if head collides with body
        for (int i = bodyParts; i >0; i--){
            if ((x[0] == x[i])&& (y[0] == y[i])){

                // this is going to trigger the gameOver method
                running = false;
            }
        }
        // check if the head of the snake touches the borders and will cuz gameOver
        if (x[0] < 0){
            running = false;
        }
        if (x[0] > SCREEN_WIDTH){
            running = false;
        }
        if (y[0] < 0){
            running = false;
        }
        if (y[0] > SCREEN_HEIGHT){
            running = false;
        }
        // stop the timer
        if (!running){
            timer.stop();
        }

    }
    public void gameOver(Graphics g){
        // score
        g.setColor(red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
        // game over text
        g.setColor(red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // calling the move function
        if (running){
            move();
            checkApple();
            checkCollisions();
        }
        // if the game is no longer running, call the repaint method
        repaint();
    }

    // innerClass
    public class MyKeyAdepter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if (direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }

    }


}
