package org.techtown.catsby;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.techtown.catsby.qrcode.QrcodeCreateActivity;
import org.techtown.catsby.community.FragmentCommunity;
import org.techtown.catsby.cattown.FragmentCatTown;
import org.techtown.catsby.home.BowlFragment;
import org.techtown.catsby.notification.NotificationActivity;
import org.techtown.catsby.retrofit.RetrofitClient;
import org.techtown.catsby.retrofit.dto.User;
import org.techtown.catsby.retrofit.service.UserService;
import org.techtown.catsby.setting.FragmentSetting_New;
import org.techtown.catsby.util.ImageUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private final BowlFragment fragmenthome = new BowlFragment();
    private final FragmentCatTown fragmentcattown = new FragmentCatTown();
    private final FragmentCommunity fragmentcommunity = new FragmentCommunity();
    private final FragmentSetting_New fragmentsetting = new FragmentSetting_New();
    private UserService userService;

    Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //permission
        AutoPermissions.Companion.loadAllPermissions(this, 101);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmenthome).commitAllowingStateLoss();
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
        menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.iconHome).setIcon(R.drawable.ic_baseline_home_24);

        userService = RetrofitClient.getUser();
        userService.getUser(FirebaseAuth.getInstance().getUid()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                Bundle bundle = new Bundle();
                bundle.putString("nickname", response.body().getNickname());
                bundle.putString("address", response.body().getAddress());
                if (response.body().getImage() != null) {
                    bundle.putString("image", response.body().getImage());
                } else {
                    bundle.putString("image", null);
                }
                fragmentsetting.setArguments(bundle);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notice:
                Intent notificateionIntent = new Intent(this, NotificationActivity.class);
                startActivity(notificateionIntent);
                break;

            case R.id.action_createQr:
                //activity to fragment
                //FragmentManager fragmentManager = getSupportFragmentManager();
                //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //fragmentTransaction.replace(R.layout.fragment_create_qr);
                //fragmentTransaction.commit();

                Intent createQrIntent = new Intent(this, QrcodeCreateActivity.class);
                startActivity(createQrIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch(menuItem.getItemId())
            {
                case R.id.iconHome:
                    transaction.replace(R.id.frameLayout, fragmenthome).commitAllowingStateLoss();
                    menuItem.setIcon(R.drawable.ic_baseline_home_24);
                    menu.findItem(R.id.iconCommunity).setIcon(R.drawable.ic_outline_people_alt_24);
                    menu.findItem(R.id.iconCatTown).setIcon(R.drawable.ic_outline_text_snippet_24);
                    menu.findItem(R.id.iconSetting).setIcon(R.drawable.ic_outline_settings_24);
                    break;
                case R.id.iconCommunity:
                    transaction.replace(R.id.frameLayout, fragmentcommunity).commitAllowingStateLoss();
                    menuItem.setIcon(R.drawable.ic_baseline_people_alt_24);
                    menu.findItem(R.id.iconHome).setIcon(R.drawable.ic_outline_home_24);
                    menu.findItem(R.id.iconCatTown).setIcon(R.drawable.ic_outline_text_snippet_24);
                    menu.findItem(R.id.iconSetting).setIcon(R.drawable.ic_outline_settings_24);
                    break;
                case R.id.iconCatTown:
                    transaction.replace(R.id.frameLayout, fragmentcattown).commitAllowingStateLoss();
                    menuItem.setIcon(R.drawable.ic_baseline_text_snippet_24);
                    menu.findItem(R.id.iconHome).setIcon(R.drawable.ic_outline_home_24);
                    menu.findItem(R.id.iconCommunity).setIcon(R.drawable.ic_outline_people_alt_24);
                    menu.findItem(R.id.iconSetting).setIcon(R.drawable.ic_outline_settings_24);
                    break;
                case R.id.iconSetting:
                    transaction.replace(R.id.frameLayout, fragmentsetting).commitAllowingStateLoss();
                    menuItem.setIcon(R.drawable.ic_baseline_settings_24);
                    menu.findItem(R.id.iconHome).setIcon(R.drawable.ic_outline_home_24);
                    menu.findItem(R.id.iconCommunity).setIcon(R.drawable.ic_outline_people_alt_24);
                    menu.findItem(R.id.iconCatTown).setIcon(R.drawable.ic_outline_text_snippet_24);
                    break;
            }
            return true;
        }
    }

    //사용자 권한
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }

    @Override
    public void onDenied(int i, String[] strings) {

    }

    @Override
    public void onGranted(int i, String[] strings) {

    }

}