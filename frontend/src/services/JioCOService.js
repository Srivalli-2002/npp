import axios from "axios";
import authHeader from "../services/auth-header";
 
const BASE_URL = "http://localhost:8080/api/jiocomplianceofficer";
 
const addVerificationDetails = async () => {
    try {
      const response = await axios.get(`${BASE_URL}/addverificationdetails`, {
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
   
  const updateVerificationDetails = async (detailsData) => {
    try {
      const response = await axios.post(`${BASE_URL}/updateverificationdetails`, detailsData, {
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
   
  const viewVerificationDetails = async () => {
    try {
      const response = await axios.get(`${BASE_URL}/getallverificationdetails`, {
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
   
  const getVerificationDetailsByPhn = async (phoneNumber) => {
    try {
      const response = await axios.post(`${BASE_URL}/getverificationdetailsbyphn`, phoneNumber, {
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
 
  const JioCOService = {
    addVerificationDetails,
    updateVerificationDetails,
    viewVerificationDetails,
    getVerificationDetailsByPhn
};
 
export default JioCOService;