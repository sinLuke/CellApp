package com.cellgroup.cellapp.ui.topics;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cellgroup.cellapp.models.Topic;

public class TopicsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TopicsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("show all topics");
    }

    public void configTopicModelwithTopic(Topic topic) {
        mText.setValue(topic.topicName);
    }

    public LiveData<String> getText() {
        return mText;
    }
}