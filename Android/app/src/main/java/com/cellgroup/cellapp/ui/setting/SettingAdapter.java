package com.cellgroup.cellapp.ui.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cellgroup.cellapp.network.UserManager;
import com.cellgroup.cellapp.ui.ViewHolderCallBackDelegate;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SettingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ViewHolderCallBackDelegate {

    private boolean mShouldRecieveUserInput;
    private Context activity;

    private Context getActivity(){
        return activity;
    }

    public SettingAdapter(Context activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        if (viewType == 1) {
            return new SettingItemHolder(layoutInflater, parent, this);
        } else {
            return new SettingGroupTitleHolder(layoutInflater, parent, this);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getClass().isAssignableFrom(SettingItemHolder.class)) {
            SettingItemHolder settingItemHolder = (SettingItemHolder) holder;
            settingItemHolder.bind(position, getActivity());
        } else if (holder.getClass().isAssignableFrom(SettingGroupTitleHolder.class)) {
            SettingGroupTitleHolder settingGroupTitleHolder = (SettingGroupTitleHolder) holder;
            settingGroupTitleHolder.bind(position, getActivity());
        }


        mShouldRecieveUserInput = true;
    }

    @Override
    public int getItemCount() {
        return 9;
    }

    @Override
    public int getItemViewType(int position) {
        if( Arrays.asList(0, 5, 7).contains(position) ) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void holderDidCallFromItemPosition(RecyclerView.ViewHolder holder, int position) {

        switch (position) {
            case 0:
                break;
            case 1:
//                    setType(1);
//                    mainTextView.setText(getActivity().getText(R.string.setting_change_profile_picture));
                break;
            case 2:
//                    setType(0);
//                    mainTextView.setText(getActivity().getText(R.string.setting_change_display_name));
//                    secondaryTextView.setText(UserManager.getCurrentUser().getDisplayName());
                break;
            case 3:
//                    setType(0);
//                    mainTextView.setText(getActivity().getText(R.string.setting_change_email));
//                    secondaryTextView.setText(UserManager.getCurrentUser().getEmail());
                break;
            case 4:
//                    setType(1);
//                    mainTextView.setText(getActivity().getText(R.string.setting_change_password));
                break;
            case 5:
                break;
            case 6:
//                    setType(2);
//                    middleTextView.setText(getActivity().getText(R.string.setting_erase));
                break;
            case 7:
                break;
            default:
                UserManager.signOutCurrentUser(getActivity());
        }
        mShouldRecieveUserInput = false;
    }

    @Override
    public void holderDidCallSendingObject(RecyclerView.ViewHolder holder, Object object) {
        return;
    }

    @Override
    public boolean holderShouldRecieveUserInput() {
        return mShouldRecieveUserInput;
    }
}