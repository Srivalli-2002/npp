import axios from "axios";
import authHeader from "../services/auth-header";
 
const BASE_URL = "http://localhost:8080/api/customerservice";
 
const addCustomer = async (customerData) => {
  try {
    const response = await axios.post(`${BASE_URL}/addcustomer`, customerData, {
      headers: {
        'Content-Type': 'application/json',
        ...authHeader(),
        'Access-Control-Allow-Origin': '*'
      }
    });
    return response.data;
  } catch (err) {
    throw err;
  }
};

const getCustomer = async (customerId) => {
  try {
    const response = await axios.post(`${BASE_URL}/getcustomer`, customerId, {
      headers: {
        'Content-Type': 'application/json',
        ...authHeader(),
        'Access-Control-Allow-Origin': '*'
      }
    });
    return response.data;
  } catch (err) {
    throw err;
  }
};

const updateCustomer = async (customerData) => {
  try {
    const response = await axios.post(`${BASE_URL}/updatecustomer`, customerData, {
      headers: {
        'Content-Type': 'application/json',
        ...authHeader(),
        'Access-Control-Allow-Origin': '*'
      }
    });
    return response.data;
  } catch (err) {
    throw err;
  }
};

const deleteCustomer = async (customerId) => {
  try {
    const response = await axios.post(`${BASE_URL}/deletecustomer`, customerId, {
      headers: {
        'Content-Type': 'application/json',
        ...authHeader(),
        'Access-Control-Allow-Origin': '*'
      }
    });
    return response.data;
  } catch (err) {
    throw err;
  }
};

const viewCustomers = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/getallcustomers`, {
      headers: {
        'Content-Type': 'application/json',
        ...authHeader(),
        'Access-Control-Allow-Origin': '*'
      }
    });
    return response.data;
   
  } catch (err) {
    throw err;
  }
  
};

const submitPortRequest = async (portRequestData) => {
  try {
    const response = await axios.post(`${BASE_URL}/submitportrequest`, portRequestData, {
      headers: {
        'Content-Type': 'application/json',
        ...authHeader(),
        'Access-Control-Allow-Origin': '*'
      }
    });
    return response.data;
  } catch (err) {
    throw err;
  }
};

const getPortRequest = async (requestId) => {
  try {
    const response = await axios.post(`${BASE_URL}/getportrequest`, requestId, {
      headers: {
        'Content-Type': 'application/json',
        ...authHeader(),
        'Access-Control-Allow-Origin': '*'
      }
    });
    return response.data;
  } catch (err) {
    throw err;
  }
};

const updatePortRequest = async (portRequestData) => {
  try {
    const response = await axios.post(`${BASE_URL}/updateportrequest`, portRequestData, {
      headers: {
        'Content-Type': 'application/json',
        ...authHeader(),
        'Access-Control-Allow-Origin': '*'
      }
    });
    return response.data;
  } catch (err) {
    throw err;
  }
};

const deletePortRequest = async (requestId) => {
  try {
    const response = await axios.post(`${BASE_URL}/deleteportrequest`, requestId, {
      headers: {
        'Content-Type': 'application/json',
        ...authHeader(),
        'Access-Control-Allow-Origin': '*'
      }
    });
    return response.data;
  } catch (err) {
    throw err;
  }
};

const viewPortRequests = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/getallportrequests`, {
      headers: {
        'Content-Type': 'application/json',
        ...authHeader(),
        'Access-Control-Allow-Origin': '*'
      }
    });
    return response.data;
   
  } catch (err) {
    throw err;
  }
  
};

const CustomerService = {
  addCustomer,
  getCustomer,
  updateCustomer,
  deleteCustomer,
  viewCustomers,
  submitPortRequest,
  getPortRequest,
  updatePortRequest,
  deletePortRequest,
  viewPortRequests
};

export default CustomerService;