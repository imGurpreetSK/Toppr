package gurpreetsk.me.toppr.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gurpreetsk.me.toppr.R;
import gurpreetsk.me.toppr.model.Data;
import gurpreetsk.me.toppr.model.Database;

/**
 * Created by Gurpreet on 25/09/16.
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>{

    ArrayList<Data> events = new ArrayList<>();
    Context context;

    public FavoriteAdapter(ArrayList<Data> events, Context context) {
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
        final Data data = events.get(position);
        holder.title.setText(data.getName());
        holder.challengeType.setText(data.getCategory());
        String image_link = data.getImage().replace("\\", "");
        Uri builder = Uri.parse(image_link).buildUpon().build();
        Picasso.with(context).load(builder).into(holder.imageView);
        if(data.getFav().equals("true"))
            holder.like.setLiked(true);
        else holder.like.setLiked(false);
        holder.like.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Database handler= new Database(context);
                handler.update("true", data.getId());
                data.setFav("true");
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Database handler= new Database(context);
                handler.update("false", data.getId());
                data.setFav("false");
            }
        });

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title, challengeType;
        ImageView imageView;
        CardView cardView;
        LikeButton like;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.mainCardView);
            title = (TextView) itemView.findViewById(R.id.recyclerView_EventTitle);
            challengeType = (TextView) itemView.findViewById(R.id.recyclerView_ChallengeType);
            imageView = (ImageView) itemView.findViewById(R.id.recyclerView_EventImage);
            like = (LikeButton)itemView.findViewById(R.id.fav_btn);
        }

    }

}
