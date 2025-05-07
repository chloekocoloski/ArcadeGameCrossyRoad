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
                raceCar.up = true;
                raceCar.down = false;
            }
            //moving down
            if(e.getKeyCode() == 40){
                System.out.println("down");
                raceCar.down = true;
                raceCar.up = false;
            }
            //moving left
            if(e.getKeyCode() == 37){
                System.out.println("left");
                raceCar.left = true;
                raceCar.right = false;
            }
            //moving right
            if(e.getKeyCode() == 39){
                System.out.println("right");
                raceCar.right = true;
                raceCar.left = false;
            }
        }


        @Override
        public void keyReleased(KeyEvent e) {
            System.out.println("released?");
            System.out.println(e.getKeyChar());
            //moving up
            if(e.getKeyCode() == 38){
                raceCar.up = false;
            }
            //moving down
            if(e.getKeyCode() == 40){
                raceCar.down = false;
            }
            //moving left
            if(e.getKeyCode() == 37){
                raceCar.left = false;
            }
            //moving right
            if(e.getKeyCode() == 39){
                raceCar.right = false;
            }
        }

        final int WIDTH = 1000;
        final int HEIGHT = 700;

        public JFrame frame;
        public Canvas canvas;
        public JPanel panel;
        public BufferStrategy bufferStrategy;
        public BufferStrategy dxbufferStrategy;
        public Image firePic;
        public Image backgroundPic;
        public Image raceCarPic;
        private item raceCar;

        item[] fireArray =new item[10];


        public static void main(String[] args) {
            CrossyRoad ex = new CrossyRoad();
            new Thread(ex).start();

        }


        public CrossyRoad() {
            setUpGraphics();

            backgroundPic = Toolkit.getDefaultToolkit().getImage("field.png");
            raceCarPic = Toolkit.getDefaultToolkit().getImage("net.webp");
            firePic = Toolkit.getDefaultToolkit().getImage("woodstock.png");
            raceCar = new item (120,600);



            for(int x = 0; x < fireArray.length; x++) {
                    fireArray[x] = new item((int) (Math.random()* 900), (int)(Math.random()* 600));
                    fireArray[x].dy = 0;
            }
        }

        public void run() {
            while (true) {
                moveThings();  //move all the game objects
                render();  // paint the graphics
                pause(20); // sleep for 10 ms
            }
        }


        public void moveThings() {
            collisions();
            raceCar.bounce();
            for(int x = 0; x < fireArray.length; x++) {
                fireArray[x].wrap();
            }


            for(int y = 0; y < fireArray.length; y++){
               fireArray[y].wrap();

            }

        }


        public void collisions(){
            for(int x = 0; x < fireArray.length; x++) {
                if (raceCar.rec.intersects(fireArray[x].rec) && raceCar.isCrashing == false && raceCar.isAlive && fireArray[x].isAlive) {
                    raceCar.dx = -raceCar.dx;
                    raceCar.dy = -raceCar.dy;
                    fireArray[x].dx = -raceCar.dx;
                    fireArray[x].dy = -fireArray[x].dy;
                    fireArray[x].isAlive = false;
                    showWoodstock = false;


                }
            }

            for(int x = 0; x < fireArray.length; x++) {
                if (!raceCar.rec.intersects(fireArray[x].rec)) {
                    raceCar.isCrashing = false;
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
        boolean showWoodstock = true;


        private void render() {
            Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
            g.clearRect(0, 0, WIDTH, HEIGHT);
            g.drawImage(backgroundPic, 0, 0, WIDTH, HEIGHT, null);
            g.drawImage(raceCarPic, 0, 0, WIDTH, HEIGHT, null);


            for(int x = 0; x < fireArray.length; x++) {
                if (showWoodstock){
                    g.drawImage(firePic, fireArray[x].xpos, fireArray[x].ypos, fireArray[x].width, fireArray[x].height, null);
                }
            }

            for (int l = 0; l < fireArray.length; l++) {
                g.drawImage(firePic, fireArray[l].xpos, fireArray[l].ypos, fireArray[l].width, fireArray[l].height, null);
            }

            for(int x = 0; x < fireArray.length; x++) {
                if (raceCar.rec.intersects(fireArray[x].rec)) {
                    showWoodstock = false;
                }
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

