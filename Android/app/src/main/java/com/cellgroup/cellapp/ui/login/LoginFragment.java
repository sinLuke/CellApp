package com.cellgroup.cellapp.ui.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cellgroup.cellapp.AppDelegate;
import com.cellgroup.cellapp.R;
import com.cellgroup.cellapp.network.UserManager;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements UserManager.UserManagerCallBackDelegate {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText conformPasswordEditText;

    private CheckBox createNewUserCheckBox;

    private Button forgetPasswordButton;
    private Button loginButton;
    private Button loginGoogleButton;

    private String email = "";
    private String password = "";
    private String conformPassword = "";

    private static ProgressDialog mDialog;

    public void setCreateNewUser(boolean pCreateNewUser) {
        createNewUser = pCreateNewUser;
        if (!createNewUser) {
            conformPasswordEditText.setVisibility(View.GONE);
            loginButton.setText(R.string.login_button_login);
            forgetPasswordButton.setVisibility(View.VISIBLE);
        } else {
            conformPasswordEditText.setVisibility(View.VISIBLE);
            loginButton.setText(R.string.login_button_signin);
            forgetPasswordButton.setVisibility(View.GONE);
        }
    }

    private boolean createNewUser;

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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

        View v = inflater.inflate(R.layout.fragment_login, container, false);
        forgetPasswordButton = v.findViewById(R.id.forgetPasswordButton);
        loginButton = v.findViewById(R.id.loginButton);
        loginGoogleButton = v.findViewById(R.id.loginGoogleButton);

        createNewUserCheckBox = v.findViewById(R.id.createNewUserCheckBox);

        emailEditText = v.findViewById(R.id.emailEditText);
        passwordEditText = v.findViewById(R.id.passwordEditText);
        conformPasswordEditText = v.findViewById(R.id.passwordConformEditText);

        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);

        forgetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
                if (email == "" || !checkEmail(email)) {
                    Toast.makeText(getActivity(), R.string.login_bad_email, Toast.LENGTH_SHORT).show();
                } else {
                    UserManager.sendUserResetPasswordEmail(email,LoginFragment.this, getActivity());
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (createNewUser) {
                    mDialog.show();
                    UserManager.createUserWithEmailAndPassword(email, password, conformPassword, LoginFragment.this, getActivity());
                } else {
                    mDialog.show();
                    UserManager.loginInUserWithEmailAndPassword(email, password, LoginFragment.this, getActivity());
                }
            }
        });

        loginGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
                UserManager.logInUserWithGoogle(getActivity(), LoginFragment.this);
            }
        });

        createNewUserCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setCreateNewUser(isChecked);
            }
        });

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                return;
            }

            @Override
            public void afterTextChanged(Editable s) {
                email = emailEditText.getText().toString();
                Log.d("email", email);
                return;
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                return;
            }

            @Override
            public void afterTextChanged(Editable s) {
                password = passwordEditText.getText().toString();
                Log.d("password", password);
                return;
            }
        });

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return !createNewUser;
                }
                return false;
            }
        });

        conformPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                return;
            }

            @Override
            public void afterTextChanged(Editable s) {
                conformPassword = conformPasswordEditText.getText().toString();
                Log.d("conformPassword", conformPassword);
                return;
            }
        });
        setCreateNewUser(false);

        conformPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UserManager.handleSigninUserWithGoogleResult(requestCode, resultCode, data, this, getActivity());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void errorDidOccur(String errorMessage) {
        mDialog.hide();
        Toast.makeText(this.getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void newUserDidFinishCreated(FirebaseUser newUser) {
        UserManager.getUserManager(newUser, getActivity());
    }

    @Override
    public void userDidLogedIn(FirebaseUser user) {
        UserManager.getUserManager(user, getActivity());
    }

    @Override
    public void TaskFinished(String conformMessage) {
        mDialog.hide();
        Toast.makeText(this.getActivity(), conformMessage, Toast.LENGTH_SHORT).show();
        return;
    }

    private static boolean checkEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
