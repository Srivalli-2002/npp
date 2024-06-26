import React, { useState } from "react";
import Customer from "../services/Customer";
import 'bootstrap/dist/css/bootstrap.min.css';
import AuthService from "../services/auth.service";
import './TrackStatus.css';  // Import your custom CSS file

const TrackStatus = () => {
  const [status, setStatus] = useState(null);
  const [error, setError] = useState(null);

  const getMyStatus = async () => {
    try {
      const username = AuthService.getCurrentUser().username;
      console.log(username);
      const statusData = await Customer.getStatus(username);
      setStatus(statusData);
      setError(null);
    } catch (error) {
      console.error('Error fetching Status', error);
      setStatus(null);
      setError("Error fetching status. Please try again.");
    }
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    getMyStatus();
  };

  return (
    <div className="container mt-5">
      <h2 className="text-center mb-4">CUSTOMER STATUS TRACKER</h2>
      <div className="card card-container p-4">
        <form onSubmit={handleSubmit} className="text-center">
          <button type="submit" className="custom-button">TRACK MY STATUS</button>
        </form>
      </div>
      {status && (
        <div className="status-container mt-4">
          <div className="card mx-auto" style={{ maxWidth: "400px" }}>
            <div className="card-header text-center">
              <h5>Status Details</h5>
            </div>
            <div className="card-body">
              <p><strong>Status:</strong> {status.status}</p>
              <p><strong>Last Updated:</strong> {status.lastUpdated}</p>
              <p><strong>Notes:</strong> {status.notes}</p>
            </div>
          </div>
        </div>
      )}
      {error && (
        <div className="alert alert-danger mt-4 text-center">
          {error}
        </div>
      )}
    </div>
  );
};

export default TrackStatus;