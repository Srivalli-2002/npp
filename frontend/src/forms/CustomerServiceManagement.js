import CustomerService from '../services/CustomerService';
import { useNavigate, Link } from 'react-router-dom';
import { useState } from 'react';
import { useRef, useEffect } from 'react';
import './CustomerServiceManagement.css';
import 'bootstrap/dist/css/bootstrap.min.css';
 
const CustomerServiceManagement = () => {
  const [newCustomer, setNewCustomer] = useState({
    name: '',
    email: '',
    phoneNumber: '',
    status: '',
    username:'',
    currentOperatorName:'',
    newOperatorName: ''
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
          status: '',
          username:'',
          currentOperatorName:'',
          newOperatorName: ''
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
      <h2> ADD CUSTOMER </h2>
      <div className="card card-container">
        <form onSubmit={handleSubmit} ref={form}>
          <div className="mb-3">
            <label htmlFor="name" className="form-label">Customer Name : </label>
            <input type="text" className="form-control" id="name" name="name" value={newCustomer.name} onChange={handleInputChange} required />
          </div>
          <div className="mb-3">
            <label htmlFor="username" className="form-label">Username : </label>
            <input type="text" className="form-control" id="username" name="username" value={newCustomer.username} onChange={handleInputChange} required />
          </div>
          <div className="mb-3">
            <label htmlFor="email" className="form-label">Email : </label>
            <input type="email" className="form-control" id="email" name="email" value={newCustomer.email} onChange={handleInputChange} required />
          </div>
          <div className="mb-3">
            <label htmlFor="phoneNumber" className="form-label">Phone Number :</label>
            <input type="number" className="form-control" id="phoneNumber" name="phoneNumber" value={newCustomer.phoneNumber} onChange={handleInputChange} required />
          </div>
          
          <button type="submit" className="btn btn-default" disabled={loading}>ADD CUSTOMER</button>
        </form>
      </div>
 
      <table className="table table-striped table-bordered">
        <thead>
          <tr>
            <th>Customer ID</th>
            <th>Name</th>
            <th>Username</th>
            <th>Email</th>
            <th>Phone Number</th>
            <th>Status</th>
            <th>Current Operator Name</th>
            <th>New Operator Name</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {customers.map(customer => (
            <tr key={customer.customerId}>
              <td>{customer.customerId}</td>
              <td>{customer.name}</td>
              <td>{customer.username}</td>
              <td>{customer.email}</td>
              <td>{customer.phoneNumber}</td>
              <td>{customer.status}</td>
              <td>{customer.currentOperator.operatorName}</td>
              <td>{customer.newOperator.operatorName}</td>
              <td>
              <button className="btn btn-default" onClick={() => fetchCustomer(customer.customerId)}>View</button>
              <button className="btn btn-default"><Link to={`/update-customer/${customer.customerId}`}>Update</Link></button>
              <button className="btn btn-default" onClick={() => deleteCustomer(customer.customerId)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>


      
      {selectedCustomer &&(
      <div>
      <h2> CUSTOMER DETAILS</h2>
      <table className="table mt-4 table-striped table-bordered">
        <thead>
          <tr>
            <th>Customer Form</th>
            <th>Customer Details</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td className="left-align">Name</td>
            <td className="left-align">{selectedCustomer.name}</td>
          </tr>
          <tr>
            <td className="left-align">Username</td>
            <td className="left-align">{selectedCustomer.username}</td>
          </tr>
          <tr>
            <td className="left-align">Email</td>
            <td className="left-align">{selectedCustomer.email}</td>
          </tr>
          <tr>
            <td className="left-align">Phone Number</td>
            <td className="left-align">{selectedCustomer.phoneNumber}</td>
          </tr>
          <tr>
            <td className="left-align">Status</td>
            <td className="left-align">{selectedCustomer.status}</td>
          </tr>
          <tr>
            <td className="left-align">Current Operator Name</td>
            <td className="left-align">{selectedCustomer.currentOperator.operatorName}</td>
          </tr>
          <tr>
            <td className="left-align">New Operator Name</td>
            <td className="left-align">{selectedCustomer.newOperator.operatorName}</td>
          </tr>
        </tbody>
      </table>
      </div>
     )}
    </div>
  );
};
 
export default CustomerServiceManagement;