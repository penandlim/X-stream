/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigationdrawerexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * This example illustrates a common usage of the DrawerLayout widget
 * in the Android support library.
 * <p/>
 * <p>When a navigation (left) drawer is present, the host activity should detect presses of
 * the action bar's Up affordance as a signal to open and close the navigation drawer. The
 * ActionBarDrawerToggle facilitates this behavior.
 * Items within the drawer should fall into one of two categories:</p>
 * <p/>
 * <ul>
 * <li><strong>View switches</strong>. A view switch follows the same basic policies as
 * list or tab navigation in that a view switch does not create navigation history.
 * This pattern should only be used at the root activity of a task, leaving some form
 * of Up navigation active for activities further down the navigation hierarchy.</li>
 * <li><strong>Selective Up</strong>. The drawer allows the user to choose an alternate
 * parent for Up navigation. This allows a user to jump across an app's navigation
 * hierarchy at will. The application should treat this as it treats Up navigation from
 * a different task, replacing the current task stack using TaskStackBuilder or similar.
 * This is the only form of navigation drawer that should be used outside of the root
 * activity of a task.</li>
 * </ul>
 * <p/>
 * <p>Right side drawers should be used for actions, not navigation. This follows the pattern
 * established by the Action Bar that navigation should be to the left and actions to the right.
 * An action should be an operation performed on the current contents of the window,
 * for example enabling or disabling a data overlay on top of the current content.</p>
 */

