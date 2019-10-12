import React from "react";
import firebase from "firebase";
import ItemTable from "./itemTable";
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
        <Grid container stackable columns="equal" verticalAlign="top">
          <Grid.Row stretched style={{ marginTop: "4em" }}>
            <Grid.Column>
              <ItemTable
                selected={this.props.topicSelected}
                setSelected={id => this.props.setSelected(0, id)}
                items={Object.values(this.props.lists.topicList).sort(function(
                  a,
                  b
                ) {
                  if (a.created < b.created) {
                    return -1;
                  }
                  if (a.created > b.created) {
                    return 1;
                  }
                  return 0;
                })}
                title={"Topics"}
                add={() => this.props.add(0, null)}
                loading={false}
                type={0}
                changed={this.props.changed}
                isUploading={this.props.isUploading}
                progress={this.props.progress}
              />
            </Grid.Column>

            {this.props.topicSelected ? (
              <Grid.Column>
                <ItemTable
                  selected={this.props.documentSelected}
                  setSelected={id => this.props.setSelected(1, id)}
                  items={this.filterDocumentList(this.props.topicSelected).sort(
                    function(a, b) {
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
                  changed={this.props.changed}
                  isUploading={this.props.isUploading}
                  progress={this.props.progress}
                />
              </Grid.Column>
            ) : (
              " "
            )}

            {this.props.documentSelected ? (
              <Grid.Column>
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
                  changed={this.props.changed}
                  isUploading={this.props.isUploading}
                  progress={this.props.progress}
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
                add={this.props.add}
                filterStepList={this.filterStepList}
                changed={this.props.changed}
                setKeyValue={this.props.setKeyValue}
                uploadHelpers={this.props.uploadHelpers}
                isUploading={this.props.isUploading}
                progress={this.props.progress}
              />
            </Grid.Column>
          </Grid.Row>
        </Grid>
      </div>
    );
  }
}
export default Home;
