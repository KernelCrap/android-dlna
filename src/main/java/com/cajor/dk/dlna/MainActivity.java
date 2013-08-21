package com.cajor.dk.dlna;

import android.app.FragmentManager;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends ListActivity
                          implements ContentDirectoryBrowseTaskFragment.Callbacks,
                          SharedPreferences.OnSharedPreferenceChangeListener {

    private ContentDirectoryBrowseTaskFragment mFragment;
    private ArrayAdapter<CustomListItem> mDeviceListAdapter;
    private ArrayAdapter<CustomListItem> mItemListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        mFragment = (ContentDirectoryBrowseTaskFragment)fragmentManager.findFragmentByTag("task");

        mDeviceListAdapter = new CustomListAdapter(this);
        mItemListAdapter = new CustomListAdapter(this);

        setListAdapter(mDeviceListAdapter);

        if (mFragment == null) {
            mFragment = new ContentDirectoryBrowseTaskFragment();
            fragmentManager.beginTransaction().add(mFragment, "task").commit();
        } else {
            mFragment.refreshDevices();
            mFragment.refreshCurrent();
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        final IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(receiver, filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                Toast.makeText(this, R.string.info_searching, Toast.LENGTH_SHORT).show();
                mFragment.refreshCurrent();
                break;

            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        mFragment.refreshCurrent();
    }

    @Override
    public void onBackPressed() {
        if (mFragment.goBack())
            super.onBackPressed();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        mFragment.navigateTo(l.getItemAtPosition(position));
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public void onDisplayDevices() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setListAdapter(mDeviceListAdapter);
            }
        });
    }

    @Override
    public void onDisplayDirectories() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mItemListAdapter.clear();
                setListAdapter(mItemListAdapter);
            }
        });
    }

    @Override
    public void onDisplayItems(final ArrayList<ItemModel> items) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mItemListAdapter.clear();
                mItemListAdapter.addAll(items);
            }
        });
    }

    @Override
    public void onDisplayItemsError(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mItemListAdapter.clear();
                mItemListAdapter.add(new CustomListItem(
                        R.drawable.ic_warning,
                        getResources().getString(R.string.info_errorlist_folders),
                        error));
            }
        });
    }

    @Override
    public void onDeviceAdded(final DeviceModel device) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int position = mDeviceListAdapter.getPosition(device);
                if (position >= 0) {
                    mDeviceListAdapter.remove(device);
                    mDeviceListAdapter.insert(device, position);
                } else {
                    mDeviceListAdapter.add(device);
                }
            }
        });
    }

    @Override
    public void onDeviceRemoved(final DeviceModel device) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDeviceListAdapter.remove(device);
            }
        });
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {

                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                        WifiManager.WIFI_STATE_UNKNOWN);

                TextView wifi_warning = (TextView)findViewById(R.id.wifi_warning);

                switch (state) {
                    case WifiManager.WIFI_STATE_ENABLED:
                        wifi_warning.setVisibility(View.GONE);

                        if (mFragment != null) {
                            mFragment.refreshDevices();
                            mFragment.refreshCurrent();
                        }
                        break;
                    case WifiManager.WIFI_STATE_DISABLED:
                        wifi_warning.setVisibility(View.VISIBLE);
                        mDeviceListAdapter.clear();
                        mItemListAdapter.clear();
                        break;
                    case WifiManager.WIFI_STATE_UNKNOWN:
                        wifi_warning.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
    };
}