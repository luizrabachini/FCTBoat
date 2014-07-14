/*
 * FCTBoat Android App
 */
package com.fctboat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Create view to capture user commands
 * 
 * @author luiz
 */
@SuppressLint({ "DefaultLocale", "ViewConstructor" })
class BoatNavView extends SurfaceView implements SurfaceHolder.Callback {
	public static final String COMMAND_FORMAT = "%.0f;%.0f&";
	
	private int REF_CIRC_RADIUS = 50; // Circumference radius of movement referential
	private int CEN_CIRC_RADIUS = 30; // Central circumference radius
	
	private ServerCom serverCom;
	
	private SurfaceHolder surfaceHolder;
	
	private Paint paintCen,
					paintRef,
					paintLineCart,
					paintLineGrid,
					paintLineRef,
					paintMovIntensity;
	
	private Canvas canvas;
	
	private float centerX,
					centerY,
					width,
				 	height;
	
	private float lastX,
				 	lastY;

	/**
	 * Constructor
	 * 
	 * @param context Context of activity
	 * @param serverCom Server communication
	 */
	public BoatNavView(Context context, ServerCom serverCom) {
		super(context);
		
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		
		this.serverCom = serverCom;
		
		paintCen = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintRef = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintLineCart = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintLineGrid = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintLineRef = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintMovIntensity = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		paintCen.setColor(Color.CYAN);
		paintCen.setStyle(Style.FILL);
		
		paintRef.setColor(Color.MAGENTA);
		paintRef.setStyle(Style.FILL);
		
		paintLineCart.setColor(Color.WHITE);
		paintLineCart.setStyle(Style.FILL);
		paintLineCart.setStrokeWidth(2);
		
		paintLineGrid.setColor(Color.GRAY);
		paintLineGrid.setStrokeWidth(1);
		
		paintLineRef.setColor(Color.CYAN);
		paintLineRef.setStyle(Style.FILL);
		paintLineRef.setStrokeWidth(2);
		
		paintMovIntensity.setColor(Color.GREEN);
		paintMovIntensity.setStyle(Style.FILL);
		paintMovIntensity.setStrokeWidth(30);
		paintMovIntensity.setAlpha(200);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean ret = false;
		
		if (surfaceHolder.getSurface().isValid()) {
			canvas = surfaceHolder.lockCanvas();
			clearScreen();
			
			lastX = event.getX();
			lastY = event.getY();
			
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					movePoint(lastX, lastY);
					ret = true;
				case MotionEvent.ACTION_MOVE:
					movePoint(lastX, lastY);
					ret = true;
				case MotionEvent.ACTION_UP:
					movePoint(lastX, lastY);
					endPoint();
			}
			
			surfaceHolder.unlockCanvasAndPost(canvas);
		}
		
		return ret;
	}
	
	/**
	 * Clear screen and redraw guidelines
	 */
	private void clearScreen() {
		canvas.drawColor(Color.BLACK);
		
		// Draw Cartesian lines
		canvas.drawLine(centerX, 0, centerX, height, paintLineCart);
		canvas.drawLine(0, centerY, width, centerY, paintLineCart);
		
		// Draw support lines 1 (vertical)
		canvas.drawLine(centerX / 2, 0, centerX / 2, height, paintLineGrid);
		canvas.drawLine(centerX + centerX / 2, 0, centerX + centerX / 2, height, paintLineGrid);
		
		// Draw support lines 2 (horizontal)
		canvas.drawLine(0, centerY / 2, width, centerY / 2, paintLineGrid);
		canvas.drawLine(0, centerY + centerY / 2, width, centerY + centerY / 2, paintLineGrid);
	}
	
	/**
	 * Central point of the interface
	 */
	private void endPoint() {
		canvas.drawCircle(centerX, centerY, CEN_CIRC_RADIUS, paintRef);
	}
	
	/**
	 * Movement of reference point
	 * 
	 * @param x Px of point
	 * @param y Py of point
	 */
	private void movePoint(float x, float y) {
		float difX = (100 * (lastX - centerX)) / (width / 2);
		float difY = (100 * (lastY - centerY)) / (height / 2);
		
		// Draw intensity lines
		canvas.drawLine(centerX, centerY, centerX, lastY, paintMovIntensity);
		canvas.drawLine(centerX, centerY, lastX, centerY, paintMovIntensity);
		
		canvas.drawCircle(x, y, REF_CIRC_RADIUS, paintCen);
		canvas.drawLine(centerX, centerY, lastX, lastY, paintLineRef);
		
		String command = String.format(COMMAND_FORMAT, difX, difY);
		serverCom.sendCommand(command);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		canvas = holder.lockCanvas();
		clearScreen();
		
		width = canvas.getWidth();
		height = canvas.getHeight();
		
		centerX = width / 2;
		centerY = height / 2;
		
		clearScreen();
		endPoint();
		
		surfaceHolder.unlockCanvasAndPost(canvas);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
}
