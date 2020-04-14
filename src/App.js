import React from 'react';
import FormManagement from './components/FormManagement'
import FormBuilder from './components/FormBuilder'
import { Router, Route, Link, Switch } from "react-router-dom"
import NavBar from "./components/NavBar";
import { useAuth0 } from "./react-auth0-spa";
import Profile from "./components/Profile";
import history from "./utils/history";
import PrivateRoute from './components/PrivateRoute'

function Index() {
  return <h2>Dynamic Form</h2>;
}

function About() {
  return <h2>Sample Dynamic Form App v1.1</h2>;
}

function App() {
  const { loading } = useAuth0();

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <div className="App">
    <Router history={history}>
        <header>
          <NavBar />
        </header>

        <nav>
          <ul>
            <li>
              <Link to="/">Home</Link>
            </li>
            <li>
              <Link to="/about">About</Link>
            </li>
            <li>
              <Link to="/formManagement/">Sample Form</Link>
            </li>
            <li>
              <Link to="/formBuilder/">Form Builder</Link>
            </li>
          </ul>
        </nav>

      <Switch>
        <Route path="/" exact component={Index} />
        <Route path="/about/" component={About} />
        <Route path="/formManagement/" component={FormManagement} />
        <Route path="/formBuilder/" component={FormBuilder} />
        <PrivateRoute path="/profile" component={Profile} />
      </Switch>
    </Router>
</div>
  );
}


export default App;
