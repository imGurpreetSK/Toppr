package gurpreetsk.me.toppr.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gurpreetsk.me.toppr.R;
import gurpreetsk.me.toppr.model.Data;

/**
 * Created by Gurpreet on 24/09/16.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder>{

    ArrayList<Data> events = new ArrayList<>();
    Context context;

    public DataAdapter(ArrayList<Data> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_element, parent, false);
        return new MyViewHolder(view);    
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Data data = events.get(position);
        holder.title.setText(data.getName());
        holder.challengeType.setText(data.getCategory());
        String image_link = data.getImage().replace("\\", "");
        Uri builder = Uri.parse(image_link).buildUpon().build();
        Picasso.with(context).load(builder).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        
        TextView title, challengeType;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.recyclerView_EventTitle);
            challengeType = (TextView) itemView.findViewById(R.id.recyclerView_ChallengeType);
            imageView = (ImageView) itemView.findViewById(R.id.recyclerView_EventImage);
        }
        
    }
    
}
