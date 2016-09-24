package gurpreetsk.me.toppr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gurpreet on 24/09/16.
 */

public class Data implements Parcelable {

    String id, name, image, category, description, experience;

    public Data(String id, String name, String image, String category, String description, String experience) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.category = category;
        this.description = description;
        this.experience = experience;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public Data(Parcel in) {
        String[] data = new String[6];
        in.readStringArray(data);
        this.id = data[0];
        this.name = data[1];
        this.image = data[2];
        this.category = data[3];
        this.description = data[4];
        this.experience = data[5];
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.id
                , this.name
                , this.image
                , this.category
                , this.description
                , this.experience});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

}
