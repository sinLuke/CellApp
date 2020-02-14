import sharedFirebase from "./firebase";
import em from "../../constants/enumerator";
import * as ROUTES from "../../constants/routes";

var db = sharedFirebase.firestore();

function setSelected(collection, id, state, currectSelected, history) {
  switch (collection) {
    case 0:
      if (Object.keys(state.topicList).includes(id)) {
        history.push(`${ROUTES.HOME}/${id}`)
      } else {
        history.push(`${ROUTES.HOME}`)
      }

      break;
    case 1:
      if (Object.keys(state.documentList).includes(id)) {
        var topicID = state.documentList[id].TOPIC_ID;
        if (Object.keys(state.topicList).includes(topicID)) {
          history.push(`${ROUTES.HOME}/${topicID}/${id}`)
        } else {
          history.push(`${ROUTES.HOME}`)
        }
      } else {
        history.push(`${ROUTES.HOME}/${currectSelected.topicID}`)
      }
      break;
    case 2:
      if (Object.keys(state.stepList).includes(id)) {
        var documentID = state.stepList[id].DOCUMENT_ID;
        if (Object.keys(state.documentList).includes(documentID)) {
          var topicID = state.documentList[documentID].TOPIC_ID;
          if (Object.keys(state.topicList).includes(topicID)) {
            history.push(`${ROUTES.HOME}/${topicID}/${documentID}/${id}`)
          } else {
            history.push(`${ROUTES.HOME}`)
          }
        } else {
          history.push(`${ROUTES.HOME}/${currectSelected.topicID}`)
        }
      } else {
        history.push(`${ROUTES.HOME}/${currectSelected.topicID}/${currectSelected.documentID}`)
      }
      break;
    default:
      if (Object.keys(state.animationItemList).includes(id)) {
        var stepID = state.animationItemList[id].STEP_ID;
        if (stepID === currectSelected.stepID) {
          history.push(`${ROUTES.HOME}/${currectSelected.topicID}/${currectSelected.documentID}/${currectSelected.stepID}/${id}`)
        }
      } else {
        history.push(`${ROUTES.HOME}/${currectSelected.topicID}/${currectSelected.documentID}/${currectSelected.stepID}`)
      }
  }
}

