import * as React from 'react';
import { Card, CardBody, CardTitle } from 'reactstrap';

const InstanceCard = (props: any) => {
  const instance = props.instance;
  const cardBody =
    <CardBody>
      <CardTitle><b>ID: </b>{instance.id}</CardTitle>
      <p><b>TYPE: </b>{instance.type}</p>
      <p><b>HOST: </b>{instance.host}</p>
      <p><b>PORT: </b>{instance.port}</p>
      <p><b>DATABASE NAME: </b>{instance.database}</p>
      <p><b>DATABASE USER LOGIN: </b> {instance.user.login}</p>
      {props.withStatus != false
        ?
        <p><b>DATABASE STATUS: </b>{instance.status == null ? "Unknon" : instance.status}</p>
        : null
      }
    </CardBody>

  let card: JSX.Element;
  if (props.withStatus) {
    card = <Card body inverse color={instance.status == "OK" ? "success" : "danger"}>{cardBody}</Card>;
  } else {
    card = <Card>{cardBody}</Card>;
  }
  return (card);
};

export default InstanceCard;