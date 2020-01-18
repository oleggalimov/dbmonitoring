import 'bootstrap/dist/css/bootstrap.css';
import * as React from 'react';
import * as ReactDom from 'react-dom';
import { HashRouter, Route, Switch } from 'react-router-dom';
import Footer from './components/common/Footer';
import AddInstance from './components/Instance/AddInstance';
import ListInstances from './components/Instance/ListInstances';
import { default as RemoveInstance } from './components/Instance/RemoveInstance';
import UpdateInstance from './components/Instance/UpdateInstance';
import PageNotFound from './components/common/PageNotFound';
import Start from './components/common/Start';
import AddUser from './components/User/AddUser';
import ListUsers from './components/User/ListUsers';
import UpdateUser from './components/User/UpdateUser';

class App extends React.Component {
    render() {
        let context = location.pathname;
        return (
            <HashRouter>
                <div>
                    <Footer contextRoot={context}/>
                    {/* <Footer /> */}
                    <br />
                    <Switch>
                        <Route path="/" component={Start} exact={true} />
                        <Route path={`/listInstance`} component={ListInstances} />
                        <Route path={`/addInstance`} component={AddInstance} />
                        <Route path={`/updateInstance`} component={UpdateInstance} />
                        <Route path={`/removeInstance`} component={RemoveInstance} />

                        <Route path={`/listUser`} component={ListUsers} />
                        <Route path={`/addUser`} component={AddUser} />
                        <Route path={`/updateUser`} component={UpdateUser} />
                        <Route path={`/removeUser`} component={RemoveInstance} />
                        
                        
                        <Route render={PageNotFound} />
                    </Switch>
                </div>
            </HashRouter>
        );
    }
}

ReactDom.render(
    <App />,
    document.getElementById("App")
) 