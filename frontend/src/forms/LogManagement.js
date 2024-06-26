import ComplianceOfficerService from '../services/ComplianceOfficerService';
import { Link } from 'react-router-dom';
import { useState, useEffect } from 'react';
import './LogManagement.css';
import 'bootstrap/dist/css/bootstrap.min.css';

const LogManagement = () => {
  const [logs, setLogs] = useState([]);
  const [selectedLog, setSelectedLog] = useState(null);

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

  const fetchLog = async (logId) => {
    try {
      const log = await ComplianceOfficerService.getLog(logId);
      setSelectedLog(log);
    } catch (error) {
      console.error('Error fetching log by ID : ', error);
    }
  };

  const generateReport = () => {
    const headers = [
      'Log ID', 'Port Request ID', 'Username', 'Phone Number', 
      'Check Passed', 'Notes', 'Check Date'
    ];
    const rows = logs.map(log => [
      log.logId, log.portRequest.requestId, log.customer.username, 
      log.customer.phoneNumber, log.checkPassed.toString(), 
      log.notes, log.checkDate
    ]);

    let csvContent = 'data:text/csv;charset=utf-8,';
    csvContent += headers.join(',') + '\n';
    rows.forEach(row => {
      csvContent += row.join(',') + '\n';
    });

    const encodedUri = encodeURI(csvContent);
    const link = document.createElement('a');
    link.setAttribute('href', encodedUri);
    link.setAttribute('download', 'logs_report.csv');
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  return (
    <div className="container">
      <h2>CUSTOMER LOGS</h2>
      <button className="btn btn-secondary mb-3" onClick={generateReport}>
        Generate Report
      </button>
      <table className="table mt-4 table-striped table-bordered">
        <thead>
          <tr>
            <th>Log ID</th>
            <th>Port Request ID</th>
            <th>Username</th>
            <th>Phone Number</th>
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
              <td>{log.customer.username}</td>
              <td>{log.customer.phoneNumber}</td>
              <td>{log.checkPassed.toString()}</td>
              <td>{log.notes}</td>
              <td>{log.checkDate}</td>
              <td>
                <button className="btn btn-default" onClick={() => fetchLog(log.logId)}>View</button>
                <button className="btn btn-default"><Link to={`/update-log/${log.logId}`}>Update</Link></button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {selectedLog && (
        <div>
        <h2>CUSTOMER LOG DETAILS</h2>
        <table className="table mt-4 table-striped table-bordered ">
          <thead>
            <tr>
              <th>Customer Form</th>
              <th>Customer Details</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>Port Request ID</td>
              <td>{selectedLog.portRequest.requestId}</td>
            </tr>
            <tr>
              <td>Username</td>
              <td>{selectedLog.customer.username}</td>
            </tr>
            <tr>
              <td>Check Passed</td>
              <td>{selectedLog.checkPassed.toString()}</td>
            </tr>
            <tr>
              <td>Notes</td>
              <td>{selectedLog.notes}</td>
            </tr>
            <tr>
              <td>Check Date</td>
              <td>{selectedLog.checkDate}</td>
            </tr>
          </tbody>
        </table>
        </div>
      )}
    </div>
  );
};

export default LogManagement;
