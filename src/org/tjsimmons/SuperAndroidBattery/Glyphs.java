package org.tjsimmons.SuperAndroidBattery;

import java.util.HashMap;
import java.util.Map;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

/**
 * @author impaler
 * @see http://obviam.net/index.php/using-bitmap-fonts-in-android/
 *
 */

public class Glyphs {
	private static final String TAG = Glyphs.class.getSimpleName();
	private Bitmap bitmap;	// bitmap containing the character map/sheet

	// Map to associate a bitmap to each character
	private Map<Character, Bitmap> glyphs = new HashMap<Character, Bitmap>(10);

	private int width;	// width in pixels of one character
	private int height;	// height in pixels of one character

	// numbers
	private char[] numbers = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	public Glyphs(Bitmap bitmap) {
		super();
		this.bitmap = bitmap;
		this.width = 16;
		this.height = 14;
		
		// Cutting up the glyphs
		for (int i = 0; i < 10; i++) {
			glyphs.put(numbers[i], Bitmap.createBitmap(bitmap, 0 + (i * width), 30, width, height));
		}
		
		Log.v(TAG, "Numbers initialised");
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	/**
	 * Draws the string onto the canvas at <code>x</code> and <code>y</code>
	 * @param text
	 */
	public void drawString(Canvas canvas, String text) {
		if (canvas == null) {
			Log.d(TAG, "Canvas is null");
		}
		for (int i = 0; i < text.length(); i++) {
			Character ch = text.charAt(i);
			if (glyphs.get(ch) != null) {
				canvas.drawBitmap(glyphs.get(ch), i * width, 0, null);
			}
		}
	}
}
