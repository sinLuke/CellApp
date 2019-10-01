import React from "react";
import { Table, Button, Icon, Image, Grids } from "semantic-ui-react";
class TopicTable extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  cellClicked(id) {
    console.log(this);
    this.props.setSelected(id);
  }

  render() {
    console.log(this.props);
    return (
      <Table celled>
        <Table.Header>
          <Table.Row>
            <Table.HeaderCell>Topics</Table.HeaderCell>
          </Table.Row>
        </Table.Header>

        <Table.Body>
          {this.props.topics.map(topic => (
            <Table.Row
              key={topic.id}
              style={{ cursor: "pointer" }}
              onClick={() => this.cellClicked(topic.id)}
            >
              <Table.Cell
                style={{
                  backgroundColor:
                    this.props.selected === topic.id ? "#007affff" : "white",
                  color: this.props.selected === topic.id ? "white" : "black"
                }}
              >
                {topic.TOPIC_NAME}
              </Table.Cell>
            </Table.Row>
          ))}
        </Table.Body>

        <Table.Footer fullWidth>
          <Table.Row>
            <Table.HeaderCell colSpan="4">
              <Button floated="left" icon labelPosition="right" size="small">
                <Icon name="add" /> Add Topic
              </Button>
            </Table.HeaderCell>
          </Table.Row>
        </Table.Footer>
      </Table>
    );
  }
}
export default TopicTable;
