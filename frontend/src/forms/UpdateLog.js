import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import ComplianceOfficerService from '../services/ComplianceOfficerService';

function UpdateLog() {
  const navigate = useNavigate();
  const { logId } = useParams();

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
    fetchDetailsDataById(logId);
  }, [logId]);

  const fetchDetailsDataById = async (logId) => {
    try {
      const response = await ComplianceOfficerService.getVerificationDetailsByLog(logId);
      if (response) {
        setDetailsData(response);
      } else {
        console.error('Error: Details are undefined.');
      }
    } catch (error) {
      console.error('Error fetching details: ', error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await ComplianceOfficerService.updateLog(logId);
      navigate("/compliancelogs");
    } catch (error) {
      console.error('Error updating log: ', error);
      alert(error.message || 'An error occurred while updating log.');
    }
  };

  return (
    <div className='container pt-4'>
      <h2>VERIFICATION DETAILS</h2>
      <form onSubmit={handleSubmit}>
        <table className="table mt-4 table-striped table-bordered">
          <thead>
            <tr>
              <th>Phone Number</th>
              <th>Customer Identity Verified</th>
              <th>No Outstanding Payments</th>
              <th>Time Since Last Port</th>
              <th>Contractual Obligations Met</th>
              <th>Number Status</th>
              <th>Notification To Operator</th>
            </tr>
          </thead>
          <tbody>
            <tr key={detailsData.phoneNumber}>
              <td>{detailsData.phoneNumber}</td>
              <td>{detailsData.customerIdentityVerified.toString()}</td>
              <td>{detailsData.noOutstandingPayments.toString()}</td>
              <td>{detailsData.timeSinceLastPort}</td>
              <td>{detailsData.contractualObligationsMet}</td>
              <td>{detailsData.numberStatus}</td>
              <td>{detailsData.notificationToCurrentOperator.toString()}</td>
            </tr>
          </tbody>
        </table>
        <button type="submit">UPDATE</button>
      </form>

      <div className="card-footer text-center">
        <button className="btn btn-default" onClick={() => navigate("/compliancelogs")}>Back to Log Management</button>
      </div>
    </div>
  );
}

export default UpdateLog;
