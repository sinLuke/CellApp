package com.cellgroup.cellapp.network;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import com.cellgroup.cellapp.R;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.*;
import com.google.android.gms.tasks.*;
import com.google.firebase.auth.*;

import androidx.annotation.NonNull;

public class UserManager {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private int RC_SIGN_IN = 0;

    public static UserManager shared = new UserManager();
    private UserManager(){return;}

    public FirebaseUser getCurrentUser(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser;
    }

    public void CreateUserWithEmailAndPassword(String email, String password, String comformedPassword, final UserManagerCallBackDelegate delegate, Activity activity) {
        if (!checkEmail(email) || email != "") {
            delegate.errorDidOccur("Email not valid");
        }

        if (password != comformedPassword || password == null) {
            delegate.errorDidOccur("Password not match");
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (delegate == null) {return;}
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    delegate.newUserDidFinishCreated(user);
                } else {
                    delegate.errorDidOccur(task.getException().getLocalizedMessage());
                }
            }
        });
    }

    public void SigninUserWithEmailAndPassword(String email, String password, final UserManagerCallBackDelegate delegate, Activity activity) {
        if (!checkEmail(email) || email != "") {
            delegate.errorDidOccur("Email is empty");
        }

        if (password != null) {
            delegate.errorDidOccur("Password is empty");
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (delegate == null) {return;}
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    delegate.userDidSignIn(user);
                } else {
                    delegate.errorDidOccur(task.getException().getLocalizedMessage());
                }
            }
        });
    }

    public void SigninUserWithGoogle(Activity activity) {

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        Intent signInIntent = gsc.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void HandleSigninUserWithGoogleResult(int requestCode, int resultCode, Intent data, final UserManagerCallBackDelegate delegate, Activity activity) {

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account, delegate, activity);
            } catch (ApiException e) {
                delegate.errorDidOccur(task.getException().getLocalizedMessage());
            }
        }
    }

    public void updateUserProfile(String displayName, String profileURL, final UserManagerCallBackDelegate delegate, Activity activity){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest.Builder profileUpdates = new UserProfileChangeRequest.Builder();

        if (displayName != "" && displayName != null) {
            profileUpdates.setDisplayName("displayName");
        }

        if (profileURL != "" && profileURL != null) {
            profileUpdates.setPhotoUri(Uri.parse(profileURL));
        }

        user.updateProfile(profileUpdates.build())
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            delegate.TaskFinished();
                        } else {
                            delegate.errorDidOccur(task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    public void updateUserEmail(String email, final UserManagerCallBackDelegate delegate, Activity activity){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (checkEmail(email) && email != ""){
            user.updateEmail("user@example.com")
                    .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                delegate.TaskFinished();
                            } else {
                                delegate.errorDidOccur(task.getException().getLocalizedMessage());
                            }
                        }
                    });
        }
    }

    public void sendUserEmailVerification(final UserManagerCallBackDelegate delegate, Activity activity){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            delegate.TaskFinished();
                        }else {
                            delegate.errorDidOccur(task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    public void resetUserPassword(String password, String comformedPassword, final UserManagerCallBackDelegate delegate, Activity activity){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (password != comformedPassword || password == null) {
            delegate.errorDidOccur("Password not match");
        }


        user.updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            delegate.TaskFinished();
                        }else {
                            delegate.errorDidOccur(task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    public void sendUserResetPasswordEmail(final UserManagerCallBackDelegate delegate, Activity activity){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        auth.sendPasswordResetEmail(user.getEmail())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            delegate.TaskFinished();
                        }else {
                            delegate.errorDidOccur(task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    public void signOutUser(){
        FirebaseAuth.getInstance().signOut();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct, final UserManagerCallBackDelegate delegate, Activity activity) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            delegate.userDidSignIn(user);
                        } else {
                            delegate.errorDidOccur(task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    private boolean checkEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    interface UserManagerCallBackDelegate {
        void errorDidOccur(String errorMessage);
        void newUserDidFinishCreated(FirebaseUser newUser);
        void userDidSignIn(FirebaseUser user);
        void TaskFinished();
    }
}
