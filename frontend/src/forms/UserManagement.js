import SystemAdminService from '../services/SystemAdminService';
import { useNavigate, Link } from 'react-router-dom';
import { useState } from 'react';
import { useRef, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
const UserManagement = () => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState('');

    const form = useRef();
    const navigate = useNavigate();
   
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
    <div className="container">
      <h2>Users</h2>
        <table className="table mt-4">
                <thead>
                <tr>
                    <th>User ID</th>
                    <th>Username</th>
                    <th>Password_Hash</th>
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
                        <button><Link to={`/update-userrole/${user.userId}`}> Update Role </Link></button>
                    </td>
                    </tr>
                ))}
                </tbody>
            </table>
            </div>
        );
};

export default UserManagement;