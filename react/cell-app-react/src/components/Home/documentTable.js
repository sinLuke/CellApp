import React from "react";
import { Table, Button, Icon, Image } from "semantic-ui-react";
class DocumentTable extends React.Component {
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
          {this.props.docs.map(doc => (
            <Table.Row
              style={{ cursor: "pointer" }}
              key={doc.id}
              onClick={() => this.cellClicked(doc.id)}
            >
              <Table.Cell
                style={{
                  backgroundColor:
                    this.props.selected === doc.id ? "#007affff" : "white",
                  color: this.props.selected === doc.id ? "white" : "black"
                }}
              >
                {doc.DOCUMENT_NAME}
              </Table.Cell>
            </Table.Row>
          ))}
        </Table.Body>
        <Table.Footer fullWidth>
          <Table.Row>
            <Table.HeaderCell colSpan="4">
              <Button floated="left" icon labelPosition="right" size="small">
                <Icon name="add" /> Add Document
              </Button>
            </Table.HeaderCell>
          </Table.Row>
        </Table.Footer>
      </Table>
    );
  }
}
export default DocumentTable;
