import CustomerService from '../services/CustomerService';
import { useNavigate, Link } from 'react-router-dom';
import { useState } from 'react';
import { useRef, useEffect } from 'react';
 
import 'bootstrap/dist/css/bootstrap.min.css';
 
const CustomerServiceManagement = () => {
  const [newCustomer, setNewCustomer] = useState({
    name: '',
    email: '',
    phoneNumber: '',
    currentOperatorId: '',
    newOperatorId: ''
  });
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [selectedCustomer, setSelectedCustomer] = useState(null);
 
  const form = useRef();
  const navigate = useNavigate();
 
  useEffect(() => {
    fetchCustomers();
  }, []);
 
  const fetchCustomers = () => {
    CustomerService.viewCustomers()
      .then(response => {
        setCustomers(response);
        console.log(response);
      })
      .catch(error => {
        console.error('Error fetching customers:', error);
      });
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewCustomer({ ...newCustomer, [name]: value });
  };
 
  const handleSubmit = (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage('');
    CustomerService.addCustomer(newCustomer)
      .then(() => {
        alert('Customer added successfully');
        setNewCustomer({
          name: '',
          email: '',
          phoneNumber: '',
          currentOperatorId: '',
          newOperatorId: ''
        });
        fetchCustomers();
      })
      .catch(error => {
        console.error('Error adding customer:', error);
        alert('An error occurred while adding customer');
      })
      .finally(() => {
        setLoading(false);
      });
  };
 
  const fetchCustomer = async (customerId) => {
    try {
      const customer = await CustomerService.getCustomer(customerId);
      setSelectedCustomer(customer); // Set the selected device
    } catch (error) {
      console.error('Error fetching customer by ID:', error);
    }
  };

  const deleteCustomer = (customerId) => {
    const confirmDelete = window.confirm('Are you sure you want to delete this customer?');
    if (confirmDelete) {
      CustomerService.deleteCustomer(customerId)
        .then(() => {
          fetchCustomers();
        })
        .catch(error => {
          console.error('Error deleting customer:', error);
        });
    }
  };
 
  return (
    <div className="container">
      <h2> Add Customer</h2>
      <div className="card card-container">
        <form onSubmit={handleSubmit} ref={form}>
          <div className="mb-3">
            <label htmlFor="name" className="form-label">Customer Name : </label>
            <input type="text" className="form-control" id="name" name="name" value={newCustomer.name} onChange={handleInputChange} required />
          </div>
          <div className="mb-3">
            <label htmlFor="email" className="form-label">Email : </label>
            <input type="email" className="form-control" id="email" name="email" value={newCustomer.email} onChange={handleInputChange} required />
          </div>
          <div className="mb-3">
            <label htmlFor="phoneNumber" className="form-label">Phone Number :</label>
            <input type="number" className="form-control" id="phoneNumber" name="phoneNumber" value={newCustomer.phoneNumber} onChange={handleInputChange} required />
          </div>
          <div className="mb-3">
            <label htmlFor="currentOperatorId" className="form-label">Current Operator ID :</label>
            <input type="number" className="form-control" id="currentOperatorId" name="currentOperatorId" value={newCustomer.currentOperatorId} onChange={handleInputChange} required />
          </div>
          <div className="mb-3">
            <label htmlFor="newOperatorId" className="form-label">New Operator ID :</label>
            <input type="number" className="form-control" id="newOperatorId" name="newOperatorId" value={newCustomer.newOperatorId} onChange={handleInputChange} required />
          </div>
          
          <button type="submit" className="btn btn-primary" disabled={loading}>Add Customer</button>
        </form>
      </div>
 
      <table className="table mt-4">
        <thead>
          <tr>
            <th>Customer ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Phone Number</th>
            <th>Status</th>
            <th>Current Operator ID</th>
            <th>New Operator ID</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {customers.map(customer => (
            <tr key={customer.customerId}>
              <td>{customer.customerId}</td>
              <td>{customer.name}</td>
              <td>{customer.email}</td>
              <td>{customer.phoneNumber}</td>
              <td>{customer.status}</td>
              <td>{customer.currentOperator.operatorId}</td>
              <td>{customer.newOperator.operatorId}</td>
              <td>
              <button className="btn btn-primary" onClick={() => fetchCustomer(customer.customerId)}>View</button>
                <button className="btn btn-danger" onClick={() => deleteCustomer(customer.customerId)}>Delete</button>
                <button><Link to={`/update-customer/${customer.customerId}`}> Update Customer </Link></button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {selectedCustomer && (
        <div>
          <h3>Customer Details :</h3>
          <p>Customer Name : {selectedCustomer.name}</p>
          <p>Customer Email : {selectedCustomer.email}</p>
          <p>Customer Phone Number : {selectedCustomer.phoneNumber}</p>
          <p>Customer Status : {selectedCustomer.status}</p>
          <p>Customer Current Operator Id : {selectedCustomer.currentOperator.operatorId}</p>
          <p>Customer New Operator Id : {selectedCustomer.newOperator.operatorId}</p>
        </div>
     )}
    </div>
  );
};
 
export default CustomerServiceManagement;