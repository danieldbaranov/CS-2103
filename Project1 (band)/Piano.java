import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.sound.midi.*;

/**
 * Implements a simulated piano with 36 keys.
 */
public class Piano extends JPanel {
	// DO NOT MODIFY THESE CONSTANTS
	public static int START_PITCH = 48;
	public static int WHITE_KEY_WIDTH = 40;
	public static int BLACK_KEY_WIDTH = WHITE_KEY_WIDTH/2;
	public static int WHITE_KEY_HEIGHT = 200;
	public static int BLACK_KEY_HEIGHT = WHITE_KEY_HEIGHT/2;
	public static int NUM_WHITE_KEYS_PER_OCTAVE = 7;
	public static int NUM_OCTAVES = 3;
	public static int NUM_WHITE_KEYS = NUM_WHITE_KEYS_PER_OCTAVE * NUM_OCTAVES;
	public static int WIDTH = NUM_WHITE_KEYS * WHITE_KEY_WIDTH;
	public static int HEIGHT = WHITE_KEY_HEIGHT;
		
	private java.util.List<Key> _keys = new ArrayList<>();
	private Receiver _receiver;
	private PianoMouseListener _mouseListener;

	// For making polygons for the white keys
	private enum keyState {
		START,
		MIDDLE,
		END
	}


	/**
	 * Returns the list of keys in the piano.
	 * @return the list of keys.
	 */
	public java.util.List<Key> getKeys () {
		return _keys;
	}

	/**
	 * Sets the MIDI receiver of the piano to the specified value.
	 * @param receiver the MIDI receiver 
	 */
	public void setReceiver (Receiver receiver) {
		_receiver = receiver;
	}

	/**
	 * Returns the current MIDI receiver of the piano.
	 * @return the current MIDI receiver 
	 */
	public Receiver getReceiver () {
		return _receiver;
	}

