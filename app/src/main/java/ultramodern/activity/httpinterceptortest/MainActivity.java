package ultramodern.activity.httpinterceptortest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import ultramodern.activity.httpinterceptortest.databinding.ActivityMainBinding;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity  {
    private IntentFilter intentFilter;

    ActivityMainBinding binding;
    public static final String BroadCastStringForAction = "CheckInternet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //checking for quick response
        intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastStringForAction);

        Intent serviceIntent = new Intent(this,MyService.class);
        startService(serviceIntent);

        binding.notConnected.setVisibility(View.GONE);
        if (isOnline(getApplicationContext())){
            set_Visibility_ON();
        }
        else {
            set_Visibility_OFF();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(myReceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver,intentFilter);
    }

    public BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BroadCastStringForAction)){
                if (intent.getStringExtra("online_status").equals("true")){
                    set_Visibility_ON();
                }
                else {
                    set_Visibility_OFF();
                }
            }
        }
    };

    public boolean isOnline(Context c){
        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo !=null && networkInfo.isConnectedOrConnecting()){
            return true;
        }
        else {
            return false;
        }
    }

    public void set_Visibility_ON(){
        binding.notConnected.setVisibility(View.GONE);
        binding.submit.setVisibility(View.VISIBLE);
        binding.parent.setBackgroundColor(Color.WHITE);

    }

    public void set_Visibility_OFF(){
        binding.notConnected.setVisibility(View.VISIBLE);
        binding.submit.setVisibility(View.GONE);
        binding.parent.setBackgroundColor(Color.RED);

    }
}

