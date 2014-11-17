import java.util.ArrayList;

import themidibus.*;
import processing.core.*;
import processing.event.KeyEvent;

public class Window extends PApplet {

    private static final long serialVersionUID = 1L;
    // Sketches available to draw
    public SketchRotatingTriangle sketchRT;
    public SketchShootingStars sketchSS;
    public SketchKeyboard sketchKB;
    public SketchKeyboard _sketchKB;

    // The keyboard sketch will be constant on the screen, but the other
    // ..sketches will be able to be swapped to change the background

    // Midibus related variables
    public int channel, pitch, velocity;
    public MidiBus bus;

    // Sketch related variables
    public int sketchIndex; // Current sketch displayed as background
    public int frameCount; // Current frame of the program
    public boolean test;
    public int score;

    public void setup() {
        size(900, 720, P3D);
        bus = new MidiBus(this, 0, "Java Sound Synthesizer");
        // 0 -> The KEYBOARD that is the first connected device
        // ..it is possible to have multiple buses and identify
        // ..them according to their respective channel.
        // "Java Sound Synthesizer" -> to have sounds in the app
        // ..and could be changed to output to other devices or
        // ..perhaps programs.
        sketchRT = new SketchRotatingTriangle(this);
        sketchSS = new SketchShootingStars(this);
        sketchKB = new SketchKeyboard(this, 1);
        _sketchKB = new SketchKeyboard(this, 2);
        score = 0;
        test = false;
        PFont f = createFont("helvetica", 120, true);
        textFont(f, 120);
    }

    public void noteOn(int channel, int pitch, int velocity) {
        this.velocity = velocity;
        this.channel = channel;
        this.pitch = pitch;
        bus.sendNoteOn(channel, pitch, velocity); // Send a noteOn to output
        // In this case, the sound output will be the "Java Sound Synthesizer"
        switch (sketchIndex) {
        case 0:
            sketchRT.setNote(channel, pitch, velocity);
            break;
        case 1:
            sketchSS.setNote(channel, pitch, velocity);
        default:
            break;
        }
        // This is so in the future a button will switch between different
        // ..sketches for the background visualization.
        sketchKB.setNote(channel, pitch, velocity);
        // The Keyboard sketch will always be displayed and will always be
        // ..sent the currently pressed key so it reacts to it as usual.
    }

    public void controllerChange(int channel, int number, int value) {
        // This is a reaction to a button that will change between
        // .. different visualizations to display as background
        if (number == 1 && value == 127) {
            sketchIndex++;
            sketchIndex %= 2;
        }
        // In this case is the 'MOD' button in the nanoKEY controller,
        // ..which upon being pressed sends a 0 'value', upon being dropped
        // ..sends a 127 'value', and always send the number as 1. Be careful
        // ..as these numbers and values depends heavily on what MIDI device
        // ..you are using, since some may have more buttons than others, or
        // ..things like switch
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKey() == 'z') {
            test = true;
            return;
        }
        char x = e.getKey();
        if (x >= 'a' && x <= 'z') {
        	x = (char) ((x - 'a') + '0');
        	x -= 20;
            noteOn(1, x + 48, 100);
        }
        if (x >= '0' && x <= '9') {
            if (x == '0')
                x += 10;
            x++;
            noteOn(1, x + 48, 100);
        }
    }

    public void draw() {
        switch (sketchIndex) {
        case 0:
            sketchRT.draw();
            break;
        case 1:
            sketchSS.draw();
            break;
        default:
            break;
        }
        frameCount++;
        sketchKB.draw();
        _sketchKB.draw();
        if (test) {
            noteOn(0, frameCount % 25 + 48, 100);
            delay(50);
            if (frameCount % 25 + 48 == 71) {
                test = false;
            }
        }
        if (frameCount % 50 == 0) {
            _sketchKB.setNote(1, (int) (Math.random() * 10 + 48), 100);
        }
        check();
        stroke(255);
        fill(255);
        text(Integer.toString(score), 20, height / 2);
    }

    public void check() {
        for (int i = 0; i < sketchKB.pressedKeys.size(); ++i) {
            Boolean bool1 = sketchKB.pressedKeys.get(i);
            Boolean bool2 = _sketchKB.pressedKeys.get(i);
            if (bool1.equals(bool2) && bool1.equals(true)) {
                score += 2;
                _sketchKB.pressedKeys.set(i, false);
            }
        }
    }

    public void delay(float time) {
        // This is to make the program wait for a certain amount of ms
        int current = millis();
        while (millis() < current + time)
            Thread.yield();
    }

    public ArrayList<Boolean> getPressedKeys() {
        return sketchKB.getPressedKeys();
    }
}