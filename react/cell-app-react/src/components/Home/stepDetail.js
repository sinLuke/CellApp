import {
  Table,
  Form,
  Image,
  Input,
  Select,
  TextArea,
  Button,
  Segment
} from "semantic-ui-react";
import React from "react";
class StepDetail extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    console.log(this.props.stepCount);
    const pageNumberOptions = [...new Array(this.props.stepCount).keys()].map(
      function(i) {
        return { key: i, text: `Page ${i + 1}`, value: i + 1 };
      }
    );
    const animationOptions = [
      { key: "auto", text: `Auto`, value: 1 },
      { key: "interactive", text: `Interactive`, value: 0 }
    ];
    console.log(pageNumberOptions);
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
                    id="step-detail-id-field"
                    control={Input}
                    label="Step ID"
                    value={this.props.step.id}
                    disabled
                  />
                </Form.Group>
                <Form.Group widths="equal">
                  <Form.Field
                    options={pageNumberOptions}
                    control={Select}
                    label={{
                      children: "Page Number",
                      htmlFor: "step-detail-page-number-field"
                    }}
                    value={this.props.step.PAGE_NUMBER}
                  />
                  <Form.Field
                    options={animationOptions}
                    control={Select}
                    label={{
                      children: "Animation",
                      htmlFor: "step-detail-animation-field"
                    }}
                    value={this.props.step.AUTO_ANIMATION}
                  />
                </Form.Group>

                <Form.Field
                  id="step-detail-text-area"
                  control={TextArea}
                  label="Text"
                  value={this.props.step.TEXT}
                />
                <Segment>
                  <Image fluid src={this.props.step.IMAGE_URL} />
                </Segment>

                <Form.Group>
                  <Form.Field
                    id="step-detail-upload-image"
                    control={Button}
                    content="Upload ..."
                  />
                  <Form.Field
                    id="step-detail-remove-image"
                    control={Button}
                    content="Remove Image"
                    color="red"
                  />
                </Form.Group>
                <Form.Group widths="equal">
                  <Form.Field
                    id="step-detail-image-subtitle-field"
                    control={Input}
                    label="Image Subtitle"
                    value={this.props.step.IAMGE_SUBTITLE}
                  />
                </Form.Group>
                <Form.Field
                  id="form-button-control-public"
                  control={Button}
                  content="Confirm"
                  primary
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
