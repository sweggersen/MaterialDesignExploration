package sweggersen.testdesignlibrary;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_ALBUM_ART_RESID = "album_art";

    private LinearLayout root;
    private ImageView icon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        root = (LinearLayout) findViewById(R.id.root_detail);
        icon = (ImageView) findViewById(R.id.icon);

        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                String id = bundle.getString("SHARED");
                icon.setTransitionName(id);
            }
        }

        Palette.generateAsync(((BitmapDrawable) icon.getDrawable()).getBitmap(), new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                root.setBackgroundColor(palette.getVibrantColor(Color.DKGRAY));
            }
        });*/

        populate();
    }


    private void populate() {
        int albumArtResId = getIntent().getIntExtra(EXTRA_ALBUM_ART_RESID, 0);
        icon.setImageResource(albumArtResId);
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }
}
