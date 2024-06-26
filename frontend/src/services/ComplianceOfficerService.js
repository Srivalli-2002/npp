import axios from "axios";
import authHeader from "../services/auth-header";
 
const BASE_URL = "http://localhost:8080/api/complianceofficer";
 
const getLog = async (logId) => {
  try {
    const response = await axios.post(`${BASE_URL}/getlog`, logId, {
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
 
const updateLog = async (logId) => {
  try {
    const response = await axios.post(`${BASE_URL}/updatelog`, logId, {
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
 
const viewLogs = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/getalllogs`, {
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
 
const getVerificationDetailsByLog = async (logId) => {
  try {
    const response = await axios.post(`${BASE_URL}/getverificationdetailsbylogid`, logId, {
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
 
const ComplianceOfficerService = {
    getLog,
    updateLog,
    viewLogs,
    addVerificationDetails,
    updateVerificationDetails,
    viewVerificationDetails,
    getVerificationDetailsByLog,
    getVerificationDetailsByPhn
};
 
export default ComplianceOfficerService;