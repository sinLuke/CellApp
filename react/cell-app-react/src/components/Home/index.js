import React from "react";
import firebase from "firebase";
import TopicTable from "./topicTable";
import { Redirect } from "react-router-dom";
import { Grid } from "semantic-ui-react";
class Home extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      user: firebase.auth().currentUser,
      admin: false,
      topic: null,
      document: null,
      step: null,
      edit: false
    };
    firebase.auth().onAuthStateChanged(
      function(user) {
        if (user) {
          console.log("set user", user);
          this.setState({ user: user });
          if (user.email) {
            this.setState({
              admin:
                firebase.auth().currentUser.email == "yinluke@gmail.com" ||
                firebase.auth().currentUser.email == "yinluke@gmail.com2"
            });
          }
        } else {
          console.log("set user", null);
          this.setState({ user: null });
        }
      }.bind(this)
    );
  }
  render() {
    if (!this.state.user) {
      return <Redirect to="/" />;
    }

    if (!this.state.admin) {
      return <div>{this.state.user.displayName} is not an administrator</div>;
    }

    return (
      <div>
        <Grid container stackable verticalAlign="top">
          <Grid.Row style={{ marginTop: "4em" }}>
            <Grid.Column width={3}>
              <TopicTable />
            </Grid.Column>
            <Grid.Column width={3}>
              <TopicTable />
            </Grid.Column>
            <Grid.Column width={3}>
              <TopicTable />
            </Grid.Column>
            <Grid.Column width={7}>
              <TopicTable />
            </Grid.Column>
          </Grid.Row>
        </Grid>
      </div>
    );
  }
}
export default Home;
