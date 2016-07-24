package br.ufc.ec.pet.bustracker.trackerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import br.ufc.ec.pet.bustracker.trackerapp.types.Bus;
import br.ufc.ec.pet.bustracker.trackerapp.types.Route;

public class ConnectActivity extends AppCompatActivity {
    private EditText mHostEt, mIdRouteEt, mNameRouteEt, mDescriptionRouteEt,
            mIdBusEt, mTimeIntervalEt;
    private ToggleButton mStartBtn;
    private Bus mBus;
    private Route mRoute;
    private ConnectionManager mConnectionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        mConnectionManager = new ConnectionManager(this, "");
        Intent it = getIntent();
        if(it != null ) {
            if (it.hasExtra("TOKEN")) {
                mConnectionManager.setToken(it.getStringExtra("TOKEN"));
                Log.d("Bus", mConnectionManager.getToken());
            }
        }


        mHostEt = (EditText) findViewById(R.id.host_et);
        mIdRouteEt = (EditText) findViewById(R.id.id_route_et);
        mNameRouteEt = (EditText) findViewById(R.id.name_route_et);
        mDescriptionRouteEt = (EditText) findViewById(R.id.description_route_et);
        mIdBusEt = (EditText) findViewById(R.id.id_bus_et);
        mTimeIntervalEt = (EditText) findViewById(R.id.time_interval_et);

        mStartBtn = (ToggleButton) findViewById(R.id.start_btn);

        mBus = new Bus();
        mRoute = new Route();
        mHostEt.setText(getResources().getString(R.string.host_default));
        mNameRouteEt.setText("UFC -");
        mIdRouteEt.setText("86");
        mIdBusEt.setText("1");
        mTimeIntervalEt.setText("1000");
        mDescriptionRouteEt.setText("Dese");


        setEvents();
    }
    private void setEvents(){
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Bus", "clicked");

                if(mStartBtn.isChecked()) {
                    mConnectionManager.setServerPrefix(getHost());
                    mConnectionManager.putRoute(getRoute());

                    Intent intent = new Intent(v.getContext(), TrackerService.class);
                    intent.putExtra("BUS", getBus());
                    intent.putExtra("ROUTE", getRoute());
                    intent.putExtra("HOST", getHost());
                    intent.putExtra("TIME_INTERVAL", getTimeInterval());
                    intent.putExtra("TOKEN", mConnectionManager.getToken());
                    startService(intent);
                }
                else
                    stopService(new Intent(v.getContext(), TrackerService.class));
            }
        });
    }
    private Bus getBus(){
        mBus.setId(Integer.parseInt(mIdBusEt.getText().toString()));
        return mBus;
    }
    private Route getRoute(){
        mRoute.setId(Integer.parseInt(mIdRouteEt.getText().toString()));
        mRoute.setDescription(mDescriptionRouteEt.getText().toString());
        mRoute.setName(mNameRouteEt.getText().toString());
        return mRoute;
    }
    private String getHost(){
        return mHostEt.getText().toString();
    }
    private long getTimeInterval(){
        return Long.parseLong(mTimeIntervalEt.getText().toString());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, TrackerService.class);
        intent.putExtra("STOP_SEND", true);
        //startService(intent);
        stopService(intent);
    }
}