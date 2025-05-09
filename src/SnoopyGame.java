import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;



    public class SnoopyGame implements Runnable, KeyListener, MouseListener {

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
                snoopy.up = true;
                snoopy.down = false;
            }
            //moving down
            if(e.getKeyCode() == 40){
                System.out.println("down");
                snoopy.down = true;
                snoopy.up = false;
            }
            //moving left
            if(e.getKeyCode() == 37){
                System.out.println("left");
                snoopy.left = true;
                snoopy.right = false;
            }
            //moving right
            if(e.getKeyCode() == 39){
                System.out.println("right");
                snoopy.right = true;
                snoopy.left = false;
            }
        }


        @Override
        public void keyReleased(KeyEvent e) {
            System.out.println("released?");
            System.out.println(e.getKeyChar());
            //moving up
            if(e.getKeyCode() == 38){
                snoopy.up = false;
            }
            //moving down
            if(e.getKeyCode() == 40){
                snoopy.down = false;
            }
            //moving left
            if(e.getKeyCode() == 37){
                snoopy.left = false;
            }
            //moving right
            if(e.getKeyCode() == 39){
                snoopy.right = false;
            }
        }

        final int WIDTH = 1000;
        final int HEIGHT = 700;

        public JFrame frame;
        public Canvas canvas;
        public JPanel panel;
        public BufferStrategy bufferStrategy;
        public BufferStrategy dxbufferStrategy;
        public Image woodstockPic;
        public Image backgroundPic;
        public Image snoopyPic;
        private item snoopy;
        public Image charliePic;

        item[] woodstockArray =new item[5];
        item[] charlieArray = new item [5];
        


        public static void main(String[] args) {
            SnoopyGame ex = new SnoopyGame();
            new Thread(ex).start();
            System.out.println("Welcome to the SnoopyGame! Use the arrow keys to control Snoopy. Don't get hit by Woodstock! Collect all the Charlie Browns!");

        }


        public SnoopyGame() {
            setUpGraphics();

            backgroundPic = Toolkit.getDefaultToolkit().getImage("field.png");
            snoopyPic = Toolkit.getDefaultToolkit().getImage("snoopy.png");
            woodstockPic = Toolkit.getDefaultToolkit().getImage("woodstock.png");
            charliePic = Toolkit.getDefaultToolkit().getImage("charlieBrown.png");
            
            snoopy = new item (450,500);




            for(int x = 0; x < woodstockArray.length; x++) {
                    woodstockArray[x] = new item((int) (Math.random()* 900), (int)(Math.random()* 600));
                    woodstockArray[x].dy = (int) (Math.random()* 5);
                    woodstockArray[x].dx = (int) (Math.random()* 5);

            }

            for(int x = 0; x < charlieArray.length; x++){
                charlieArray[x] = new item((int) (Math.random()* 900), (int)(Math.random()* 600));
                charlieArray[x].dx = 0;
                charlieArray[x].dy = 0;

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
            snoopy.bounce();

            for(int x = 0; x < woodstockArray.length; x++) {
                woodstockArray[x].wrap();
            }
            for(int y = 0; y < woodstockArray.length; y++){
               woodstockArray[y].wrap();
            }
            for(int x = 0; x < charlieArray.length; x++) {
                charlieArray[x].bounce();
            }
            for(int y = 0; y < charlieArray.length; y++){
                charlieArray[y].bounce();
            }


        }


        public void collisions(){
            for(int x = 0; x < woodstockArray.length; x++) {
                if (snoopy.rec.intersects(woodstockArray[x].rec) && snoopy.isAlive && woodstockArray[x].isAlive) {
//                    snoopy.dx = -snoopy.dx;
//                    snoopy.dy = -snoopy.dy;
                    snoopy.dx = snoopy.dx + 5;
                    snoopy.dy = snoopy.dy + 5;
                    woodstockArray[x].dx = 0;
                    woodstockArray[x].dy = 0;
                    woodstockArray[x].isAlive = false;


                }
            }

            for(int x = 0; x < charlieArray.length; x++) {
                if (snoopy.rec.intersects(charlieArray[x].rec) && snoopy.isAlive && charlieArray[x].isAlive) {
                    snoopy.dx = snoopy.dx + 5;
                    snoopy.dy = snoopy.dy + 5;
                    charlieArray[x].dx = 5;
                    charlieArray[x].dy = 5;
                    charlieArray[x].isAlive = false;


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
            g.drawImage(snoopyPic, snoopy.xpos, snoopy.ypos, snoopy.width, snoopy.height, null);



            for(int x = 0; x < woodstockArray.length; x++) {
                if (woodstockArray[x].isAlive){
                    g.drawImage(woodstockPic, woodstockArray[x].xpos, woodstockArray[x].ypos, woodstockArray[x].width, woodstockArray[x].height, null);
                }
            }
            for(int x = 0; x < charlieArray.length; x++) {
                if (charlieArray[x].isAlive){
                    g.drawImage(charliePic, charlieArray[x].xpos, charlieArray[x].ypos, charlieArray[x].width, charlieArray[x].height, null);
                }
            }

//           

            for(int x = 0; x < woodstockArray.length; x++) {
                if (snoopy.rec.intersects(woodstockArray[x].rec)) {
                }
            }
            for(int x = 0; x < charlieArray.length; x++) {
                if (snoopy.rec.intersects(charlieArray[x].rec)) {
                    charlieArray[x].bounce();
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

