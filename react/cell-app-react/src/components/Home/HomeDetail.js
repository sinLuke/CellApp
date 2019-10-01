import { Card, Segment, Icon, Image } from "semantic-ui-react";
import React from "react";
import StepDetail from "./stepDetail";
import DocumentDetail from "./documentDetail";
import TopicDetail from "./topicDetail";
class HomeDetail extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    console.log(this.props);
    if (this.props.topicSelected) {
      if (this.props.documentSelected) {
        if (this.props.stepSelected) {
          return (
            <StepDetail
              step={this.props.lists.stepList[this.props.stepSelected]}
              stepCount={Object.keys(this.props.lists.stepList).length}
              title={`${this.props.lists.documentList[this.props.documentSelected].DOCUMENT_NAME} - Step ${this.props.lists.stepList[this.props.stepSelected].PAGE_NUMBER}`}
            />
          );
        } else {
          return <DocumentDetail />;
        }
      } else {
        console.log(this.props);
        return <TopicDetail />;
      }
    } else {
      return (
        <Segment disabled textAlign="center">
          Select anything on the left
        </Segment>
      );
    }
  }
}
export default HomeDetail;
