import * as React from 'react';
import { Card, CardBody, CardTitle } from 'reactstrap';
import uuid = require('uuid');

const UserCard = (props: any) => {
  const userObject = props.userObject;
  const roles: Array<string> = userObject.roles;
  const newArray = roles.map(element => <div key={uuid()}>{element}</div>);

  const cardBody =
    <CardBody>
      <CardTitle><b>LOGIN: </b>{userObject.login}</CardTitle>
      <p><b>FIRST NAME: </b>{userObject.firstName}</p>
      <p><b>LAST NAME: </b>{userObject.lastName}</p>
      <p><b>PERSON NUMBER: </b>{userObject.personNumber}</p>
      <p><b>E-MAIL: </b>{userObject.email}</p>
      <p><b>STATUS: </b>{userObject.status}</p>
      <b>ROLES: </b>
      {newArray}
    </CardBody>;
  return <Card>{cardBody}</Card>;;
};

export default UserCard;