import React from "react";
import sharedFirebase from "./firebase";
import firebase from "firebase";
import { BrowserRouter as Router, Route } from "react-router-dom";
import Navigation from "../Navigation";
import LoginPage from "../Login";
import HomePage from "../Home";
import * as ROUTES from "../../constants/routes";
import { Button, Grid, GridColumn } from "semantic-ui-react";
import { deflate } from "zlib";
class App extends React.Component {
  constructor(prop) {
    super(prop);
    this.state = {
      topic: null,
      document: null,
      step: null,
      animationItem: null,
      topicList: {},
      stepList: {},
      documentList: {},
      animationItemList: {},
      currentVersion: null,
      changed: new Set(),
      isUploading: null,
      progress: 0,
      editAnimation: false
    };

    this.setSelected = this.setSelected.bind(this);
    this.load = this.load.bind(this);
    this.add = this.add.bind(this);
    this.update = this.update.bind(this);
    this.updateAll = this.updateAll.bind(this);
    this.setKeyValue = this.setKeyValue.bind(this);
    this.uploadHelpers = {
      handleUploadStart: this.handleUploadStart.bind(this),
      handleProgress: this.handleProgress.bind(this),
      handleUploadError: this.handleUploadError.bind(this),
      handleUploadSuccess: this.handleUploadSuccess.bind(this),
      update: this.update.bind(this)
    };

    //firebase setup

    this.db = sharedFirebase.firestore();
    this.load();
  }

  handleUploadStart(id) {
    console.log("handleUploadStart", id);
    this.setState({ isUploading: id, progress: 0 });
  }
  handleProgress(progress) {
    console.log("handleProgress", progress);
    this.setState({ progress });
  }
  handleUploadError(error) {
    console.log("handleUploadError", error);
    this.setState({ isUploading: null });
    window.alert(error);
  }
  handleUploadSuccess(collection, id, filename) {
    console.log("handleUploadSuccess", collection, id, filename);
    firebase
      .storage()
      .ref("images")
      .child(filename)
      .getDownloadURL()
      .then(
        function(url) {
          var _changed = this.state.changed;
          _changed.add(id);
          switch (collection) {
            case 0:
              if (this.state.topicList[id]) {
                var _topicList = this.state.topicList;
                _topicList[id].IMAGE_URL = url;
                this.setState({
                  topicList: _topicList,
                  progress: 100,
                  isUploading: null,
                  changed: _changed
                });
              }
              break;
            case 1:
              if (this.state.documentList[id]) {
                var _documentList = this.state.documentList;
                _documentList[id].IMAGE_URL = url;
                this.setState({
                  documentList: _documentList,
                  progress: 100,
                  isUploading: null,
                  changed: _changed
                });
              }
              break;
            case 2:
              if (this.state.stepList[id]) {
                var _stepList = this.state.stepList;
                _stepList[id].IMAGE_URL = url;
                this.setState({
                  stepList: _stepList,
                  progress: 100,
                  isUploading: null,
                  changed: _changed
                });
              }
              break;
            default:
              if (this.state.animationItemList[id]) {
                var _animationItemList = this.state.animationItemList;
                _animationItemList[id].IMAGE_URL = url;
                this.setState({
                  animationItemList: _animationItemList,
                  progress: 100,
                  isUploading: null,
                  changed: _changed
                });
              }
          }
        }.bind(this)
      );
  }

