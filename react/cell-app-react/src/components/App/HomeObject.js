import React from "react";
import HomePage from "../Home";
import { Button, Grid, GridColumn } from "semantic-ui-react";
import service from "./Services"
import * as ROUTES from "../../constants/routes";
import em from "../../constants/enumerator";

class HomeObject extends React.Component {
    constructor(props) {
        super(props)
        if (!props.stepID && props.superState.editAnimation) {
            props.setSuperState({
                editAnimation: false
            })
            return
        }

        if (props.animationItemID && Object.keys(props.superState.animationItemList).includes(props.animationItemID)) {
            let STEP_ID = props.superState.animationItemList[props.animationItemID].STEP_ID
            let DOCUMENT_ID = props.superState.stepList[STEP_ID].DOCUMENT_ID
            let TOPIC_ID = props.superState.documentList[DOCUMENT_ID].TOPIC_ID
            props.history.replace(`${ROUTES.HOME}/${TOPIC_ID}/${DOCUMENT_ID}/${STEP_ID}/${props.animationItemID}`)
        } else {
            if (props.stepID && Object.keys(props.superState.stepList).includes(props.stepID)) {
                let DOCUMENT_ID = props.superState.stepList[props.stepID].DOCUMENT_ID
                let TOPIC_ID = props.superState.documentList[DOCUMENT_ID].TOPIC_ID
                props.history.replace(`${ROUTES.HOME}/${TOPIC_ID}/${DOCUMENT_ID}/${props.stepID}`)
            } else {
                if (props.docID && Object.keys(props.superState.documentList).includes(props.docID)) {
                    props.history.replace(`${ROUTES.HOME}/${props.superState.documentList[props.docID].TOPIC_ID}/${props.docID}`)
                } else {
                    if (props.topicID && Object.keys(props.superState.topicList).includes(props.topicID)) {
                        props.history.replace(`${ROUTES.HOME}/${props.topicID}`)
                    } else {
                        props.history.replace(`${ROUTES.HOME}`)
                    }
                }
            }
        }
    }

    updateAll() {
        this.props.superState.changed.forEach(document => {
            this.props.uploadHelpers.update(em.Collections.TOPIC, document);
            this.props.uploadHelpers.update(em.Collections.DOCUMENTS, document);
            this.props.uploadHelpers.update(em.Collections.STEP, document);
            this.props.uploadHelpers.update(em.Collections.ANIMATION_ITEM, document);
        });
    }

    render() {
        console.log(this.props)
        return (<div>
            <Button
                primary
                disabled={this.props.superState.changed.size === 0}
                onClick={() => this.updateAll()}
                style={{ marginTop: "4em", marginLeft: "16px" }}
            >
                {this.props.superState.changed.size} Change(s), Save All
            </Button>

            {this.props.superState.editAnimation ? (
                <Button
                    disabled={!this.props.stepID}
                    onClick={() => {
                        this.props.setSuperState({ editAnimation: false });
                    }}
                    style={{ marginTop: "4em", marginLeft: "16px" }}
                >
                    Back
                </Button>
            ) : (
                    <Button
                        primary
                        disabled={
                            !this.props.stepID ||
                            this.props.superState.stepList[this.props.stepID].TYPE >= 10
                        }
                        onClick={() => {
                            this.props.setSuperState({ editAnimation: true });
                        }}
                        style={{ marginTop: "4em" }}
                    >
                        Edit Animation
                    </Button>
                )}

            <HomePage
                window={this.props.window}
                editAnimation={this.props.superState.editAnimation}
                setSelected={(collection, id) => service.setSelected(collection, id, this.props.superState, {
                    topicID: this.props.topicID,
                    documentID: this.props.docID,
                    stepID: this.props.stepID,
                    animationItemID: this.props.animationItemID
                }, this.props.history)}
                topicSelected={this.props.topicID}
                documentSelected={this.props.docID}
                stepSelected={this.props.stepID}
                animationItemSelected={this.props.animationItemID}
                lists={{
                    topicList: this.props.superState.topicList,
                    documentList: this.props.superState.documentList,
                    stepList: this.props.superState.stepList,
                    animationItemList: this.props.superState.animationItemList
                }}
                update={(collection, document) => service.update(collection, document, this.props.superState, this.props.setSuperState)}
                load={() => service.load(this.props.superState, this.props.setSuperState)}
                add={(collection, parent) => service.add(collection, parent, this.props.superState, this.props.setSuperState)}
                setKeyValue={(collection, document, parent, key, value) => service.setKeyValue(collection, document, parent, key, value, this.props.superState, this.props.setSuperState)}
                changed={this.props.superState.changed}
                uploadHelpers={this.props.uploadHelpers}
                isUploading={this.props.superState.isUploading}
                progress={this.props.superState.progress}
            />
        </div>)
    }
}

export default HomeObject