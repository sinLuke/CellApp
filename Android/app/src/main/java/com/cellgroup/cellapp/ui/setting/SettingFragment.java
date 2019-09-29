package com.cellgroup.cellapp.ui.setting;

import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cellgroup.cellapp.R;
import com.cellgroup.cellapp.network.UserManager;
import com.cellgroup.cellapp.ui.ViewHolderCallBackDelegate;


public class SettingFragment extends Fragment {

    private RecyclerView settingRecyclerView;
    private SettingAdapter adapter;
    private boolean mShouldRecieveUserInput;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_setting, container, false);

        settingRecyclerView = v.findViewById(R.id.setting_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        settingRecyclerView.setLayoutManager(layoutManager);
        updateUI();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mShouldRecieveUserInput = true;
        updateUI();
    }

    private void updateUI(){
        if (adapter == null) {
            adapter = new SettingAdapter();
            settingRecyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private class SettingGroupTitleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView groupTitle;

        public SettingGroupTitleHolder(LayoutInflater inflater, ViewGroup parent, ViewHolderCallBackDelegate pCallBackDelegate) {
            super(inflater.inflate(R.layout.list_group_title_setting, parent, false));
            groupTitle = itemView.findViewById(R.id.setting_group_title_text);
        }

        public void bind(int pPosition) {
            switch (pPosition) {
                case 0:
                    groupTitle.setText(R.string.setting_edit_user_profile);
                    break;
                default:
                    groupTitle.setText("");
            }
            itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        @Override
        public void onClick(View v) {
            return;
        }
    }

    private class SettingItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mainTextView;
        private TextView secondaryTextView;
        private TextView middleTextView;
        private ImageView disclosureImage;
        private ViewHolderCallBackDelegate callBackDelegate;
        private int position;
        private int type = 0;

        public SettingItemHolder(LayoutInflater inflater, ViewGroup parent, ViewHolderCallBackDelegate pCallBackDelegate) {
            super(inflater.inflate(R.layout.list_item_setting, parent, false));
            callBackDelegate = pCallBackDelegate;

            mainTextView = itemView.findViewById(R.id.setting_list_main_title);
            secondaryTextView = itemView.findViewById(R.id.setting_list_secondary_title);
            middleTextView = itemView.findViewById(R.id.setting_list_middle_title);
            disclosureImage = itemView.findViewById(R.id.image_disclosure);

            itemView.setOnClickListener(this);
        }

        public void bind(int pPosition) {
            switch (pPosition) {
                case 0:
                    break;
                case 1:
                    setType(1);
                    mainTextView.setText(getActivity().getText(R.string.setting_change_profile_picture));
                    break;
                case 2:
                    setType(0);
                    mainTextView.setText(getActivity().getText(R.string.setting_change_display_name));
                    secondaryTextView.setText(UserManager.getCurrentUser().getDisplayName());
                    break;
                case 3:
                    setType(0);
                    mainTextView.setText(getActivity().getText(R.string.setting_change_email));
                    secondaryTextView.setText(UserManager.getCurrentUser().getEmail());
                    break;
                case 4:
                    setType(1);
                    mainTextView.setText(getActivity().getText(R.string.setting_change_password));
                    break;
                case 5:
                    break;
                case 6:
                    setType(2);
                    middleTextView.setText(getActivity().getText(R.string.setting_erase));
                    break;
                case 7:
                    break;
                default:
                    setType(2);
                    middleTextView.setText(getActivity().getText(R.string.setting_logout));
            }
            itemView.setBackgroundColor(Color.TRANSPARENT);

            position = pPosition;
        }

        private void setType(int type) {
            mainTextView.setText("");
            secondaryTextView.setText("");
            middleTextView.setText("");
            if (type == 0) {
                mainTextView.setVisibility(View.VISIBLE);
                secondaryTextView.setVisibility(View.VISIBLE);
                middleTextView.setVisibility(View.GONE);
                disclosureImage.setVisibility(View.VISIBLE);
            } else if (type == 1) {
                mainTextView.setVisibility(View.VISIBLE);
                secondaryTextView.setVisibility(View.GONE);
                middleTextView.setVisibility(View.GONE);
                disclosureImage.setVisibility(View.VISIBLE);
            } else {
                mainTextView.setVisibility(View.GONE);
                secondaryTextView.setVisibility(View.GONE);
                middleTextView.setVisibility(View.VISIBLE);
                disclosureImage.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            if (callBackDelegate.holderShouldRecieveUserInput()) {
                itemView.setBackgroundColor(Color.GRAY);
                callBackDelegate.holderDidCallFromItemPosition(this, position);
            }
        }
    }

    private class SettingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ViewHolderCallBackDelegate {

        private boolean mShouldRecieveUserInput;

        public SettingAdapter() {
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            if (viewType == 0) {
                return new SettingItemHolder(layoutInflater, parent, this);
            } else {
                return new SettingGroupTitleHolder(layoutInflater, parent, this);
            }

        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            SettingItemHolder settingItemHolder = (SettingItemHolder) holder;
            SettingGroupTitleHolder settingGroupTitleHolder = (SettingGroupTitleHolder) holder;
            holder.bind(position);
            mShouldRecieveUserInput = true;
        }

        @Override
        public int getItemCount() {
            return 9;
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
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
        public boolean holderShouldRecieveUserInput() {
            return mShouldRecieveUserInput;
        }
    }
}