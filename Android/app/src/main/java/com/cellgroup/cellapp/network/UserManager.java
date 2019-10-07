package com.cellgroup.cellapp.network;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.cellgroup.cellapp.AppDelegate;
import com.cellgroup.cellapp.R;
import com.cellgroup.cellapp.ui.login.InitializeUserActivity;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.*;
import com.google.android.gms.tasks.*;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class UserManager {

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static GoogleSignInOptions gso;
    private static GoogleSignInClient gsc;
    private static int RC_SIGN_IN = 0;
    private static UserManager shared;
    private static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private int currentUserScopte = 0;

    private UserManager(int scope) {
        currentUserScopte = scope;
    }

    public static FirebaseUser getCurrentUser(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser;
    }

    public FirebaseUser getCurrentUserScope(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser;
    }

    public static void createUserWithEmailAndPassword(String email, String password, String comformedPassword, final UserManagerCallBackDelegate delegate, Activity activity) {
        if (!checkEmail(email) || email == "") {
            delegate.errorDidOccur(activity.getString(R.string.login_bad_email));
            return;
        }

        if (!password.equals(comformedPassword) || password == "") {
            delegate.errorDidOccur(activity.getString(R.string.login_bad_password));
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (delegate == null) {return;}
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    delegate.newUserDidFinishCreated(user);
                } else {
                    delegate.errorDidOccur(task.getException().getMessage());
                }
            }
        });
    }

    public static void loginInUserWithEmailAndPassword(String email, String password, final UserManagerCallBackDelegate delegate, Activity activity) {
        if (!checkEmail(email) || email == "") {
            delegate.errorDidOccur(activity.getString(R.string.login_bad_email));
            return;
        }

        if (password == "") {
            delegate.errorDidOccur(activity.getString(R.string.login_empty_password));
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (delegate == null) {return;}
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    delegate.userDidLogedIn(user);
                } else {
                    delegate.errorDidOccur(task.getException().getMessage());
                }
            }
        });
    }

    public static void logInUserWithGoogle(Activity activity, Fragment f) {

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(activity, gso);
        Intent signInIntent = gsc.getSignInIntent();
        f.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public static void handleSigninUserWithGoogleResult(int requestCode, int resultCode, Intent data, final UserManagerCallBackDelegate delegate, Activity activity) {

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account, delegate, activity);
            } catch (ApiException e) {
                delegate.errorDidOccur(task.getException().getMessage());
            }
        }
    }

    public void updateUserProfile(String displayName, String profileURL, final UserManagerCallBackDelegate delegate, final Activity activity){
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
                            delegate.TaskFinished(activity.getString(R.string.user_manager_profile_update));
                        } else {
                            delegate.errorDidOccur(task.getException().getMessage());
                        }
                    }
                });
    }

    public void updateUserEmail(String email, final UserManagerCallBackDelegate delegate, final Activity activity){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (checkEmail(email) && email != ""){
            user.updateEmail("user@example.com")
                    .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                delegate.TaskFinished(activity.getString(R.string.user_manager_email_update));
                            } else {
                                delegate.errorDidOccur(task.getException().getMessage());
                            }
                        }
                    });
        }
    }

    public void sendUserEmailVerification(final UserManagerCallBackDelegate delegate, final Activity activity){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            delegate.TaskFinished(activity.getString(R.string.user_manager_email_verify));
                        }else {
                            delegate.errorDidOccur(task.getException().getMessage());
                        }
                    }
                });
    }

    public void resetUserPassword(String password, String comformedPassword, final UserManagerCallBackDelegate delegate, final Activity activity){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (password != comformedPassword || password == null) {
            delegate.errorDidOccur("Password not match");
        }


        user.updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            delegate.TaskFinished(activity.getString(R.string.user_manager_password_reset));
                        }else {
                            delegate.errorDidOccur(task.getException().getMessage());
                        }
                    }
                });
    }

    public static void sendUserResetPasswordEmail(String email, final UserManagerCallBackDelegate delegate, final Activity activity){

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            delegate.TaskFinished(activity.getString(R.string.user_manager_email_reset));
                        }else {
                            delegate.errorDidOccur(task.getException().getMessage());
                        }
                    }
                });
    }

    public static void signOutCurrentUser(Activity activity){
        FirebaseAuth.getInstance().signOut();
        AppDelegate.shared.applicationDidlaunched(activity);
    }

    private static void firebaseAuthWithGoogle(GoogleSignInAccount acct, final UserManagerCallBackDelegate delegate, Activity activity) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            delegate.userDidLogedIn(user);
                        } else {
                            delegate.errorDidOccur(task.getException().getMessage());
                        }
                    }
                });
    }

    public static void getUserManager(final FirebaseUser user, final Activity activity){
        DocumentReference userRef = firebaseFirestore.collection("Users").document(user.getUid());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> pTask) {
                if (pTask.isSuccessful()) {
                    DocumentSnapshot document = pTask.getResult();
                    if (document.exists()) {
                        int userScope = FirestoreCoder.getDataFromUser(document.getData());
                        shared = new UserManager(userScope);
                        AppDelegate.shared.applicationDidFinisheLogin(activity, shared);
                    } else {
                        DocumentReference userRef = firebaseFirestore.collection("Users").document(user.getUid());
                        userRef.set(FirestoreCoder.setDataByUser(10));
                        shared = new UserManager(10);
                        AppDelegate.shared.applicationDidFinisheLogin(activity, shared);
                    }
                } else {
                    AppDelegate.shared.applicationDidReportException("Firestore get failed with " + pTask.getException());
                }
            }
        });
    }


    //handling user first login
    public void checkIfUserFirstLogin(Activity activity) {
        Uri PhotoUrl = getCurrentUser().getPhotoUrl();
        if (getCurrentUser().getDisplayName() == "" || getCurrentUser().getPhotoUrl() == null) {
            Intent i = new Intent(activity, InitializeUserActivity.class);
            activity.startActivity(i);
        } else {
            checkIfUserEmailVarified(activity);
        }
    }

    public void checkIfUserEmailVarified(Activity activity) {
        if (getCurrentUser().isEmailVerified()) {
            AppDelegate.shared.applicationDidInitializeUser(activity, this);
        }
    }

    private static boolean checkEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public interface UserManagerCallBackDelegate {
        void errorDidOccur(String errorMessage);
        void newUserDidFinishCreated(FirebaseUser newUser);
        void userDidLogedIn(FirebaseUser user);
        void TaskFinished(String conformMessage);
    }
}
