package com.cellgroup.cellapp.ui;

import androidx.recyclerview.widget.RecyclerView;

public interface ViewHolderCallBackDelegate {
    void holderDidCallFromItemPosition(RecyclerView.ViewHolder holder, int position);
    boolean holderShouldRecieveUserInput();
}