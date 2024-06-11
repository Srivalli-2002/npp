import SystemAdminService from '../services/SystemAdminService';
import { Link } from 'react-router-dom';
import { useState, useEffect, useRef } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './UserManagement.css'; 

const UserManagement = () => {
  const [users, setUsers] = useState([]);

  const form = useRef();

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
    <div className="container mt-5 pt-3">
      <h2 className="mb-5">USER MANAGEMENT</h2>
      <table className="table table-striped table-bordered">
        <thead>
          <tr>
            <th>User ID</th>
            <th>Username</th>
            <th>Password Hash</th>
            <th>Operator ID</th>
            <th>Role ID</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {users.map(user => (
            <tr key={user.userId}>
              <td>{user.userId}</td>
              <td>{user.username}</td>
              <td>{user.passwordHash}</td>
              <td>{user.operator.operatorId}</td>
              <td>{user.role.id}</td>
              <td>
                <Link to={`/update-userrole/${user.userId}`} className="btn btn-default btn-sm">
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
