package gurpreetsk.me.toppr.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import gurpreetsk.me.toppr.activities.DetailActivity;
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
    String search_keyword = "";
    SQLiteOpenHelper database;
    SQLiteDatabase db;

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
                                        if (category.equals(get))
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
//        fav = (ImageButton) v.findViewById(R.id.favorite_button);
        adapter = new DataAdapter(events, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, events.get(position));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

                Vibrator vibe = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(50);
                database = new Database(getContext());
                db = database.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(Database.COLUMN_ID, events.get(position).getId());
                values.put(Database.COLUMN_NAME, events.get(position).getName());
                values.put(Database.COLUMN_IMAGE, events.get(position).getImage());
                values.put(Database.COLUMN_CATEGORY, events.get(position).getCategory());
                values.put(Database.COLUMN_DESCRIPTION, events.get(position).getDescription());
                values.put(Database.COLUMN_EXPERIENCE, events.get(position).getExperience());
                values.put(Database.COLUMN_FAVORITE, events.get(position).getFav());
                db.insert(Database.TABLE_NAME, null, values);
                Toast.makeText(getContext(), "Favorite added!", Toast.LENGTH_SHORT).show();

            }
        }));
        return v;
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
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
        inflater.inflate(R.menu.detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings_overflow:
                startActivity(new Intent(getContext(), SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);

    }

}