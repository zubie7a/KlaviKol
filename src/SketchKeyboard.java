import java.util.ArrayList;

public class SketchKeyboard {

    public ArrayList<Key> downKeys;
    public ArrayList<Key> upKeys;
    public ArrayList<Boolean> pressedKeys;
    public int pressedKeysNum;
    private Window w;
    private int type;
    private int height;
    private int keyNum;
    private int keyWidth;
    private int keyHeight;
    
    public SketchKeyboard(Window window, int type){
        this.w = window;
        this.type = type;
        this.keyNum = 15;
        this.keyWidth = w.width / keyNum;
        this.keyHeight = keyWidth * 2;
        if(type == 1){
            height = w.height;
        }
        else{
            height = keyHeight;
        }
        createNotes();
    }
    
    public void createNotes(){
        // Each 'Key' list will have 29 keys.
        // The 'up' list will have the keys of the top row marked
        // ..correctly as "UP", and the rest marked as "NONE".
        // The 'down' list will have the keys of the bottom row marked
        // ..correctly as "DOWN", and the rest marked as "NONE".
        
        // The nanoKEY keyboard has 25 keys, but we're assuming that the
        // ..4 spaces in the top row without keys really have keys, but
        // ..always considered as invalid.
        
        //   01  03  !!  07  09  11  !!  15  17  !!  21  23  25  !!
        // 00  02  04  06  08  10  12  14  16  18  20  22  24  26  28
        
        // This brings a total of 29 keys, with 4 being invalid by default
        // ..and marked as such with the !!. 
        
        // Then, for the bottom row list of keys, all the upper row keys are 
        // ..going to be skipped and marked as such with **.
        
        //   **  **  **  **  **  **  **  **  **  **  **  **  **  **
        // 00  02  04  06  08  10  12  14  16  18  20  22  24  26  28

        // Then, for the top row list of keys, all the bottom row keys are 
        // ..going to be skipped and marked as such with **.
        // The !! is special because nothing is drawn, but the space is left
        // ..in there as if it was an invisible key.
        
        //   01  03  !!  07  09  11  !!  15  17  !!  21  23  25  !!
        // **  **  **  **  **  **  **  **  **  **  **  **  **  **  **

        // Upper row and bottom row keys can't be drawn in the same loop because
        // ..then the key to the right of another one, will always be on top of it
        // ..and its problematic because the upper row will always have to be on top
        // ..so the fix is drawing first the bottom row only, and then the upper row
        // ..ensuring that the upper will always be drawn last and therefore on top.
        
        upKeys = new ArrayList<Key>();
        downKeys = new ArrayList<Key>();
        for(int i = 0; i < 29; ++i){
            Key k = makeKey("UP", i);
            // This will return a bunch of keys marked either as UP or none.
            upKeys.add(k);
        }
        for(int i = 0; i < 29; ++i){
            Key k = makeKey("DOWN", i);
            // This will return a bunch of keys marked either as DOWN or none.
            downKeys.add(k);
        }
        pressedKeysNum = 0;
        pressedKeys = new ArrayList<Boolean>();
        for(int i = 0; i < 25; ++i){
            pressedKeys.add(false);
        }
    }
    
    public void draw(){
        // This method is called every single frame by the sketch.
        drawKeys();
        dropPressedNote();
    }
    
    public void drawKeys(){
        w.rectMode(w.CORNERS);
        // This is so a rectangle is drawn with the coordinates of the 
        // ..left-upper-most corner and the right-bottom-most corner.
        // If this is not defined, it will draw using the coordinates
        // ..of the left-upper-most corner, and then the length of each
        // ..pair of sides, which could be problematic if one is unaware.
        w.noStroke();
        // Number of keys in the bottom row        
        // Drawing the lower row of keys
        for(int i = 0, iKey = 0; i < downKeys.size(); ++i){
            // iKey is the current key position, for drawing purposes
            // since maybe some invalid keys may be found along the way
            // and those would not add to the amount of found valid keys.
            Key k = downKeys.get(i);
            if(k.type.equals("NONE")){
                continue; 
            }
            int x1 = iKey * keyWidth;
            int x2 = (iKey + 1) * keyWidth;
            int y1 = height - keyHeight;
            int y2 = height;
            w.colorMode(w.RGB, 255);
            w.fill(64, 64, 64);
            if(k.time > 0){
                w.colorMode(w.HSB, 12);
                w.fill(k.key % 12, 12, 12);
            }
            w.rect(x1 + 2, y1, x2 - 2, y2);
            iKey++;
        }
        
        // Drawing the upper row of keys
        for(int i = 0, iKey = 0; i < upKeys.size(); ++i){
            // iKey is the current key position, for drawing purposes
            // since maybe some invalid keys may be found along the way
            // and those would not add to the amount of found valid keys.
            Key k = upKeys.get(i);
            if(k.type.equals("NONE")){
                if(k.key == -2){
                    // -2 = invalid key, but its one of the invisible ones
                    iKey++;
                }
                continue; 
            }
            int x1 = iKey * keyWidth;
            int x2 = (iKey + 1) * keyWidth;
            int y1 = height - keyHeight;
            int y2 = height;
            if(type == 2){
                y1 += keyHeight;
                y2 += keyHeight;
            }
            // Shift it a bit to the right
            x1 += keyWidth / 2;
            x2 += keyWidth / 2;
            // And then a bit upwards
            y1 -= keyHeight / 2;
            y2 -= keyHeight / 2;
            w.colorMode(w.RGB, 255);
            w.fill(128, 128, 128);
            if(k.time > 0){
                w.colorMode(w.HSB, 12);
                w.fill(k.key % 12, 12, 12);
            }
            w.rect(x1 + 2, y1, x2 - 2, y2);
            // The small changes in X are so the keys have a 
            // ..small horizontal space between them.
            iKey++;
        }
    }
    
