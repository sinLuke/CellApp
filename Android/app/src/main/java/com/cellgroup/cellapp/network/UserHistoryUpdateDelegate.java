package com.cellgroup.cellapp.network;

import android.content.Context;

import com.cellgroup.cellapp.models.UserHistory;

public interface UserHistoryUpdateDelegate {
    void didUpdateUserHistory(Context activity);
}