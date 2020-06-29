package com.fcs.billingapp.spotbilling;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fcs.billingapp.fragments.BillingFragment;
import com.fcs.billingapp.fragments.DownloadFragment;
import com.fcs.billingapp.fragments.SettingsFragment;

public class   HomeActivity extends AppCompatActivity
        implements

        NavigationView.OnNavigationItemSelectedListener,
        DownloadFragment.OnFragmentInteractionListener,
        BillingFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener

        {

    // User Session Manager Class
   // UserSessionManagement session;
    String user_id = "";
    String imei_no = "";
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    public void onGroupItemClick(MenuItem item) {
        // One of the group items (using the onClick attribute) was clicked
        // The item parameter passed here indicates which item it is
        // All other menu item clicks are handled by onOptionsItemSelected()
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedScreenItems(id);
        return true;

    }

    private void displaySelectedScreenItems(int ItemID) {

        Bundle data = new Bundle();//Use bundle to pass data
        data.putString("user_id", user_id);//put string, int, etc in bundle with a key value
        data.putString("imei_no", imei_no);//put string, int, etc in bundle with a key valuE
        data.putString("password",password);


        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (ItemID) {
            case R.id.nav_billing:
                fragment = new BillingFragment();
                fragment.setArguments(data);//Finally set argument bundle to fragment
                break;
            case R.id.nav_download:
                fragment = new DownloadFragment();
                fragment.setArguments(data);//Finally set argument bundle to fragment
                break;
            case R.id.nav_settings:
                fragment = new SettingsFragment();
                fragment.setArguments(data);//Finally set argument bundle to fragment
                break;
           /* case R.id.nav_Logout:
                LogOutFunction();
                break;*/
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

            @Override
            public void onFragmentInteraction(Uri uri) {

            }
        }
