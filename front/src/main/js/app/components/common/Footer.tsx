import * as React from 'react';
import { connect } from 'react-redux';
import { NavLink } from 'react-router-dom';
import { Button, Collapse, DropdownItem, DropdownMenu, DropdownToggle, Nav, Navbar, NavbarBrand, UncontrolledDropdown } from 'reactstrap';


class Footer extends React.Component<Props, {}> {
  constructor(props: any) {
    super(props);
  }


  render() {
    let loginButton: JSX.Element;
    const token = this.props.propsToken;
    if (token == null || token == "" || token == undefined) {
      loginButton = <> Anonimus &nbsp;<Button color="primary"> <NavLink to={`/login`} style={{ color: "WHITE" }} > Sign in </NavLink></Button> </>;
    } else {
      loginButton = <>{this.props.propsUserName}  &nbsp; <Button color="secondary"> <NavLink to={`logout`} style={{ color: "WHITE" }} > Sign out </NavLink></Button></>;
    }


    return (
      <div>
        <Navbar color="light" light expand="md">
          <NavbarBrand href="#">Database monitoring system</NavbarBrand>

          <Collapse navbar>
            <Nav className="mr-auto" navbar>
              <UncontrolledDropdown nav inNavbar id="instanceDropDown">
                <DropdownToggle nav caret>
                  Instance management
              </DropdownToggle>
                <DropdownMenu right>
                  <DropdownItem>
                    <NavLink to={`listInstance`} style={{ color: "DIMGRAY" }}>Full list of instances</NavLink>
                  </DropdownItem>
                  <DropdownItem divider />
                  <DropdownItem>
                    <NavLink to={`addInstance`} style={{ color: "DIMGRAY" }}>Add instance</NavLink>
                  </DropdownItem>
                  <DropdownItem>
                    <NavLink to={`updateInstance`} style={{ color: "DIMGRAY" }}>Update instance</NavLink>
                  </DropdownItem>

                  <DropdownItem >
                    <NavLink to={`removeInstance`} style={{ color: "DIMGRAY" }}>Remove instance</NavLink>
                  </DropdownItem>
                </DropdownMenu>
              </UncontrolledDropdown>
              <UncontrolledDropdown nav inNavbar id="userDropDown">
                <DropdownToggle nav caret>
                  Account management
              </DropdownToggle>
                <DropdownMenu right>
                  <DropdownItem>
                    <NavLink to={`listUser`} style={{ color: "DIMGRAY" }}>Full list of accounts</NavLink>
                  </DropdownItem>
                  <DropdownItem divider />
                  <DropdownItem>
                    <NavLink to={`addUser`} style={{ color: "DIMGRAY" }}> Add account</NavLink>
                  </DropdownItem>
                  <DropdownItem>
                    <NavLink to={`updateUser`} style={{ color: "DIMGRAY" }}> Update account</NavLink>
                  </DropdownItem>
                  <DropdownItem>
                    <NavLink to={`removeUser`} style={{ color: "DIMGRAY" }}> Remove account</NavLink>
                  </DropdownItem>
                </DropdownMenu>
              </UncontrolledDropdown>
              <Button color="link">
              </Button>
              <Button color="link">
                <NavLink to={`metircs`} style={{ color: "DIMGRAY" }}>View metrics</NavLink>
              </Button>
            </Nav>
            {
              loginButton
            }
          </Collapse>
        </Navbar>
      </div >
    );
  }
}


const mapStateToProps = (state: any) => ({
  propsToken: state.token,
  propsUserName: state.userName,
  contextPath: ""
});



interface Props {
  propsToken: string,
  propsUserName: string,
  contextRoot: string
}

export default connect(mapStateToProps)(Footer);