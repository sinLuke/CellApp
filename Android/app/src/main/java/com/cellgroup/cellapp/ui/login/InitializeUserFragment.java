package com.cellgroup.cellapp.ui.login;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cellgroup.cellapp.R;
import com.cellgroup.cellapp.network.UserManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InitializeUserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InitializeUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InitializeUserFragment extends Fragment {

    ImageView profileImageView;
    Button pickProfileImageButton;
    Button takeProfileImageButton;
    Button nextButton;
    Button notMeButton;
    EditText displayNameEditText;
    TextView headline;

    private OnFragmentInteractionListener mListener;

    public InitializeUserFragment() {
        // Required empty public constructor
    }

    public static InitializeUserFragment newInstance() {
        InitializeUserFragment fragment = new InitializeUserFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_initalize_user, container, false);
        profileImageView = v.findViewById(R.id.init_profile_picture);
        pickProfileImageButton = v.findViewById(R.id.init_pick_profile_picture);
        takeProfileImageButton = v.findViewById(R.id.init_take_profile_picture);
        nextButton = v.findViewById(R.id.init_next_button);
        displayNameEditText = v.findViewById(R.id.init_display_name);
        headline = v.findViewById(R.id.init_headline);
        notMeButton = v.findViewById(R.id.init_notMeButton);

        notMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.signOutCurrentUser(getActivity());
            }
        });

        headline.setText(getActivity().getString(R.string.init_headline) + ": " + UserManager.getCurrentUser().getEmail());
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
