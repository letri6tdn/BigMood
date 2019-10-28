package edu.ualberta.cmput301f19t17.bigmood.fragment.ui.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import edu.ualberta.cmput301f19t17.bigmood.R;
import edu.ualberta.cmput301f19t17.bigmood.adapter.MoodAdapter;
import edu.ualberta.cmput301f19t17.bigmood.fragment.dialog.DefineMoodDialogFragment;
import edu.ualberta.cmput301f19t17.bigmood.fragment.dialog.ViewMoodDialogFragment;
import edu.ualberta.cmput301f19t17.bigmood.fragment.dialog.ViewUserMoodDialogFragment;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;

public class UserMoodsFragment extends Fragment {
    private ArrayList<Mood> moodList;
    private ArrayAdapter<Mood> moodAdapter;

    private UserMoodsViewModel userMoodsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        userMoodsViewModel = ViewModelProviders.of(this).get(UserMoodsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_user_moods, container, false);

//        final TextView textView = root.findViewById(R.id.text_user_moods);

//        userMoodsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        FloatingActionButton fab = root.findViewById(R.id.floatingActionButton);

        final ListView moodListView = root.findViewById(R.id.mood_list);

        moodList = new ArrayList<>();
        moodAdapter = new MoodAdapter(root.getContext(), R.layout.mood_item, moodList);
        moodListView.setAdapter(moodAdapter);

        //TODO Cameron 10-26-2019 remove canned data
        final Mood mockMood1 = new Mood("2019-07-25", "12:24", "Sad");
        final Mood mockMood2 = new Mood("2019-10-20", "11:11", "Happy");
        moodList.add(mockMood1);
        moodList.add(mockMood2);
        moodAdapter.notifyDataSetChanged();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DefineMoodDialogFragment fragment = DefineMoodDialogFragment.newInstance();
                fragment.setOnButtonPressListener(
                        new DefineMoodDialogFragment.OnButtonPressListener() {
                            @Override
                            public void onSavePressed(Mood mood) {
                                //TODO add mood to moodlist
                                Log.d("Save Pressed", "Adding Mood");
                                moodList.add(mood);
                                moodAdapter.notifyDataSetChanged();
                            }
                        });
                fragment.show(getFragmentManager(), "DEFINE_MOOD_FRAGMENT_ADD");

            }
        });


        moodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ViewUserMoodDialogFragment fragment = ViewUserMoodDialogFragment.newInstance(moodList.get(i));
                final int finalIndex = i;
                fragment.setOnButtonPressListener(new ViewUserMoodDialogFragment.OnButtonPressListener() {
                    @Override
                    public void onDeletePressed() {

                        moodList.remove(finalIndex);
                        moodAdapter.notifyDataSetChanged();

                        Toast.makeText(getContext(), "The Mood was deleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onEditPressed() {
                        final Mood moodToEdit = moodList.get(finalIndex);

                        DefineMoodDialogFragment defineFragment = DefineMoodDialogFragment.newInstance(moodToEdit);
                        defineFragment.setOnButtonPressListener(
                                        new DefineMoodDialogFragment.OnButtonPressListener() {
                                    @Override
                                    public void onSavePressed(Mood mood) {
                                        //TODO Cameron Oct 28, 2019 add location and image
                                        moodToEdit.setState(mood.getState());
                                        moodToEdit.setReason(mood.getReason());
                                        moodToEdit.setSituation(mood.getSituation());
                                    }
                                });
                        defineFragment.show(getFragmentManager(), "DEFINE_MOOD_FRAGMENT_EDIT");

                    }
                });

                fragment.show(getFragmentManager(), "VIEW_MOOD_FRAGMENT");
            }
        });

        return root;
    }
}