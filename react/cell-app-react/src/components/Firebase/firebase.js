import app from "firebase/app";
import "firebase/auth";

const config = {
  apiKey: "AIzaSyBM-C_d1o-gVCFRQykdbBI4svOSed8JbDY",
  authDomain: "cell-app.firebaseapp.com",
  databaseURL: "https://cell-app.firebaseio.com",
  projectId: "cell-app",
  storageBucket: "cell-app.appspot.com",
  messagingSenderId: "854633007097",
  appId: "1:854633007097:web:0f1cadc2d35c247e2fdf28",
  measurementId: "G-3ZZ2QEG61Z"
};

class Firebase {
  constructor() {
    app.initializeApp(config);
    this.auth = app.auth();
  }

  // *** Auth API ***
  doSignInWithEmailAndPassword = (email, password) =>
    this.auth.signInWithEmailAndPassword(email, password);

  doSignOut = () => this.auth.signOut();
}
export default Firebase;
