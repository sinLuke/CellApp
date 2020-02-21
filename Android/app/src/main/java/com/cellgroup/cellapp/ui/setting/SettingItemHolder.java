package com.cellgroup.cellapp.ui.setting;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cellgroup.cellapp.Print;
import com.cellgroup.cellapp.R;
import com.cellgroup.cellapp.network.UserManager;
import com.cellgroup.cellapp.ui.ViewHolderCallBackDelegate;

import androidx.recyclerview.widget.RecyclerView;

public class SettingItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Context mActivity;
    private TextView mainTextView;
    private TextView secondaryTextView;
    private TextView middleTextView;
    private ImageView disclosureImage;
    private View backgroundView;
    private ViewHolderCallBackDelegate callBackDelegate;
    private int position;
    private int type = 0;

    public SettingItemHolder(LayoutInflater inflater, ViewGroup parent, ViewHolderCallBackDelegate pCallBackDelegate) {
        super(inflater.inflate(R.layout.list_item_setting, parent, false));
        callBackDelegate = pCallBackDelegate;
        backgroundView = itemView.findViewById(R.id.setting_list_background_view);
        mainTextView = itemView.findViewById(R.id.setting_list_main_title);
        secondaryTextView = itemView.findViewById(R.id.setting_list_secondary_title);
        middleTextView = itemView.findViewById(R.id.setting_list_middle_title);
        disclosureImage = itemView.findViewById(R.id.image_disclosure);
        itemView.setOnClickListener(this);
    }

    public void bind(int pPosition, Context activity) {
        mActivity = activity;
        backgroundView.setBackgroundColor(mActivity.getColor(R.color.systemBackground));
        switch (pPosition) {
            case 0:
                break;
            case 1:
                setType(1);
                mainTextView.setText(activity.getText(R.string.setting_change_profile_picture));
                break;
            case 2:
                setType(0);
                mainTextView.setText(activity.getText(R.string.setting_change_display_name));
                secondaryTextView.setText(UserManager.getCurrentUser().getDisplayName());
                break;
            case 3:
                setType(0);
                mainTextView.setText(activity.getText(R.string.setting_change_email));
                secondaryTextView.setText(UserManager.getCurrentUser().getEmail());
                break;
            case 4:
                setType(1);
                mainTextView.setText(activity.getText(R.string.setting_change_password));
                break;
            case 5:
                break;
            case 6:
                setType(2);
                middleTextView.setText(activity.getText(R.string.setting_erase));
                break;
            case 7:
                break;
            default:
                setType(2);
                middleTextView.setText(activity.getText(R.string.setting_logout));
        }

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
        Print.print("OnClick");
        if (callBackDelegate.holderShouldRecieveUserInput()) {
            Print.print("holderShouldRecieveUserInput");
            backgroundView.setBackgroundColor(mActivity.getColor(R.color.systemFill));
            callBackDelegate.holderOnClickFromItemPosition(this, position);
        }
    }
}