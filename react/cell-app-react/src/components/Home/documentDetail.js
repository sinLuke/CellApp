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

class DocumentDetail extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const topicOptions = Object.values(this.props.topicList).map(function(
      topic
    ) {
      return {
        key: topic.id,
        text: topic.TOPIC_NAME,
        value: topic.id
      };
    });

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
                    id="document-detail-id-field"
                    control={Input}
                    label="Document ID"
                    value={this.props.document.id}
                    disabled
                  />
                </Form.Group>
                <Form.Group widths="equal">
                  <Form.Field
                    id="document-detail-name-field"
                    control={Input}
                    label="Document Name"
                    value={this.props.document.DOCUMENT_NAME}
                    onChange={function(e, { value }) {
                      this.props.setKeyValue("DOCUMENT_NAME", value);
                    }.bind(this)}
                  />
                </Form.Group>
                <Form.Group widths="equal">
                  <Form.Field
                    options={topicOptions}
                    control={Select}
                    label={{
                      children: "In Topic",
                      htmlFor: "document-detail-topic-id"
                    }}
                    value={this.props.document.TOPIC_ID}
                    onChange={function(e, { value }) {
                      this.props.setKeyValue("TOPIC_ID", value);
                    }.bind(this)}
                  />
                </Form.Group>
                <Form.Group widths="equal">
                  <Form.Field
                    id="document-detail-difficulty-field"
                    control={Input}
                    label="Difficulty"
                    value={function() {
                      if (isNaN(this.props.document.DIFFICULTY)) {
                        return 0;
                      } else {
                        return this.props.document.DIFFICULTY;
                      }
                    }.bind(this)()}
                    onChange={function(e, { value }) {
                      var difficulty = parseInt(value);
                      if (isNaN(difficulty) || difficulty < 1) {
                        difficulty = 0;
                      }
                      this.props.setKeyValue("DIFFICULTY", difficulty);
                    }.bind(this)}
                  />
                  <Form.Field
                    id="document-detail-pagenumber-field"
                    control={Input}
                    label="Page Number"
                    value={function() {
                      if (isNaN(this.props.document.PAGE_NUMBER)) {
                        return 0;
                      } else {
                        return this.props.document.PAGE_NUMBER + 1;
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
                <Form.Field
                  id="document-detail-text-area"
                  control={TextArea}
                  label="Introduction"
                  value={this.props.document.INTRODUCTION}
                  onChange={function(e, { value }) {
                    this.props.setKeyValue("INTRODUCTION", value);
                  }.bind(this)}
                />
                <Form.Field label="Document Image"></Form.Field>
                {this.props.document.IMAGE_URL ? (
                  <Segment>
                    <Image
                      draggable="false"
                      fluid
                      src={this.props.document.IMAGE_URL}
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
                        name="document_image"
                        randomizeFilename
                        hidden
                        storageRef={firebase.storage().ref("images")}
                        onUploadStart={function() {
                          this.props.uploadHelpers.handleUploadStart(
                            this.props.document.id
                          );
                        }.bind(this)}
                        onUploadError={
                          this.props.uploadHelpers.handleUploadError
                        }
                        onUploadSuccess={function(filename) {
                          this.props.uploadHelpers.handleUploadSuccess(
                            1,
                            this.props.document.id,
                            filename
                          );
                        }.bind(this)}
                        onProgress={this.props.uploadHelpers.handleProgress}
                        disabled={this.props.isUploading}
                      />
                    </label>

                    <Form.Field
                      id="document-detail-remove-image"
                      control={Button}
                      content="Remove Image"
                      color="red"
                      disabled={
                        !this.props.document.IMAGE_URL || this.props.isUploading
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
                    !this.props.changed.has(this.props.document.id) ||
                    this.props.isUploading
                  }
                  onClick={function() {
                    this.props.uploadHelpers.update(1, this.props.document.id);
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
export default DocumentDetail;
