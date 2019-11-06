package edu.ualberta.cmput301f19t17.bigmood.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import edu.ualberta.cmput301f19t17.bigmood.R;
import edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;

/**
 * This class serves as a custom ArrayAdapter specifically for Moods.
 * This Adapter does the following:
 * 1) Stores a collection of Ride objects in tandem with the ArrayList passed into its constructor.
 * 2) Inflates the different aspects of the row layout that are defined.
 */
public class MoodAdapter extends ArrayAdapter<Mood> implements Filterable {
    private final int resource;
    private ArrayList<Mood> arrayMoodList;
    private ArrayList<Mood> originalArrayMood;

    /**
     * This constructor is used to create a new MoodAdapter
     * @param context the activity that the MoodAdapter is created in
     * @param resource the ID of the layout resource that getView() would inflate to create the view
     * @param moodList the list of moods
     */
    public MoodAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Mood> moodList) {
        super(context, resource, moodList);
        this.resource=resource;
        this.arrayMoodList = moodList;
        this.originalArrayMood = moodList;
    }

    @Override
    public Mood getItem(int position){
        return arrayMoodList.get(position);
    }


    /**
     *  This method gets called when a row is either being created or re-created (recycled).
     *  Since findViewByIds can be expensive especially in a large list,
     *  we cache the TextView objects in a small holder class we've defined below.
     * @param position the position of the view we are creating? TODO Cameron 10-26-2019 research position
     * @param convertView this is the view that we receive if the view is being recycled
     * @param parent the parent ViewGroup that the view is contained within (Eg. LinearLayout)
     * @return convertView, which is either the recycled view, or the newly created/inflated view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //This MoodHolder will hold our views while we create them
        MoodHolder moodHolder = new MoodHolder();

        // We test if convertView is null so we can know if we have to inflate it or not (findViewById)
        if (convertView == null) {
            
            // Define new inflater and inflate the view.
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resource, parent, false);
            
            // Create new holder object since we are in a part of execution where the row has not been cached yet.
            moodHolder = new MoodHolder();

            // Set all fields of the holder class
            moodHolder.date = convertView.findViewById(R.id.mood_item_date);
            moodHolder.time = convertView.findViewById(R.id.mood_item_time);
            moodHolder.state = convertView.findViewById(R.id.mood_item_state);
            moodHolder.image = convertView.findViewById(R.id.mood_item_emoticon);

            // Cache views for that row using setTag on the full row view
            convertView.setTag(moodHolder);

        } else {

            // The row has been created and we can reuse it, but to change the fields in
            // the row we need to pull the holder from cache using getTag
            moodHolder = (MoodHolder) convertView.getTag();

        }

        // Get the current ride in the array using methods in ArrayAdapter
        Mood currentMood = this.getItem(position);

        // Set each of the fields in the row. For the date and time, we get the already formatted string from the Ride object. For the distance we do some manual formatting with the distance data.

        Date date = currentMood.getDatetime().getTime();
        
        moodHolder.state.setText(currentMood.getState().toString());
        moodHolder.date.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).format(date));
        moodHolder.time.setText(new SimpleDateFormat("HH:mm", Locale.CANADA).format(date));

        // Set image based on enum
        Resources res = this.getContext().getResources();
        Drawable emoticon = res.getDrawable(currentMood.getState().getDrawableId());
        moodHolder.image.setImageDrawable(emoticon);

        // Return the created/reused view as per the method signature
        return convertView;
    }

    /**
     * This class is a small helper class to cache the views taken from
     * convertView.findViewById() since these finds can be expensive when in a ListView.
     * It just holds TextView resources we'll get and set in this class only.
     */
    private static class MoodHolder {
        //TODO Cameron 10-26-2019 implement location and image?
        TextView date;
        TextView time;
        TextView state;
        ImageView image;
    }

    /**
     * This class implements Filterable to enable filtering mood list by emotional state
     */
    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Mood> filteredMoodList = new ArrayList<Mood>();



                if (constraint.toString() == "None" || constraint.toString().length() == 0 || constraint == null) {
                    results.count = originalArrayMood.size();
                    results.values = originalArrayMood;
                } else {
                    for (int i = 0; i < originalArrayMood.size(); i++) {
                        Mood currentMood = originalArrayMood.get(i);
                        EmotionalState emotionalState = currentMood.getState();
                        String state = emotionalState.toString();
                        if (state.startsWith(constraint.toString())) {
                            filteredMoodList.add(currentMood);
                        }
                    }
                    results.count = filteredMoodList.size();
                    results.values = filteredMoodList;
                }
                return results;

            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results!=null && results.values!=null) {
                    arrayMoodList = (ArrayList<Mood>) results.values;
                    notifyDataSetChanged();
                } else {
                    arrayMoodList = originalArrayMood;
                }
            }

        };
        return filter;
    }
}


