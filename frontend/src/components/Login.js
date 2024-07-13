import React, { useState, useRef } from "react";
import { useNavigate, Link } from 'react-router-dom';
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";
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

const Login = () => {
  let navigate = useNavigate();

  const form = useRef();
  const checkBtn = useRef();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");

  const onChangeUsername = (e) => {
    const username = e.target.value;
    setUsername(username);
  };

  const onChangePassword = (e) => {
    const password = e.target.value;
    setPassword(password);
  };

  const handleLogin = (e) => {
    e.preventDefault();

    setMessage("");
    setLoading(true);

    form.current.validateAll();

    if (checkBtn.current.context._errors.length === 0) {
      AuthService.login(username, password).then(
        () => {
          navigate("/profile");
          window.location.reload();
        },
        (error) => {
          const resMessage =
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString();

          setLoading(false);
          setMessage(resMessage);
        }
      );
    } else {
      setLoading(false);
    }
  };

  return (
    <div className="container-fluid vh-100 d-flex align-items-center justify-content-center" style={{ color: 'white' }}>
      <div className="card p-4 shadow-lg border-0 rounded-3" style={{ maxWidth: '400px', width: '100%', backgroundColor: 'white', color: '#a21232', fontFamily: 'Cambria' }}>
        <div className="card-body">
          <h3 className="card-title text-center mb-4" style={{color: '#a21232', fontFamily:'Cambria'}}>Sign In</h3>
          <img
            src="//ssl.gstatic.com/accounts/ui/avatar_2x.png"
            alt="profile-img"
            className="profile-img-card rounded-circle mx-auto d-block mb-3"
            style={{ width: "100px", height: "100px" }}
          />

          <Form onSubmit={handleLogin} ref={form}>
            <div className="form-group mb-3">
              <label htmlFor="username" className="form-label" style={{ fontFamily: 'Cambria', color:'#a21232'}}>Username</label>
              <Input
                type="text"
                className="form-control rounded-pill"
                name="username"
                value={username}
                onChange={onChangeUsername}
                validations={[required]}
                style={{ backgroundColor: '#f0f0f0', color: '#001f3f' }}
              />
            </div>

            <div className="form-group mb-3">
              <label htmlFor="password" className="form-label" style={{ fontFamily: 'Cambria', color:'#a21232'}}>Password</label>
              <Input
                type="password"
                className="form-control rounded-pill"
                name="password"
                value={password}
                onChange={onChangePassword}
                validations={[required]}
                style={{ backgroundColor: '#f0f0f0', color: '#001f3f' }}
              />
            </div>

            <div className="d-grid mb-3">
              <button className="btn btn-default btn-block rounded-pill" disabled={loading} style={{ backgroundColor: '#a21232', borderColor: '#a21232', fontFamily:'Cambria', color: '#ffffff' }}>
                {loading && (
                  <span className="spinner-border spinner-border-sm"></span>
                )}
                <span>Login</span>
              </button>
            </div>

            {message && (
              <div className="form-group mt-3">
                <div className="alert alert-danger" role="alert">
                  {message}
                </div>
              </div>
            )}
            <CheckButton style={{ display: "none" }} ref={checkBtn} />
          </Form>
        </div>
        <div className="card-footer text-center">
          <div className="small" style={{ fontFamily: 'Cambria'}}>
            Don't have an account? <Link to="/register" className="#" style={{color: '#a21232', fontFamily: 'Cambria'}}>Sign up</Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;
