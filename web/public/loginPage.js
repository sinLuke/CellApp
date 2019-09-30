"use strict";

class LoginPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = { liked: false };
  }

  render() {
    if (this.state.liked) {
      return "You liked this.";
    }

    return (
      <a onClick={() => this.setState({ liked: true })} herf="#">
        Like
      </a>
    );
  }
}

const domContainer = document.querySelector("#login_page_container");
ReactDOM.render(LoginPage, domContainer);
