import AirtelCOService from '../services/AirtelCOService';
import { Link } from 'react-router-dom';
import { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'font-awesome/css/font-awesome.min.css';
 
const AirtelVerificationDetailsManagement = () => {
  const [details, setDetails] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
 
  useEffect(() => {
    fetchDetails();
  }, []);
 
  const handleSearchChange = (e) => {
    setSearchQuery(e.target.value);
  };
 
  const filteredDetails = details.filter(detail =>
    String(detail.phoneNumber).includes(searchQuery)
  );
  const fetchDetails = () => {
    AirtelCOService.viewVerificationDetails()
      .then(response => {
        setDetails(response);
        console.log(response);
      })
      .catch(error => {
        console.error('Error fetching details : ', error);
      });
  };
 
  return (
    <div className="container mt-5 pt-5">
      <h2 className="mb-5 pt-3">AIRTEL VERIFICATION DETAILS</h2>
      <div className="mb-4">
        <div className="input-group">
          <div className="input-group-prepend">
            <span className="input-group-text">
              <i className="fa fa-search"></i>
            </span>
          </div>
          <input
            type="text"
            className="form-control"
            placeholder="Search by phone number"
            value={searchQuery}
            onChange={handleSearchChange}
          />
        </div>
      </div>
      <table className="table table-striped table-bordered">
        <thead>
          <tr>
            <th>Phone Number</th>
            <th>Customer Identity Verified</th>
            <th>No Outstanding Payments</th>
            <th>Time Since Last Port</th>
            <th>Contractual Obligations Met</th>
            <th>Number Status</th>
            <th>Notification To Operator</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
        {filteredDetails.map(detail => (
            <tr key={detail.phoneNumber}>
              <td>{detail.phoneNumber}</td>
              <td>{detail.customerIdentityVerified.toString()}</td>
              <td>{detail.noOutstandingPayments.toString()}</td>
              <td>{detail.timeSinceLastPort}</td>
              <td>{detail.contractualObligationsMet}</td>
              <td>{detail.numberStatus}</td>
              <td>{detail.notificationToCurrentOperator.toString()}</td>
              <td>
                <Link to={`/update-airtel-deatils/${detail.phoneNumber}`} className="btn btn-light">
                  Update
                </Link>
              </td>
            </tr>
        ))}
        </tbody>
      </table>
    </div>
  );
};
 
export default AirtelVerificationDetailsManagement;
