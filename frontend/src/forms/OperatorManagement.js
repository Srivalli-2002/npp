import React, { useState, useRef, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import SystemAdminService from '../services/SystemAdminService';
import 'bootstrap/dist/css/bootstrap.min.css';

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
    <div className="container mt-5 pt-3"> {/* Reduced top padding from pt-5 to pt-3 */}
      <h2 className="mb-5 pt-5">ADD OPERATOR</h2>
      <div className="card card-container p-3"> {/* Removed top padding from p-5 to p-3 */}
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
          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? 'Adding...' : 'Add Operator'}
          </button>
        </form>
      </div>

      <table className="table table-striped table-bordered">
        <thead className="thead-dark">
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
                <button className="btn btn-primary btn-sm mx-1" onClick={() => fetchOperator(operator.operatorId)}>View</button>
                <button className="btn btn-danger btn-sm mx-1" onClick={() => deleteOperator(operator.operatorId)}>Delete</button>
                <Link to={`/update-operator/${operator.operatorId}`} className="btn btn-secondary btn-sm mx-1">Update</Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {selectedOperator && (
        <div className="mt-4">
          <h3>Operator Details:</h3>
          <p>Operator Name: {selectedOperator.operatorName}</p>
          <p>Contact Info: {selectedOperator.contactInfo}</p>
        </div>
      )}
    </div>
  );
};

export default OperatorManagement;