function add(collection, parent, state, callback) {
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
        PAGE_NUMBER: Object.values(state.stepList).filter(function (
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
  db
    .collection(collectionName)
    .add(item)
    .then(
      (docRef) => {
        item.id = docRef.id;
        db
          .collection("var")
          .doc("db_version")
          .set({ value: state.currentVersion + 1 });
        switch (collection) {
          case 0:
            var _topicList = state.topicList;
            _topicList[docRef.id] = item;
            callback({ topic: docRef.id, topicList: _topicList });
            break;
          case 1:
            console.log(state)
            var _documentList = state.documentList;
            _documentList[docRef.id] = item;
            callback({
              document: docRef.id,
              documentList: _documentList
            });
            break;
          case 2:
            var _stepList = state.stepList;
            _stepList[docRef.id] = item;
            callback({ step: docRef.id, stepList: _stepList });
            break;
          default:
            var _animationItemList = state.animationItemList;
            _animationItemList[docRef.id] = item;
            callback({
              animationItem: docRef.id,
              animationItemList: _animationItemList
            });
        }
      }
    );
}

function update(collection, document, state, callback) {
  var collectionName = "TOPIC";
  var item = null;
  switch (collection) {
    case 0:
      collectionName = "TOPIC";
      item = state.topicList[document];
      break;
    case 1:
      collectionName = "DOCUMENT";
      item = state.documentList[document];
      break;
    case 2:
      collectionName = "STEP";
      item = state.stepList[document];
      break;
    default:
      collectionName = "ANIMATION_ITEM";
      item = state.animationItemList[document];
  }

  if (item) {
    db
      .collection(collectionName)
      .doc(document)
      .set(item)
      .then(
        () => {
          db
            .collection("var")
            .doc("db_version")
            .set({ value: state.currentVersion + 1 });

          var _changed = state.changed;
          _changed.delete(document);
          callback({ changed: _changed });
        }
      );
  }
}

function load(state, callback) {
  var topicList = {};
  var stepList = {};
  var documentList = {};
  var animationItemList = {};

  Promise.all([
    db.collection("TOPIC").get(),
    db.collection("DOCUMENT").get(),
    db.collection("STEP").get(),
    db.collection("ANIMATION_ITEM").get()
  ]).then(
    ([
      topicSnapshots,
      documentSnapshots,
      stepSnapshots,
      animationItemSnapshots
    ]) => {
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

      console.log(callback)

      callback({
        topicList: topicList,
        documentList: documentList,
        stepList: stepList,
        animationItemList: animationItemList,
        changed: new Set()
      });
    }
  );
}

function setKeyValue(collection, document, parent, key, value, state, callback) {
  var collectionName = "TOPIC";
  var _changed = state.changed;

  switch (collection) {
    case 0:
      collectionName = "TOPIC";
      //this topic dosen't exist
      if (!state.topicList[document]) {
        return;
      }

      //get copy of the whole topicList
      var _topicList = state.topicList;

      //change the key-value
      _topicList[document][key] = value;

      //add id to changed list
      _changed.add(document);

      callback({
        topicList: _topicList,
        changed: _changed
      });

      break;
    case 1:
      collectionName = "DOCUMENT";

      //this document dosen't exist
      if (!state.documentList[document]) {
        return;
      }

      //get copy of the whole documentList
      var _documentList = state.documentList;

      var _topic = state.topic;

      //If change the topic id, make sure it's exist
      if (key === "TOPIC_ID") {
        if (!state.topicList[value]) {
          return;
        }
        _topic = value;
      }

      //change the key-value
      _documentList[document][key] = value;

      //add id to changed list
      _changed.add(document);

      callback({
        documentList: _documentList,
        changed: _changed,
        topic: _topic
      });

      break;
    case 2:
      collectionName = "STEP";

      //this step dosen't exist
      if (!state.stepList[document]) {
        return;
      }

      //get copy of the whole stepList
      var _stepList = state.stepList;

      var _document = state.document;
      var _topic = state.topic;
      //If change the document id, make sure it's exist
      if (key === "DOCUMENT_ID") {
        if (!state.documentList[value]) {
          return;
        }
        if (
          !state.topicList[state.documentList[value]["TOPIC_ID"]]
        ) {
          return;
        }
        _document = value;
        _topic = state.documentList[value]["TOPIC_ID"];
      }

      //change the key-value
      _stepList[document][key] = value;

      //add id to changed list
      _changed.add(document);

      callback({
        stepList: _stepList,
        changed: _changed,
        document: _document,
        topic: _topic
      });
      break;

    default:
      //this animation item dosen't exist
      if (!state.animationItemList[document]) {
        return;
      }
      //get copy of the whole animationItemList
      var _animationItemList = state.animationItemList;
      //change the key-value
      _animationItemList[document][key] = value;
      //add id to changed list
      _changed.add(document);

      callback({
        animationItemList: _animationItemList,
        changed: _changed
      });
  }
}

function handleUploadStart(id, callback) {
  callback({ isUploading: id, progress: 0 });
}
function handleProgress(progress, callback) {
  callback({ progress });
}
function handleUploadError(error, callback) {
  callback({ isUploading: null });
  window.alert(error);
}
function handleUploadSuccess(collection, id, filename, state, callback) {
  sharedFirebase
    .storage()
    .ref("images")
    .child(filename)
    .getDownloadURL()
    .then(
      (url) => {
        var _changed = state.changed;
        _changed.add(id);
        switch (collection) {
          case em.Collections.TOPIC:
            if (state.topicList[id]) {
              var _topicList = state.topicList;
              _topicList[id].IMAGE_URL = url;
              callback({
                topicList: _topicList,
                progress: 100,
                isUploading: null,
                changed: _changed
              });
            }
            break;
          case em.Collections.DOCUMENTS:
            if (state.documentList[id]) {
              var _documentList = state.documentList;
              _documentList[id].IMAGE_URL = url;
              callback({
                documentList: _documentList,
                progress: 100,
                isUploading: null,
                changed: _changed
              });
            }
            break;
          case em.Collections.STEP:
            if (state.stepList[id]) {
              var _stepList = state.stepList;
              _stepList[id].IMAGE_URL = url;
              callback({
                stepList: _stepList,
                progress: 100,
                isUploading: null,
                changed: _changed
              });
            }
            break;
          case em.Collections.ANIMATION_ITEM:
            if (state.animationItemList[id]) {
              var _animationItemList = state.animationItemList;
              _animationItemList[id].IMAGE_URL = url;
              callback({
                animationItemList: _animationItemList,
                progress: 100,
                isUploading: null,
                changed: _changed
              });
            }
          default:
            return
        }
      }
    );
}

export default { setSelected, add, update, load, setKeyValue, handleUploadStart, handleProgress, handleUploadError, handleUploadSuccess }