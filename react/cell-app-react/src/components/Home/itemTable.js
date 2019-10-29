import React from "react";
import { Table, Button, Icon, Image, Radio } from "semantic-ui-react";
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
      case 2:
        return `Step ${item.PAGE_NUMBER + 1}`;
      default:
        return item.DESCRIPTION;
    }
  }

  getAddButtonLabel() {
    switch (this.state.type) {
      case 0:
        return "Add Topic";
      case 1:
        return "Add Document";
      case 2:
        return "Add Step";
      default:
        return "Add Animation Item";
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
      <Table
        celled
        disabled={this.props.isUploading}
        inverted={this.props.editAnimation}
      >
        <Table.Header>
          <Table.Row>
            <Table.HeaderCell singleLine>{this.props.title}</Table.HeaderCell>
            {this.props.type === 3 ? (
              <Table.HeaderCell>Edit</Table.HeaderCell>
            ) : (
              " "
            )}
          </Table.Row>
        </Table.Header>

        <Table.Body padded>
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
              <Table.Cell
                warning={
                  this.props.selected !== item.id &&
                  this.props.changed.has(item.id) &&
                  !this.props.editAnimation
                }
                style={{
                  backgroundColor: this.props.editAnimation
                    ? this.props.selected === item.id
                      ? "#0a84ffff"
                      : "#333333ff"
                    : this.props.selected === item.id
                    ? "#007affff"
                    : "white",
                  color: this.props.editAnimation
                    ? this.props.selected === item.id
                      ? "#black"
                      : "white"
                    : this.props.selected === item.id
                    ? "white"
                    : "black"
                }}
              >
                {this.props.changed.has(item.id) ? (
                  <Icon name="dot circle outline" />
                ) : (
                  " "
                )}
                {this.getCellContent(item)}
              </Table.Cell>
              {this.props.type === 3 ? (
                <Table.Cell
                  warning={
                    this.props.selected !== item.id &&
                    this.props.changed.has(item.id) &&
                    !this.props.editAnimation
                  }
                  style={{
                    backgroundColor: this.props.editAnimation
                      ? this.props.selected === item.id
                        ? "#0a84ffff"
                        : "#333333ff"
                      : this.props.selected === item.id
                      ? "#007affff"
                      : "white",
                    color: this.props.editAnimation
                      ? this.props.selected === item.id
                        ? "#black"
                        : "white"
                      : this.props.selected === item.id
                      ? "white"
                      : "black"
                  }}
                >
                  {item.IMAGE_URL ? (
                    <Button
                      color="red"
                      disabled={!item.IMAGE_URL || this.props.isUploading}
                      onClick={function() {
                        this.props.setKeyValue(
                          3,
                          item.id,
                          null,
                          "IMAGE_URL",
                          null
                        );
                      }.bind(this)}
                    >
                      Delete Image
                    </Button>
                  ) : (
                    "No Image Uploaded"
                  )}
                </Table.Cell>
              ) : (
                " "
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
