import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import SystemAdminService from '../services/SystemAdminService';
import './UpdateOperator.css';

 
function UpdateOperator() {
  const navigate = useNavigate();
  const { operatorId } = useParams();
 
  const [operatorData, setOperatorData] = useState({
    operatorName: '',
    contactInfo: ''
  });
 
  useEffect(() => {
    fetchOperatorDataById(operatorId);
  }, [operatorId]);
 
  const fetchOperatorDataById = async (operatorId) => {
    try {
       
      const response = await SystemAdminService.getOperator(operatorId);
      if (response) {
        setOperatorData(response);
      } else {
        console.error('Error: Operator data is undefined.');
      }
    } catch (error) {
      console.error('Error fetching operator data : ', error);
    }
  };
 
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setOperatorData((prevOperatorData) => ({
      ...prevOperatorData,
      [name]: value
    }));
  };
 
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await SystemAdminService.updateOperator(operatorData);
      navigate("/operatormanagement");
    } catch (error) {
      console.error('Error updating operator : ', error);
      alert(error.message || 'An error occurred while updating operator.');
    }
  };
 
  return (
    <div className="auth-container mt-5 pt-5">
      <h2>UPDATE OPERATOR</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Operator Name :</label>
          <input type="text" name="operatorName" value={operatorData.operatorName || ''} onChange={handleInputChange} />
        </div>
        <div className="form-group">
          <label>Contact Info :</label>
          <input type="email" name="contactInfo" value={operatorData.contactInfo || ''} onChange={handleInputChange} />
        </div>
        
        <button type="submit">UPDATE</button>

        <div className="card-footer text-center">
          <button className="btn btn-default" onClick={() => navigate("/operatormanagement")}>Back to Operator Management</button>
        </div>
      </form>
    </div>
  );
}
 
export default UpdateOperator;