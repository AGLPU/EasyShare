package com.appslabs.shareeasypro.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.appslabs.shareeasypro.adapter.FileListAdapter;
import com.appslabs.shareeasypro.app.Activity;
import com.appslabs.shareeasypro.app.EditableListFragment;
import com.appslabs.shareeasypro.util.Adsbuilder;
import com.appslabs.shareeasypro.util.FileUtils;
import com.appslabs.shareeasypro.widget.GroupEditableListAdapter;
import com.appslabs.shareeasypro.R;
import com.appslabs.shareeasypro.exception.NotReadyException;
import com.appslabs.shareeasypro.fragment.FileExplorerFragment;
import com.facebook.ads.AudienceNetworkAds;
import com.genonbeta.android.framework.io.DocumentFile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

/**
 * Created by: veli
 * Date: 5/29/17 3:18 PM
 */

public class FilePickerActivity extends Activity
{
    public static final String ACTION_CHOOSE_DIRECTORY = "com.genonbeta.intent.action.CHOOSE_DIRECTORY";
    public static final String ACTION_CHOOSE_FILE = "com.genonbeta.intent.action.CHOOSE_FILE";

    public static final String EXTRA_ACTIVITY_TITLE = "activityTitle";
    public static final String EXTRA_START_PATH = "startPath";
    // belongs to returned result intent
    public static final String EXTRA_CHOSEN_PATH = "chosenPath";

    private FileExplorerFragment mFileExplorerFragment;
    private FloatingActionButton mFAB;

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
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filepicker);

        setupFBAds();
        mFileExplorerFragment = (FileExplorerFragment) getSupportFragmentManager().findFragmentById(R.id.activitiy_filepicker_fragment_files);
        mFAB = findViewById(R.id.content_fab);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if (getIntent() != null) {
            boolean hasTitlesDefined = false;

            if (getIntent() != null && getSupportActionBar() != null) {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                if (hasTitlesDefined = getIntent().hasExtra(EXTRA_ACTIVITY_TITLE))
                    getSupportActionBar().setTitle(getIntent().getStringExtra(EXTRA_ACTIVITY_TITLE));

            }

            if (ACTION_CHOOSE_DIRECTORY.equals(getIntent().getAction())) {
                if (getSupportActionBar() != null) {
                    if (!hasTitlesDefined)
                        getSupportActionBar().setTitle(R.string.text_chooseFolder);
                    else
                        getSupportActionBar().setSubtitle(R.string.text_chooseFolder);
                }

                mFileExplorerFragment
                        .getAdapter()
                        .setConfiguration(true, false, null);

                mFileExplorerFragment.refreshList();

                RecyclerView recyclerView = mFileExplorerFragment
                        .getListView();

                recyclerView.setPadding(0, 0, 0, 200);

                recyclerView.setClipToPadding(false);

                mFAB.show();
                mFAB.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        DocumentFile selectedPath = mFileExplorerFragment.getAdapter().getPath();

                        adsbuilder.showAds();
                        if (selectedPath != null && selectedPath.canWrite())
                            finishWithResult(selectedPath);
                        else
                            Snackbar.make(v, R.string.mesg_currentPathUnavailable, Snackbar.LENGTH_SHORT).show();
                    }
                });
            } else if (ACTION_CHOOSE_FILE.equals(getIntent().getAction())) {
                adsbuilder.showAds();
                if (getSupportActionBar() != null) {
                    if (!hasTitlesDefined)
                        getSupportActionBar().setTitle(R.string.text_chooseFile);
                    else
                        getSupportActionBar().setSubtitle(R.string.text_chooseFolder);
                }

                mFileExplorerFragment.setLayoutClickListener(new EditableListFragment.LayoutClickListener<GroupEditableListAdapter.GroupViewHolder>()
                {
                    @Override
                    public boolean onLayoutClick(EditableListFragment listFragment, GroupEditableListAdapter.GroupViewHolder holder, boolean longClick)
                    {
                        if (longClick)
                            return false;

                        try {
                            adsbuilder.showAds();
                            FileListAdapter.GenericFileHolder fileHolder = mFileExplorerFragment
                                    .getAdapter()
                                    .getItem(holder.getAdapterPosition());

                            if (fileHolder instanceof FileListAdapter.FileHolder) {
                                finishWithResult(((FileListAdapter.FileHolder) fileHolder).file);
                                return true;
                            }
                        } catch (NotReadyException e) {
                            e.printStackTrace();
                        }

                        return false;
                    }
                });
            } else
                finish();

            if (!isFinishing())
                if (getIntent().hasExtra(EXTRA_START_PATH)) {
                    try {
                        mFileExplorerFragment.goPath(FileUtils.fromUri(this, Uri.parse(getIntent().getStringExtra(EXTRA_START_PATH))));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        } else
            finish();
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
    public void onBackPressed()
    {
        if (mFileExplorerFragment == null
                || !mFileExplorerFragment.onBackPressed())
            super.onBackPressed();
    }

    private void finishWithResult(DocumentFile file)
    {
        setResult(Activity.RESULT_OK, new Intent(ACTION_CHOOSE_DIRECTORY)
                .putExtra(EXTRA_CHOSEN_PATH, file.getUri()));

        finish();
    }
}
