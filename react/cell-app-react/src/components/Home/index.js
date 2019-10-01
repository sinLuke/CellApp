import React from "react";
import firebase from "firebase";
import TopicTable from "./topicTable";
import DocumentTable from "./documentTable";
import StepTable from "./stepTable";
import { Redirect } from "react-router-dom";
import { Grid, Segment } from "semantic-ui-react";
import HomeDetail from "./HomeDetail";
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
                firebase.auth().currentUser.email === "yinluke@gmail.com" ||
                firebase.auth().currentUser.email === "yinluke@gmail.com2"
            });
          }
        } else {
          console.log("set user", null);
          this.setState({ user: null });
        }
      }.bind(this)
    );
    this.filterDocumentList = this.filterDocumentList.bind(this);
    this.filterStepList = this.filterStepList.bind(this);
  }
  filterDocumentList(id) {
    return Object.values(this.props.lists.documentList).filter(function(doc) {
      return doc.TOPIC_ID === id;
    });
  }

  filterStepList(id) {
    return Object.values(this.props.lists.stepList).filter(function(step) {
      return step.DOCUMENT_ID === id;
    });
  }

  render() {
    if (!this.state.user) {
      return <Redirect to="/" />;
    }

    if (!this.state.admin) {
      return <div>{this.state.user.displayName} is not an administrator</div>;
    }

    console.log(this.props);

    return (
      <div>
        <Grid container stackable columns="equal" verticalAlign="top">
          <Grid.Row stretched style={{ marginTop: "4em" }}>
            <Grid.Column>
              <TopicTable
                selected={this.props.topicSelected}
                setSelected={id => this.props.setSelected(0, id)}
                topics={Object.values(this.props.lists.topicList)}
              />
            </Grid.Column>

            {this.props.topicSelected ? (
              <Grid.Column>
                <DocumentTable
                  selected={this.props.documentSelected}
                  setSelected={id => this.props.setSelected(1, id)}
                  docs={this.filterDocumentList(this.props.topicSelected)}
                  title={
                    this.props.lists.topicList[this.props.topicSelected]
                      .TOPIC_NAME
                  }
                />
              </Grid.Column>
            ) : (
              " "
            )}

            {this.props.documentSelected ? (
              <Grid.Column>
                <StepTable
                  selected={this.props.stepSelected}
                  setSelected={id => this.props.setSelected(2, id)}
                  steps={this.filterStepList(this.props.documentSelected)}
                  title={
                    this.props.lists.documentList[this.props.documentSelected]
                      .DOCUMENT_NAME
                  }
                />
              </Grid.Column>
            ) : (
              " "
            )}

            <Grid.Column width={8}>
              <HomeDetail
                topicSelected={this.props.topicSelected}
                documentSelected={this.props.documentSelected}
                stepSelected={this.props.stepSelected}
                lists={this.props.lists}
              />
            </Grid.Column>
          </Grid.Row>
        </Grid>
      </div>
    );
  }
}
export default Home;
