import { Segment, Progress, Form, Button } from "semantic-ui-react";
import FileUploader from "react-firebase-file-uploader";
import firebase from "firebase";
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
            if (this.props.item) {
              if (this.props.item.IMAGE_URL) {
                console.log(this.props.lists.animationItemList);
                console.log(this.props.item);
                return (
                  <AnimationStepDetail
                    step={this.props.lists.stepList[this.props.stepSelected]}
                    item={this.props.item}
                    stepCount={
                      this.props.filterAnimationList(this.props.stepSelected)
                        .length
                    }
                    title={`${this.props.lists.documentList[this.props.documentSelected].DOCUMENT_NAME} - Step ${this.props.lists.stepList[this.props.stepSelected].PAGE_NUMBER}`}
                    changed={this.props.changed}
                    uploadHelpers={this.props.uploadHelpers}
                    setKeyValue={function(key, value) {
                      this.props.setKeyValue(
                        3,
                        this.props.item.id,
                        this.props.stepSelected,
                        key,
                        value
                      );
                    }.bind(this)}
                    lists={this.props.lists.animationItemList}
                    isUploading={this.props.isUploading}
                    progress={this.props.progress}
                    documentList={this.props.lists.documentList}
                    topicList={this.props.lists.topicList}
                  />
                );
              } else {
                console.log(this.props.item);
                return (
                  <div>
                    <Segment warning disabled inverted textAlign="center">
                      Upload an image to create animationItem.
                    </Segment>
                    {this.props.isUploading ? (
                      <Progress
                        color={"grey"}
                        percent={this.props.progress}
                        indicating
                      />
                    ) : (
                      <Form>
                        <Form.Group>
                          <label
                            style={{
                              backgroundColor: "lightGrey",
                              color: "black",
                              marginLeft: 8,
                              padding: 8,
                              paddingLeft: 16,
                              paddingRight: 16,
                              borderRadius: 4,
                              cursor: "pointer"
                            }}
                          >
                            <b>Upload an Image</b>
                            <FileUploader
                              accept="image/*"
                              name="step_image"
                              randomizeFilename
                              hidden
                              storageRef={firebase.storage().ref("images")}
                              onUploadStart={function() {
                                this.props.uploadHelpers.handleUploadStart(
                                  this.props.item.id
                                );
                              }.bind(this)}
                              onUploadError={
                                this.props.uploadHelpers.handleUploadError
                              }
                              onUploadSuccess={function(filename) {
                                this.props.uploadHelpers.handleUploadSuccess(
                                  3,
                                  this.props.item.id,
                                  filename
                                );
                              }.bind(this)}
                              onProgress={
                                this.props.uploadHelpers.handleProgress
                              }
                              disabled={this.props.isUploading}
                            />
                          </label>
                        </Form.Group>
                      </Form>
                    )}
                  </div>
                );
              }
            } else {
              return (
                <Segment warning disabled inverted textAlign="center">
                  Please add an animation item to add animation to this step.
                </Segment>
              );
            }
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
