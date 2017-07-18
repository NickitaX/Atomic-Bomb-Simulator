package nickita.gq.atomicbombsimulator;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import nickita.gq.atomicbombsimulator.fragments.BombConfigurationFragment;
import nickita.gq.atomicbombsimulator.fragments.CounterFragment;
import nickita.gq.atomicbombsimulator.fragments.MapFragment;
import nickita.gq.atomicbombsimulator.fragments.ResultFragment;
import nickita.gq.atomicbombsimulator.model.Bomb;
import nickita.gq.atomicbombsimulator.model.EventMessage;
import nickita.gq.atomicbombsimulator.utils.Notifications;
import nickita.gq.atomicbombsimulator.utils.Values;

/**
 * Created by Nickita on 28/2/17.
 */
public class MainActivity extends AppCompatActivity {
    ResideMenu mResideMenu;
    Button mMenuButton;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventMessage event) {
        switch (event.getType()) {
            case Values.SHOW_RESULT: {
                getFragmentManager().beginTransaction().replace(R.id.bomb_configuration_container, new ResultFragment(), Values.RESULT_FRAGMENT_TAG).commit();
                break;
            }
            case Values.DROP_ANOTHER: {
                getFragmentManager().beginTransaction().replace(R.id.bomb_configuration_container, new BombConfigurationFragment(), Values.CONFIGURATION_FRAGMENT_TAG).commit();
                break;
            }
        }
    }

    private void setUpDrawer(){
        mResideMenu = new ResideMenu(this);
        mResideMenu.setBackground(R.drawable.blurred_background);
        mResideMenu.attachToActivity(this);
        mResideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        mResideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);

        //TODO (2) Implement menu logic

        for (int i = 0; i < Values.MENU_TITLES.length; i++){
            ResideMenuItem item = new ResideMenuItem(this, Values.MENU_ICONS[i], Values.MENU_TITLES[i]);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            mResideMenu.addMenuItem(item,  ResideMenu.DIRECTION_LEFT);
        }
    }

    private void initialize(){
        mMenuButton = (Button) findViewById(R.id.menu_button);
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mResideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initialize();
        setUpFragments();
        setUpDrawer();
        setUpFab();
        EventBus.getDefault().register(this);
        Notifications.showAlert(this, Values.INSTRUCTIONS_MESSAGE_TITLE, Values.INSTRUCTIONS_MESSAGE_BODY, Values.BUTTON_CONFIRMATION_TEXT );        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setUpFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.launch_bomb_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new EventMessage<Bomb>(null, Values.PREPARE_BOMB));
            }
        });
    }

    private void setUpFragments() {
        getFragmentManager().beginTransaction().add(R.id.maps_container, new MapFragment(), Values.MAP_FRAGMENT_TAG).commit();
        getFragmentManager().beginTransaction().add(R.id.bomb_configuration_container, new BombConfigurationFragment(), Values.CONFIGURATION_FRAGMENT_TAG).commit();
        getFragmentManager().beginTransaction().add(R.id.middle_container, new CounterFragment(), Values.COUNTER_FRAGMENT_TAG).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_facebook:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Values.FACEBOOK_URI));
                startActivity(intent);
                return true;
            case R.id.action_feedback:
                Notifications.showAlert(this, Values.FEEDBACK_MESSAGE_TITLE, Values.FEEDBACK_MESSAGE_BODY , Values.BUTTON_CONFIRMATION_TEXT );
                return true;
            case R.id.action_faq:
                Notifications.showAlert(this, Values.INSTRUCTIONS_MESSAGE_TITLE , Values.INSTRUCTIONS_MESSAGE_BODY , Values.BUTTON_CONFIRMATION_TEXT );
                return true;
            case R.id.action_request_gps:
                EventBus.getDefault().post(new EventMessage<Bomb>(null, Values.ASK_GPS));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
