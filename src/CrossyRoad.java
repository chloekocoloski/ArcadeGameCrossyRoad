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
        private item fire;


        item [] raceCarArray = new item[0];//raceCar
        item[] fireArray =new item[10];


        public static void main(String[] args) {
            CrossyRoad ex = new CrossyRoad();
            new Thread(ex).start();

        }

        public CrossyRoad() {
            setUpGraphics();

            backgroundPic = Toolkit.getDefaultToolkit().getImage("Background.png");
            Image raceCarPic = Toolkit.getDefaultToolkit().getImage("racecar.png");
            firePic = Toolkit.getDefaultToolkit().getImage("fire.png");
            Image newBackgroundPic = Toolkit.getDefaultToolkit().getImage(getClass().getResource("newBackground.webp"));

            raceCar = new item (120,600);



            for(int x = 0; x < raceCarArray.length; x++) {
                raceCarArray[x] = new item((int) (Math.random()* 900), (int)(Math.random()* 600));
            }

            for(int x = 0; x < fireArray.length; x++) {
                    fireArray[x] = new item((int) (Math.random()* 900), (int)(Math.random()* 600));
                    fireArray[x].dy = 0;
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
            raceCar.bounce();
            for(int x = 0; x < fireArray.length; x++) {
                fireArray[x].wrap();
            }


            for(int y = 0; y < raceCarArray.length; y++){
                raceCarArray[y].wrap();

            }

            for(int y = 0; y < fireArray.length; y++){
               fireArray[y].wrap();

            }

        }


        public void collisions(){
            for(int x = 0; x < fireArray.length; x++) {
                if (raceCar.rec.intersects(fireArray[x].rec) && raceCar.isCrashing == false && raceCar.isAlive && fire.isAlive) {
                    raceCar.dx = -raceCar.dx;
                    raceCar.dy = -raceCar.dy;
                    fireArray[x].dx = -raceCar.dx;
                    fireArray[x].dy = -fireArray[x].dy;
                    fireArray[x].isAlive = false;
                    raceCar.isCrashing = true;

                }
            }

            for(int x = 0; x < fireArray.length; x++) {
                if (!raceCar.rec.intersects(fireArray[x].rec)) {
                    raceCar.isCrashing = false;
                }
            }

            for(int b = 0; b < raceCarArray.length; b++){
                if(raceCar.rec.intersects(raceCarArray[b].rec)){
                }

                for(int c = 0; c < fireArray.length; c++) {
                    if (fire.rec.intersects(raceCarArray[c].rec)) {
                    }
                }
            }

            item[] fire = new item[1];

            for (int i = 0; i < fire.length; i++) {
                int startX = (int)(Math.random() * 900);     // random starting x position
                int startY = (int)(Math.random() * 650);     // random starting y position

                fire[i] = new item (startX, startY);

                // Make them move left or right
                fire[i].dy = (Math.random() > 9) ? 2 + i : (1 + i);
                fire[i].dx = (Math.random() > 0.5) ? 2 + i : (2 + i); // diff speeds & directions
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
        boolean showAstro = true;


        private void render() {
            Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
            g.clearRect(0, 0, WIDTH, HEIGHT);
            g.drawImage(backgroundPic, 0, 0, WIDTH, HEIGHT, null);

            // only draw fire if it should be shown

            if (showAstro) {
                g.drawImage(raceCarPic, raceCar.xpos, raceCar.ypos, raceCar.width, raceCar.height, null);
            }

            // always draw fire
          //  g.drawImage(firePic, fire.xpos, fire.ypos, fire.width, fire.height, null);

            // draw  rest of the item array
            for (int l = 0; l < raceCarArray.length; l++) {
                g.drawImage(raceCarPic, raceCarArray[l].xpos, raceCar.ypos, raceCar.width, raceCar.height, null);
            }

            for (int l = 0; l < fireArray.length; l++) {
                g.drawImage(firePic, fireArray[l].xpos, fireArray[l].ypos, fireArray[l].width, fireArray[l].height, null);
            }

            // collision: once collided, stop showing fire
//            if (raceCar.rec.intersects(fire.rec)) {
//                showAstro = false;
//                backgroundPic = Toolkit.getDefaultToolkit().getImage("newBackground.webp");
//
//            }
            raceCarPic = Toolkit.getDefaultToolkit().getImage("racecar.png");



//            for (int i = 0; i < astro2.length; i++) {
//                g.drawImage(astro2Pic, astro2[i].xpos, astro[i].ypos, astro2[i].width, astro2[i].height, null);
//            }



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

