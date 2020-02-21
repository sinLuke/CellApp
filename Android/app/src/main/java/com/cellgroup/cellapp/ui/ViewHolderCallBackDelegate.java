package com.cellgroup.cellapp.ui;

import androidx.recyclerview.widget.RecyclerView;

public interface ViewHolderCallBackDelegate {
    void holderOnClickFromItemPosition(RecyclerView.ViewHolder holder, int position);
    void holderOnClickSendingObject(RecyclerView.ViewHolder holder, Object object);
    boolean holderShouldRecieveUserInput();
}