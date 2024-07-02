import React, { useState, useRef, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import SystemAdminService from '../services/SystemAdminService';
import 'bootstrap/dist/css/bootstrap.min.css';
import './OperatorManagement.css';

const OperatorManagement = () => {
  const [newOperator, setNewOperator] = useState({ operatorName: '', contactInfo: '' });
  const [operators, setOperators] = useState([]);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [selectedOperator, setSelectedOperator] = useState(null);

  const form = useRef();
  const navigate = useNavigate();

  useEffect(() => {
    fetchOperators();
  }, []);

  const fetchOperators = () => {
    SystemAdminService.viewOperators()
      .then(response => {
        setOperators(response);
      })
      .catch(error => {
        console.error('Error fetching operators:', error);
      });
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewOperator({ ...newOperator, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage('');

    SystemAdminService.addOperator(newOperator)
      .then(() => {
        setMessage('Operator added successfully');
        setNewOperator({ operatorName: '', contactInfo: '' });
        fetchOperators();
      })
      .catch(error => {
        console.error('Error adding operator:', error);
        setMessage('An error occurred while adding the operator');
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const fetchOperator = async (operatorId) => {
    try {
      const operator = await SystemAdminService.getOperator(operatorId);
      setSelectedOperator(operator);
    } catch (error) {
      console.error('Error fetching operator by ID:', error);
    }
  };

  const deleteOperator = (operatorId) => {
    if (window.confirm('Are you sure you want to delete this operator?')) {
      SystemAdminService.deleteOperator(operatorId)
        .then(() => {
          fetchOperators();
        })
        .catch(error => {
          console.error('Error deleting operator:', error);
        });
    }
  };

  return (
    <div className="container mt-5 pt-3">
      <h2 className="pb-3">ADD OPERATOR</h2>
      <div className="card card-container mt-3 p-3">
        <form onSubmit={handleSubmit} ref={form}>
          <div className="mb-3">
            <label htmlFor="operatorName" className="form-label">Operator Name:</label>
            <input
              type="text"
              className="form-control"
              id="operatorName"
              name="operatorName"
              value={newOperator.operatorName}
              onChange={handleInputChange}
              required
            />
          </div>
          <div className="mb-3">
            <label htmlFor="contactInfo" className="form-label">Contact Info:</label>
            <input
              type="email"
              className="form-control"
              id="contactInfo"
              name="contactInfo"
              value={newOperator.contactInfo}
              onChange={handleInputChange}
              required
            />
          </div>
          <button type="submit" className="btn btn-default" disabled={loading}>
            {loading ? 'Adding...' : 'ADD OPERATOR'}
          </button>
        </form>
      </div>

      <table className="table table-striped table-bordered">
        <thead>
          <tr>
            <th className="text-center">Operator ID</th>
            <th>Operator Name</th>
            <th>Contact Info</th>
            <th className="text-center">Actions</th>
          </tr>
        </thead>
        <tbody>
          {operators.map(operator => (
            <tr key={operator.operatorId}>
              <td className="text-center">{operator.operatorId}</td>
              <td>{operator.operatorName}</td>
              <td>{operator.contactInfo}</td>
              <td className="d-flex justify-content-center">
                <button className="btn btn-default btn-sm mx-1" onClick={() => fetchOperator(operator.operatorId)}>View</button>
                <Link to={`/update-operator/${operator.operatorId}`} className="btn btn-default btn-sm mx-1">Update</Link>
                <button className="btn btn-default btn-sm mx-1" onClick={() => deleteOperator(operator.operatorId)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      
      {selectedOperator && (
        <div>
          <h2>OPERATOR DETAILS</h2>
        <table className="table mt-4 table-striped table-bordered ">
        <thead>
          <tr>
            <th>Operator Form</th>
            <th>Operator Details</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td className="left-align">Operator Name</td>
            <td className="left-align">{selectedOperator.operatorName}</td>
          </tr>
          <tr>
            <td className="left-align">Contact Information</td>
            <td className="left-align">{selectedOperator.contactInfo}</td>
          </tr>
        </tbody>
      </table>
      </div>
     )}

    </div>
  );
};

export default OperatorManagement;