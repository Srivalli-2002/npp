import SystemAdminService from '../services/SystemAdminService';
import { useNavigate, Link } from 'react-router-dom';
import { useState } from 'react';
import { useRef, useEffect } from 'react';
 
import 'bootstrap/dist/css/bootstrap.min.css';
 
const OperatorManagement = () => {
  const [newOperator, setNewOperator] = useState({
    operatorName: '',
    contactInfo: ''
  });
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
        console.log(response);
      })
      .catch(error => {
        console.error('Error fetching operators : ', error);
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
        alert('Operator added successfully');
        setNewOperator({
            operatorName: '',
            contactInfo: ''
        });
        fetchOperators();
      })
      .catch(error => {
        console.error('Error adding operator : ', error);
        alert('An error occurred while adding operator');
      })
      .finally(() => {
        setLoading(false);
      });
  };
 
  const fetchOperator = async (operatorId) => {
    try {
      const operator = await SystemAdminService.getOperator(operatorId);
      setSelectedOperator(operator); // Set the selected device
    } catch (error) {
      console.error('Error fetching operator by ID : ', error);
    }
  };

  const deleteOperator = (operatorId) => {
    const confirmDelete = window.confirm('Are you sure you want to delete this operator ?');
    if (confirmDelete) {
      SystemAdminService.deleteOperator(operatorId)
        .then(() => {
          fetchOperators();
        })
        .catch(error => {
          console.error('Error deleting operator : ', error);
        });
    }
  };
 
  return (
    <div className="container">
      <h2>Add Operator</h2>
      <div className="card card-container">
        <form onSubmit={handleSubmit} ref={form}>
          <div className="mb-3">
            <label htmlFor="operatorName" className="form-label">Operator Name : </label>
            <input type="text" className="form-control" id="operatorName" name="operatorName" value={newOperator.operatorName} onChange={handleInputChange} required />
          </div>
          <div className="mb-3">
            <label htmlFor="contactInfo" className="form-label">Contact Info : </label>
            <input type="email" className="form-control" id="contactInfo" name="contactInfo" value={newOperator.contactInfo} onChange={handleInputChange} required />
          </div>
          <button type="submit" className="btn btn-primary" disabled={loading}>Add Operator</button>
        </form>
      </div>
 
      <table className="table mt-4">
        <thead>
          <tr>
            <th>Operator ID</th>
            <th>Operator Name</th>
            <th>Contact Info</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {operators.map(operator => (
            <tr key={operator.operatorId}>
              <td>{operator.operatorId}</td>
              <td>{operator.operatorName}</td>
              <td>{operator.contactInfo}</td>
              <td>
              <button className="btn btn-primary" onClick={() => fetchOperator(operator.operatorId)}>View</button>
                <button className="btn btn-danger" onClick={() => deleteOperator(operator.operatorId)}>Delete</button>
                <button><Link to={`/update-operator/${operator.operatorId}`}> Update Operator </Link></button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {selectedOperator && (
        <div>
          <h3>Operator Details :</h3>
          <p>Operator Name : {selectedOperator.operatorName}</p>
          <p>Contact Info : {selectedOperator.contactInfo}</p>
        </div>
     )}
    </div>
  );
};
 
export default OperatorManagement;