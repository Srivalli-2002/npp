import ComplianceOfficerService from '../services/ComplianceOfficerService';
import { useNavigate, Link } from 'react-router-dom';
import { useState } from 'react';
import { useRef, useEffect } from 'react';
 
import 'bootstrap/dist/css/bootstrap.min.css';
 
const LogManagement = () => {
  const [newLog, setNewLog] = useState({
    portRequestId: '',
    checkPassed: '',
    notes: '',
    checkDate: ''
  });
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [selectedLog, setSelectedLog] = useState(null);
 
  const form = useRef();
  const navigate = useNavigate();
 
  useEffect(() => {
    fetchLogs();
  }, []);
 
  const fetchLogs = () => {
    ComplianceOfficerService.viewLogs()
      .then(response => {
        setLogs(response);
        console.log(response);
      })
      .catch(error => {
        console.error('Error fetching logs : ', error);
      });
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewLog({ ...newLog, [name]: value });
  };
 
  const handleSubmit = (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage('');
    ComplianceOfficerService.addLog(newLog)
      .then(() => {
        alert('Log added successfully');
        setNewLog({
            portRequestId: '',
            checkPassed: '',
            notes: '',
            checkDate: ''
        });
        fetchLogs();
      })
      .catch(error => {
        console.error('Error adding log : ', error);
        alert('An error occurred while adding log');
      })
      .finally(() => {
        setLoading(false);
      });
  };
 
  const fetchLog = async (logId) => {
    try {
      const log = await ComplianceOfficerService.getLog(logId);
      setSelectedLog(log); // Set the selected device
    } catch (error) {
      console.error('Error fetching log by ID : ', error);
    }
  };
 
  return (
    <div className="container">
      <h2>Add Log</h2>
      <div className="card card-container">
        <form onSubmit={handleSubmit} ref={form}>
          <div className="mb-3">
            <label htmlFor="portRequestId" className="form-label">Port Request ID : </label>
            <input type="number" className="form-control" id="portRequestId" name="portRequestId" value={newLog.portRequestId} onChange={handleInputChange} required />
          </div>
          <div className="mb-3">
            <label htmlFor="checkPassed" className="form-label">Check Passed : </label>
            <input type="text" className="form-control" id="checkPassed" name="checkPassed" value={newLog.checkPassed} onChange={handleInputChange} required />
          </div>
          <div className="mb-3">
            <label htmlFor="notes" className="form-label">Notes : </label>
            <input type="text" className="form-control" id="notes" name="notes" value={newLog.notes} onChange={handleInputChange} required />
          </div>
          <div className="mb-3">
            <label htmlFor="checkDate" className="form-label">Check Date : </label>
            <input type="date" className="form-control" id="checkDate" name="checkDate" value={newLog.checkDate} onChange={handleInputChange} required />
          </div>
          <button type="submit" className="btn btn-primary" disabled={loading}>Add Log</button>
        </form>
      </div>
 
      <table className="table mt-4">
        <thead>
          <tr>
            <th>Log ID</th>
            <th>Port Request ID</th>
            <th>Check Passed</th>
            <th>Notes</th>
            <th>Check Date</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {logs.map(log => (
            <tr key={log.logId}>
              <td>{log.logId}</td>
              <td>{log.portRequest.requestId}</td>
              <td>{log.checkPassed.toString()}</td>
              <td>{log.notes}</td>
              <td>{log.checkDate}</td>
              <td>
              <button className="btn btn-primary" onClick={() => fetchLog(log.logId)}>View</button>
                <button><Link to={`/update-log/${log.logId}`}> Update Log </Link></button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {selectedLog && (
        <div>
          <h3>Log Details :</h3>
          <p>Port Request ID : {selectedLog.portRequest.requestId}</p>
          <p>Check Passed : {selectedLog.checkPassed.toString()}</p>
          <p>Notes : {selectedLog.notes}</p>
          <p>Check Date : {selectedLog.checkDate}</p>
        </div>
     )}
    </div>
  );
};
 
export default LogManagement;