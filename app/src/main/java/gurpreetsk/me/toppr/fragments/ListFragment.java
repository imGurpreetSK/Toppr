package gurpreetsk.me.toppr.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import gurpreetsk.me.toppr.R;
import gurpreetsk.me.toppr.activities.DemographicsActivity;
import gurpreetsk.me.toppr.activities.FavoriteActivity;
import gurpreetsk.me.toppr.activities.SettingsActivity;
import gurpreetsk.me.toppr.adapters.DataAdapter;
import gurpreetsk.me.toppr.model.Data;
import gurpreetsk.me.toppr.model.Database;

import static android.widget.Toast.makeText;

public class ListFragment extends Fragment {

    private static final String TAG = ListFragment.class.getSimpleName();
    RecyclerView recyclerView;
    ImageButton fav;
    DataAdapter adapter;
    ArrayList<Data> events = new ArrayList<>();
    SQLiteDatabase db;
    Database handler;
    private EditText search;
    SwipeRefreshLayout swipeRefreshLayout;

    public ListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (events != null)
            events.clear();
        fetchJson();
    }

    private void fetchJson() {
        if (isNetworkConnected()) {
            Uri uri = Uri.parse("https://hackerearth.0x10.info/api/toppr_events?type=json&query=list_events");
            String url = uri.toString();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            final String get = prefs.getString(getString(R.string.settings_key), getString(R.string.all));

            RequestQueue queue = Volley.newRequestQueue(getActivity());
            if (get.equals("all")) {
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    for (int i = 0; i < 12; i++) {
                                        JSONObject obj = response.getJSONArray("websites").getJSONObject(i);
                                        String id = obj.getString("id");
                                        String name = obj.getString("name");
                                        String image = obj.getString("image");
                                        String category = obj.getString("category");
                                        String description = obj.getString("description");
                                        String experience = obj.getString("experience");
                                        handler.addEvents(new Data(id, name, image, category, description, experience, "false"));
                                        events.add(new Data(id, name, image, category, description, experience, "false"));
                                    }
                                    adapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },

                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, error.toString());
                            }
                        });

                queue.add(request);
            } else {
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    for (int i = 0; i < 12; i++) {
                                        JSONObject obj = response.getJSONArray("websites").getJSONObject(i);
                                        String id = obj.getString("id");
                                        String name = obj.getString("name");
                                        String image = obj.getString("image");
                                        String category = obj.getString("category");
                                        String description = obj.getString("description");
                                        String experience = obj.getString("experience");
                                        if (category.equals(get)) {
                                            handler.addEvents(new Data(id, name, image, category, description, experience, "false"));
                                            events.add(new Data(id, name, image, category, description, experience, "false"));
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },

                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, error.toString());
                            }
                        });

                queue.add(request);
            }
        } else {
            makeText(getContext(), "Please connect to Internet and try again.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        search = (EditText) v.findViewById(R.id.search);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        adapter = new DataAdapter(events, getContext());
        handler = new Database(getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        addTextListener();
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fetchJson();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
        return v;
    }

    public void addTextListener() {

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {
                query = query.toString().toLowerCase();
                ArrayList<Data> inter = new ArrayList<>();
                for (int i = 0; i < events.size(); i++) {
                    final String searchname = events.get(i).getName().toLowerCase();
                    final String searchcategory = events.get(i).getCategory().toLowerCase();
                    if (searchname.contains(query) || searchcategory.contains(query)) {
                        inter.add(events.get(i));
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter = new DataAdapter(inter, getContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
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
            case R.id.pieChart:
                Intent intent = new Intent(getContext(), DemographicsActivity.class);
                intent.putExtra("DATA", events);
                startActivity(intent);

        }
        return super.onOptionsItemSelected(item);

    }

}