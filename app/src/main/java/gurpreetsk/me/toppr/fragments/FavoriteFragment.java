package gurpreetsk.me.toppr.fragments;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import gurpreetsk.me.toppr.R;
import gurpreetsk.me.toppr.activities.DetailActivity;
import gurpreetsk.me.toppr.activities.FavoriteActivity;
import gurpreetsk.me.toppr.activities.SettingsActivity;
import gurpreetsk.me.toppr.adapters.FavoriteAdapter;
import gurpreetsk.me.toppr.model.Data;
import gurpreetsk.me.toppr.model.Database;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {


    private static final String TAG = ListFragment.class.getSimpleName();
    RecyclerView recyclerView;
    ImageButton fav;
    FavoriteAdapter adapter;
    ArrayList<Data> abbajabba = new ArrayList<>();
    String search_keyword = "";
    SQLiteOpenHelper database;
    SQLiteDatabase db;
    Database handler;

    public FavoriteFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (abbajabba != null)
            abbajabba.clear();
        PopulateList();
    }

    public void PopulateList() {
        ArrayList<Data> DataArrayList = handler.getAllData();
        for (int i = 0; i < DataArrayList.size(); i++) {
            Data data2 = DataArrayList.get(i);
            if(data2.getFav().equals("true")){
                abbajabba.add(new Data(data2.getId(), data2.getName(),data2.getImage(), data2.getCategory(),data2.getDescription(), data2.getExperience(), data2.getFav()));
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        adapter = new FavoriteAdapter(abbajabba, getContext());
        handler = new Database(getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings_overflow:
                startActivity(new Intent(getContext(), SettingsActivity.class));
                break;
            case R.id.favorite_icon:
                startActivity(new Intent(getContext(), FavoriteActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);

    }


}
