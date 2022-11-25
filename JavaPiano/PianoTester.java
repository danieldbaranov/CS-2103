import static org.junit.jupiter.api.Assertions.*;
import javax.swing.*;
import javax.sound.midi.*;
import java.awt.event.*;
import org.junit.jupiter.api.*;

/**
 * Contains a set of unit tests for the Piano class.
 */
class PianoTester {
	private TestReceiver _receiver;
	private Piano _piano;
	private PianoMouseListener _mouseListener;

	private MouseEvent makeMouseEvent (int x, int y) {
		return new MouseEvent(_piano, 0, 0, 0, x, y, 0, false);
	}

	/**
	 * Sets up the piano before each test begins
	 */
	@BeforeEach
	void setup () {
		// A new TestReceiver will be created before running *each*
		// test. Hence, the "turn on" and "turn off" counts will be
		// reset to 0 before *each* test.
		_receiver = new TestReceiver();
		_piano = new Piano(_receiver);
		_mouseListener = _piano.getMouseListener();
	}

	/**
	 * Tests that when clicking the upper-leftmost pixel the first pitch plays
	 */
	@Test
	void testClickUpperLeftMostPixel () {
		// Pressing the mouse should cause the key to turn on.
		_mouseListener.mousePressed(makeMouseEvent(0, 0));
		assertTrue(_receiver.isKeyOn(Piano.START_PITCH));
	}

	/**
	 * Tests drag within key to make sure that the sound is only played once
	 */
	@Test
	void testDragWithinKey () {
		// Test that pressing and dragging the mouse *within* the same key
		// should cause the key to be turned on only once, not multiple times.
		// Use makeMouseEvent and TestReceiver.getKeyOnCount.
		_mouseListener.mouseDragged(makeMouseEvent(1, 1));
		_mouseListener.mouseDragged(makeMouseEvent(1, 10));
		assertTrue(_receiver.getKeyOnCount(48) == 1);
	}

	/**
	 * Tests to see if the rightmost edge of the first key will play the next white key instead.
	 */
	@Test
	void testEdgesOfKey(){
		_mouseListener.mousePressed(makeMouseEvent(_piano.WHITE_KEY_WIDTH, _piano.BLACK_KEY_HEIGHT + 1));
		assertTrue(_receiver.isKeyOn(Piano.START_PITCH + 2));
	}

	/**
	 * Tests dragging between the first and second white key to
	 * make sure they are only played once each
	 */
	@Test
	void testDragBetweenKeys(){
		_mouseListener.mouseDragged(makeMouseEvent(Piano.WHITE_KEY_WIDTH - 1, Piano.WHITE_KEY_HEIGHT - 1));
		_mouseListener.mouseDragged(makeMouseEvent(Piano.WHITE_KEY_WIDTH + 1, Piano.WHITE_KEY_HEIGHT - 1));
		assertTrue(_receiver.getKeyOnCount(Piano.START_PITCH) == 1);
		assertTrue(_receiver.getKeyOnCount(Piano.START_PITCH + 2) == 1);
	}

	/**
	 * Tests the pitch of all the keys to see if they are in the right order.
	 * Does this by dragging along the entire top of the piano
	 */
	@Test
	void testPitchForAllKeys(){
		for(int i = 0; i < Piano.WHITE_KEY_WIDTH * 36; i++){
			_mouseListener.mouseDragged(makeMouseEvent(i, 1));
		}
		for(int i = 0; i < 36; i++){
			assertTrue(_receiver.getKeyOnCount(Piano.START_PITCH + i) == 1);
		}
	}

}
