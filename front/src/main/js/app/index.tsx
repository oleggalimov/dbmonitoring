import 'bootstrap/dist/css/bootstrap.css';
import * as React from 'react';
import * as ReactDom from 'react-dom';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import About from './components/About';
import { Navbar, NavbarBrand, Collapse, Nav, NavItem, UncontrolledDropdown, DropdownToggle, DropdownMenu, DropdownItem, Button, NavLink } from 'reactstrap';

class App extends React.Component {
    render() {
        console.log(`pathname is ${location.pathname}`);
        let aboutPAth=`${location.pathname}about`;
        console.log(`about is ${aboutPAth}`);

        return (
            <BrowserRouter>
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
                                    <DropdownToggle nav caret>Options</DropdownToggle>
                                    <DropdownMenu right>
                                        <DropdownItem>Option 1</DropdownItem>
                                        <DropdownItem>Option 2</DropdownItem>
                                        <DropdownItem divider />
                                        <DropdownItem>Reset</DropdownItem>
                                    </DropdownMenu>
                                </UncontrolledDropdown>
                            </Nav>
                            <Button color="primary">Войти</Button>
                        </Collapse>
                    </Navbar>
                </div>
                <div>
                    <Switch>
                        <Route path={`/`} component={App} exact={true} />
                        <Route path="database_monitoring/about" component={About} />
                        <Route path='/users' component={(props) => <div>Пользователи</div>} />
                        <Route render={(props) => <div>Страница не найдена</div>} />
                    </Switch>
                </div>
            </BrowserRouter>);
    }
}

ReactDom.render(
    <App />,
    document.getElementById("App")
) 