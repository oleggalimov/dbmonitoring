import * as React from 'react';
import { NavLink } from 'react-router-dom';
import { Button, Collapse, Nav, Navbar, NavbarBrand, UncontrolledDropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap';


export default class Footer extends React.Component<{ contextRoot: String, basicToken:string }, { contextPath: String, token:string }> {
  constructor(props: Readonly<{ contextRoot: String; basicToken: string; }>) {
    super(props);
    this.state = {
      contextPath: props.contextRoot,
      token:props.basicToken
    }
  }


  render() {
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
              this.state.token=="" ? <Button color="primary"> <NavLink to={`/login`} style={{ color: "WHITE" }} > Sign in </NavLink></Button>
              :
              <Button color="secondary"> <NavLink to={`/`} style={{ color: "WHITE" }} > Sign out </NavLink></Button>
            }
            
          </Collapse>
        </Navbar>
      </div >
    );
  }
}