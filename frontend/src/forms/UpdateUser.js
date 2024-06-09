import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import SystemAdminService from '../services/SystemAdminService';
 
function UpdateUser() {
  const navigate = useNavigate();
  const { userId } = useParams();

  const [userData, setUserData] = useState({
    userId: '',
    role: ''
  });

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
    <div className="auth-container">
      <h2>Update User Role</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>User ID :</label>
          <input type="number" name="userId" value={userData.userId || ''} onChange={handleInputChange} />
        </div>
        <div className="form-group">
        <label htmlFor="role" className="form-label">Role :</label>
            <select className="form-select" id="role" name="role" value={userData.role} onChange={handleInputChange} required>
              <option value="">Select Role</option>
              <option value="ROLE_SYSTEM_ADMIN">ROLE_SYSTEM_ADMIN</option>
              <option value="ROLE_COMPLIANCE_OFFICER">ROLE_COMPLIANCE_OFFICER</option>
              <option value="ROLE_CUSTOMER_SERVICE">ROLE_CUSTOMER_SERVICE</option>
              <option value="ROLE_USER">ROLE_USER</option>
            </select>
        </div>
        <button type="submit">Update</button>
      </form>
    </div>
  );
}
 
export default UpdateUser;