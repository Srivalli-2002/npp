import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import CustomerService from '../services/CustomerService';
 
function UpdatePortRequest() {
  const navigate = useNavigate();
  const { requestId } = useParams();
 
  const [portRequestData, setPortRequestData] = useState({
    requestId: '',
    customerId: '',
    requestDate: ''
  });
 
  useEffect(() => {
    fetchPortRequestDataById(requestId);
  }, [requestId]);
 
  const fetchPortRequestDataById = async (requestId) => {
    try {
       
      const response = await CustomerService.getPortRequest(requestId);
      if (response) {
        setPortRequestData(response);
      } else {
        console.error('Error: Port request data is undefined.');
      }
    } catch (error) {
      console.error('Error fetching port request data:', error);
    }
  };
 
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setPortRequestData((prevPortRequestData) => ({
      ...prevPortRequestData,
      [name]: value
    }));
  };
 
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await CustomerService.updatePortRequest(portRequestData);
      navigate("/handleportrequest");
    } catch (error) {
      console.error('Error updating port request : ', error);
      alert(error.message || 'An error occurred while updating port request.');
    }
  };
 
  return (
    <div className="auth-container">
      <h2>Update Port Request</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Request ID :</label>
          <input type="number" name="requestId" value={portRequestData.requestId || ''} onChange={handleInputChange} />
        </div>
        <div className="form-group">
          <label>Customer ID :</label>
          <input type="number" name="customerId" value={portRequestData.customerId || ''} onChange={handleInputChange} />
        </div>
        <div className="form-group">
          <label>Request Date :</label>
          <input type="date" name="requestDate" value={portRequestData.requestDate || ''} onChange={handleInputChange} />
        </div>
        <button type="submit">Update Port Request</button>
      </form>
    </div>
  );
}
 
export default UpdatePortRequest;