package gurpreetsk.me.toppr.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;

import java.util.ArrayList;

import gurpreetsk.me.toppr.R;
import gurpreetsk.me.toppr.model.Data;

public class DemographicsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demographics);

        this.setTitle("Demographic Chart");

        int hiring = 0, hackathon = 0, bot = 0;

        ArrayList<Data> dataList = getIntent().getParcelableArrayListExtra("DATA");

        for (Data data : dataList) {
            switch (data.getCategory().toLowerCase()) {
                case "hackathon":
                    hackathon++;
                    break;
                case "bot":
                    bot++;
                    break;
                case "hiring":
                    hiring++;
                    break;
            }
        }

        PieGraph pg = (PieGraph) findViewById(R.id.graph);

        PieSlice slice = new PieSlice();
        slice.setColor(getResources().getColor(R.color.color1));
        slice.setSelectedColor(getResources().getColor(R.color.colorAccent));
        slice.setValue(hackathon);
        slice.setTitle("Hackathon");
        pg.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(getResources().getColor(R.color.color2));
        slice.setValue(hiring);
        slice.setTitle("Hiring");
        pg.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(getResources().getColor(R.color.color3));
        slice.setValue(bot);
        slice.setTitle("Bot");
        pg.addSlice(slice);

    }
}