    public Key makeKey(String type, int key){
        // There are 25 keys in the MIDI keyboard, but there
        // ..could be 29 counting the places of the upper row
        // ..where there are no keys. For programming easiness,
        // ..we assume there are keys there, but of type "NONE"
        // ..so when they are going to be drawn, they are skipped.
        switch(key){
        case  5: return new Key("NONE", -2);
        case 13: return new Key("NONE", -2);
        case 19: return new Key("NONE", -2);
        case 27: return new Key("NONE", -2);
        default: break;
        }
        if(type.equals("UP")){
            switch(key){
            case  1: return new Key("UP",  1);
            case  3: return new Key("UP",  3);
            case  7: return new Key("UP",  6);
            case  9: return new Key("UP",  8);
            case 11: return new Key("UP", 10);
            case 15: return new Key("UP", 13);
            case 17: return new Key("UP", 15);
            case 21: return new Key("UP", 18);
            case 23: return new Key("UP", 20);
            case 25: return new Key("UP", 22);
            default: break;
            }
            return new Key("NONE", -1);
        }
        else{
            if(type.equals("DOWN")){
                switch(key){
                case  0: return new Key("DOWN",  0);
                case  2: return new Key("DOWN",  2);
                case  4: return new Key("DOWN",  4);
                case  6: return new Key("DOWN",  5);
                case  8: return new Key("DOWN",  7);
                case 10: return new Key("DOWN",  9);
                case 12: return new Key("DOWN", 11);
                case 14: return new Key("DOWN", 12);
                case 16: return new Key("DOWN", 14);
                case 18: return new Key("DOWN", 16);
                case 20: return new Key("DOWN", 17);
                case 22: return new Key("DOWN", 19);
                case 24: return new Key("DOWN", 21);
                case 26: return new Key("DOWN", 23);
                case 28: return new Key("DOWN", 24);
                default: break;
                }
            }
            return new Key("NONE", -1);
        }
    }
    
    public int mapKey(int key){
        // Convert from the usual 0-24 index on the keyboard to 0-28
        // ..assuming there are four invisible keys on the upper row, 
        // ..which helps for when the application will draw that row.
        switch(key){
        case  0: return  0;
        case  1: return  1;
        case  2: return  2;
        case  3: return  3;
        case  4: return  4;
        case  5: return  6;
        case  6: return  7;
        case  7: return  8;
        case  8: return  9;
        case  9: return 10;
        case 10: return 11;
        case 11: return 12;
        case 12: return 14;
        case 13: return 15;
        case 14: return 16;
        case 15: return 17;
        case 16: return 18;
        case 17: return 20;
        case 18: return 21;
        case 19: return 22;
        case 20: return 23;
        case 21: return 24;
        case 22: return 25;
        case 23: return 26;
        case 24: return 28;
        default: break;
        }
        return -1;
    }
    
    public void setNote(int channel, int pitch, int velocity){
        // Everytime a key is pressed, this method is called
        // ..This sets the currently pressed key as being pressed
        // ..so that its drawn with a different color
        int key = mapKey((pitch + 2) % 25);
        Key k1 = makeKey("DOWN", key);
        Key k2 = makeKey("UP", key);
        if(k1.type.equals("DOWN")){
            if(downKeys.get(key).time == 0){
                pressedKeysNum++;
            }
            downKeys.get(key).time = 50; // This key was freshly pressed
            pressedKeys.set(k1.key, true);
        }
        if(k2.type.equals("UP")){
            if(upKeys.get(key).time == 0){
                pressedKeysNum++;
            }
            upKeys.get(key).time = 50; // This key was freshly pressed
            pressedKeys.set(k2.key, true);
        }
    }
    
    public void dropPressedNote(){
        // Each frame, the pressed time for all keys will decrease.
        // The program only detects one key press at a time, two keys
        // ..being pressed aren't detected simultaneously but one after
        // ..the other. This way, two keys will appear to have been pre-
        // ..sed at once, with a 50 frame 'pressed' range.
        for(int i = 0; i < upKeys.size(); ++i){
            Key k = upKeys.get(i);
            if(k.time > 0){
                // If the time was greater than zero and...
                k.time--;
                if(k.time == 0){
                     // ..then it dropped to zero...
                     // This key's pressed time has expired
                    pressedKeysNum--;
                    pressedKeys.set(k.key, false);
                    if(type == 2){
                        w.score--;
                    }
                }
            }
        }
        for(int i = 0; i < downKeys.size(); ++i){
            Key k = downKeys.get(i);
            if(k.time > 0){
                // If the time was greater than zero and...
                k.time--;
                if(k.time == 0){
                     // ..then it dropped to zero...
                     // This key's pressed time has expired
                    pressedKeysNum--;
                    pressedKeys.set(k.key, false);
                    if(type == 2){
                        w.score--;
                    }
                }
            }
        }
    }
    
    private class Key{
        String type;  // If its a key from the "UP" row or the "DOWN" row
        int key;      // The value of the key
        int time;     // The remainding time it has.
        // When a key its pressed, time becomes 50. Then each frame, this
        // ..number will decrease by 1. While its different to 0, this key
        // ..will be drawn with a special color. When time drops to 0, the
        // ..color will revert back to normal.
        private Key(String type, int key){
            this.type = type;
            this.key = key;
        }
    }
    
    public ArrayList<Boolean> getPressedKeys(){
        return pressedKeys;
    }
}
