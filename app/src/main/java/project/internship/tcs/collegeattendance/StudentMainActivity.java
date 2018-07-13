package project.internship.tcs.collegeattendance;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;

public class StudentMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sp;
    SharedPreferences.Editor se;
    ImageView pic;
    TextView name,profession;
    LinearLayout fab;
    ImageView att;
    Toolbar toolbar;
    Fragment fragment=null;
    DrawerLayout drawer;
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        att=(ImageView)findViewById(R.id.att);
        fab = (LinearLayout) findViewById(R.id.fab);
        att.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StudentMainActivity.this,MarkAttendance.class);
                intent.putExtra("rollno",sp.getString("rollno",null));
                    ActivityOptions options = ActivityOptions.makeCustomAnimation(StudentMainActivity.this, R.anim.fade_in, R.anim.fade_out);
                    startActivity(intent, options.toBundle());
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        pic=headerView.findViewById(R.id.imageView);
        name=headerView.findViewById(R.id.textView);
        profession=headerView.findViewById(R.id.textView2);

        sp=getSharedPreferences("college_data",MODE_PRIVATE);
        se=sp.edit();
        populate();
    }

    private void populate() {
        name.setText(sp.getString("firstname",null)+" "+sp.getString("lastname",null));
        profession.setText(sp.getString("position",null));
        if(sp.getString("pic",null)!=null) {
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                File myImageFile = new File(directory, "profilepic.jpeg");
                Picasso.with(this).load(myImageFile).into(pic);
        }
        else
            pic.setImageResource(R.mipmap.profilepic);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {return false;}

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
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            fab.setVisibility(View.VISIBLE);
            drawer.closeDrawer(GravityCompat.START);
            flag=0;
            return true;
        }else if(id==R.id.nav_view_profile){
            fragment=new StudentViewProfile();
            flag=1;
        }else if (id==R.id.nav_edit_profile){
            fragment=new StudentEditProfile();
            flag=1;
        }else if (id==R.id.nav_docs){
            fragment=new ViewFiles();
            flag=1;
        }
        fab.setVisibility(View.INVISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.screen_area,fragment).commit();
        drawer.closeDrawer(GravityCompat.START);
        flag=1;
        return true;
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (flag == 0) {
                if (doubleBackToExitPressedOnce) {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            } else {
                fragment = new Home();
                flag = 0;
                fab.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.screen_area,fragment).commit();
                drawer.closeDrawer(GravityCompat.START);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            }
        }
    }
}
