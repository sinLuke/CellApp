import React from "react";
import firebase from "firebase";
import ItemTable from "./itemTable";
import { Redirect } from "react-router-dom";
import { Grid, Segment } from "semantic-ui-react";
import HomeDetail from "./HomeDetail";
import AnimationDetail from "./animationDetail";

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
          this.setState({ user: user });
          if (user.email) {
            this.setState({
              admin:
                firebase.auth().currentUser.email === "yinluke@gmail.com" ||
                firebase.auth().currentUser.email === "patelks2394@gmail.com" ||
                firebase.auth().currentUser.email === "yinaiguo@gmail.com"
            });
          }
        } else {
          this.setState({ user: null });
        }
      }.bind(this)
    );
    this.filterDocumentList = this.filterDocumentList.bind(this);
    this.filterStepList = this.filterStepList.bind(this);
    this.filterAnimationList = this.filterAnimationList.bind(this);
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
  filterAnimationList(id) {
    return Object.values(this.props.lists.animationItemList).filter(function(
      item
    ) {
      return item.STEP_ID === id;
    });
  }

  render() {
    if (!this.state.user) {
      return <Redirect to="/" />;
    }

    if (!this.state.admin) {
      return (
        <div style={{ margin: "4em" }}>
          <Segment>
            {this.state.user.displayName} is not an administrator
          </Segment>
        </div>
      );
    }

    return (
      <div>
        <Grid columns="equal" verticalAlign="top" style={{ padding: "16px" }}>
          <Grid.Row stretched>
            {!this.props.editAnimation ? (
              <Grid.Column width={3}>
                <ItemTable
                  selected={this.props.topicSelected}
                  setSelected={id => this.props.setSelected(0, id)}
                  items={Object.values(this.props.lists.topicList).sort(
                    function(a, b) {
                      if (a.PAGE_NUMBER < b.PAGE_NUMBER) {
                        return -1;
                      }
                      if (a.PAGE_NUMBER > b.PAGE_NUMBER) {
                        return 1;
                      }
                      if (a.created < b.created) {
                        return -1;
                      }
                      if (a.created > b.created) {
                        return 1;
                      }
                      return 0;
                    }
                  )}
                  title={"Topics"}
                  add={() => this.props.add(0, null)}
                  loading={false}
                  type={0}
                  setKeyValue={this.props.setKeyValue}
                  changed={this.props.changed}
                  isUploading={this.props.isUploading}
                  progress={this.props.progress}
                  editAnimation={this.props.editAnimation}
                />
              </Grid.Column>
            ) : (
              " "
            )}

            {this.props.topicSelected && !this.props.editAnimation ? (
              <Grid.Column width={3}>
                <ItemTable
                  selected={this.props.documentSelected}
                  setSelected={id => this.props.setSelected(1, id)}
                  items={this.filterDocumentList(this.props.topicSelected).sort(
                    function(a, b) {
                      if (a.PAGE_NUMBER < b.PAGE_NUMBER) {
                        return -1;
                      }
                      if (a.PAGE_NUMBER > b.PAGE_NUMBER) {
                        return 1;
                      }
                      if (a.created < b.created) {
                        return -1;
                      }
                      if (a.created > b.created) {
                        return 1;
                      }
                      return 0;
                    }
                  )}
                  title={
                    this.props.lists.topicList[this.props.topicSelected]
                      ? this.props.lists.topicList[this.props.topicSelected]
                          .TOPIC_NAME
                      : "Docuemnt"
                  }
                  add={parent => this.props.add(1, this.props.topicSelected)}
                  loading={false}
                  type={1}
                  setKeyValue={this.props.setKeyValue}
                  changed={this.props.changed}
                  isUploading={this.props.isUploading}
                  progress={this.props.progress}
                  editAnimation={this.props.editAnimation}
                />
              </Grid.Column>
            ) : (
              " "
            )}

            {this.props.documentSelected ? (
              <Grid.Column width={2}>
                <ItemTable
                  selected={this.props.stepSelected}
                  setSelected={id => this.props.setSelected(2, id)}
                  items={this.filterStepList(this.props.documentSelected).sort(
                    function(a, b) {
                      if (a.PAGE_NUMBER < b.PAGE_NUMBER) {
                        return -1;
                      }
                      if (a.PAGE_NUMBER > b.PAGE_NUMBER) {
                        return 1;
                      }
                      return 0;
                    }
                  )}
                  title={
                    this.props.lists.documentList[this.props.documentSelected]
                      .DOCUMENT_NAME
                  }
                  add={parent => this.props.add(2, this.props.documentSelected)}
                  loading={false}
                  type={2}
                  setKeyValue={this.props.setKeyValue}
                  changed={this.props.changed}
                  isUploading={this.props.isUploading}
                  progress={this.props.progress}
                  editAnimation={this.props.editAnimation}
                />
              </Grid.Column>
            ) : (
              " "
            )}

            {this.props.editAnimation ? (
              <Grid.Column width={6}>
                <ItemTable
                  selected={this.props.animationItemSelected}
                  setSelected={id => this.props.setSelected(3, id)}
                  items={this.filterAnimationList(this.props.stepSelected)}
                  title={`${
                    this.props.lists.documentList[this.props.documentSelected]
                      .DOCUMENT_NAME
                  } - Step ${this.props.lists.stepList[this.props.stepSelected]
                    .PAGE_NUMBER + 1}`}
                  add={parent => this.props.add(3, this.props.stepSelected)}
                  loading={false}
                  type={3}
                  setKeyValue={this.props.setKeyValue}
                  changed={this.props.changed}
                  isUploading={this.props.isUploading}
                  progress={this.props.progress}
                  editAnimation={this.props.editAnimation}
                />
              </Grid.Column>
            ) : (
              " "
            )}

            {this.props.editAnimation && this.props.stepSelected ? (
              <Grid.Column>
                <AnimationDetail
                  topicSelected={this.props.topicSelected}
                  documentSelected={this.props.documentSelected}
                  stepSelected={this.props.stepSelected}
                  item={
                    this.props.lists.animationItemList[
                      this.props.animationItemSelected
                    ]
                  }
                  lists={this.props.lists}
                  add={this.props.add}
                  filterAnimationList={this.filterAnimationList}
                  changed={this.props.changed}
                  setKeyValue={this.props.setKeyValue}
                  uploadHelpers={this.props.uploadHelpers}
                  isUploading={this.props.isUploading}
                  progress={this.props.progress}
                />
              </Grid.Column>
            ) : (
              <Grid.Column>
                <HomeDetail
                  topicSelected={this.props.topicSelected}
                  documentSelected={this.props.documentSelected}
                  stepSelected={this.props.stepSelected}
                  lists={this.props.lists}
                  add={this.props.add}
                  filterStepList={this.filterStepList}
                  changed={this.props.changed}
                  setKeyValue={this.props.setKeyValue}
                  uploadHelpers={this.props.uploadHelpers}
                  isUploading={this.props.isUploading}
                  progress={this.props.progress}
                />
              </Grid.Column>
            )}
          </Grid.Row>
        </Grid>
      </div>
    );
  }
}
export default Home;
