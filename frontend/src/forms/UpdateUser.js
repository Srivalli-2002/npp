import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import SystemAdminService from '../services/SystemAdminService';
import 'bootstrap/dist/css/bootstrap.min.css';

function UpdateUser() {
  const navigate = useNavigate();

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
    <div className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card shadow-lg" style={{ borderRadius: '15px' }}>
            <div className="card-header text-center" style={{ backgroundColor: '#0056b3', color: 'white', borderTopLeftRadius: '15px', borderTopRightRadius: '15px' }}>
              <h2 style={{ color: 'white' }}>UPDATE ROLE</h2>
            </div>
            <div className="card-body">
              <form onSubmit={handleSubmit}>
                <div className="form-group mb-4">
                  <label htmlFor="userId" className="form-label">User ID :</label>
                  <input
                    type="number"
                    name="userId"
                    className="form-control"
                    id="userId"
                    value={userData.userId || ''}
                    onChange={handleInputChange}
                    required
                  />
                </div>
                <div className="form-group mb-4">
                  <label htmlFor="role" className="form-label">Role :</label>
                  <select
                    className="form-select"
                    id="role"
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
                <button type="submit" className="btn btn-secondary w-100">UPDATE</button>
              </form>
            </div>
            <div className="card-footer text-center" style={{ borderBottomLeftRadius: '15px', borderBottomRightRadius: '15px' }}>
              <button className="btn btn-default" onClick={() => navigate("/usermanagement")}>Back to User Management</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default UpdateUser;
