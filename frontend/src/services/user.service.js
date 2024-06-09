import axios from "axios";
import authHeader from "./auth-header";

const API_URL = "http://localhost:8080/api/test/";

const getPublicContent = () => {
  return axios.get(API_URL + "all");
};

const getSystemAdmin = () => {
  return axios.get(API_URL + "admin", { headers: authHeader() });
};

const getCustomerService = () => {
  return axios.get(API_URL + "customeservices", { headers: authHeader() });
};

const getComplianceOfficer = () => {
  return axios.get(API_URL + "complianceofficer", { headers: authHeader() });
};

const UserService = {
  getPublicContent,
  getSystemAdmin,
  getCustomerService,
  getComplianceOfficer,
};

export default UserService;
