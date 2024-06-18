import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import CustomerService from '../services/CustomerService';
 
function UpdateCustomer() {
  const navigate = useNavigate();
  const { customerId } = useParams();
 
  const [customerData, setCustomerData] = useState({
    name: '',
    email: '',
    phoneNumber: '',
    currentOperatorId: '',
    newOperatorId: ''
  });
 
  useEffect(() => {
    fetchCustomerDataById(customerId);
  }, [customerId]);
 
  const fetchCustomerDataById = async (customerId) => {
    try {
       
      const response = await CustomerService.getCustomer(customerId);
      if (response) {
        setCustomerData(response);
      } else {
        console.error('Error: Customer data is undefined.');
      }
    } catch (error) {
      console.error('Error fetching customer data:', error);
    }
  };
 
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setCustomerData((prevCustomerData) => ({
      ...prevCustomerData,
      [name]: value
    }));
  };
 
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await CustomerService.updateCustomer(customerData);
      navigate("/customermanagement");
    } catch (error) {
      console.error('Error updating customer:', error);
      alert(error.message || 'An error occurred while updating customer.');
    }
  };
 
  return (
    <div className="auth-container">
      <h2>UPDATE CUSTOMER</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Customer Name :</label>
          <input type="text" name="name" value={customerData.name || ''} onChange={handleInputChange} />
        </div>
        <div className="form-group">
          <label>Email :</label>
          <input type="email" name="email" value={customerData.email || ''} onChange={handleInputChange} />
        </div>
        <div className="form-group">
          <label>Phone Number :</label>
          <input type="number" name="phoneNumber" value={customerData.phoneNumber || ''} onChange={handleInputChange} />
        </div>
        <div className="form-group">
          <label>Current Operator ID :</label>
          <input type="number" name="currentOperatorId" value={customerData.currentOperatorId || ''} onChange={handleInputChange} />
        </div>
        <div className="form-group">
          <label>New Operator ID :</label>
          <input type="number" name="newOperatorId" value={customerData.newOperatorId || ''} onChange={handleInputChange} />
        </div>
        <button type="submit">UPDATE</button>
        <div className="card-footer text-center">
          <button className="btn btn-default" onClick={() => navigate("/customermanagement")}>Back to Customer Management</button>
        </div>
      </form>
    </div>
  );
}
 
export default UpdateCustomer;