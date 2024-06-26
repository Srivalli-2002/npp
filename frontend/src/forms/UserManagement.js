import SystemAdminService from '../services/SystemAdminService';
import { Link } from 'react-router-dom';
import { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './UserManagement.css';

const UserManagement = () => {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = () => {
    SystemAdminService.viewUsers()
      .then(response => {
        setUsers(response);
        console.log(response);
      })
      .catch(error => {
        console.error('Error fetching users : ', error);
      });
  };

  return (
    <div className="container mt-5 pt-5">
      <h2 className="mb-5 pt-3">USER MANAGEMENT</h2>
      <table className="table table-striped table-bordered">
        <thead>
          <tr>
            <th>User ID</th>
            <th>Username</th>
            <th>Operator Name</th>
            <th>Role</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {users.map(user => (
            <tr key={user.userId}>
              <td>{user.userId}</td>
              <td>{user.username}</td>
              <td>{user.operator.operatorName}</td>
              <td>{user.role.name}</td>
              <td>
                <Link to={`/update-userrole/${user.userId}`} className="btn btn-light">
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

export default UserManagement;
