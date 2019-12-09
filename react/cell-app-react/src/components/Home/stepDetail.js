import {
  Table,
  Form,
  Image,
  Input,
  Select,
  TextArea,
  Button,
  Segment,
  Progress,
  Label,
  Icon
} from "semantic-ui-react";
import React from "react";
import FileUploader from "react-firebase-file-uploader";
import firebase from "firebase";

class StepDetail extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const typeOptions = [
      { key: "interactive", text: `Interactive Animation`, value: 0 },
      { key: "auto", text: `Auto Animation`, value: 1 },
      { key: "choice_quiz", text: `Multiple Choice`, value: 10 },
      { key: "fill_quiz", text: `Fill Blanks`, value: 11 }
    ];
    console.log("topicList", this.props);
    const documentOptions = Object.values(this.props.documentList)
      .map(
        function(document) {
          if (this.props.topicList[document.TOPIC_ID]) {
            return {
              key: document.id,
              text: `${this.props.topicList[document.TOPIC_ID].TOPIC_NAME} - ${
                document.DOCUMENT_NAME
              }`,
              value: document.id
            };
          } else {
            return {
              key: document.id,
              text: `${document.DOCUMENT_NAME}`,
              value: document.id
            };
          }
        }.bind(this)
      )
      .sort(function(a, b) {
        if (a.text > b.text) {
          return -1;
        }
        if (a.text < b.text) {
          return 1;
        }
        return 0;
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
                {/* Step ID */}
                <Form.Group widths="equal">
                  <Form.Field
                    id="step-detail-id-field"
                    control={Input}
                    label="Step ID"
                    value={this.props.step.id}
                    disabled
                  />
                </Form.Group>

                {/* Document ID */}

                <Form.Group widths="equal">
                  <Form.Field
                    options={documentOptions}
                    control={Select}
                    label={{
                      children: "In Document",
                      htmlFor: "step-detail-document-id"
                    }}
                    value={this.props.step.DOCUMENT_ID}
                    onChange={function(e, { value }) {
                      this.props.setKeyValue("DOCUMENT_ID", value);
                    }.bind(this)}
                    disabled
                  />
                </Form.Group>

                {/* PageNumber and Type */}

                <Form.Group widths="equal">
                  <Form.Field
                    control={Input}
                    label={{
                      children: "Page Number",
                      htmlFor: "step-detail-page-number-field"
                    }}
                    value={function() {
                      if (isNaN(this.props.step.PAGE_NUMBER)) {
                        return 0;
                      } else {
                        return this.props.step.PAGE_NUMBER + 1;
                      }
                    }.bind(this)()}
                    onChange={function(e, { value }) {
                      var newPageNumber = parseInt(value);
                      if (isNaN(newPageNumber)) {
                        newPageNumber = 1;
                      }
                      this.props.setKeyValue("PAGE_NUMBER", newPageNumber - 1);
                    }.bind(this)}
                  />
                  <Form.Field
                    options={typeOptions}
                    control={Select}
                    label={{
                      children: "Type",
                      htmlFor: "step-detail-animation-field"
                    }}
                    value={this.props.step.TYPE}
                    onChange={function(e, { value }) {
                      this.props.setKeyValue("TYPE", value);
                    }.bind(this)}
                  />
                </Form.Group>

                {/* Main TextField */}

                <Form.Field
                  id="step-detail-text-area"
                  control={TextArea}
                  label="Text"
                  value={this.props.step.TEXT}
                  onChange={function(e, { value }) {
                    this.props.setKeyValue("TEXT", value);
                  }.bind(this)}
                />

                {/* Choice TextField */}

                {this.props.step.TYPE == 10 ? (
                  <Form.Field
                    id="step-detail-choices-area"
                    control={TextArea}
                    label='Choices (seperate by ",")'
                    value={this.props.step.CHOICES}
                    onChange={function(e, { value }) {
                      this.props.setKeyValue("CHOICES", value);
                    }.bind(this)}
                  />
                ) : (
                  " "
                )}

                {/* Answer TextField */}

                {this.props.step.TYPE == 10 || this.props.step.TYPE == 11 ? (
                  <Form.Field
                    id="step-detail-answer-area"
                    control={TextArea}
                    label="Answer"
                    disabled={this.props.step.TYPE == 10}
                    value={this.props.step.ANSWER}
                    onChange={function(e, { value }) {
                      this.props.setKeyValue("ANSWER", value);
                    }.bind(this)}
                  />
                ) : (
                  " "
                )}

                {/* Answer Explanation */}

                {this.props.step.TYPE == 10 || this.props.step.TYPE == 11 ? (
                  <Form.Field
                    id="step-detail-answer-explanation-area"
                    control={TextArea}
                    label="Answer Explanation"
                    value={this.props.step.ANSWER_EXPLANATION}
                    onChange={function(e, { value }) {
                      this.props.setKeyValue("ANSWER_EXPLANATION", value);
                    }.bind(this)}
                  />
                ) : (
                  " "
                )}

                {/* Multiple Choice Preview Area */}

                {this.props.step.TYPE == 10 && this.props.step.CHOICES ? (
                  <div style={{ paddingBottom: "0.5em" }}>
                    <Form.Field
                      id="step-detail-choice-field"
                      label="Select the correct answer"
                    />
                    {this.props.step.CHOICES.trim()
                      .split(",")
                      .filter(choice => {
                        return choice.trim().toLowerCase() !== "";
                      })
                      .map(choice => (
                        <div>
                          {this.props.step.ANSWER.trim().toLowerCase() ===
                            choice.trim().toLowerCase() &&
                          choice.trim().toLowerCase() !== "" ? (
                            <Label color="red" style={{ margin: "0.2em" }}>
                              <Icon name="check" />
                              {choice}
                            </Label>
                          ) : (
                            <Label
                              as="a"
                              onClick={() =>
                                this.props.setKeyValue("ANSWER", choice.trim())
                              }
                              style={{ margin: "0.2em" }}
                            >
                              {choice}
                            </Label>
                          )}

                          <br></br>
                        </div>
                      ))}
                  </div>
                ) : (
                  " "
                )}

                {/* Step Image */}

                <Form.Field label="Step Image"></Form.Field>
                {this.props.step.IMAGE_URL ? (
                  <Segment>
                    <Image
                      fluid
                      draggable="false"
                      src={this.props.step.IMAGE_URL}
                    />
                  </Segment>
                ) : (
                  " "
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
                        name="step_image"
                        randomizeFilename
                        hidden
                        storageRef={firebase.storage().ref("images")}
                        onUploadStart={function() {
                          this.props.uploadHelpers.handleUploadStart(
                            this.props.step.id
                          );
                        }.bind(this)}
                        onUploadError={
                          this.props.uploadHelpers.handleUploadError
                        }
                        onUploadSuccess={function(filename) {
                          this.props.uploadHelpers.handleUploadSuccess(
                            2,
                            this.props.step.id,
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
                        !this.props.step.IMAGE_URL || this.props.isUploading
                      }
                      onClick={function() {
                        this.props.setKeyValue("IMAGE_URL", null);
                      }.bind(this)}
                    />
                  </Form.Group>
                )}

                {/* Step Image Subtitle */}

                <Form.Group widths="equal">
                  <Form.Field
                    id="step-detail-image-subtitle-field"
                    control={Input}
                    label="Image Subtitle"
                    value={this.props.step.IMAGE_SUBTITLE}
                    onChange={function(e, { value }) {
                      this.props.setKeyValue("IMAGE_SUBTITLE", value);
                    }.bind(this)}
                  />
                </Form.Group>

                {/* Save Button */}

                <Form.Field
                  id="step-detail-comform"
                  control={Button}
                  content="Save"
                  primary
                  disabled={
                    !this.props.changed.has(this.props.step.id) ||
                    this.props.isUploading
                  }
                  onClick={function() {
                    this.props.uploadHelpers.update(2, this.props.step.id);
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
export default StepDetail;
