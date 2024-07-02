import axios from "axios";
import authHeader from "./auth-header";
 
const BASE_URL = "http://localhost:8080/api/customer";
 
const getStatus = async (customerData) => {
  try {
    const response = await axios.post(`${BASE_URL}/trackstatus`, customerData, {
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


const Customer = {
  getStatus
};

export default Customer;