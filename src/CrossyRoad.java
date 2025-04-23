import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;


    public class CrossyRoad implements Runnable, KeyListener, MouseListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            System.out.println(e.getKeyChar());
            System.out.println(e.getKeyCode());

            //moving up
            if(e.getKeyCode() == 38){
                System.out.println("up");
                astro.up = true;
                astro.down = false;
            }
            //moving down
            if(e.getKeyCode() == 40){
                System.out.println("down");
                astro.down = true;
                astro.up = false;
            }
            //moving left
            if(e.getKeyCode() == 37){
                System.out.println("left");
                astro.left = true;
                astro.right = false;
            }
            //moving right
            if(e.getKeyCode() == 39){
                System.out.println("right");
                astro.right = true;
                astro.left = false;
            }
        }


        @Override
        public void keyReleased(KeyEvent e) {
            System.out.println("released?");
            System.out.println(e.getKeyChar());
            //moving up
            if(e.getKeyCode() == 38){
                astro.up = false;
            }
            //moving down
            if(e.getKeyCode() == 40){
                astro.down = false;
            }
            //moving left
            if(e.getKeyCode() == 37){
                astro.left = false;
            }
            //moving right
            if(e.getKeyCode() == 39){
                astro.right = false;
            }
        }

        final int WIDTH = 1000;
        final int HEIGHT = 700;

        public JFrame frame;
        public Canvas canvas;
        public JPanel panel;

        public BufferStrategy bufferStrategy;
        public Image astroPic;
        public Image astro2Pic;
        public Image backgroundPic;

        private Astronaut astro;
        private Astronaut astro2;


        Astronaut [] astronautsArray = new Astronaut[0];


        public static void main(String[] args) {
            CrossyRoad ex = new CrossyRoad();
            new Thread(ex).start();
        }

        public CrossyRoad() {
            setUpGraphics();

            backgroundPic = Toolkit.getDefaultToolkit().getImage("Background.png");
            astroPic = Toolkit.getDefaultToolkit().getImage("astronaut.png");
            astro2Pic = Toolkit.getDefaultToolkit().getImage("astronaut2.png");

            astro = new Astronaut(120,600);
            astro2 = new Astronaut(0,350);

            astro2.dy = 0;

            for(int x = 0; x <astronautsArray.length; x++) {
                astronautsArray[x] = new Astronaut((int) (Math.random()* 900), (int)(Math.random()* 600));
            }
        }

        public void run() {
            //for the moment we will loop things forever.
            while (true) {
                moveThings();  //move all the game objects
                render();  // paint the graphics
                pause(20); // sleep for 10 ms
            }
        }


        public void moveThings() {
            collisions();
            astro.bounce();
            astro2.wrap();

            for(int y = 0; y < astronautsArray.length; y++){
                astronautsArray[y].wrap();
            }
        }

        public void collisions(){
            if(astro.rec.intersects(astro2.rec) && astro.isCrashing == false && astro.isAlive && astro2.isAlive){
                astro.dx = -astro.dx;
                astro.dy = -astro.dy;
                astro2.dx = -astro.dx;
                astro2.dy = -astro2.dy;
                astro2.isAlive = false;
                astro.width = astro.width + 0;
                astro.height = astro.height + 0;
                astro2.dx = astro2.dx + 0;
                astro2.dy = astro2.dy + 0;
                astro.isCrashing = true;
            }

            if(!astro.rec.intersects(astro2.rec)){
                astro.isCrashing = false;
            }

            for(int b = 0; b < astronautsArray.length; b++){
                if(astro.rec.intersects(astronautsArray[b].rec)){
                }
            }



        }

        public void pause(int time ){
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
            }
        }

        private void setUpGraphics() {
            frame = new JFrame("Application Template");

            panel = (JPanel) frame.getContentPane();
            panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
            panel.setLayout(null);


            canvas = new Canvas();
            canvas.setBounds(0, 0, WIDTH, HEIGHT);
            canvas.setIgnoreRepaint(true);

            canvas.addKeyListener(this);
            panel.add(canvas);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setResizable(false);
            frame.setVisible(true);
            canvas.addMouseListener(this);
            canvas.createBufferStrategy(2);
            bufferStrategy = canvas.getBufferStrategy();
            canvas.requestFocus();
            System.out.println("DONE graphic setup");

        }


        private void render() {
            Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
            g.clearRect(0, 0, WIDTH, HEIGHT);
            g.drawImage(backgroundPic, 0, 0, WIDTH, HEIGHT, null);
            g.drawImage(astroPic, astro.xpos, astro.ypos, astro.width, astro.height, null);
            g.drawImage(astro2Pic, astro2.xpos, astro2.ypos, astro2.width, astro2.height, null);

            for(int l = 0; l < astronautsArray.length; l++){
                g.drawImage(astroPic, astronautsArray[l].xpos, astro.ypos, astro.width, astro.height, null);
            }


            g.dispose();
            bufferStrategy.show();
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            System.out.println("x "+e.getX());
            System.out.println("y "+e.getY());
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

