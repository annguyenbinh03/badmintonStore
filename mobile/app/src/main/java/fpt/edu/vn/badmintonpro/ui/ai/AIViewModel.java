package fpt.edu.vn.badmintonpro.ui.ai;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AIViewModel extends ViewModel {

    private final MutableLiveData<String> mTitle;

    public AIViewModel() {
        mTitle = new MutableLiveData<>();
        mTitle.setValue("AI Assistant is ready");
    }

    public LiveData<String> getTitle() {
        return mTitle;
    }
}
