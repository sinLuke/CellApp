import React from "react";
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
      step: null
    };
    this.topicList = {
      zHC1UQ9hpS1bFuNT0m2yA: {
        TOPIC_NAME:
          "Primary cells provide an excellent model to study disease and test drugs. Since they are not modified, they usually retain many of the characteristics of in vivo cells from the tissue they were obtained from.",
        IMAGE_URL: "https://www.w3schools.com/w3css/img_lights.jpg",
        id: "zHC1UQ9hpS1bFuNT0m2yA"
      },
      zHC1UQ9hpS1bFuNT0m2yB: {
        TOPIC_NAME: "topic1",
        IMAGE_URL: "https://www.w3schools.com/w3css/img_lights.jpg",
        id: "zHC1UQ9hpS1bFuNT0m2yB"
      },
      zHC1UQ9hpS1bFuNT0m2y: {
        TOPIC_NAME: "topic2",
        IMAGE_URL: "https://www.w3schools.com/w3css/img_lights.jpg",
        id: "zHC1UQ9hpS1bFuNT0m2y"
      }
    };
    this.stepList = {
      ztMf0bhWQH4oerYvgq6J: {
        AUTO_ANIMATION: 1,
        DOCUMENT_ID: "C9D7OVhbccVDxvxMJe7v",
        PAGE_NUMBER: 1,
        IMAGE_URL:
          "https://i.all3dp.com/cdn-cgi/image/fit=cover,w=1284,h=722,gravity=0.5x0.5,format=auto/wp-content/uploads/2018/12/28144052/background-images-can-come-in-handy-when-modeling-tian-ooi-all3dp-181228.jpg",
        IMAGE_SUBTITLE: "Subtitle",
        TEXT:
          "imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet.",
        id: "ztMf0bhWQH4oerYvgq6J"
      },
      ztMf0bhWQH4oerYvgq6K: {
        AUTO_ANIMATION: 1,
        DOCUMENT_ID: "C9D7OVhbccVDxvxMJe7v",
        PAGE_NUMBER: 2,
        IMAGE_URL: "https://www.w3schools.com/w3css/img_snowtops.jpg",
        TEXT:
          "imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet.",
        id: "ztMf0bhWQH4oerYvgq6K"
      }
    };
    this.documentList = {
      C9D7OVhbccVDxvxMJe7v: {
        DOCUMENT_NAME: "Document 1",
        DIFFICULTY: "topic0",
        INTRODUCTION: "https://www.w3schools.com/w3css/img_lights.jpg",
        TOPIC_ID: "zHC1UQ9hpS1bFuNT0m2y",
        available_scope: 10,
        id: "C9D7OVhbccVDxvxMJe7v"
      }
    };
    this.setSelected = this.setSelected.bind(this);
  }
  setSelected(item, id) {
    console.log(this);
    switch (item) {
      case 0:
        if (Object.keys(this.topicList).includes(id)) {
          this.setState({ topic: id, document: null, step: null });
        } else {
          this.setState({ topic: null, document: null, step: null });
        }

        break;
      case 1:
        if (Object.keys(this.documentList).includes(id)) {
          var topicID = this.documentList[id].TOPIC_ID;
          if (Object.keys(this.topicList).includes(topicID)) {
            this.setState({ topic: topicID, document: id, step: null });
          } else {
            this.setState({ topic: null, document: null, step: null });
          }
        } else {
          this.setState({ document: null, step: null });
        }
        break;
      default:
        if (Object.keys(this.stepList).includes(id)) {
          var documentID = this.stepList[id].DOCUMENT_ID;
          if (Object.keys(this.documentList).includes(documentID)) {
            var topicID = this.documentList[documentID].TOPIC_ID;
            if (Object.keys(this.topicList).includes(topicID)) {
              this.setState({ topic: topicID, document: documentID, step: id });
            } else {
              this.setState({ topic: null, document: null, step: null });
            }
          } else {
            this.setState({ document: null, step: null });
          }
        } else {
          this.setState({ step: null });
        }
    }
  }

  hello() {
    console.log("hello");
  }
  load() {}
  render() {
    console.log(this.topicList, this.documentList, this.stepList);
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
                  topicList: this.topicList,
                  documentList: this.documentList,
                  stepList: this.stepList
                }}
              />
            )}
            prop={this.hello}
          />
        </div>
      </Router>
    );
  }
}
export default App;
