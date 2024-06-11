import CustomerService from '../services/CustomerService';
import { useNavigate, Link } from 'react-router-dom';
import { useState } from 'react';
import { useRef, useEffect } from 'react';
 
import 'bootstrap/dist/css/bootstrap.min.css';
 
const PortRequestManagement = () => {
  const [newPortRequest, setNewPortRequest] = useState({
    customerId: '',
    requestDate: ''
  });
  const [portRequests, setPortRequests] = useState([]);
  const [loading, setLoading] = useState(false);
  const [setMessage] = useState('');
  const [selectedPortRequest, setSelectedPortRequest] = useState(null);
 
  const form = useRef();
  const navigate = useNavigate();
 
  useEffect(() => {
    fetchPortRequests();
  }, []);
 
  const fetchPortRequests = () => {
    CustomerService.viewPortRequests()
      .then(response => {
        setPortRequests(response);
        console.log(response);
      })
      .catch(error => {
        console.error('Error fetching PortRequests:', error);
      });
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewPortRequest({ ...newPortRequest, [name]: value });
  };
 
  const handleSubmit = (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage('');
 
    CustomerService.submitPortRequest(newPortRequest)
      .then(() => {
        alert('PortRequest added successfully');
        setNewPortRequest({
          customerId: '',
          requestDate: ''
        });
        fetchPortRequests();
      })
      .catch(error => {
        console.error('Error adding PortRequest:', error);
        alert('An error occurred while adding PortRequest');
      })
      .finally(() => {
        setLoading(false);
      });
  };
 
  const fetchPortRequest = async (requestId) => {
    try {
      const portRequest = await CustomerService.getPortRequest(requestId);
      setSelectedPortRequest(portRequest); // Set the selected device
    } catch (error) {
      console.error('Error fetching port request by ID : ', error);
    }
  };

  const deletePortRequest = (requestId) => {
    const confirmDelete = window.confirm('Are you sure you want to delete this port request ?');
    if (confirmDelete) {
      CustomerService.deletePortRequest(requestId)
        .then(() => {
          fetchPortRequests();
        })
        .catch(error => {
          console.error('Error deleting port request : ', error);
        });
    }
  };
 
  return (
    <div className="container">
      <h2>SUBMIT PORT REQUEST</h2>
      <div className="card card-container">
        <form onSubmit={handleSubmit} ref={form}>
          <div className="mb-3">
            <label htmlFor="customerId" className="form-label">Customer ID : </label>
            <input type="number" className="form-control" id="customerId" name="customerId" value={newPortRequest.customerId} onChange={handleInputChange} required />
          </div>
          <div className="mb-3">
            <label htmlFor="requestDate" className="form-label">Request Date : </label>
            <input type="date" className="form-control" id="requestDate" name="requestDate" value={newPortRequest.requestDate} onChange={handleInputChange} required />
          </div>
          
          <button type="submit" className="btn btn-primary" disabled={loading}>SUBMIT</button>
        </form>
      </div>
 
      <table className="table mt-4">
        <thead>
          <tr>
            <th>Request ID</th>
            <th>Customer ID</th>
            <th>Approval Status</th>
            <th>Request Date</th>
            <th>Compliance Check</th>
            <th>Completion Date</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {portRequests.map(portRequest => (
            <tr key={portRequest.requestId}>
              <td>{portRequest.requestId}</td>
              <td>{portRequest.customer.customerId}</td>
              <td>{portRequest.approvalStatus}</td>
              <td>{portRequest.requestDate}</td>
              <td>{portRequest.complianceChecked.toString()}</td>
              <td>{portRequest.completionDate}</td>
              <td>
              <button className="btn btn-default" onClick={() => fetchPortRequest(portRequest.requestId)}>View</button>
              <button className="btn btn-default"><Link to={`/update-portrequest/${portRequest.requestId}`}>Update</Link></button>
              <button className="btn btn-default" onClick={() => deletePortRequest(portRequest.requestId)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {selectedPortRequest && (
        <div>
          <h3>Port Request details :</h3>
          <p>Request ID : {selectedPortRequest.requestId}</p>
          <p>Customer ID : {selectedPortRequest.customer.customerId}</p>
          <p>Approval Status : {selectedPortRequest.approvalStatus}</p>
          <p>Request Date : {selectedPortRequest.requestDate}</p>
          <p>Compliance Checked : {selectedPortRequest.complianceChecked.toString()}</p>
          <p>Completion Date : {selectedPortRequest.completionDate}</p>
        </div>
     )}
    </div>
  );
};
 
export default PortRequestManagement;