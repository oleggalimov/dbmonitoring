import 'bootstrap/dist/css/bootstrap.css';
import * as React from 'react';
import * as ReactDom from 'react-dom';
import { HashRouter, Route, Switch } from 'react-router-dom';
import Footer from './components/common/Footer';
import ForbiddenMeesage from './components/common/ForbiddenMeesage';
import PageNotFound from './components/common/PageNotFound';
import LoginForm from './components/common/LoginForm';
import Start from './components/common/Start';
import AddInstance from './components/Instance/AddInstance';
import ListInstances from './components/Instance/ListInstances';
import { default as RemoveInstance } from './components/Instance/RemoveInstance';
import UpdateInstance from './components/Instance/UpdateInstance';
import AddUser from './components/User/AddUser';
import ListUsers from './components/User/ListUsers';
import RemoveUser from './components/User/RemoveUser';
import UpdateUser from './components/User/UpdateUser';
import BasicAuthTokenCreator from './utils/BasicAuthTokenCreator';
import ConfigStore from './utils/ConfigStore';
import ErrorComponentFactory from './utils/ErrorComponentFactory';
import MessageComponentFactory from './utils/MessageComponentFactory';
import { Provider } from 'react-redux';

const mainStore = ConfigStore();

class App extends React.Component<{}, { basicToken: string, userLogin: string, userPassword: string }>  {
    constructor(props: any) {
        super(props);
        this.state = {
            basicToken: "",
            userLogin: "",
            userPassword: ""
        }
    }


    handleLoginFormChanges = (event: any) => {
        const fieldId = event.target.id;
        const fieldValue = event.target.value;
        switch (fieldId) {
            case "login": this.setState({ userLogin: fieldValue });
                break;
            case "password": this.setState({ userPassword: fieldValue });
                break;
            default: console.debug("Field id is undefined!")
        }
    }
  


    render() {
        let context = location.pathname;
        return (
            <>
                <HashRouter>
                    <Provider store={mainStore}>
                        <div>
                            <Footer contextRoot={context} basicToken={this.state.basicToken} />
                            <br />
                            <Switch>
                                <Route path="/" component={() => <Start userName={this.state.basicToken == "" ? "" : ""} />} exact={true} />
                                <Route path={`/listInstance`} component={ListInstances} />
                                <Route path={`/addInstance`} component={AddInstance} />
                                <Route path={`/updateInstance`} component={UpdateInstance} />
                                <Route path={`/removeInstance`} component={RemoveInstance} />
                                <Route path={`/listUser`} component={() => <ListUsers token={this.state.basicToken} />} />
                                <Route path={`/addUser`} component={AddUser} />
                                <Route path={`/updateUser`} component={UpdateUser} />
                                <Route path={`/removeUser`} component={RemoveUser} />
                                <Route path={`/login`} component={LoginForm} />
                                <Route render={PageNotFound} />
                            </Switch>
                        </div>
                    </Provider>
                </HashRouter>


            </>
        );
    }
}

ReactDom.render(
    <App />,
    document.getElementById("App")
) 