package com.deepika.newsnow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

public class HomeTabActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        Log.v("news category",pref.getString("news_category_list",null));
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent modifySettings=new Intent(HomeTabActivity.this,SettingsActivity.class);

            startActivity(modifySettings);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int sectionNo = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView = null;
            if (sectionNo == 1)
                rootView = inflater.inflate(R.layout.fragment_home_tab, container, false);
            else if (sectionNo == 2)
                rootView = inflater.inflate(R.layout.fragment_trending_tab, container, false);
            else if (sectionNo == 3)
                rootView = inflater.inflate(R.layout.fragment_bookmark_tab, container, false);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        Drawable myDrawable;
        String title;
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment = new Fragment();
            if (position == 0){
                fragment = new HeadlinesFragment();

            }else if (position == 1){
                fragment = new TrendingFragment();

            }else if (position == 2){
                fragment = new BookmarkFragment();

            }

            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    myDrawable = getResources().getDrawable(R.drawable.ic_new_releases_white_18dp);
                    title = getResources().getString(R.string.HEADLINES);
                    break;
                case 1:
                    myDrawable = getResources().getDrawable(R.drawable.ic_whatshot_white_18dp);
                    title = getResources().getString(R.string.TRENDING);
                    break;
                case 2:
                    myDrawable = getResources().getDrawable(R.drawable.ic_bookmark_white_18dp);
                    title = getResources().getString(R.string.BOOKMARK);
                    break;
            }
            SpannableStringBuilder sb = new SpannableStringBuilder("   " + title); // space added before text for convenience
            try {
                myDrawable.setBounds(5, 5, myDrawable.getIntrinsicWidth(), myDrawable.getIntrinsicHeight());
                ImageSpan span = new ImageSpan(myDrawable, DynamicDrawableSpan.ALIGN_BASELINE);
                sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (Exception e) {
                // TODO: handle exception
            }
            return sb;
        }
    }
}
