package com.appslabs.shareeasypro.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.appslabs.shareeasypro.app.Activity;
import com.appslabs.shareeasypro.ui.callback.PowerfulActionModeSupport;
import com.appslabs.shareeasypro.R;
import com.appslabs.shareeasypro.fragment.TextStreamListFragment;
import com.appslabs.shareeasypro.util.Adsbuilder;
import com.facebook.ads.AudienceNetworkAds;
import com.genonbeta.android.framework.widget.PowerfulActionMode;

public class TextStreamActivity
        extends Activity
        implements PowerfulActionModeSupport
{
    private PowerfulActionMode mActionMode;
    private TextStreamListFragment mStreamListFragment;

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
        setContentView(R.layout.activity_text_stream);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupFBAds();
        mActionMode = findViewById(R.id.action_mode);
        mStreamListFragment = (TextStreamListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_text_stream_fragment);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mActionMode.setOnSelectionTaskListener(new PowerfulActionMode.OnSelectionTaskListener()
        {
            @Override
            public void onSelectionTask(boolean started, PowerfulActionMode actionMode)
            {
                toolbar.setVisibility(!started ? View.VISIBLE : View.GONE);
                adsbuilder.showAds();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        if (mActionMode.hasActive(mStreamListFragment.getSelectionCallback()))
            mActionMode.finish(mStreamListFragment.getSelectionCallback());
        else
            super.onBackPressed();
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

    @Override
    public PowerfulActionMode getPowerfulActionMode()
    {
        return mActionMode;
    }
}
