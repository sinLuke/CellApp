import React from "react";
import { Table, Label } from "semantic-ui-react";
import { setState } from "expect/build/jestMatchersObject";
class TopicTable extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      topicList: [
        { name: "topic0", id: "aaaaaa" },
        { name: "topic1", id: "bbbbbb" },
        { name: "topic2", id: "cccccc" }
      ],
      selected: "aaaaaa"
    };
  }

  cellClicked(id) {
    this.setState({ selected: id });
  }

  render() {
    return (
      <Table celled>
        <Table.Header>
          <Table.Row>
            <Table.HeaderCell>Topics</Table.HeaderCell>
          </Table.Row>
        </Table.Header>

        <Table.Body>
          {this.state.topicList.map(topic => (
            <Table.Row
              key={topic.id}
              onClick={() => this.cellClicked(topic.id)}
            >
              <Table.Cell
                style={{
                  backgroundColor:
                    this.state.selected == topic.id ? "#007affff" : "white",
                  color: this.state.selected == topic.id ? "white" : "black"
                }}
              >
                topic.name
              </Table.Cell>
            </Table.Row>
          ))}
        </Table.Body>
      </Table>
    );
  }
}
export default TopicTable;
