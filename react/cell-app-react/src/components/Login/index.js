import React from "react";
import StyledFirebaseAuth from "react-firebaseui/StyledFirebaseAuth";
import firebase from "firebase";
import { Redirect } from "react-router-dom";
import { Header, Grid } from "semantic-ui-react";

class Login extends React.Component {
  constructor(props) {
    super(props);
    this.state = { user: firebase.auth().currentUser };
    firebase.auth().onAuthStateChanged(
      function(user) {
        if (user) {
          console.log("set user", user);
          this.setState({ user: user });
        } else {
          console.log("set user", null);
          this.setState({ user: null });
        }
      }.bind(this)
    );
  }
  render() {
    if (this.state.user) {
      return <Redirect to="/home" />;
    }

    const uiConfig = {
      signInFlow: "popup",
      callbacks: {
        signInSuccessWithAuthResult: function(authResult, redirectUrl) {
          return false;
        }
      },

      signInOptions: [
        firebase.auth.GoogleAuthProvider.PROVIDER_ID,
        firebase.auth.EmailAuthProvider.PROVIDER_ID
      ]
    };

    return (
      <div>
        <Grid container stackable verticalAlign="top">
          <Grid.Row>
            <Grid.Column width={4} />
            <Grid.Column width={8}>
              <Header
                size="huge"
                style={{
                  fontWeight: "normal",
                  marginBottom: 0,
                  marginTop: "2em",
                  marginLeft: "2em",
                  marginBottom: "2em"
                }}
              >
                Login
              </Header>

              <StyledFirebaseAuth
                uiConfig={uiConfig}
                firebaseAuth={firebase.auth()}
              />
            </Grid.Column>
            <Grid.Column width={4} />
          </Grid.Row>
        </Grid>
      </div>
    );
  }
}

export default Login;
