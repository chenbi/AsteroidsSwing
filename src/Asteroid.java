import java.util.Random;

public class Asteroid extends SpaceObject {

    private final int INITIAL_X = 400;
    private final int INITIAL_Y = 400;
    int dx, dy;

    public Asteroid(int x, int y, int dx, int dy) {
        super(x, y);
        this.dx=dx;
        this.dy=dy;
        initAlien();
    }

    private void initAlien() {

        loadImage("alien.png");
        getImageDimensions();
    }

    public void move() {

        if (x < 0) {
            x = INITIAL_X;
        }
        if (x > INITIAL_X) {
            x = 0;
        }
        if (y < 0) {
            y = INITIAL_Y;
        }
        if (y > INITIAL_Y) {
            y = 0;
        }
        x = x + dx;
        y = y + dy;
    }
}