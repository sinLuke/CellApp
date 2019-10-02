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
    if (this.props.topicSelected) {
      if (this.props.documentSelected) {
        if (this.props.stepSelected) {
          return (
            <StepDetail
              step={this.props.lists.stepList[this.props.stepSelected]}
              stepCount={
                this.props.filterStepList(this.props.documentSelected).length
              }
              title={`${this.props.lists.documentList[this.props.documentSelected].DOCUMENT_NAME} - Step ${this.props.lists.stepList[this.props.stepSelected].PAGE_NUMBER}`}
              changed={this.props.changed}
              uploadHelpers={this.props.uploadHelpers}
              setKeyValue={function(key, value) {
                this.props.setKeyValue(
                  2,
                  this.props.stepSelected,
                  this.props.documentSelected,
                  key,
                  value
                );
              }.bind(this)}
              isUploading={this.props.isUploading}
              progress={this.props.progress}
              documentList={this.props.lists.documentList}
              topicList={this.props.lists.topicList}
            />
          );
        } else {
          return (
            <DocumentDetail
              document={
                this.props.lists.documentList[this.props.documentSelected]
              }
              changed={this.props.changed}
              title={`${this.props.lists.documentList[this.props.documentSelected].DOCUMENT_NAME}`}
              uploadHelpers={this.props.uploadHelpers}
              setKeyValue={function(key, value) {
                this.props.setKeyValue(
                  1,
                  this.props.documentSelected,
                  this.props.topicSelected,
                  key,
                  value
                );
              }.bind(this)}
              isUploading={this.props.isUploading}
              progress={this.props.progress}
              topicList={this.props.lists.topicList}
            />
          );
        }
      } else {
        return (
          <TopicDetail
            topic={this.props.lists.topicList[this.props.topicSelected]}
            changed={this.props.changed}
            uploadHelpers={this.props.uploadHelpers}
            title={`${this.props.lists.topicList[this.props.topicSelected].TOPIC_NAME}`}
            setKeyValue={function(key, value) {
              this.props.setKeyValue(
                0,
                this.props.topicSelected,
                null,
                key,
                value
              );
            }.bind(this)}
            isUploading={this.props.isUploading}
            progress={this.props.progress}
          />
        );
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
