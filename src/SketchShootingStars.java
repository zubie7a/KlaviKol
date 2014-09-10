import java.util.ArrayList;

public class SketchShootingStars {

    private int channel, pitch, velocity;
    private ArrayList<Point3D> special;
    private ArrayList<Point3D> stars;
    private Window w;

    public SketchShootingStars(Window window) {
        this.w = window;
        makeStars();
        makeSpecial();
        pitch = -1;
    }

    public Point3D makePoint() {
        double angle = w.random(360);
        double radius = Math.abs(w.random(400));
        int x = (int) (radius * Math.cos(angle));
        int y = (int) (radius * Math.sin(angle));
        return new Point3D(x, y, 0.1f);
    }

    public void makeStars() {
        stars = new ArrayList<Point3D>();
        for (int i = 0; i < 500; i++) {
            stars.add(makePoint());
        }
    }

    public void makeSpecial() {
        special = new ArrayList<Point3D>();
        for (int i = 0; i < 25; ++i) {
            Point3D p = new Point3D(0, 15 * (i + 1), 0);
            special.add(p);
        }
    }

    public void setNote(int channel, int pitch, int velocity) {
        if (this.pitch != -1) {
            Point3D prev = special.get(this.pitch);
            double angle = Math.atan2(prev.y, prev.x);
            double radius = 15 * (((pitch + 2) % 25) + 1);
            float x = (float) (radius * Math.cos(angle));
            float y = (float) (radius * Math.sin(angle));
            Point3D p = new Point3D(x, y, 0);
            special.set((pitch + 2) % 25, p);
        }
        this.channel = channel;
        this.pitch = ((pitch + 2) % 25);
        this.velocity = velocity;
    }

    public void draw() {
        fadeBackground();
        w.colorMode(w.RGB, 255);
        w.stroke(255);
        w.strokeWeight(1);
        for (int i = 0; i < stars.size(); ++i) {
            Point3D p1 = stars.get(i);
            Point3D p2 = p1.clone();
            int d = (int) (w.random(10) * (w.random(2) + 1));
            p2.translate(0, 0, d);
            if (p1.w != -1) {
                doLine(p1, p2);
            }
            if (w.abs(p1.z) > 600) {
                stars.set(i, makePoint());
            } else {
                stars.set(i, p2.clone());
            }
        }
        w.colorMode(w.HSB, 12);
        // Use Hue-Saturation-Brightness instead of Red-Green-Blue
        // We'll paint colors according to the current note. Whats
        // ..cool about this is that the octave scale goes from C
        // ..to C in a circular fashion, just like the Hue goes from
        // ..Red to Red in a circular fashion, so having a color de-
        // ..pending on the pitch, will make colors go circularly
        // ..just as the musical notes do!
        w.strokeWeight(15);
        // Change a bit the line thickness
        w.noFill();
        int vel = (int) (velocity * 6f / 100f) + 6;
        ArrayList<Boolean> pressedKeys = w.getPressedKeys();
        for (int i = 0; i < special.size(); ++i) {
            if (pressedKeys.get(i % 25) == true) {
                w.stroke(i % 12, vel, vel);
                Point3D p1 = special.get(i);
                Point3D p2 = p1.clone();
                p2.rotate(1, 0, 0);
                doLine(p1, p2);
                special.set(i, p2.clone());
            }
        }
    }

    public void doLine(Point3D p1, Point3D p2) {
        // This does a 3D line, with mapped points
        float x1 = p1.x;
        float y1 = p1.y;
        float z1 = p1.z;
        float x2 = p2.x;
        float y2 = p2.y;
        float z2 = p2.z;
        w.line(mapX(x1), mapY(y1), mapZ(z1), mapX(x2), mapY(y2), mapZ(z2));
    }

    // These map functions will take some coordinates
    // ..to the center of the sketch.
    public float mapX(float x) {
        // X is simply shifted to the right
        return w.width / 2 + x;
    }

    public float mapY(float y) {
        // Y is shifted down and signs change (because
        // ..usually Y increases downwards)
        return w.height / 2 - y;
    }

    public float mapZ(float z) {
        // Z is simply changed into the opposite system
        // if its right handed, to the left, and viceversa
        return z;
    }

    public void fadeBackground() {
        // This draws a black background with just a bit of alpha
        // ..so its like the image is fading away into black.
        w.colorMode(w.RGB, 255);
        w.noStroke();
        w.fill(0, 0, 0, 8);
        w.rect(0, 0, w.width, w.height);
    }
}
