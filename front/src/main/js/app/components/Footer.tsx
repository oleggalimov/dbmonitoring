import * as React from 'react';
import { NavLink } from 'react-router-dom';
import { Button, Collapse, Nav, Navbar, NavbarBrand, UncontrolledDropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap';


export default class Footer extends React.Component<{ contextRoot: String }, { contextPath: String }> {
  constructor(props) {
    super(props);
    this.state = {
      contextPath: props.contextRoot
    }
  }


  render() {
    console.log(`location is ${location}`);
    console.log(`href is ${location.href}`);
    console.log(`protocol is ${location.protocol}`);
    console.log(`pathname is ${location.pathname}`);
    console.log(`origin is ${location.origin}`);
    return (
      <div>
        <Navbar color="light" light expand="md">
          <NavbarBrand href="#">Система мониторинга баз данных</NavbarBrand>

          <Collapse navbar>
            <Nav className="mr-auto" navbar>
              <UncontrolledDropdown nav inNavbar id="instanceDropDown">
                <DropdownToggle nav caret>
                  Управление экземплярами
              </DropdownToggle>
                <DropdownMenu right>
                  <DropdownItem>
                    <NavLink to={`listInstance`} style={{ color: "DIMGRAY" }}>Полный список экземпляров</NavLink>
                    <DropdownItem divider />
                  </DropdownItem>
                  <DropdownItem>
                    <NavLink to={`addInstance`} style={{ color: "DIMGRAY" }}>Добавить</NavLink>
                  </DropdownItem>
                  <DropdownItem>
                    <NavLink to={`updateInstance`} style={{ color: "DIMGRAY" }}>Изменить</NavLink>
                  </DropdownItem>
                  
                  <DropdownItem >
                  <NavLink to={`removeInstance`} style={{ color: "DIMGRAY" }}>Удалить</NavLink>
                </DropdownItem>
                </DropdownMenu>
              </UncontrolledDropdown>
              <UncontrolledDropdown nav inNavbar id="userDropDown">
                <DropdownToggle nav caret>
                  Управление пользователями
              </DropdownToggle>
                <DropdownMenu right>
                  <DropdownItem>
                    <NavLink to={`listUser`} style={{ color: "DIMGRAY" }}>Полный список пользователей</NavLink>
                  </DropdownItem>
                  <DropdownItem divider />
                  <DropdownItem>
                    <NavLink to={`addUser`} style={{ color: "DIMGRAY" }}> Добавить</NavLink>
                  </DropdownItem>
                  <DropdownItem>
                    <NavLink to={`updateUser`} style={{ color: "DIMGRAY" }}> Изменить</NavLink>
                  </DropdownItem>
                  <DropdownItem>
                  <NavLink to={`removeUser`} style={{ color: "DIMGRAY" }}> Удалить</NavLink>
                </DropdownItem>
                </DropdownMenu>
              </UncontrolledDropdown>
              

              <Button color="link">

              </Button>
              <Button color="link">
                <NavLink to={`metircs`} style={{ color: "DIMGRAY" }}>Просмотр метрик</NavLink>
              </Button>
            </Nav>
            <Button color="primary"> <NavLink to={`/login`} style={{ color: "WHITE" }}> Войти</NavLink></Button>
          </Collapse>
        </Navbar>
      </div >
    );
  }
}