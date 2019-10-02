import React from "react";
import { Table, Button, Icon, Text } from "semantic-ui-react";
class ItemTable extends React.Component {
  constructor(props) {
    super(props);
    this.state = { loading: props.loading, type: props.type };
    this.getCellContent = this.getCellContent.bind(this);
    this.getAddButtonLabel = this.getAddButtonLabel.bind(this);
    this.cellClicked = this.cellClicked.bind(this);
  }

  getCellContent(item) {
    switch (this.state.type) {
      case 0:
        return item.TOPIC_NAME;
      case 1:
        return item.DOCUMENT_NAME;
      default:
        return `Step ${item.PAGE_NUMBER + 1}`;
    }
  }

  getAddButtonLabel() {
    switch (this.state.type) {
      case 0:
        return "Add Topic";
      case 1:
        return "Add Document";
      default:
        return "Add Step";
    }
  }

  cellClicked(id) {
    this.props.setSelected(id);
  }

  componentWillReceiveProps() {
    if (this.state.loading) {
      this.setState({ loading: false });
    }
  }

  render() {
    console.log("itemList item prop", this.props.items);
    return (
      <Table celled disabled={this.props.isUploading}>
        <Table.Header>
          <Table.Row>
            <Table.HeaderCell>{this.props.title}</Table.HeaderCell>
          </Table.Row>
        </Table.Header>

        <Table.Body>
          {this.props.items.map(item => (
            <Table.Row
              style={{ cursor: "pointer" }}
              key={item.id}
              onClick={function() {
                if (!this.props.isUploading) {
                  this.cellClicked(item.id);
                }
              }.bind(this)}
            >
              {this.props.changed.has(item.id) ? (
                <Table.Cell
                  warning={this.props.selected != item.id}
                  style={{
                    backgroundColor:
                      this.props.selected === item.id ? "#007affff" : "white",
                    color: this.props.selected === item.id ? "white" : "black"
                  }}
                >
                  <Icon name="dot circle outline" />
                  {this.getCellContent(item)}
                </Table.Cell>
              ) : (
                <Table.Cell
                  style={{
                    backgroundColor:
                      this.props.selected === item.id ? "#007affff" : "white",
                    color: this.props.selected === item.id ? "white" : "black"
                  }}
                >
                  {this.getCellContent(item)}
                </Table.Cell>
              )}
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
                loading={this.state.loading}
                disabled={this.state.loading}
                onClick={function() {
                  if (this.state.loading) {
                    return;
                  }
                  this.setState({ loading: true });
                  this.props.add();
                }.bind(this)}
              >
                <Icon name="add" /> {this.getAddButtonLabel()}
              </Button>
            </Table.HeaderCell>
          </Table.Row>
        </Table.Footer>
      </Table>
    );
  }
}
export default ItemTable;
