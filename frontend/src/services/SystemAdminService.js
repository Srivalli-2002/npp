import axios from "axios";
import authHeader from "../services/auth-header";
 
const BASE_URL = "http://localhost:8080/api/admin";
 
const updateUserRole = async (userData) => {
    try {
      const response = await axios.post(`${BASE_URL}/updateuserrole`, userData, {
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
  
  const viewUsers = async () => {
    try {
      const response = await axios.get(`${BASE_URL}/getallusers`, {
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

const addOperator = async (operatorData) => {
  try {
    const response = await axios.post(`${BASE_URL}/addoperator`, operatorData, {
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

const getOperator = async (operatorId) => {
  try {
    const response = await axios.post(`${BASE_URL}/getoperator`, operatorId, {
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

const updateOperator = async (operatorData) => {
  try {
    const response = await axios.post(`${BASE_URL}/updateoperator`, operatorData, {
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

const deleteOperator = async (operatorId) => {
  try {
    const response = await axios.post(`${BASE_URL}/deleteoperator`, operatorId, {
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

const viewOperators = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/getalloperators`, {
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



const SystemAdmin = {
    updateUserRole,
    viewUsers,
    addOperator,
    getOperator,
    updateOperator,
    deleteOperator,
    viewOperators
};

export default SystemAdmin;