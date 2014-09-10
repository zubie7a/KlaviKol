public class SketchRotatingTriangle {

    private int channel, pitch, velocity;
    private Point3D pTriangle;
    private Window w;
    
    public SketchRotatingTriangle(Window window) {
        pTriangle = new Point3D(0, 100, 0);
        // This point will be the only point used to define a triangle
        this.w = window;
    }

    public void setNote(int channel, int pitch, int velocity) {
        // Everytime a key is pressed, this method is called
        this.velocity = velocity;
        this.channel = channel;
        this.pitch = pitch;
        // Because the key may have a different pitch than the previous
        // ..one, and the pitch defines the triangle size, we'll make a
        // ..new triangle, but with the same orientation as the previous
        // ..one, so it looks like the triangle grew in the same place
        float ang = (float) Math.atan2(pTriangle.y, pTriangle.x);
        // Get the current angle of the point that defines the triangle
        float rad = pitch * 4;
        // The radius is always 4 times the current pitch
        // At this point we have a Polar Coordinate. Angle and Radius!
        int newX = (int) (rad * Math.cos(ang));
        int newY = (int) (rad * Math.sin(ang));
        // Get the new X and Y converting from Polar to Cartesian
        pTriangle = new Point3D(newX, newY, 0);
        // This is the new point that will define the triangle
    }

    public void draw() {
        // This method is called every single frame by the sketch
        fadeBackground();
        drawTriangle();
    }

    public void drawTriangle() {
        w.colorMode(w.HSB, 12);
        // Use Hue-Saturation-Brightness instead of Red-Green-Blue
        // We'll paint colors according to the current note. Whats
        // ..cool about this is that the octave scale goes from C
        // ..to C in a circular fashion, just like the Hue goes from
        // ..Red to Red in a circular fashion, so having a color de-
        // ..pending on the pitch, will make colors go circularly 
        // ..just as the musical notes do!
        w.strokeWeight(3);
        // Change a bit the line thickness
        w.noFill();
        int vel = (int) (velocity * 6f / 100f) + 6;
        w.stroke(pitch % 12, vel, vel);
        // The hue will be the pitch % 12, so it goes from 0 to 12,
        // ..and 12 is the amount of notes in an octave. The Satur-
        // ..ation and Brightness will be defined by the 'velocity'
        // ..or how hard/fast was the key pushed.
        w.beginShape();
        for (int i = 0; i <= 3; i++) {
            pTriangle.rotate(120, 0, 0);
            // The point that defines the triangle is rotated 3 times
            doVertex(pTriangle.x, pTriangle.y, pTriangle.z);
            // And then each rotation defines a vertex. It has to end
            // ..where it began! be careful with this.
        }
        pTriangle.rotate(1, 0, 0);
        // But then its rotated a single degree, which will accumulate
        // ..across frames and make the triangle spin on itself!
        w.endShape();
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
        return 0 - z;
    }

    public void doVertex(float x, float y, float z) {
        // This does the vertexes of a shape, with mapped points
        w.vertex(mapX(x), mapY(y), mapZ(z));
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
