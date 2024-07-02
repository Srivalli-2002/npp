import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import JioCOService from '../services/JioCOService';
 
function UpdateJioDetails() {
  const navigate = useNavigate();
  const { phoneNumber } = useParams();
 
  const [detailsData, setDetailsData] = useState({
    phoneNumber: '',
    customerIdentityVerified: '',
    noOutstandingPayments: '',
    timeSinceLastPort: '',
    numberStatus: '',
    contractualObligationsMet: '',
    notificationToCurrentOperator: ''
  });
 
  useEffect(() => {
    fetchDetailsByPhn(phoneNumber);
  }, [phoneNumber]);
 
  const fetchDetailsByPhn = async (phoneNumber) => {
    try {
       
      const response = await JioCOService.getVerificationDetailsByPhn(phoneNumber);
      if (response) {
        setDetailsData(response);
      } else {
        console.error('Error: Details data is undefined.');
      }
    } catch (error) {
      console.error('Error fetching details data:', error);
    }
  };
 
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setDetailsData((prevDetailsData) => ({
      ...prevDetailsData,
      [name]: value
    }));
  };
 
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await JioCOService.updateVerificationDetails(detailsData);
      navigate("/jiodetails");
    } catch (error) {
      console.error('Error updating details:', error);
      alert(error.message || 'An error occurred while updating details.');
    }
  };
 
  return (
    <div className="auth-container">
      <h2>UPDATE DETAILS</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Customer Identity Verified :</label>
          <input type="text" name="customerIdentityVerified" value={detailsData.customerIdentityVerified || ''} onChange={handleInputChange} />
        </div>
        <div className="form-group">
          <label>No Outstanding Payments :</label>
          <input type="text" name="noOutstandingPayments" value={detailsData.noOutstandingPayments || ''} onChange={handleInputChange} />
        </div>
        <div className="form-group">
          <label>Time Since Last Port :</label>
          <input type="number" name="timeSinceLastPort" value={detailsData.timeSinceLastPort || ''} onChange={handleInputChange} />
        </div>
        <div className="form-group">
          <label>Contractual Obligations Met :</label>
          <input type="text" name="contractualObligationsMet" value={detailsData.contractualObligationsMet || ''} onChange={handleInputChange} />
        </div>
        <div className="form-group">
          <label>Number Status :</label>
          <input type="text" name="numberStatus" value={detailsData.numberStatus || ''} onChange={handleInputChange} />
        </div>
        <div className="form-group">
          <label>Notification To Operator :</label>
          <input type="text" name="notificationToCurrentOperator" value={detailsData.notificationToCurrentOperator || ''} onChange={handleInputChange} />
        </div>
        <button type="submit">UPDATE</button>
        <div className="card-footer text-center">
          <button className="btn btn-default" onClick={() => navigate("/details")}>Back to Customer Management</button>
        </div>
      </form>
    </div>
  );
}
 
export default UpdateJioDetails;