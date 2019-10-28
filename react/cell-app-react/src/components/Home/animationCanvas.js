import { Card, Segment, Icon, Image } from "semantic-ui-react";
import React from "react";
import Draggable from "react-draggable";
class AnimationCanvas extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    return (
      <Draggable>
        <Image
          src="https://firebasestorage.googleapis.com/v0/b/cell-app.appspot.com/o/images%2Fbd7398fc-6563-45f6-a6e2-75326fdd1973.png?alt=media&token=aa2d3ddc-a387-436c-a483-1ac23e9cf06b"
          draggable="false"
        />
      </Draggable>
    );
  }
}
export default AnimationCanvas;
