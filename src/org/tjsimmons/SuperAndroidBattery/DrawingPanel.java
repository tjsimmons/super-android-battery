package org.tjsimmons.SuperAndroidBattery;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author impaler
 * @see http://obviam.net/index.php/using-bitmap-fonts-in-android/
 *
 */

public class DrawingPanel extends SurfaceView implements SurfaceHolder.Callback {
	private Canvas canvas;		// the canvas to draw on
	private Glyphs glyphs;		// the glyphs
	private static final String TAG = DrawingPanel.class.getSimpleName();

	public DrawingPanel(Context context) {
		super(context);
		// adding the panel to handle events
		getHolder().addCallback(this);

		// initialise resources
		loadResources();

		// making the Panel focusable so it can handle events
		setFocusable(true);
	}

	/** Loads the images of the glyphs */
	private void loadResources() {
		this.glyphs = new Glyphs(BitmapFactory.decodeResource(getResources(), R.drawable.numbers));
		Log.v(TAG, "Glyphs loaded");
	}
	
	public void drawPercentage(String percent) {
		try {
			canvas = getHolder().lockCanvas();
			synchronized (getHolder()) {
				// clear the screen
				canvas.drawColor(Color.BLACK);
				// draw glyphs
				glyphs.drawString(canvas, percent);
			}
		} finally {
			if (canvas != null) {
				getHolder().unlockCanvasAndPost(canvas);
			}
		}
		
	}

	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}
}