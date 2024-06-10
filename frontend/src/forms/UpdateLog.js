import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import ComplianceOfficerService from '../services/ComplianceOfficerService';
 
function UpdateLog() {
  const navigate = useNavigate();
  const { logId } = useParams();
 
  const [logData, setLogData] = useState({
    portRequestId: '',
    checkPassed: '',
    notes: '',
    checkDate: ''
  });
 
  useEffect(() => {
    fetchLogDataById(logId);
  }, [logId]);
 
  const fetchLogDataById = async (logId) => {
    try {
       
      const response = await ComplianceOfficerService.getLog(logId);
      if (response) {
        setLogData(response);
      } else {
        console.error('Error: Log data is undefined.');
      }
    } catch (error) {
      console.error('Error fetching log data : ', error);
    }
  };
 
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setLogData((prevLogData) => ({
      ...prevLogData,
      [name]: value
    }));
  };
 
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await ComplianceOfficerService.updateLog(logData);
      navigate("/compliancelogs");
    } catch (error) {
      console.error('Error updating log : ', error);
      alert(error.message || 'An error occurred while updating log.');
    }
  };
 
  return (
    <div className="auth-container">
      <h2>Update Log</h2>
      <form onSubmit={handleSubmit}>
      <div className="form-group">
          <label>Log ID :</label>
          <input type="number" name="logId" value={logData.logId || ''} onChange={handleInputChange} />
        </div>
        <div className="form-group">
          <label>Port Request ID :</label>
          <input type="number" name="portRequestId" value={logData.portRequestId || ''} onChange={handleInputChange} />
        </div>
        <div className="form-group">
          <label>Check Passed :</label>
          <input type="text" name="checkPassed" value={logData.checkPassed || ''} onChange={handleInputChange} />
        </div>
        <div className="form-group">
          <label>Notes :</label>
          <input type="text" name="notes" value={logData.notes || ''} onChange={handleInputChange} />
        </div>
        <div className="form-group">
          <label>Check Date :</label>
          <input type="date" name="checkDate" value={logData.checkDate || ''} onChange={handleInputChange} />
        </div>
        
        <button type="submit">Update</button>
      </form>
    </div>
  );
}
 
export default UpdateLog;