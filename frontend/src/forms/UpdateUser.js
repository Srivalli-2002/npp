import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import SystemAdminService from '../services/SystemAdminService';
import './UpdateUser.css'; // Import the custom CSS file

function UpdateUser() {
  const navigate = useNavigate();
  const { userId } = useParams();

  const [userData, setUserData] = useState({
    userId: '',
    role: ''
  });

  useEffect(() => {
    fetchUserDataById(userId);
  }, [userId]);

  const fetchUserDataById = async (userId) => {
    try {
      const response = await SystemAdminService.getUser(userId);
      if (response) {
        setUserData(response);
      } else {
        console.error('Error: User data is undefined.');
      }
    } catch (error) {
      console.error('Error fetching user data : ', error);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUserData((prevUserData) => ({
      ...prevUserData,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await SystemAdminService.updateUserRole(userData);
      navigate("/usermanagement");
    } catch (error) {
      console.error('Error updating user : ', error);
      alert(error.message || 'An error occurred while updating user.');
    }
  };

  return (
    <div className="auth-container mt-5 pt-5">
      <h2>UPDATE ROLE</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>User ID :</label>
          <input
            type="number"
            name="userId"
            value={userData.userId || ''}
            onChange={handleInputChange}
            required
          />
        </div>
        <div className="form-group">
          <label>Role :</label>
          <select
            name="role"
            value={userData.role}
            onChange={handleInputChange}
            required
          >
            <option value="">Select Role</option>
            <option value="ROLE_SYSTEM_ADMIN">ROLE_SYSTEM_ADMIN</option>
            <option value="ROLE_COMPLIANCE_OFFICER">ROLE_COMPLIANCE_OFFICER</option>
            <option value="ROLE_CUSTOMER_SERVICE">ROLE_CUSTOMER_SERVICE</option>
            <option value="ROLE_USER">ROLE_USER</option>
          </select>
        </div>
        <button type="submit" className="btn btn-default w-100">UPDATE</button>
        <div className="card-footer text-center">
          <button className="btn btn-default" onClick={() => navigate("/usermanagement")}>Back to User Management</button>
        </div>
      </form>
    </div>
  );
}

export default UpdateUser;
