import React from "react";
import { Link } from "react-router-dom";
import * as ROUTES from "../../constants/routes";
import firebase from "firebase";
import { Menu } from "semantic-ui-react";
class Navigation extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    return (
      <Menu inverted={this.props.editAnimation} fixed="top">
        <Menu.Item>
          <Link
            to={ROUTES.LOGIN}
            onClick={() => {
              firebase.auth().signOut();
            }}
          >
            Sign Out
          </Link>
        </Menu.Item>
        <Menu.Item to={ROUTES.HOME}>
          <Link>Home</Link>
        </Menu.Item>
      </Menu>
    );
  }
}

export default Navigation;
