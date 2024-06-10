import axios from "axios";
import authHeader from "../services/auth-header";
 
const BASE_URL = "http://localhost:8080/api/complianceofficer";
 
const addLog = async (logData) => {
  try {
    const response = await axios.post(`${BASE_URL}/addlog`, logData, {
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

const updateLog = async (logData) => {
  try {
    const response = await axios.post(`${BASE_URL}/updatelog`, logData, {
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

const ComplianceOfficerService = {
    addLog,
    getLog,
    updateLog,
    viewLogs
};

export default ComplianceOfficerService;