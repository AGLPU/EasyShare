package com.appslabs.shareeasypro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;

import com.appslabs.shareeasypro.app.Activity;
import com.appslabs.shareeasypro.R;
import com.appslabs.shareeasypro.util.Adsbuilder;
import com.facebook.ads.AudienceNetworkAds;

public class ManageDevicesActivity extends Activity
{
    private void setupFBAds() {
        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);


        // Find the Ad Container
        // LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        adsbuilder=new Adsbuilder(this,null);
        adsbuilder.buildAdsListner();
        adsbuilder.loadAds();
        // adsbuilder.loadBannerAds();
    }
    Adsbuilder adsbuilder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_devices);

        setupFBAds();
        findViewById(R.id.fixConnectionButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(ManageDevicesActivity.this, ConnectionManagerActivity.class)
                        .putExtra(ConnectionManagerActivity.EXTRA_REQUEST_TYPE, ConnectionManagerActivity.RequestType.RETURN_RESULT.toString())
                        .putExtra(ConnectionManagerActivity.EXTRA_ACTIVITY_SUBTITLE, getString(R.string.text_fixConnection)));
                adsbuilder.showAds();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        adsbuilder.destroyInterstialAds();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == android.R.id.home)
            finish();
        else
            return super.onOptionsItemSelected(item);

        return true;
    }
}
