import React, { useState, useRef } from "react";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";
import { Link } from 'react-router-dom';

import AuthService from "../services/auth.service";

const required = (value) => {
  if (!value) {
    return (
      <div className="alert alert-danger" role="alert">
        This field is required!
      </div>
    );
  }
};

const vusername = (value) => {
  if (value.length < 3 || value.length > 20) {
    return (
      <div className="alert alert-danger" role="alert">
        The username must be between 3 and 20 characters.
      </div>
    );
  }
};

const vpassword = (value) => {
  if (value.length < 6 || value.length > 40) {
    return (
      <div className="alert alert-danger" role="alert">
        The password must be between 6 and 40 characters.
      </div>
    );
  }
};

const Register = () => {
  const form = useRef();
  const checkBtn = useRef();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [operatorId, setOperatorId] = useState("");
  const [successful, setSuccessful] = useState(false);
  const [message, setMessage] = useState("");

  const onChangeUsername = (e) => {
    const username = e.target.value;
    setUsername(username);
  };

  const onChangePassword = (e) => {
    const password = e.target.value;
    setPassword(password);
  };

  const onChangeOperatorId = (e) => {
    const operatorId = e.target.value;
    setOperatorId(operatorId);
  };

  const handleRegister = (e) => {
    e.preventDefault();

    setMessage("");
    setSuccessful(false);

    form.current.validateAll();

    if (checkBtn.current.context._errors.length === 0) {
      AuthService.register(username, password, operatorId).then(
        (response) => {
          setMessage(response.data.message);
          setSuccessful(true);
        },
        (error) => {
          const resMessage =
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString();

          setMessage(resMessage);
          setSuccessful(false);
        }
      );
    }
  };

  return (
    <div className="container-fluid vh-100 d-flex align-items-center justify-content-center bg-light">
      <div className="card p-4 shadow-lg border-0 rounded-3" style={{ maxWidth: '400px', width: '100%' }}>
        <div className="card-body">
          <h3 className="card-title text-center mb-4">Sign Up</h3>
          <img
            src="//ssl.gstatic.com/accounts/ui/avatar_2x.png"
            alt="profile-img"
            className="profile-img-card rounded-circle mx-auto d-block mb-3"
            style={{ width: "100px", height: "100px" }}
          />

          <Form onSubmit={handleRegister} ref={form}>
            {!successful && (
              <div>
                <div className="form-group mb-3">
                  <label htmlFor="username" className="form-label">Username</label>
                  <Input
                    type="text"
                    className="form-control rounded-pill"
                    name="username"
                    value={username}
                    onChange={onChangeUsername}
                    validations={[required, vusername]}
                  />
                </div>

                <div className="form-group mb-3">
                  <label htmlFor="password" className="form-label">Password</label>
                  <Input
                    type="password"
                    className="form-control rounded-pill"
                    name="password"
                    value={password}
                    onChange={onChangePassword}
                    validations={[required, vpassword]}
                  />
                </div>

                <div className="form-group mb-3">
                  <label htmlFor="operatorId" className="form-label">Operator Id</label>
                  <Input
                    type="text"
                    className="form-control rounded-pill"
                    name="operatorId"
                    value={operatorId}
                    onChange={onChangeOperatorId}
                    validations={[required]}
                  />
                </div>
                
                <div className="d-grid mb-3">
                  <button className="btn btn-primary btn-block rounded-pill">Sign Up</button>
                </div>
                <div className="card-footer text-center">
                <div className="small">
                Already have an account? <Link to="/login" className="link-primary">Sign in</Link>
                </div>
              </div>
              </div>
            )}

            {message && (
              <div className="form-group">
                <div
                  className={
                    successful ? "alert alert-success" : "alert alert-danger"
                  }
                  role="alert"
                >
                  {message}
                </div>
              </div>
            )}
            <CheckButton style={{ display: "none" }} ref={checkBtn} />
          </Form>
        </div>
      </div>
    </div>
  );
};

export default Register;