  setSelected(collection, id) {
    switch (collection) {
      case 0:
        if (Object.keys(this.state.topicList).includes(id)) {
          this.setState({
            topic: id,
            document: null,
            step: null,
            animationItem: null
          });
        } else {
          this.setState({
            topic: null,
            document: null,
            step: null,
            animationItem: null
          });
        }

        break;
      case 1:
        if (Object.keys(this.state.documentList).includes(id)) {
          var topicID = this.state.documentList[id].TOPIC_ID;
          if (Object.keys(this.state.topicList).includes(topicID)) {
            this.setState({
              topic: topicID,
              document: id,
              step: null,
              animationItem: null
            });
          } else {
            this.setState({
              topic: null,
              document: null,
              step: null,
              animationItem: null
            });
          }
        } else {
          this.setState({ document: null, step: null, animationItem: null });
        }
        break;
      case 2:
        if (Object.keys(this.state.stepList).includes(id)) {
          var documentID = this.state.stepList[id].DOCUMENT_ID;
          if (Object.keys(this.state.documentList).includes(documentID)) {
            var topicID = this.state.documentList[documentID].TOPIC_ID;
            if (Object.keys(this.state.topicList).includes(topicID)) {
              this.setState({
                topic: topicID,
                document: documentID,
                step: id,
                animationItem: null
              });
            } else {
              this.setState({
                topic: null,
                document: null,
                step: null,
                animationItem: null
              });
            }
          } else {
            this.setState({ document: null, step: null, animationItem: null });
          }
        } else {
          this.setState({ step: null, animationItem: null });
        }
        break;
      default:
        if (Object.keys(this.state.animationItemList).includes(id)) {
          var stepID = this.state.animationItemList[id].STEP_ID;
          if (stepID === this.state.step) {
            this.setState({ animationItem: id });
          }
        } else {
          this.setState({ animationItem: null });
        }
    }
  }

  setKeyValue(collection, document, parent, key, value) {
    var collectionName = "TOPIC";
    var _changed = this.state.changed;

    switch (collection) {
      case 0:
        collectionName = "TOPIC";
        //this topic dosen't exist
        if (!this.state.topicList[document]) {
          return;
        }

        //get copy of the whole topicList
        var _topicList = this.state.topicList;

        //change the key-value
        _topicList[document][key] = value;

        //add id to changed list
        _changed.add(document);

        this.setState({
          topicList: _topicList,
          changed: _changed
        });

        break;
      case 1:
        collectionName = "DOCUMENT";

        //this document dosen't exist
        if (!this.state.documentList[document]) {
          return;
        }

        //get copy of the whole documentList
        var _documentList = this.state.documentList;

        var _topic = this.state.topic;

        //If change the topic id, make sure it's exist
        if (key === "TOPIC_ID") {
          if (!this.state.topicList[value]) {
            return;
          }
          _topic = value;
        }

        //change the key-value
        _documentList[document][key] = value;

        //add id to changed list
        _changed.add(document);

        this.setState({
          documentList: _documentList,
          changed: _changed,
          topic: _topic
        });

        break;
      case 2:
        collectionName = "STEP";

        //this step dosen't exist
        if (!this.state.stepList[document]) {
          return;
        }

        //get copy of the whole stepList
        var _stepList = this.state.stepList;

        var _document = this.state.document;
        var _topic = this.state.topic;
        //If change the document id, make sure it's exist
        if (key === "DOCUMENT_ID") {
          if (!this.state.documentList[value]) {
            return;
          }
          if (
            !this.state.topicList[this.state.documentList[value]["TOPIC_ID"]]
          ) {
            return;
          }
          _document = value;
          _topic = this.state.documentList[value]["TOPIC_ID"];
        }

        //change the key-value
        _stepList[document][key] = value;

        //add id to changed list
        _changed.add(document);

        this.setState({
          stepList: _stepList,
          changed: _changed,
          document: _document,
          topic: _topic
        });
        break;

      default:
        //this animation item dosen't exist
        if (!this.state.animationItemList[document]) {
          return;
        }
        //get copy of the whole animationItemList
        var _animationItemList = this.state.animationItemList;
        //change the key-value
        _animationItemList[document][key] = value;
        //add id to changed list
        _changed.add(document);

        this.setState({
          animationItemList: _animationItemList,
          changed: _changed
        });
    }
  }

