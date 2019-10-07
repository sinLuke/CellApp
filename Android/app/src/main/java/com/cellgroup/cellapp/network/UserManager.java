package com.cellgroup.cellapp.network;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.cellgroup.cellapp.AppDelegate;
import com.cellgroup.cellapp.R;
import com.cellgroup.cellapp.models.Doc;
import com.cellgroup.cellapp.models.DocumentCompleteRate;
import com.cellgroup.cellapp.models.Step;
import com.cellgroup.cellapp.models.UserHistory;
import com.cellgroup.cellapp.ui.login.InitializeUserActivity;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.*;
import com.google.android.gms.tasks.*;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.SortedList;

public class UserManager implements UserHistoryUpdateDelegate {

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static GoogleSignInOptions gso;
    private static GoogleSignInClient gsc;
    private static int RC_SIGN_IN = 0;
    public static UserManager shared;
    private static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private int currentUserScopte = 0;
    private Map<String, UserHistory> userHistory;
    private Set<Doc> existDocs = new HashSet();

    private UserManager(int scope) {
        currentUserScopte = scope;
    }

    public static FirebaseUser getCurrentUser(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser;
    }

    public int getCurrentUserScope(){
        return currentUserScopte;
    }

    public Date getLastViewDateOfDocument(Doc doc) {
        Date maxDate = null;
        for (Step step: doc.steps.values()) {
            UserHistory thisUserHistory = this.userHistory.get(step.id);
            if (thisUserHistory != null) {
                Date lastViewed = this.userHistory.get(step.id).lastViewd;
                if (maxDate == null || lastViewed.after(maxDate)) {
                    maxDate = lastViewed;
                }
            }
        }
        return maxDate;
    }

    public DocumentCompleteRate getCompleteRateOfDocument(Doc doc) {
        int StepCount = 0;
        int CompletionCount = 0;
        for (Step step: doc.steps.values()) {
            StepCount += 1;
            UserHistory thisUserHistory = this.userHistory.get(step.id);
            if (isStepCompleted(step)) {
                CompletionCount += 1;
            }
        }

        return new DocumentCompleteRate(StepCount, CompletionCount);
    }

    public boolean isStepCompleted(Step step) {
        UserHistory thisUserHistory = this.userHistory.get(step.id);
        if (thisUserHistory != null) {
            return thisUserHistory.finished;
        }
        return false;
    }

    public boolean isDocumentCompleted(Doc doc){
        DocumentCompleteRate rate = getCompleteRateOfDocument(doc);
        return rate.isFinished();
    }

    public boolean isDocumentExistInUserHistory(Doc doc){
        return existDocs.contains(doc);
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

    public static void signOutCurrentUser(Context activity){
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

    public static void getUserManager(final FirebaseUser user, final Context activity){
        DocumentReference userRef = firebaseFirestore.collection("Users").document(user.getUid());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> pTask) {
                if (pTask.isSuccessful()) {
                    DocumentSnapshot document = pTask.getResult();
                    if (document.exists()) {
                        int userScope = FirestoreCoder.getDataFromUser(document.getData());
                        shared = new UserManager(userScope);
                        shared.willUpdateUserHistory(activity, shared);
                    } else {
                        DocumentReference userRef = firebaseFirestore.collection("Users").document(user.getUid());
                        userRef.set(FirestoreCoder.setDataByUser(10));
                        shared = new UserManager(10);
                        shared.willUpdateUserHistory(activity, shared);
                    }
                } else {
                    AppDelegate.shared.applicationDidReportException("Firestore get failed with " + pTask.getException());
                }
            }
        });
    }

    public void willUpdateUserHistory(final Context activity, final UserHistoryUpdateDelegate delegate){
        CollectionReference userHistoryRef = firebaseFirestore.collection("UserHistory");
        userHistoryRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> data = document.getData();
                        String id = document.getId();
                        try {
                            UserHistory userHistory = new UserHistory(data, id);
                            Step step = userHistory.step.get();
                            if (step != null) {
                                Doc doc = step.doc.get();
                                existDocs.add(doc);
                                UserManager.shared.userHistory.put(id, userHistory);
                            }

                        } catch (Exception e) {

                        }
                    }

                    delegate.didUpdateUserHistory(activity);
                } else {
                    NetworkManager.shared.networkManagerDidDownloadData("Error When Downloading User Historys");
                }
            }
        });
    }

    @Override
    public void didUpdateUserHistory(Context activity) {
        AppDelegate.shared.applicationDidFinisheLogin(activity, UserManager.shared);
    }

    //handling user first login
    public void checkIfUserFirstLogin(Context activity) {
        Uri PhotoUrl = getCurrentUser().getPhotoUrl();
        if (getCurrentUser().getDisplayName() == "" || getCurrentUser().getPhotoUrl() == null) {
            Intent i = new Intent(activity, InitializeUserActivity.class);
            activity.startActivity(i);
        } else {
            checkIfUserEmailVarified(activity);
        }
    }

    public void checkIfUserEmailVarified(final Context activity) {
        if (getCurrentUser().isEmailVerified()) {
            AppDelegate.shared.applicationDidInitializeUser(activity, this);
        } else {
            UserManager.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(activity);
                        dlgAlert.setMessage("A email has sent to your email address");
                        dlgAlert.setTitle("Email Verification");
                        dlgAlert.setPositiveButton("I receive the email",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        UserManager.shared.checkIfUserEmailVarified(activity);
                                    }
                                });
                        dlgAlert.setNegativeButton("Sign out",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        AppDelegate.shared.applicationDidReportException("Email is not verified");
                                    }
                                });
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                    }
                }
            });
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
