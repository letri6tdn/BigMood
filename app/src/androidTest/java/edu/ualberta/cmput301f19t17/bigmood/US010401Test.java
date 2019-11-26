package edu.ualberta.cmput301f19t17.bigmood;

import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;
import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.database.MockRepository;
import edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState;
import edu.ualberta.cmput301f19t17.bigmood.model.SocialSituation;

import static org.junit.Assert.assertEquals;

public class US010401Test {
    private Solo solo;
    private static AppPreferences appPreferences;
    private static MockRepository mockRepository;

    @BeforeClass
    public static void setRepository() {

        // Set app preferences
        US010401Test.appPreferences = AppPreferences.getInstance();

        // Create new in-memory database and set the app preferences to use it
        US010401Test.mockRepository = new MockRepository();
        US010401Test.appPreferences.setRepository(US010401Test.mockRepository);

        // Login with a user from the database using a specialized method in MockRepository
        US010401Test.appPreferences.login(US010401Test.mockRepository.getUser("user1"));

    }
    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before //Clears the mood list before each test
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        appPreferences = AppPreferences.getInstance();
        solo.sleep(1000);
    }

    @Test
    public void checkEditMood() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        ListAdapter moodArrayAdapter = ((ListView) solo.getView(R.id.mood_list)).getAdapter();
        int originalNumListItems = moodArrayAdapter.getCount();

        //select mood3
        solo.clickOnMenuItem("Happy");

        solo.clickOnButton("EDIT");
        solo.waitForText("Edit Mood", 1, 1000); //make sure DefineMoodDialogFragment opens itself correctly as a "Edit" rather than "Add"

        solo.pressSpinnerItem(0, EmotionalState.SADNESS.getStateCode()); //sad
        solo.pressSpinnerItem(3, SocialSituation.CROWD.getSituationCode()+1); //crowd

        solo.clickOnView(solo.getView(R.id.action_save));

        //make sure the edit worked by checking that the SADNESS shows up
        solo.waitForText(EmotionalState.SADNESS.toString(), 1, 1000);

        //make sure no new items were added, and no items deleted
        solo.sleep(2000);
        assertEquals(originalNumListItems, moodArrayAdapter.getCount());

    }

}