  add(collection, parent) {
    var collectionName = "TOPIC";
    var item = null;
    switch (collection) {
      case 0:
        collectionName = "TOPIC";
        item = {
          IMAGE_URL: null,
          TOPIC_NAME: "New Topic",
          created: new Date().getTime()
        };
        break;
      case 1:
        collectionName = "DOCUMENT";
        item = {
          DIFFICULTY: 1,
          DOCUMENT_NAME: "New Document",
          IMAGE_URL: null,
          INTRODUCTION: "Add Introduction Here ...",
          TOPIC_ID: parent,
          available_scope: 10,
          created: new Date().getTime()
        };
        break;
      case 2:
        collectionName = "STEP";
        item = {
          TYPE: 1,
          ANSWER: "",
          CHOICES: "",
          ANSWER_EXPLANATION: "Good job!",
          DOCUMENT_ID: parent,
          IMAGE_SUBTITLE: "Subtitle",
          IMAGE_URL: null,
          TEXT: "Add Text Here ...",
          PAGE_NUMBER: Object.values(this.state.stepList).filter(function(
            step
          ) {
            return step.DOCUMENT_ID === parent;
          }).length,
          created: new Date().getTime()
        };
        break;
      default:
        collectionName = "ANIMATION_ITEM";
        item = {
          DESCRIPTION: "New Animation Item",
          END_POSITION_X: 0.0,
          END_POSITION_Y: 0.0,
          START_POSITION_X: 0.0,
          START_POSITION_Y: 0.0,
          IMAGE_URL: null,
          SIZE: 10,
          STEP_ID: parent,
          created: new Date().getTime()
        };
    }
    this.db
      .collection(collectionName)
      .add(item)
      .then(
        function(docRef) {
          item.id = docRef.id;
          this.db
            .collection("var")
            .doc("db_version")
            .set({ value: this.state.currentVersion + 1 });
          switch (collection) {
            case 0:
              var _topicList = this.state.topicList;
              _topicList[docRef.id] = item;
              this.setState({ topic: docRef.id, topicList: _topicList });
              break;
            case 1:
              var _documentList = this.state.documentList;
              _documentList[docRef.id] = item;
              this.setState({
                document: docRef.id,
                documentList: _documentList
              });
              break;
            case 2:
              var _stepList = this.state.stepList;
              _stepList[docRef.id] = item;
              this.setState({ step: docRef.id, stepList: _stepList });
              break;
            default:
              var _animationItemList = this.state.animationItemList;
              _animationItemList[docRef.id] = item;
              this.setState({
                animationItem: docRef.id,
                animationItemList: _animationItemList
              });
          }
        }.bind(this)
      );
  }

  updateAll() {
    this.state.changed.forEach(document => {
      this.update(0, document);
      this.update(1, document);
      this.update(2, document);
      this.update(3, document);
    });
  }

  update(collection, document) {
    var collectionName = "TOPIC";
    var item = null;
    switch (collection) {
      case 0:
        collectionName = "TOPIC";
        item = this.state.topicList[document];
        break;
      case 1:
        collectionName = "DOCUMENT";
        item = this.state.documentList[document];
        break;
      case 2:
        collectionName = "STEP";
        item = this.state.stepList[document];
        break;
      default:
        collectionName = "ANIMATION_ITEM";
        item = this.state.animationItemList[document];
    }

    if (item) {
      this.db
        .collection(collectionName)
        .doc(document)
        .set(item)
        .then(
          function() {
            this.db
              .collection("var")
              .doc("db_version")
              .set({ value: this.state.currentVersion + 1 });

            var _changed = this.state.changed;
            _changed.delete(document);
            this.setState({ changed: _changed });
          }.bind(this)
        );
    }
  }