	// DO NOT MODIFY THIS METHOD.
	/**
	 * @param receiver the MIDI receiver to use in the piano.
	 */
	public Piano (Receiver receiver) {
		// Some Swing setup stuff; don't worry too much about it.
		setFocusable(true);
		setLayout(null);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));

		setReceiver(receiver);
		_mouseListener = new PianoMouseListener(_keys);
		addMouseListener(_mouseListener);
		addMouseMotionListener(_mouseListener);
		makeKeys();
	}

	/**
	 * Returns the PianoMouseListener associated with the piano.
	 * @return the PianoMouseListener associated with the piano.
	 */
	public PianoMouseListener getMouseListener () {
		return _mouseListener;
	}

	/**
	 * Instantiate all the Key objects with their correct polygons and pitches, and
	 * add them to the _keys array.
	 */
	private void makeKeys () {
		int pitch = START_PITCH;
		int keyStartPoint = 0;

		for(int octave = 0; octave < NUM_OCTAVES; octave++){
			pitch = makeKeySet(keyStartPoint, 5, pitch) + 1;
			keyStartPoint += WHITE_KEY_WIDTH * 3;

			pitch = makeKeySet(keyStartPoint, 7, pitch) + 1;
			keyStartPoint += WHITE_KEY_WIDTH * 4;

		}
	}

	/**
	 * Makes a set of keys with interchanging black keys in the middle.
	 * You need to call this twice, once with 7 keys and one with 5 to get an octave.
	 * !!! num must be an odd number and must be at least 3
	 */
	private int makeKeySet (int baseX, int num, int pitch) {

		java.util.List<Key> whiteKeys = new ArrayList<>();
		java.util.List<Key> blackKeys = new ArrayList<>();

		whiteKeys.add(new Key(makeWhiteKey(keyState.START, baseX), pitch, this, Color.WHITE)); // specify the starting white key
		baseX += WHITE_KEY_WIDTH;
		pitch ++;
		for(int i = 1; i < num-1; i++){ // for the middle keys
			if(i % 2 == 1){ // Black key
				blackKeys.add(new Key(makeBlackKey(baseX), pitch, this, Color.BLACK));
			} else { // white key
				whiteKeys.add(new Key(makeWhiteKey(keyState.MIDDLE, baseX), pitch, this, Color.WHITE));
				baseX += WHITE_KEY_WIDTH;
			}
			pitch ++;
		}
		whiteKeys.add(new Key(makeWhiteKey(keyState.END, baseX), pitch, this, Color.WHITE)); // specify the ending white key

		addKeys(whiteKeys, blackKeys);
		return pitch; // returns pitch so that makeKeys can work off that for the next call to this function
	}

	/**
	 * Adds up two arrays of white and black keys to _keys
	 * First white then black keys so that black is rendered on top to avoid outline
	 */
	private void addKeys(java.util.List<Key> whiteKeys, java.util.List<Key> blackKeys) {
		for (Key key : whiteKeys) {
			_keys.add(key);
		}
		for (Key key : blackKeys) {
			_keys.add(key);
		}
	}

	/**
	 * Creates a polygon of a black key depending on the initial x value of the upper left most vertex
	 */
	private Polygon makeBlackKey (int baseX){
		int[] xCoords = new int[] {
				baseX - BLACK_KEY_WIDTH/2,
				baseX + BLACK_KEY_WIDTH/2,
				baseX + BLACK_KEY_WIDTH/2,
				baseX - BLACK_KEY_WIDTH/2
		};
		int[] yCoords = new int[] {
				0,
				0,
				BLACK_KEY_HEIGHT,
				BLACK_KEY_HEIGHT
		};
		return new Polygon(xCoords, yCoords, 4);
	}

	/**
	 * Creates a Polygon of a white key.
	 * Needs the white key's keyState of start, middle, or end to create proper shape.
	 * Also needs the x value of the leftmost part of the key.
	 */
	private Polygon makeWhiteKey (keyState ks, int baseX){
		int[] xCoords;
		int[] yCoords;
		if(ks == keyState.START) { // starting key in keySet
			xCoords = new int[] {
					baseX,
					baseX + WHITE_KEY_WIDTH - BLACK_KEY_WIDTH/2,
					baseX + WHITE_KEY_WIDTH - BLACK_KEY_WIDTH/2,
					baseX + WHITE_KEY_WIDTH,
					baseX + WHITE_KEY_WIDTH,
					baseX
			};
			yCoords = new int[] {
					0,
					0,
					BLACK_KEY_HEIGHT,
					BLACK_KEY_HEIGHT,
					WHITE_KEY_HEIGHT,
					WHITE_KEY_HEIGHT,
			};
		} else if (ks == keyState.END) { // ending key in keySet
			xCoords = new int[]{
					baseX,
					baseX + BLACK_KEY_WIDTH / 2,
					baseX + BLACK_KEY_WIDTH / 2,
					baseX + WHITE_KEY_WIDTH,
					baseX + WHITE_KEY_WIDTH,
					baseX
			};
			yCoords = new int[]{
					BLACK_KEY_HEIGHT,
					BLACK_KEY_HEIGHT,
					0,
					0,
					WHITE_KEY_HEIGHT,
					WHITE_KEY_HEIGHT,
			};
		} else {
			xCoords = new int[] { // middle white keys in keySet
					baseX,
					baseX + BLACK_KEY_WIDTH/2,
					baseX + BLACK_KEY_WIDTH/2,
					baseX + WHITE_KEY_WIDTH - BLACK_KEY_WIDTH/2,
					baseX + WHITE_KEY_WIDTH - BLACK_KEY_WIDTH/2,
					baseX + WHITE_KEY_WIDTH,
					baseX + WHITE_KEY_WIDTH,
					baseX
			};
			yCoords = new int[] {
					BLACK_KEY_HEIGHT,
					BLACK_KEY_HEIGHT,
					0,
					0,
					BLACK_KEY_HEIGHT,
					BLACK_KEY_HEIGHT,
					WHITE_KEY_HEIGHT,
					WHITE_KEY_HEIGHT,
			};
		}
		return new Polygon(xCoords, yCoords, xCoords.length);
	}


	// DO NOT MODIFY THIS METHOD.
	@Override
	/**
	 * Paints the piano and all its constituent keys.
	 * @param g the Graphics object to use for painting.
	 */
	public void paint (Graphics g) {
		// Delegates to all the individual keys to draw themselves.
		for (Key key: _keys) {
			key.paint(g);
		}
	}
}
