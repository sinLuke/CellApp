import { Table, Checkbox, Image, Form, Input } from "semantic-ui-react";
import React from "react";
import AnimationCanvas from "./animationCanvas";

class AnimationStepDetail extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      start: true
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
        function (document) {
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
      .sort(function (a, b) {
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
              Start
              <Checkbox
                style={{
                  paddingTop: "4px",
                  marginLeft: "16px",
                  marginRight: "16px"
                }}
                slider
                value={this.state.start}
                onChange={this.changeStartEnd}
              />
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
                    width: "100%",
                    height: "100%"
                  }}
                >
                  <AnimationCanvas
                    start={this.state.start}
                    item={this.props.item}
                    items={Object.values(this.props.lists).filter(item => {
                      return item.STEP_ID === this.props.item.STEP_ID;
                    })}
                    onControlledDragStop={(e, x, y) => {

                      if (x.isNaN) {
                        x = 0
                      }

                      if (y.isNaN) {
                        y = 0
                      }

                      if (this.state.start) {
                        this.props.setKeyValue("START_POSITION_X", x);
                        this.props.setKeyValue("START_POSITION_Y", y);
                      } else {
                        this.props.setKeyValue("END_POSITION_X", x);
                        this.props.setKeyValue("END_POSITION_Y", y);
                      }
                    }}
                  />
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
                    value={this.props.item.DESCRIPTION}
                    onChange={function (e, { value }) {
                      this.props.setKeyValue("DESCRIPTION", value);
                    }.bind(this)}
                  />
                  <Form.Field
                    id="animation-item-detail-size-field"
                    control={Input}
                    label="Size"
                    value={(() => {
                      if (isNaN(this.props.item.SIZE)) {
                        return "10";
                      } else {
                        return this.props.item.SIZE;
                      }
                    })()}
                    onChange={(e, { value }) => {
                      let intValue = parseInt(value)
                      this.props.setKeyValue("SIZE", intValue);
                    }}
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