  load() {
    var topicList = {};
    var stepList = {};
    var documentList = {};
    var animationItemList = {};

    Promise.all([
      this.db.collection("TOPIC").get(),
      this.db.collection("DOCUMENT").get(),
      this.db.collection("STEP").get(),
      this.db.collection("ANIMATION_ITEM").get()
    ]).then(
      function([
        topicSnapshots,
        documentSnapshots,
        stepSnapshots,
        animationItemSnapshots
      ]) {
        topicSnapshots.forEach(doc => {
          var thisDoc = doc.data();
          if (thisDoc.hidden !== true) {
            thisDoc.id = doc.id;
            topicList[doc.id] = thisDoc;
          }
        });
        documentSnapshots.forEach(doc => {
          var thisDoc = doc.data();
          if (thisDoc.hidden !== true) {
            thisDoc.id = doc.id;
            documentList[doc.id] = thisDoc;
          }
        });
        stepSnapshots.forEach(doc => {
          var thisDoc = doc.data();
          if (thisDoc.hidden !== true) {
            thisDoc.id = doc.id;
            stepList[doc.id] = thisDoc;
          }
        });
        animationItemSnapshots.forEach(doc => {
          var thisDoc = doc.data();
          if (thisDoc.hidden !== true) {
            thisDoc.id = doc.id;
            animationItemList[doc.id] = thisDoc;
          }
        });

        this.setState({
          topicList: topicList,
          documentList: documentList,
          stepList: stepList,
          animationItemList: animationItemList,
          changed: new Set()
        });
      }.bind(this)
    );
  }

  render() {
    this.props.window.onbeforeunload = function(e) {
      if (this.state.changed.size !== 0) {
        console.log("onbeforeunload");

        // Cancel the event
        e.preventDefault();

        // Chrome requires returnValue to be set
        e.returnValue = "Really want to quit?";
      }
    }.bind(this);
    var imageURL =
      "https://firebasestorage.googleapis.com/v0/b/cell-app.appspot.com/o/bkimg?alt=media&token=4ea31dfb-89c0-4738-913f-066ab55c21f1";
    return (
      <Router>
        <div
          style={{
            backgroundColor: this.state.editAnimation ? "black" : "white"
          }}
        >
          <Navigation editAnimation={this.state.editAnimation} />
          <hr />
          <Route exact path={ROUTES.LOGIN} render={props => <LoginPage />} />
          <Route
            path={ROUTES.HOME}
            render={props => (
              <div>
                <Button
                  primary
                  disabled={this.state.changed.size === 0}
                  onClick={this.updateAll}
                  style={{ marginTop: "4em", marginLeft: "16px" }}
                >
                  {this.state.changed.size} Change(s), Save All
                </Button>

                {this.state.editAnimation ? (
                  <Button
                    disabled={!this.state.step}
                    onClick={() => {
                      this.setState({ editAnimation: false });
                    }}
                    style={{ marginTop: "4em", marginLeft: "16px" }}
                  >
                    Back
                  </Button>
                ) : (
                  <Button
                    primary
                    disabled={
                      !this.state.step ||
                      this.state.stepList[this.state.step].TYPE >= 10
                    }
                    onClick={() => {
                      this.setState({ editAnimation: true });
                    }}
                    style={{ marginTop: "4em" }}
                  >
                    Edit Animation
                  </Button>
                )}

                <HomePage
                  window={this.props.window}
                  style={{ backgroundImage: `url(${imageURL})` }}
                  editAnimation={this.state.editAnimation}
                  setSelected={this.setSelected}
                  topicSelected={this.state.topic}
                  documentSelected={this.state.document}
                  stepSelected={this.state.step}
                  animationItemSelected={this.state.animationItem}
                  lists={{
                    topicList: this.state.topicList,
                    documentList: this.state.documentList,
                    stepList: this.state.stepList,
                    animationItemList: this.state.animationItemList
                  }}
                  update={this.update}
                  load={this.load}
                  add={this.add}
                  setKeyValue={this.setKeyValue}
                  changed={this.state.changed}
                  uploadHelpers={this.uploadHelpers}
                  isUploading={this.state.isUploading}
                  progress={this.state.progress}
                />
              </div>
            )}
          />
        </div>
      </Router>
    );
  }
}
export default App;
