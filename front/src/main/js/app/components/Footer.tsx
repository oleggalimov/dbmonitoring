import * as React from 'react';
import { BrowserRouter, useRouteMatch } from 'react-router-dom';
import { Button, Collapse, DropdownItem, DropdownMenu, DropdownToggle, Nav, Navbar, NavbarBrand, NavItem, NavLink, UncontrolledDropdown } from 'reactstrap';

export default class Footer extends React.Component {  

  render() {
    console.log(`location is ${location}`);
    console.log(`href is ${location.href}`);
    console.log(`protocol is ${location.protocol}`);
    console.log(`pathname is ${location.pathname}`);
    console.log(`origin is ${location.origin}`);
    let element = <BrowserRouter>
     <div>
     <Navbar color="light" light expand="md">
        <NavbarBrand>Система мониторинга баз данных</NavbarBrand>
        <Collapse navbar>
          <Nav className="mr-auto" navbar>
              <NavItem>
                <NavLink href='/'>Главная</NavLink>
              </NavItem>
            <NavItem>
              <NavLink href="/instances/">Управление экземплярами</NavLink>
            </NavItem>
            <NavItem>
              <NavLink href="/users">Управление пользователями</NavLink>
            </NavItem>
            <NavItem>
              <NavLink href={`${location.pathname}about`}>О программе</NavLink>
            </NavItem>
            <UncontrolledDropdown nav inNavbar>
              <DropdownToggle nav caret>
                Options
              </DropdownToggle>
              <DropdownMenu right>
                <DropdownItem>
                  Option 1
                </DropdownItem>
                <DropdownItem>
                  Option 2
                </DropdownItem>
                <DropdownItem divider />
                <DropdownItem>
                  Reset
                </DropdownItem>
              </DropdownMenu>
            </UncontrolledDropdown>
          </Nav>
          <Button color="primary">Войти</Button>
        </Collapse>
      </Navbar>
  </div>
  
  </BrowserRouter>
  
    return (
        element
    );
  }
}