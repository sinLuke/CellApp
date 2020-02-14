import {
  Table,
  Form,
  Image,
  Input,
  Select,
  TextArea,
  Button,
  Segment,
  Progress
} from "semantic-ui-react";
import React from "react";
import FileUploader from "react-firebase-file-uploader";
import firebase from "firebase";

class TopicDetail extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <Table celled>
        <Table.Header>
          <Table.Row>
            <Table.HeaderCell>{this.props.title}</Table.HeaderCell>
          </Table.Row>
        </Table.Header>

        <Table.Body>
          <Table.Row>
            <Table.Cell>
              <Form>
                <Form.Group widths="equal">
                  <Form.Field
                    id="topic-detail-id-field"
                    control={Input}
                    label="Topic ID"
                    value={this.props.topic.id}
                    disabled
                  />
                </Form.Group>
                <Form.Group widths="equal">
                  <Form.Field
                    id="step-detail-image-subtitle-field"
                    control={Input}
                    label="Topic Name"
                    value={this.props.topic.TOPIC_NAME}
                    onChange={function(e, { value }) {
                      this.props.setKeyValue("TOPIC_NAME", value);
                    }.bind(this)}
                  />
                </Form.Group>
                <Form.Group widths="equal">
                  <Form.Field
                    id="step-detail-page-number-field"
                    control={Input}
                    label="Page Number"
                    value={function() {
                      if (isNaN(this.props.topic.PAGE_NUMBER)) {
                        return 0;
                      } else {
                        return this.props.topic.PAGE_NUMBER + 1;
                      }
                    }.bind(this)()}
                    onChange={function(e, { value }) {
                      var pageNumber = parseInt(value);
                      if (isNaN(pageNumber) || pageNumber < 0) {
                        pageNumber = 1;
                      }
                      this.props.setKeyValue("PAGE_NUMBER", pageNumber - 1);
                    }.bind(this)}
                  />
                </Form.Group>
                <Form.Field label="Topic Image"></Form.Field>
                {this.props.topic.IMAGE_URL ? (
                  <Segment>
                    <Image
                      draggable="false"
                      fluid
                      src={this.props.topic.IMAGE_URL}
                    />
                  </Segment>
                ) : (
                  <div />
                )}
                {this.props.isUploading ? (
                  <Progress
                    color={"grey"}
                    percent={this.props.progress}
                    indicating
                  />
                ) : (
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
                        name="topic_image"
                        randomizeFilename
                        hidden
                        storageRef={firebase.storage().ref("images")}
                        onUploadStart={function() {
                          this.props.uploadHelpers.handleUploadStart(
                            this.props.topic.id
                          );
                        }.bind(this)}
                        onUploadError={
                          this.props.uploadHelpers.handleUploadError
                        }
                        onUploadSuccess={function(filename) {
                          this.props.uploadHelpers.handleUploadSuccess(
                            0,
                            this.props.topic.id,
                            filename
                          );
                        }.bind(this)}
                        onProgress={this.props.uploadHelpers.handleProgress}
                        disabled={this.props.isUploading}
                      />
                    </label>

                    <Form.Field
                      id="step-detail-remove-image"
                      control={Button}
                      content="Remove Image"
                      color="red"
                      disabled={
                        !this.props.topic.IMAGE_URL || this.props.isUploading
                      }
                      onClick={function() {
                        this.props.setKeyValue("IMAGE_URL", null);
                      }.bind(this)}
                    />
                  </Form.Group>
                )}

                <Form.Field
                  id="step-detail-comform"
                  control={Button}
                  content="Save"
                  primary
                  disabled={
                    !this.props.changed.has(this.props.topic.id) ||
                    this.props.isUploading
                  }
                  onClick={function() {
                    this.props.uploadHelpers.update(0, this.props.topic.id);
                  }.bind(this)}
                />
              </Form>
            </Table.Cell>
          </Table.Row>
        </Table.Body>
      </Table>
    );
  }
}
export default TopicDetail;
