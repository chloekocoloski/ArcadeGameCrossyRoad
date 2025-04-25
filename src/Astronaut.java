import java.awt.*;

/**
 * Created by chales on 11/6/2017.
 */
public class Astronaut {


    public String name;
    public int xpos;
    public int ypos;
    public int dx;                    //the speed of the hero in the x direction
    public int dy;                    //the speed of the hero in the y direction
    public int width;
    public int height;
    public boolean isAlive;            //a boolean to denote if the hero is alive or dead.
    public Rectangle rec;
    public boolean isCrashing;
    public boolean up;
    public boolean down;
    public boolean right;
    public boolean left;



    // METHOD DEFINITION SECTION

    // Constructor Definition
    // A constructor builds the object when called and sets variable values.


    //This is a SECOND constructor that takes 3 parameters.  This allows us to specify the hero's name and position when we build it.
    // if you put in a String, an int and an int the program will use this constructor instead of the one above.
    public Astronaut(int pXpos, int pYpos) {


        xpos = pXpos;
        ypos = pYpos;
        dx = 3; //direction and speed, left and right
        dy = 3; //slope
        width = 75;
        height = 75;
        isAlive = true;
        rec = new Rectangle(xpos, ypos, width, height);
        isCrashing = false;
        up = false;
        down = false;
        right = false;
        left = false;


    } // constructor

    //The move method.  Everytime this is run (or "called") the hero's x position and y position change by dx and dy
    public void move() {
        xpos = xpos + dx;
        ypos = ypos + dy;

        rec = new Rectangle(xpos, ypos, width, height);


    }

    public void bounce() {
        //moving up
        if (up == true) {
            dy = -5;
        }
        //moving down
        if (down == true) {
            dy = 5;
        }
        //moving left
        if (left == true) {
            dx = -5;
        }
        //moving right
        if (right == true) {
            dx = 5;
        }
        if (up == false && down == false) {
            dy = 0;
        }

        if (left == false && right == false) {
            dx = 0;
        }
        //bounce off the east wall
        if (xpos > 950) {
            dx = -dx;
        }

        //bounce of the West Wall
        if (xpos < 0) {
            dx = -dx;
        }
        //bounce of the South Wall
        if (ypos > 650) {
            dy = -dy;

        }

        //bounce of the North Wall
        if (ypos < 0) {
            dy = -dy;
        }

        xpos = xpos + dx;
        ypos = ypos + dy;

        rec = new Rectangle(xpos, ypos, width, height);



    }

    public void wrap() {
        //hits the east wall

        if (xpos > 950) {
            xpos = 0;
        }

        //hits the west wall

        if (xpos < 0) {
            xpos = 950;
        }

        //hits the south wall
        if (ypos > 650) {
            ypos = 0;
        }

        //hits the north wall
        if (ypos < 0) {
            ypos = 650;
        }

        xpos = xpos + dx;
        ypos = ypos + dy;

        rec = new Rectangle(xpos, ypos, width, height);

    }

}










