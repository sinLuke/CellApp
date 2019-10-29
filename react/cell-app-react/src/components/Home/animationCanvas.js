import { Card, Segment, Icon, Image } from "semantic-ui-react";
import React from "react";
import Draggable from "react-draggable";
class AnimationCanvas extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      width: 100,
      height: 100
    };
  }
  componentDidMount() {
    this.setState({
      width: this.container.offsetWidth,
      height: this.container.offsetHeight
    });
  }
  render() {
    console.log(this.props.items);

    return (
      <div
        ref={el => (this.container = el)}
        style={{ width: "100%", height: "100%" }}
      >
        {this.props.items.map(item => (
          <Draggable
            style={{
              zIndex: item.id === this.props.item.id ? "100" : "1",
              position: "absolute"
            }}
            onStart={() => item.id === this.props.item.id}
            bounds="parent"
            onStop={(e, position) => {
              const { x, y } = position;
              this.props.onControlledDragStop(
                e,
                x / 0.01 / this.state.width,
                y / 0.01 / this.state.height
              );
            }}
            position={{
              x:
                (this.props.start
                  ? item.START_POSITION_X
                  : item.END_POSITION_X) *
                0.01 *
                this.state.width,
              y:
                (this.props.start
                  ? item.START_POSITION_Y
                  : item.END_POSITION_Y) *
                0.01 *
                this.state.height
            }}
          >
            <Image
              style={{
                position: "absolute",
                height: `${item.SIZE}%`,
                outline:
                  item.id === this.props.item.id ? "2px dotted #000000" : "",
                backgroundColor:
                  item.id === this.props.item.id ? "rgba(255, 255, 0, 0.3)" : ""
              }}
              src={item.IMAGE_URL}
              draggable="false"
            />
          </Draggable>
        ))}
      </div>
    );
  }
}
export default AnimationCanvas;