public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ImageLoader imageLoader;

    public static final String PREFS_NAME = "MyPrefsFile";
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;
    public static Context mContext;
    private static GridView gridView;
    public static Video video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(mContext);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);


        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens.
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        gridView = (GridView)findViewById(R.id.gridview);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
            if (!mDrawerLayout.isDrawerOpen(GravityCompat.START))
                mDrawerLayout.openDrawer(mDrawerList);
            else
                mDrawerLayout.closeDrawer(mDrawerList);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.action_settings_button:
                // create intent to perform web search for this planet
                Intent intent = new Intent(this,SettingsActivity.class);
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        Bundle args = new Bundle();
        System.out.println(position);
        final EditText editText = (EditText) findViewById(R.id.search_button);
        switch (position){
            case 0 :
                new loadData_xvideo().execute("http://www.xvideos.com");
                editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId,KeyEvent event) {
                        if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            in.hideSoftInputFromWindow(editText.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                            new loadData_xvideo().execute(video.xvid_search(editText.getText().toString(), "", "", ""));
                            return true;
                        }
                        return false;
                    }
                });
                break;
            case 1 :
                new loadData_xnxx().execute("http://www.xnxx.com");
                editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId,KeyEvent event) {
                        if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            in.hideSoftInputFromWindow(editText.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                            new loadData_xnxx().execute(video.xnxx_search(editText.getText().toString(), "", "", ""));
                            return true;
                        }
                        return false;
                    }
                });
                break;
            case 2 :
                new loadData_redtube().execute("http://www.redtube.com");
                editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId,KeyEvent event) {
                        if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            in.hideSoftInputFromWindow(editText.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                            new loadData_redtube().execute(video.redtube_search(editText.getText().toString(), ""));
                            return true;
                        }
                        return false;
                    }
                });
                break;
            case 3 :
                new loadData_pornhub().execute("http://www.pornhub.com");
                editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId,KeyEvent event) {
                        if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            in.hideSoftInputFromWindow(editText.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                            new loadData_pornhub().execute(video.pornhub_search(editText.getText().toString(), ""));
                            return true;
                        }
                        return false;
                    }
                });
                break;
            case 4 :
                new loadData_porn().execute("http://www.porn.com");
                editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId,KeyEvent event) {
                        if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            in.hideSoftInputFromWindow(editText.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                            new loadData_porn().execute(video.porn_search(editText.getText().toString(), "",""));
                            return true;
                        }
                        return false;
                    }
                });
                break;
            case 5 :
                new loadData_xhamster().execute("http://www.xhamster.com");
                editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId,KeyEvent event) {
                        if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            in.hideSoftInputFromWindow(editText.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                            new loadData_xhamster().execute(video.xhamster_search(editText.getText().toString()));
                            return true;
                        }
                        return false;
                    }
                });
                break;
            case 8 :
                Intent intent = new Intent(mContext,SettingsActivity.class);
                startActivity(intent);
                break;
            default :
                new loadData_xvideo().execute("http://www.xvideos.com");
                break;
        }



        // Sets position, title, and closes the drawer.
        if (position !=8) {
            mDrawerList.setItemChecked(position, true);
            setTitle(mPlanetTitles[position]);
            editText.setText("");
        }
        else
            mDrawerList.setItemChecked(position, false);
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
        gridView.setNumColumns(getResources().getInteger(R.integer.grid_rows));
    }


    public class loadData_xvideo extends AsyncTask<String, Void, List<videoObject_xvideo>> {

        @Override
        protected List<videoObject_xvideo> doInBackground(String... strings) {
            List<videoObject_xvideo> kList = video.xvid_page(strings[0]);
            //Log.d("doInBackground", kList.get(0).getTitle());
            return kList;
        }

        @Override
        protected void onPostExecute(final List<videoObject_xvideo> video_Objects){
            gridView.setAdapter(new GridAdapter_xvideos(mContext, video_Objects, imageLoader));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(mContext, "Loading video...",
                            Toast.LENGTH_LONG).show();
                    new playvideo_xvideo().execute(video_Objects.get(i).getVideoURL());
                }
                class playvideo_xvideo extends AsyncTask<String, Void, Void> {

                    @Override
                    protected Void doInBackground(String... strings) {
                        videoObject_xvideo.playVideo(videoObject_xvideo.getVideoSourceURL(strings[0]),mContext);
                        return null;
                    }
                }
            });
        }

    }

    public class loadData_xnxx extends AsyncTask<String, Void, List<videoObject_xvideo>> {

        @Override
        protected List<videoObject_xvideo> doInBackground(String... strings) {
            List<videoObject_xvideo> kList = video.xnxx_page(strings[0]);
            //Log.d("doInBackground", kList.get(0).getTitle());
            return kList;
        }

        @Override
        protected void onPostExecute(final List<videoObject_xvideo> video_Objects){
            gridView.setAdapter(new GridAdapter_xvideos(mContext, video_Objects, imageLoader));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(mContext, "Loading video...",
                            Toast.LENGTH_LONG).show();
                    new playvideo_xvideo().execute(video_Objects.get(i).getVideoURL());
                }

                class playvideo_xvideo extends AsyncTask<String, Void, Void> {

                    @Override
                    protected Void doInBackground(String... strings) {
                        videoObject_xvideo.playVideo(videoObject_xvideo.getVideoSourceURL(strings[0]), mContext);
                        return null;
                    }
                }
            });
        }
    }

    public class loadData_redtube extends AsyncTask<String, Void, List<videoObject_redtube>> {

        @Override
        protected List<videoObject_redtube> doInBackground(String... strings) {
            List<videoObject_redtube> kList = video.redtube_page(strings[0]);
            //Log.d("doInBackground", kList.get(0).getTitle());
            return kList;
        }

        @Override
        protected void onPostExecute(final List<videoObject_redtube> video_Objects){
            gridView.setAdapter(new GridAdapter_redtube(mContext, video_Objects, imageLoader));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(mContext, "Loading video...",
                            Toast.LENGTH_LONG).show();
                    new playvideo_redtube().execute(video_Objects.get(i).getVideoURL());
                }

                class playvideo_redtube extends AsyncTask<String, Void, Void> {

                    @Override
                    protected Void doInBackground(String... strings) {
                        videoObject_redtube.playVideo(videoObject_redtube.getVideoSourceURL(strings[0]), mContext);
                        return null;
                    }
                }


            });
        }
    }

    public class loadData_pornhub extends AsyncTask<String, Void, List<videoObject_pornhub>> {

        @Override
        protected List<videoObject_pornhub> doInBackground(String... strings) {
            List<videoObject_pornhub> kList = video.pornhub_page(strings[0]);
            //Log.d("doInBackground", kList.get(0).getTitle());
            return kList;
        }

        @Override
        protected void onPostExecute(final List<videoObject_pornhub> video_Objects){
            gridView.setAdapter(new GridAdapter_pornhub(mContext, video_Objects, imageLoader));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(mContext, "Loading video...",
                            Toast.LENGTH_LONG).show();
                    new playvideo_pornhub().execute(video_Objects.get(i).getVideoURL());
                }

                class playvideo_pornhub extends AsyncTask<String, Void, Void> {

                    @Override
                    protected Void doInBackground(String... strings) {
                        videoObject_pornhub.playVideo(videoObject_pornhub.getVideoSourceURL(strings[0]), mContext);
                        return null;
                    }
                }


            });
        }
    }

    public class loadData_porn extends AsyncTask<String, Void, List<videoObject_porn>> {

        @Override
        protected List<videoObject_porn> doInBackground(String... strings) {
            List<videoObject_porn> kList = video.porn_page(strings[0]);
            //Log.d("doInBackground", kList.get(0).getTitle());
            return kList;
        }

        @Override
        protected void onPostExecute(final List<videoObject_porn> video_Objects){
            gridView.setAdapter(new GridAdapter_porn(mContext, video_Objects, imageLoader));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(mContext, "Loading video...",
                            Toast.LENGTH_LONG).show();
                    new playvideo_porn().execute(video_Objects.get(i).getVideoURL());
                }

                class playvideo_porn extends AsyncTask<String, Void, Void> {

                    @Override
                    protected Void doInBackground(String... strings) {
                        videoObject_porn.playVideo(videoObject_porn.getVideoSourceURL(strings[0]), mContext);
                        return null;
                    }
                }


            });
        }
    }

    public class loadData_xhamster extends AsyncTask<String, Void, List<videoObject_xhamster>> {

        @Override
        protected List<videoObject_xhamster> doInBackground(String... strings) {
            List<videoObject_xhamster> kList = video.xhamster_page(strings[0]);
            //Log.d("doInBackground", kList.get(0).getTitle());
            return kList;
        }

        @Override
        protected void onPostExecute(final List<videoObject_xhamster> video_Objects){
            gridView.setAdapter(new GridAdapter_xhamster(mContext, video_Objects, imageLoader));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(mContext, "Loading video...",
                            Toast.LENGTH_LONG).show();
                    new playvideo_xhamster().execute(video_Objects.get(i).getVideoURL());
                }

                class playvideo_xhamster extends AsyncTask<String, Void, Void> {

                    @Override
                    protected Void doInBackground(String... strings) {
                        videoObject_xhamster.playVideo(videoObject_xhamster.getVideoSourceURL(strings[0]), mContext);
                        return null;
                    }
                }


            });
        }
    }
}
