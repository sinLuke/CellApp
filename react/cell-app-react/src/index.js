import React from "react";
import ReactDOM from "react-dom";
import "./index.css";
import * as serviceWorker from "./serviceWorker";
import "semantic-ui-css/semantic.min.css";

import App from "./components/App";

import { Responsive } from "semantic-ui-react";

window.onbeforeunload = function(e) {
  console.log("onbeforeunload");

  // Cancel the event
  e.preventDefault();

  // Chrome requires returnValue to be set
  e.returnValue = "Really want to quit the game?";
};

//Prevent Ctrl+S (and Ctrl+W for old browsers and Edge)
document.onkeydown = function(e) {
  e = e || window.event; //Get event

  if (!e.ctrlKey) return;

  var code = e.which || e.keyCode; //Get key code

  switch (code) {
    case 83: //Block Ctrl+S
    case 87: //Block Ctrl+W -- Not work in Chrome and new Firefox
      e.preventDefault();
      e.stopPropagation();
      break;
  }
};

ReactDOM.render(
  <Responsive>
    <App />
  </Responsive>,
  document.getElementById("root")
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
