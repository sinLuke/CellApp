package com.cellgroup.cellapp.network;

import com.cellgroup.cellapp.models.*;

import java.util.List;
import java.util.Map;

public class NetworkManager {
    private List<Topic> topics;
    private Map<String, List<Doc>> documents;

    public static NetworkManager shared = new NetworkManager();
    private NetworkManager(){return;}

    public List<Topic> getAllTopics() {
        return topics;
    }

    public List<Doc> getDocumentsForTopic(String topicID) {
        return documents.get(topicID);
    }
}
