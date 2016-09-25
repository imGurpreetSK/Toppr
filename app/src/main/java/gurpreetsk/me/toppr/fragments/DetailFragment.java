package gurpreetsk.me.toppr.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import gurpreetsk.me.toppr.R;
import gurpreetsk.me.toppr.activities.DemographicsActivity;
import gurpreetsk.me.toppr.activities.FavoriteActivity;
import gurpreetsk.me.toppr.activities.SettingsActivity;
import gurpreetsk.me.toppr.model.Data;

public class DetailFragment extends Fragment {

    TextView title, description, experience, ctc;
    ImageView imageView;

    public DetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        procedure_GetHandles(v);

        Data data = getActivity().getIntent().getParcelableExtra(Intent.EXTRA_TEXT);
        title.setText(data.getName());
        getActivity().setTitle(data.getName());
        description.setText(data.getDescription());
        experience.setText(getResources().getText(R.string.experience) + " " + data.getExperience());
        if (data.getCategory().equals("HIRING"))
            ctc.setText(getResources().getText(R.string.ctc_string));
        else
            ctc.setText("");
        String image_link = data.getImage().replace("\\", "");
        Uri builder = Uri.parse(image_link).buildUpon().build();
        Picasso.with(getContext()).load(builder).into(imageView);

        return v;
    }

    private void procedure_GetHandles(View v) {
        title = (TextView) v.findViewById(R.id.detail_title);
        description = (TextView) v.findViewById(R.id.detail_description);
        experience = (TextView) v.findViewById(R.id.detail_experience);
        imageView = (ImageView) v.findViewById(R.id.detail_imageView);
        ctc = (TextView) v.findViewById(R.id.detail_CTC);
    }

}
