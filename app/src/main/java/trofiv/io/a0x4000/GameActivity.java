package trofiv.io.a0x4000;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class GameActivity extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new GameView(this));
    }
}
