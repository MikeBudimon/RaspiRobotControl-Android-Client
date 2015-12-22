package de.mikebudimon.raspirobotcontrolapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set a Toolbar to replace the ActionBar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Find DrawerLayout
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = setupDrawerToggle();
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.setDrawerListener(mDrawerToggle);

        // Find NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(navigationView);


        // Set first Fragment at StartUp
        if (savedInstanceState == null) {
            try {
                replaceFragment(InstructionsFragment.class.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
            setTitle(R.string.instructions_fragment);
        }
    }

    /**
     * Adds ItemSelectedListener for NDrawer
     *
     * @param navigationView NavigationView
     */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    /**
     * Changes the Fragment based on which menuItem was clicked
     *
     * @param menuItem item which was clicked in the drawer
     */
    private void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on position
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_instructions_fragment:
                fragmentClass = InstructionsFragment.class;
                break;
            case R.id.nav_login_fragment:
                fragmentClass = LoginFragment.class;
                break;
            case R.id.nav_about_fragment:
                fragmentClass = AboutFragment.class;
                break;
            default:
                fragmentClass = InstructionsFragment.class;
        }

        try {
            replaceFragment((Fragment) fragmentClass.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Highlight the selected item, update the title
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());

    }

    /**
     * Insert the fragment by replacing any existing fragment
     *
     * @param fragment: which fragment should be displayed
     */
    private void replaceFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, fragment).commit();

        // Closes the NavigationDrawer
        mDrawer.closeDrawers();
    }


    /**
     * Setup NavigationDrawer Icon with animation
     *
     * @return NavigationDrawer Icon with animation
     */
    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
