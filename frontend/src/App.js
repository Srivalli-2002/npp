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
import TrackStatus from "./forms/TrackStatus";
import HelpAndSupport from "./forms/HelpAndSupport";
import UserGuide1 from "./forms/UserGuide1";
import UserGuide2 from "./forms/UserGuide2";
import AirtelVerificationDetailsManagement from "./forms/AirtelVerificationDetailsManagement";
import UpdateAirtelDetails from "./forms/UpdateAirtelDetails";
import JioVerificationDetailsManagement from "./forms/JioVerificationDetailsManagement";
import UpdateJioDetails from "./forms/UpdateJioDetails";

const App = () => {
  const [showSystemAdmin, setShowSystemAdmin] = useState(false);
  const [showCustomerService, setShowCustomerService] = useState(false);
  const [showComplianceOfficer, setShowComplianceOfficer] = useState(false);
  const [showAirtelComplianceOfficer, setShowAirtelComplianceOfficer] = useState(false);
  const [showJioComplianceOfficer, setShowJioComplianceOfficer] = useState(false);
  const [showUsers, setShowUsers] = useState(false);
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
      setShowAirtelComplianceOfficer(user.role.includes("ROLE_AIRTEL_COMPLIANCE_OFFICER"));
      setCurrentUser(user);
      setShowJioComplianceOfficer(user.role.includes("ROLE_JIO_COMPLIANCE_OFFICER"));
      setCurrentUser(user);
      setShowUsers(user.role.includes("ROLE_USER"));
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
    setShowJioComplianceOfficer(false);
    setShowAirtelComplianceOfficer(false);
    setShowUsers(false);
    setCurrentUser(undefined);
  };

  return (
    <div>
      <nav className="navbar fixed-top navbar-expand custom-navbar">
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
                Logs Dashboard
              </Link>
            </li>
          )}     

          {showAirtelComplianceOfficer && (
            <li className="nav-item">
              <Link to={"/airteldetails"} className="nav-link">
                Details Dashboard
              </Link>
            </li>
          )}      

          {showJioComplianceOfficer && (
            <li className="nav-item">
              <Link to={"/jiodetails"} className="nav-link">
                Details Dashboard
              </Link>
            </li>
          )}     

          {showUsers && (
            <li className="nav-item">
              <Link to={"/trackstatus"} className="nav-link">
                Track Status
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
              <Link to={"/helpdesk"} className="nav-link">
                Help Desk
              </Link>
            </li>
            <li className="nav-item">
              <a href="/login" className="nav-link" onClick={logOut}>
                Logout
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
          <Route path="/helpdesk" element={<HelpAndSupport/>} />
          <Route path="/helpdesk/userguide1" element={<UserGuide1/>} />
          <Route path="/helpdesk/userguide2" element={<UserGuide2/>} />

          <Route path="/usermanagement" element={<UserManagement/>} />
          <Route path="/operatormanagement" element={<OperatorManagement/>} />
          <Route path="/update-userrole/:userId" element={<UpdateUser/>} />
          <Route path="/update-operator/:operatorId" element={<UpdateOperator/>} />

          <Route path="/customermanagement" element={<CustomerServiceManagement/>} />
          <Route path="/handleportrequest" element={<PortRequestManagement/>} />
          <Route path="/update-customer/:customerId" element={<UpdateCustomer/>} />
          <Route path="/update-portrequest/:requestId" element={<UpdatePortRequest/>} />
          <Route path="/trackstatus" element={<TrackStatus/>}/>

          <Route path="/compliancelogs" element={<LogManagement/>} />
          <Route path="/update-log/:logId" element={<UpdateLog/>} />
          <Route path="/airteldetails" element={<AirtelVerificationDetailsManagement/>} />
          <Route path="/update-airtel-deatils/:phoneNumber" element={<UpdateAirtelDetails/>}/>
          <Route path="/jiodetails" element={<JioVerificationDetailsManagement/>} />
          <Route path="/update-jio-deatils/:phoneNumber" element={<UpdateJioDetails/>}/>
        </Routes>
      </div>

    </div>
  );
};

export default App;
