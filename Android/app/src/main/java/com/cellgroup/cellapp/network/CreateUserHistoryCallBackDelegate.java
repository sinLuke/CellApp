package com.cellgroup.cellapp.network;

import com.cellgroup.cellapp.models.UserHistory;

public interface CreateUserHistoryCallBackDelegate{
    void handler(UserHistory userHistory);
}