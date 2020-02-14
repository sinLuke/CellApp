import React from "react";
import sharedFirebase from "./firebase";
import firebase from "firebase";
import { BrowserRouter as Router, Route } from "react-router-dom";
import Navigation from "../Navigation";
import LoginPage from "../Login";
import HomePage from "../Home";
import * as ROUTES from "../../constants/routes";
import HomeObject from "./HomeObject";
import { Grid, GridColumn } from "semantic-ui-react";
import service from "./Services"

class App extends React.Component {
  constructor(prop) {
    super(prop);
    this.state = {
      loading: true,
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

    this.db = sharedFirebase.firestore();
    this.load();
  }

  load() {
    service.load(this.state, (obj) => {
      obj.loading = false
      this.setState(obj)
    })
  }

  render() {

    if (this.state.loading) {
      return <h1>Loading ...</h1>
    }

    this.props.window.onbeforeunload = (e) => {
      if (this.state.changed.size !== 0) {
        console.log("onbeforeunload");

        // Cancel the event
        e.preventDefault();

        // Chrome requires returnValue to be set
        e.returnValue = "Really want to quit?";
      }
    }

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
            exact
            path={ROUTES.HOME}
            render={props => {

              return (<HomeObject

                topicID={null}
                docID={null}
                stepID={null}
                animationItemID={null}

                history={props.history}

                window={this.props.window}
                superState={this.state}
                setSuperState={this.setState.bind(this).bind(this)}
                uploadHelpers={{
                  handleUploadStart: (id) => service.handleUploadStart(id, this.setState.bind(this)),
                  handleProgress: (progress) => service.handleProgress(progress, this.setState.bind(this)),
                  handleUploadError: (error) => service.handleUploadError(error, this.setState.bind(this)),
                  handleUploadSuccess: (collection, id, filename) => service.handleUploadSuccess(collection, id, filename, this.state, this.setState.bind(this)),
                  update: (collection, document) => service.update(collection, document, this.state, this.setState.bind(this))
                }}
              />
              )
            }}
          />
          <Route
            exact
            path={ROUTES.HOME + "/:topicID"}
            render={props => {
              return (<HomeObject

                topicID={props.match.params.topicID}
                docID={null}
                stepID={null}
                animationItemID={null}

                history={props.history}

                window={this.props.window}
                superState={this.state}
                setSuperState={this.setState.bind(this).bind(this)}
                uploadHelpers={{
                  handleUploadStart: (id) => service.handleUploadStart(id, this.setState.bind(this)),
                  handleProgress: (progress) => service.handleProgress(progress, this.setState.bind(this)),
                  handleUploadError: (error) => service.handleUploadError(error, this.setState.bind(this)),
                  handleUploadSuccess: (collection, id, filename) => service.handleUploadSuccess(collection, id, filename, this.state, this.setState.bind(this)),
                  update: (collection, document) => service.update(collection, document, this.state, this.setState.bind(this))
                }}
              />
              )
            }}
          />
          <Route
            exact
            path={ROUTES.HOME + "/:topicID" + "/:docID"}
            render={props => {
              return (<HomeObject

                topicID={props.match.params.topicID}
                docID={props.match.params.docID}
                stepID={null}
                animationItemID={null}

                history={props.history}

                window={this.props.window}
                superState={this.state}
                setSuperState={this.setState.bind(this).bind(this)}
                uploadHelpers={{
                  handleUploadStart: (id) => service.handleUploadStart(id, this.setState.bind(this)),
                  handleProgress: (progress) => service.handleProgress(progress, this.setState.bind(this)),
                  handleUploadError: (error) => service.handleUploadError(error, this.setState.bind(this)),
                  handleUploadSuccess: (collection, id, filename) => service.handleUploadSuccess(collection, id, filename, this.state, this.setState.bind(this)),
                  update: (collection, document) => service.update(collection, document, this.state, this.setState.bind(this))
                }}
              />
              )
            }}
          />
          <Route
            exact
            path={ROUTES.HOME + "/:topicID" + "/:docID" + "/:stepID"}
            render={props => {

              return (<HomeObject

                topicID={props.match.params.topicID}
                docID={props.match.params.docID}
                stepID={props.match.params.stepID}
                animationItemID={null}

                history={props.history}

                window={this.props.window}
                superState={this.state}
                setSuperState={this.setState.bind(this).bind(this)}
                uploadHelpers={{
                  handleUploadStart: (id) => service.handleUploadStart(id, this.setState.bind(this)),
                  handleProgress: (progress) => service.handleProgress(progress, this.setState.bind(this)),
                  handleUploadError: (error) => service.handleUploadError(error, this.setState.bind(this)),
                  handleUploadSuccess: (collection, id, filename) => service.handleUploadSuccess(collection, id, filename, this.state, this.setState.bind(this)),
                  update: (collection, document) => service.update(collection, document, this.state, this.setState.bind(this))
                }}
              />
              )
            }}
          />
          <Route
            exact
            path={ROUTES.HOME + "/:topicID" + "/:docID" + "/:stepID" + "/:animationItemID"}
            render={props => {
              return (<HomeObject

                topicID={props.match.params.topicID}
                docID={props.match.params.docID}
                stepID={props.match.params.stepID}
                animationItemID={props.match.params.animationItemID}

                history={props.history}

                window={this.props.window}
                superState={this.state}
                setSuperState={this.setState.bind(this).bind(this)}
                uploadHelpers={{
                  handleUploadStart: (id) => service.handleUploadStart(id, this.setState.bind(this)),
                  handleProgress: (progress) => service.handleProgress(progress, this.setState.bind(this)),
                  handleUploadError: (error) => service.handleUploadError(error, this.setState.bind(this)),
                  handleUploadSuccess: (collection, id, filename) => service.handleUploadSuccess(collection, id, filename, this.state, this.setState.bind(this)),
                  update: (collection, document) => service.update(collection, document, this.state, this.setState.bind(this))
                }}
              />
              )
            }}
          />
        </div>
      </Router>
    );
  }
}
export default App;
