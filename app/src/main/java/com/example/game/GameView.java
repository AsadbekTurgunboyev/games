package com.example.game;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogRecord;


public class GameView extends View {
    Runnable runnable;
    Bitmap background;
    Handler handler;
    Display display;
    Point point;
    final  int UPDATE_MILLIS = 30;
    int dWidth, dHeight;
    Rect rect;
    Bitmap [] birds;
    Bitmap [] bird;
    Bitmap topTube, bottomTube;
    int birdFrame = 0;
    int velocity = 0, gravity = 3;
    boolean gameState = false;
    int birdX, birdY;
    int gap = 400;
    int minTubeOffSett, maxTubeOffSet;
    int numberOfTubs = 4;
    int distanceBetweenTubes;
    int tubeX;
    int topTubeY;
    Random random;


    public GameView(Context context) {
        super(context);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
      background = BitmapFactory.decodeResource(getResources(),R.drawable.bg);
      topTube = BitmapFactory.decodeResource(getResources(),R.drawable.toptube);
      bottomTube = BitmapFactory.decodeResource(getResources(),R.drawable.bottomtube);
      display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        dWidth = point.x;
        dHeight = point.y;
        rect = new Rect(0,0,dWidth,dHeight);
        birds = new Bitmap[2];
        birds[0] = BitmapFactory.decodeResource(getResources(),R.drawable.flybird);
        birds[1] = BitmapFactory.decodeResource(getResources(),R.drawable.flybird2);
        birds[0] = ThumbnailUtils.extractThumbnail(birds[0],200,144,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        birds[1] = ThumbnailUtils.extractThumbnail(birds[1],200,144,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        birdX = dWidth/2-birds[0].getWidth()/2;
        birdY = dHeight/2-birds[0].getHeight()/2;
        distanceBetweenTubes = dWidth *3 / 4;
        minTubeOffSett = gap / 2;
        random = new Random();
        maxTubeOffSet = dHeight - minTubeOffSett - gap;
        tubeX = dWidth / 2 - topTube.getWidth()/2;
        topTubeY = minTubeOffSett + random.nextInt(maxTubeOffSet - minTubeOffSett +1);



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background,null,rect,null);
        if(birdFrame == 0){
            birdFrame = 1;
        }else {
            birdFrame = 0;
        }
        if(gameState) {

            if (birdY < dHeight - birds[0].getHeight()) {
                velocity += gravity;
                birdY += velocity;

            }
            canvas.drawBitmap(topTube,tubeX,topTubeY - topTube.getHeight(),null);
            canvas.drawBitmap(bottomTube,tubeX,topTubeY+gap,null);
        }
        canvas.drawBitmap(birds[birdFrame],birdX,birdY,null);
        handler.postDelayed(runnable,UPDATE_MILLIS);
//        Executors.newSingleThreadScheduledExecutor().schedule(runnable, 200,TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN){
            velocity = -30 ;
            gameState = true;
            topTubeY = minTubeOffSett + random.nextInt(maxTubeOffSet - minTubeOffSett +1);

        }


        return true;
    }
}
