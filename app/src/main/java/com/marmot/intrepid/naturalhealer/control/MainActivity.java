package com.marmot.intrepid.naturalhealer.control;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.marmot.intrepid.naturalhealer.R;
import com.marmot.intrepid.naturalhealer.data.DAOBase;
import com.marmot.intrepid.naturalhealer.model.*;
import com.marmot.intrepid.naturalhealer.model.enumerations.*;
import com.marmot.intrepid.naturalhealer.service.GameService;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements
        MainFragment.OnFragmentInteractionListener,
        InventoryFragment.OnFragmentInteractionListener,
        GrimoireFragment.OnFragmentInteractionListener,
        QuestBookFragment.OnFragmentInteractionListener,
        ShopFragment.OnFragmentInteractionListener,
        InfoFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener {

    private GameService game;
    static Context actContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actContext = getApplicationContext();
        // ========== GAME CREATION ==========

        game = GameService.getInstance();
        game.fillInventory();

         final DAOBase db = Room.databaseBuilder(getApplicationContext(), DAOBase.class, "db-thenaturalhealer").build();
         new AsyncTask<Void, Void, List<Player>>(){
            @Override
            protected List<Player> doInBackground(Void... params) {
                System.out.println(db.playerDAO().getAll());
                return db.playerDAO().getAll();
            }

            @Override
            protected void onPostExecute(List<Player> players) {
                if(players.size()==0){
                    Player player1 = new Player("Jean-Michel Druide", "ic_player", new Rank(RankEnum.APPRENTICE), 930, 500.00);
                    db.playerDAO().insertOne(player1);
                    List<Player> pList = db.playerDAO().getPlayer("Jean-Michel Druide");
                    for (Player player : players) {
                        System.out.println("Player : " + player.getNickname());
                    }
                }
                else {
                    for (Player player : players) {
                        System.out.println("Player : " + player.getNickname());
                    }
                }
            }
        }.execute();
        
        List<Player> players = db.playerDAO().getAll();
        if(players == null){
            Player player1 = new Player("Jean-Michel Druide", "ic_player", new Rank(RankEnum.APPRENTICE), 930, 500.00);
            db.playerDAO().insertOne(player1);
            System.out.println(db.playerDAO().getPlayer("Jean-Michel Druide"));
        }
        else {
            System.out.println(players.toString());
        }

        game.fillInventory();

        Player player = new Player("Jean-Michel Druide", "ic_player", new Rank(RankEnum.APPRENTICE), 930, 500.00);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        TextView playerName = (TextView) header.findViewById(R.id.playerName);
        TextView playerRank = (TextView) header.findViewById(R.id.playerRank);
        TextView xpText = (TextView) header.findViewById(R.id.xpText);

        ImageView playerPic = (ImageView) header.findViewById(R.id.playerPic);

        ProgressBar xpBar = (ProgressBar) header.findViewById(R.id.xpBar);

        playerName.setText(player.getNickname());
        playerRank.setText("Rank : " + player.getRank().getName().getEn());
        xpText.setText(player.getXp() + " / " + player.getRank().getGoal());

        Context context = playerPic.getContext();
        int id = context.getResources().getIdentifier(player.getPicName(), "mipmap", context.getPackageName());
        playerPic.setImageResource(id);

        xpBar.setMax(player.getRank().getGoal());
        xpBar.setProgress(player.getXp());

        // ========== END OF THE GAME CREATION ==========

        //startService(new Intent(this, GameService.class));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //NOTE:  Checks first item in the navigation drawer initially
        navigationView.setCheckedItem(R.id.nav_map);

        //NOTE:  Open fragment1 initially.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new MainFragment());
        ft.commit();
        super.onStop();
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        //NOTE: creating fragment object
        android.support.v4.app.Fragment fragment = null;
        Class fragmentClass = null;

        if (id == R.id.nav_map) {
            fragmentClass = MainFragment.class;
        } else if (id == R.id.nav_inventory) {
            fragmentClass = InventoryFragment.class;
        } else if (id == R.id.nav_grimoire) {
            fragmentClass = GrimoireFragment.class;
        } else if (id == R.id.nav_questBook) {
            fragmentClass = QuestBookFragment.class;
        } else if (id == R.id.nav_shop) {
            fragmentClass = ShopFragment.class;
        } else if (id == R.id.nav_info) {
            fragmentClass = InfoFragment.class;
        }

        try {
            fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

        //NOTE:  Closing the drawer after selecting
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout); //Ya you can also globalize this variable :P
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(String s) {
        // NOTE:  Code to replace the toolbar title based current visible fragment
        getSupportActionBar().setTitle(s);
    }

    public static Context getContext() {return actContext;}
}
