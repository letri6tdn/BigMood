package edu.ualberta.cmput301f19t17.bigmood.fragment.ui.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserMoodsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UserMoodsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("User Moods fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}