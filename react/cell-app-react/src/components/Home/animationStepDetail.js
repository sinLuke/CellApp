import { Table, Checkbox, Image, Form, Input } from "semantic-ui-react";
import React from "react";
import AnimationCanvas from "./animationCanvas";

class AnimationStepDetail extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      start: true,
      size: 1.0,
      description: "New Animation Item"
    };
    this.changeStartEnd = this.changeStartEnd.bind(this);
  }

  changeStartEnd(_, data) {
    this.setState({ start: !data.checked });
  }

  render() {
    const animationOptions = [
      { key: "auto", text: `Auto`, value: 2 },
      { key: "interactive", text: `Interactive`, value: 0 }
    ];
    console.log("topicList", this.props);
    const documentOptions = Object.values(this.props.documentList)
      .map(
        function(document) {
          if (this.props.topicList[document.TOPIC_ID]) {
            return {
              key: document.id,
              text: `${this.props.topicList[document.TOPIC_ID].TOPIC_NAME} - ${document.DOCUMENT_NAME}`,
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
      <Table compact celled color="black" inverted>
        <Table.Header>
          <Table.Row>
            <Table.HeaderCell>Animations - {this.props.title}</Table.HeaderCell>
            <Table.HeaderCell verticalAlign="middle">
              Start{" "}
              <Checkbox
                style={{
                  paddingTop: "4px",
                  marginLeft: "16px",
                  marginRight: "16px"
                }}
                slider
                value={this.state.start}
                onChange={this.changeStartEnd}
              />{" "}
              End
            </Table.HeaderCell>
          </Table.Row>
        </Table.Header>

        <Table.Body>
          <Table.Row>
            <Table.Cell style={{ padding: "16px" }}>
              <div
                className="animation_image_canvas"
                style={{ position: "relative" }}
              >
                <Image src={this.props.step.IMAGE_URL} fluid />
                <div
                  style={{
                    position: "absolute",
                    top: "0",
                    left: "0",
                    width: "10%",
                    height: "10%"
                  }}
                >
                  <AnimationCanvas />
                </div>
              </div>
            </Table.Cell>
          </Table.Row>
        </Table.Body>
        <Table.Footer fullWidth>
          <Table.Row>
            <Table.Cell>
              <Form inverted>
                <Form.Group widths="equal">
                  <Form.Field
                    id="animation-item-detail-size-field"
                    control={Input}
                    label="Description"
                    value={this.state.description}
                  />
                  <Form.Field
                    id="animation-item-detail-size-field"
                    control={Input}
                    label="Size"
                    value={this.state.size}
                  />
                </Form.Group>
              </Form>
            </Table.Cell>
          </Table.Row>
        </Table.Footer>
      </Table>
    );
  }
}
export default AnimationStepDetail;
