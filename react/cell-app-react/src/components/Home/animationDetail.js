import { Card, Segment, Icon, Image } from "semantic-ui-react";
import React from "react";
import AnimationStepDetail from "./animationStepDetail";
class AnimationDetail extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    var _step = this.props.lists.stepList[this.props.stepSelected];
    if (this.props.topicSelected) {
      if (this.props.documentSelected) {
        if (this.props.stepSelected) {
          if (_step && _step.IMAGE_URL) {
            return (
              <AnimationStepDetail
                step={this.props.lists.stepList[this.props.stepSelected]}
                stepCount={
                  this.props.filterAnimationList(this.props.stepSelected).length
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
              <Segment warning disabled inverted textAlign="center">
                Please upload an image to add animations on this step.
              </Segment>
            );
          }
        } else {
          return (
            <Segment warning disabled textAlign="center">
              Please select a step to edit the animation.
            </Segment>
          );
        }
      } else {
        return (
          <Segment warning disabled textAlign="center">
            Please select a step to edit the animation.
          </Segment>
        );
      }
    } else {
      return (
        <Segment warning disabled textAlign="center">
          Please select a step to edit the animation.
        </Segment>
      );
    }
  }
}
export default AnimationDetail;
