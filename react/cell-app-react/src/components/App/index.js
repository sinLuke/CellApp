import React from "react";
import sharedFirebase from "./firebase";
import firebase from "firebase";
import { BrowserRouter as Router, Route } from "react-router-dom";
import Navigation from "../Navigation";
import LoginPage from "../Login";
import HomePage from "../Home";
import * as ROUTES from "../../constants/routes";
class App extends React.Component {
  constructor(prop) {
    super(prop);
    this.state = {
      topic: null,
      document: null,
      step: null,
      topicList: {},
      stepList: {},
      documentList: {},
      currentVersion: null,
      changed: new Set(),
      isUploading: null,
      progress: 0
    };

    this.setSelected = this.setSelected.bind(this);
    this.load = this.load.bind(this);
    this.add = this.add.bind(this);
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
    this.setState({ isUploading: id, progress: 0 });
  }
  handleProgress(progress) {
    this.setState({ progress });
  }
  handleUploadError(error) {
    this.setState({ isUploading: null });
    window.alert(error);
  }
  handleUploadSuccess(collection, id, filename) {
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
            default:
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
          }
        }.bind(this)
      );
  }

  setSelected(collection, id) {
    switch (collection) {
      case 0:
        if (Object.keys(this.state.topicList).includes(id)) {
          console.log(1);
          this.setState({ topic: id, document: null, step: null });
        } else {
          console.log(2);
          this.setState({ topic: null, document: null, step: null });
        }

        break;
      case 1:
        if (Object.keys(this.state.documentList).includes(id)) {
          var topicID = this.state.documentList[id].TOPIC_ID;
          if (Object.keys(this.state.topicList).includes(topicID)) {
            console.log(3);
            this.setState({ topic: topicID, document: id, step: null });
          } else {
            console.log(4);
            this.setState({ topic: null, document: null, step: null });
          }
        } else {
          console.log(5);
          this.setState({ document: null, step: null });
        }
        break;
      default:
        if (Object.keys(this.state.stepList).includes(id)) {
          var documentID = this.state.stepList[id].DOCUMENT_ID;
          if (Object.keys(this.state.documentList).includes(documentID)) {
            var topicID = this.state.documentList[documentID].TOPIC_ID;
            if (Object.keys(this.state.topicList).includes(topicID)) {
              this.setState({ topic: topicID, document: documentID, step: id });
            } else {
              console.log(6);
              this.setState({ topic: null, document: null, step: null });
            }
          } else {
            console.log(7);
            this.setState({ document: null, step: null });
          }
        } else {
          console.log(8, this.state.stepList, id);
          this.setState({ step: null });
        }
    }
  }

  setKeyValue(collection, document, parent, key, value) {
    var collectionName = "TOPIC";
    var _changed = this.state.changed;

    console.log("setKeyValue", this.state.stepList[document]);

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
          console.log("_topic 1", _topic, value);
          _topic = value;
          console.log("_topic 2", _topic, value);
        }

        //change the key-value
        _documentList[document][key] = value;

        //add id to changed list
        _changed.add(document);

        console.log("_topic 3", _topic, key, value);

        this.setState({
          documentList: _documentList,
          changed: _changed,
          topic: _topic
        });

        break;
      default:
        collectionName = "STEP";

        //this step dosen't exist
        if (!this.state.stepList[document]) {
          return;
        }

        //get copy of the whole stepList
        var _stepList = this.state.stepList;

        //swap pagenumber for target step
        if (key === "PAGE_NUMBER") {
          Object.values(_stepList).forEach(
            function(step) {
              if (step.DOCUMENT_ID === parent && step.PAGE_NUMBER === value) {
                // same object
                if (step.id == document) {
                  return;
                }

                //swap the pageNUmber
                var targetValue = _stepList[document].PAGE_NUMBER;
                _stepList[step.id].PAGE_NUMBER = targetValue;
                _stepList[document].PAGE_NUMBER = value;

                Promise.all([
                  this.db
                    .collection("STEP")
                    .doc(step.id)
                    .update({ PAGE_NUMBER: targetValue }),
                  this.db
                    .collection("STEP")
                    .doc(document)
                    .update({ PAGE_NUMBER: value })
                ]).then(
                  function() {
                    //set the updated stepList back
                    this.setState({
                      stepList: _stepList
                    });
                  }.bind(this)
                );
                return;
              }
            }.bind(this)
          );
          return;
        }

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

        console.log("setKeyValue", this.state.stepList[document]);
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
      default:
        collectionName = "STEP";
        item = {
          AUTO_ANIMATION: 1,
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
            default:
              var _stepList = this.state.stepList;
              _stepList[docRef.id] = item;
              this.setState({ step: docRef.id, stepList: _stepList });
          }
        }.bind(this)
      );
  }

  update(collection, document) {
    console.trace("update");
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
      default:
        collectionName = "STEP";
        item = this.state.stepList[document];
    }
    console.trace("update", item);
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
    console.log("load");
    var topicList = {};
    var stepList = {};
    var documentList = {};

    Promise.all([
      this.db.collection("TOPIC").get(),
      this.db.collection("DOCUMENT").get(),
      this.db.collection("STEP").get()
    ]).then(
      function([topicSnapshots, documentSnapshots, stepSnapshots]) {
        topicSnapshots.forEach(doc => {
          var thisDoc = doc.data();
          thisDoc.id = doc.id;
          topicList[doc.id] = thisDoc;
        });
        documentSnapshots.forEach(doc => {
          var thisDoc = doc.data();
          thisDoc.id = doc.id;
          documentList[doc.id] = thisDoc;
        });
        stepSnapshots.forEach(doc => {
          var thisDoc = doc.data();
          thisDoc.id = doc.id;
          stepList[doc.id] = thisDoc;
        });

        this.setState({
          topicList: topicList,
          documentList: documentList,
          stepList: stepList,
          changed: new Set()
        });
      }.bind(this)
    );
  }

  render() {
    return (
      <Router>
        <div>
          <Navigation />
          <hr />
          <Route exact path={ROUTES.LOGIN} render={props => <LoginPage />} />
          <Route
            path={ROUTES.HOME}
            render={props => (
              <HomePage
                setSelected={this.setSelected}
                topicSelected={this.state.topic}
                documentSelected={this.state.document}
                stepSelected={this.state.step}
                lists={{
                  topicList: this.state.topicList,
                  documentList: this.state.documentList,
                  stepList: this.state.stepList
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
            )}
          />
        </div>
      </Router>
    );
  }
}
export default App;
