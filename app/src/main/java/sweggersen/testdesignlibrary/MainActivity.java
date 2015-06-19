package sweggersen.testdesignlibrary;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeImageTransform;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    ViewPager viewPager;
    FloatingActionButton fab;
    TabLayout tabLayout;

    Page[] pages = new Page[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pages[0] = new Page("Option 1", Color.parseColor("#009688"));
        pages[1] = new Page("Option 2", Color.parseColor("#0097A7"));
        pages[2] = new Page("Option 3", Color.parseColor("#039BE5"));
        pages[3] = new Page("Option 4", Color.parseColor("#689F38"));
        pages[4] = new Page("Option 5", Color.parseColor("#43A047"));

        setupToolbar();
        setupViewPager();
        setupFab();
        setupTablayout();
    }

    private void setupViewPager() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new TestPagerAdapter(getSupportFragmentManager()));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                ValueAnimator animator = ValueAnimator.ofInt(((ColorDrawable) toolbar.getBackground()).getColor(), pages[position].color);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int newColor = (int) animation.getAnimatedValue();
                        toolbar.setBackgroundColor(newColor);
                        tabLayout.setBackgroundColor(newColor);

                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(darker(newColor));
                        }
                    }
                });
                animator.setEvaluator(new ArgbEvaluator());
                animator.setDuration(500);
                animator.start();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupFab(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    private void setupTablayout(){
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Show menu icon
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_action_dice);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Snackbar
                        .make(findViewById(R.id.coordinatorLayout),
                                "This is Snackbar",
                                Snackbar.LENGTH_LONG)
                        .setAction("Action", this)
                        .show(); // Do not forget to show!
                break;
        }
    }

    class TestPagerAdapter extends FragmentStatePagerAdapter {

        public TestPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ScreenSlidePageFragment();
        }

        @Override
        public int getCount() {
            return pages.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pages[position].text;
        }
    }

    public static class ScreenSlidePageFragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.cardviews, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            if (view == null) return;

            LinearLayout cardLayout = (LinearLayout) view.findViewById(R.id.card_parent);
            for (int i = 0; i < 4; i++) {
                final View card = LayoutInflater.from(getActivity()).inflate(R.layout.card, cardLayout, false);

                if (i == 0) {
                    int margin = (int) getResources().getDimension(R.dimen.card_margin);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(margin, margin, margin, margin);
                    card.setLayoutParams(params);
                }

                final String transitionName = "imageIcon";

                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageView imageIcon = (ImageView) card.findViewById(R.id.icon);
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra(DetailActivity.EXTRA_ALBUM_ART_RESID, R.drawable.icon);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), imageIcon, transitionName);
                            Bundle bundle = options.toBundle();
                            /*bundle.putString("SHARED", transitionName);
                            imageIcon.setTransitionName(transitionName);*/

                            getActivity().startActivity(intent, bundle);
                        } else {
                            getActivity().startActivity(intent);
                        }

                    }
                });

                cardLayout.addView(card);
            }
        }
    }

    public static class Page {

        String text;
        int color;

        public Page(String text, int color) {
            this.text = text;
            this.color = color;
        }
    }

    private static final double FACTOR = 0.7;

    public static int darker(int color) {
        return Color.rgb(Math.max((int) (Color.red(color) * FACTOR), 0),
                Math.max((int) (Color.green(color) * FACTOR), 0),
                Math.max((int) (Color.blue(color) * FACTOR), 0));
    }
}
