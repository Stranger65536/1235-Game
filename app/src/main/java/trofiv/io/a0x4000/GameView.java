package trofiv.io.a0x4000;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import trofiv.io.a0x4000.R.mipmap;

import static trofiv.io.a0x4000.Logger.LoggerDepth;
import static trofiv.io.a0x4000.Logger.getLogger;

public final class GameView extends SurfaceView implements Callback {
    private static final Logger LOGGER = getLogger();
    private final Sprite rectangleSprite;
    private Thread gameThread;

    public GameView(final Context context) {
        super(context);
        //noinspection ThisEscapedInObjectConstruction
        getHolder().addCallback(this);
        final Theme theme = context.getTheme();
        final Drawable spriteDrawable = getResources()
                .getDrawable(mipmap.ic_launcher_round, theme);
        rectangleSprite = new Sprite(drawableToBitmap(spriteDrawable));
        setFocusable(true);
    }


    public static Bitmap drawableToBitmap(final Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        final Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    public void surfaceCreated(final SurfaceHolder holder) {
        final Runnable gameRunnable = new GameRunnable(getHolder(), this);
        this.gameThread = new Thread(gameRunnable);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(
            final SurfaceHolder holder,
            final int format,
            final int width,
            final int height) {
    }

    @Override
    public void surfaceDestroyed(final SurfaceHolder holder) {
        gameThread.interrupt();
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Logger.e("Interruption issue!", e, LoggerDepth.ACTUAL_METHOD);
        }
    }

    @Override
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            rectangleSprite.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        //noinspection NumericCastThatLosesPrecision
        final int x = (int) event.getX();
        //noinspection NumericCastThatLosesPrecision
        final int y = (int) event.getY();
        Logger.i("x: " + x + ", y: " + y, LoggerDepth.ACTUAL_METHOD);
        //noinspection SwitchStatementWithoutDefaultBranch
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                rectangleSprite.update(x - rectangleSprite.getBitmap().getWidth() / 2,
                        y - rectangleSprite.getBitmap().getHeight() / 2);
                break;
            case MotionEvent.ACTION_MOVE:
                rectangleSprite.update(x - rectangleSprite.getBitmap().getWidth() / 2,
                        y - rectangleSprite.getBitmap().getHeight() / 2);
                break;
            case MotionEvent.ACTION_UP:
                rectangleSprite.update(x - rectangleSprite.getBitmap().getWidth() / 2,
                        y - rectangleSprite.getBitmap().getHeight() / 2);
        }
        this.invalidate();
        return true;
    }
}
