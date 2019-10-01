import { Card, Segment, Icon, Image } from "semantic-ui-react";
import React from "react";
class TopicDetail extends React.Component {
  constructor(props) {
    super(props);
    console.log(this.props);
  }
  render() {
    console.log(this.props);
    return (
      <Card>
        <Image src="/images/avatar/large/matthew.png" wrapped ui={false} />
        <Card.Content>
          <Card.Header>Matthew</Card.Header>
          <Card.Meta>
            <span className="date">Joined in 2015</span>
          </Card.Meta>
          <Card.Description>TopicDetail</Card.Description>
        </Card.Content>
        <Card.Content extra>
          <a>
            <Icon name="user" />
            22 Friends
          </a>
        </Card.Content>
      </Card>
    );
  }
}
export default TopicDetail;
