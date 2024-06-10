import React, { useState, useEffect } from "react";
import { Routes, Route, Link } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

import AuthService from "./services/auth.service";

import Login from "./components/Login";
import Register from "./components/Register";
import Home from "./components/Home";
import Profile from "./components/Profile";

import EventBus from "./common/EventBus";
import UserManagement from "./forms/UserManagement";
import UpdateUser from "./forms/UpdateUser";
import OperatorManagement from "./forms/OperatorManagement";
import UpdateOperator from "./forms/UpdateOperator"
import CustomerServiceManagement from "./forms/CustomerServiceManagement";
import UpdateCustomer from "./forms/UpdateCustomer";
import PortRequestManagement from "./forms/PortRequestManagement";
import UpdatePortRequest from "./forms/UpdatePortRequest";
import LogManagement from "./forms/LogManagement";
import UpdateLog from "./forms/UpdateLog";


const App = () => {
  const [showSystemAdmin, setShowSystemAdmin] = useState(false);
  const [showCustomerService, setShowCustomerService] = useState(false);
  const [showComplianceOfficer, setShowComplianceOfficer] = useState(false);
  const [currentUser, setCurrentUser] = useState(undefined);

  useEffect(() => {
    const user = AuthService.getCurrentUser();

    if (user) {
      setShowSystemAdmin(user.role.includes("ROLE_SYSTEM_ADMIN"));
      setCurrentUser(user);
      setShowCustomerService(user.role.includes("ROLE_CUSTOMER_SERVICE"));
      setCurrentUser(user);
      console.log(currentUser);
      setShowComplianceOfficer(user.role.includes("ROLE_COMPLIANCE_OFFICER"));
      setCurrentUser(user);
    }
    EventBus.on("logout", () => {
      logOut();
    });

    return () => {
      EventBus.remove("logout");
    };
  }, []);

  const logOut = () => {
    AuthService.logout();
    setShowSystemAdmin(false);
    setShowCustomerService(false);
    setShowComplianceOfficer(false);
    setCurrentUser(undefined);
  };

  return (
    <div>
      <nav className="navbar fixed-top navbar-expand navbar-dark bg-dark">
        <Link to={"/"} className="navbar-brand">
        NUMBER PORTABILITY PORTAL
        </Link>
        <div className="navbar-nav mr-auto">
          <li className="nav-item">
            <Link to={"/home"} className="nav-link">
              Home
            </Link>
          </li>

          {showSystemAdmin && (
            <li className="nav-item">
              <Link to={"/usermanagement"} className="nav-link">
              User Dashboard
              </Link>
            </li>
          )}

          {showSystemAdmin && (
            <li className="nav-item">
              <Link to={"/operatormanagement"} className="nav-link">
                Operator Dashboard
              </Link>
            </li>
          )}

          {showCustomerService && (
            <li className="nav-item">
              <Link to={"/customermanagement"} className="nav-link">
                Customer Service Dashboard
              </Link>
            </li>
          )}

          {showCustomerService && (
            <li className="nav-item">
              <Link to={"/handleportrequest"} className="nav-link">
                Port Request Dashboard
              </Link>
            </li>
          )}

          {showComplianceOfficer && (
            <li className="nav-item">
              <Link to={"/compliancelogs"} className="nav-link">
                Compliance Logs Dashboard
              </Link>
            </li>
          )}
        </div>

        {currentUser ? (
          <div className="navbar-nav ml-auto">
            <li className="nav-item">
              <Link to={"/profile"} className="nav-link">
                {currentUser.username}
              </Link>
            </li>
            <li className="nav-item">
              <a href="/login" className="nav-link" onClick={logOut}>
                LogOut
              </a>
            </li>
          </div>
        ) : (
          <div className="navbar-nav ml-auto">
            <li className="nav-item">
              <Link to={"/login"} className="nav-link">
                Sign in
              </Link>
            </li>

            <li className="nav-item">
              <Link to={"/register"} className="nav-link">
                Sign up
              </Link>
            </li>
          </div>
        )}
      </nav>

      <div className="container mt-3">
        <Routes>
          <Route path="/" element={<Home/>} />
          <Route path="/home" element={<Home/>} />
          <Route path="/login" element={<Login/>} />
          <Route path="/register" element={<Register/>} />
          <Route path="/profile" element={<Profile/>} />
          {/* <Route path="/admin" element={<SystemAdminDashboard/>} /> */}
          <Route path="/customermanagement" element={<CustomerServiceManagement/>} />
          <Route path="/handleportrequest" element={<PortRequestManagement/>} />
          <Route path="/update-customer/:customerId" element={<UpdateCustomer/>} />
          <Route path="/update-portrequest/:requestId" element={<UpdatePortRequest/>} />
          <Route path="/usermanagement" element={<UserManagement/>} />
          <Route path="/operatormanagement" element={<OperatorManagement/>} />
          <Route path="/update-userrole/:userId" element={<UpdateUser/>} />
          <Route path="/update-operator/:operatorId" element={<UpdateOperator/>} />
          <Route path="/compliancelogs" element={<LogManagement/>} />
          <Route path="/update-log/:logId" element={<UpdateLog/>} />
        </Routes>
      </div>

    </div>
  );
};

export default App;
