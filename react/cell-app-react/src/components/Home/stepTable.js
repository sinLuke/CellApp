import React from "react";
import { Table, Button, Icon, Image } from "semantic-ui-react";
class StepTable extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  cellClicked(id) {
    this.props.setSelected(id);
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
          {this.props.steps.map(step => (
            <Table.Row
              style={{ cursor: "pointer" }}
              key={step.id}
              onClick={() => this.cellClicked(step.id)}
            >
              <Table.Cell
                style={{
                  backgroundColor:
                    this.props.selected === step.id ? "#007affff" : "white",
                  color: this.props.selected === step.id ? "white" : "black"
                }}
              >
                Step {step.PAGE_NUMBER}
              </Table.Cell>
            </Table.Row>
          ))}
        </Table.Body>
        <Table.Footer fullWidth>
          <Table.Row>
            <Table.HeaderCell colSpan="4">
              <Button
                floated="left"
                icon
                labelPosition="right"
                size="small"
                primary
              >
                <Icon name="add" /> Add Step
              </Button>
            </Table.HeaderCell>
          </Table.Row>
        </Table.Footer>
      </Table>
    );
  }
}
export default StepTable;
