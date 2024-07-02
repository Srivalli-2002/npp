import React from "react";
import AuthService from "../services/auth.service";

const Profile = () => {
  const currentUser = AuthService.getCurrentUser();

  return (
    <div className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card shadow">
            <div className="card-body">
            <h3 className="card-title mb-4 text-center" style={{ backgroundColor: '#488b8f', color: 'white', padding: '15px', borderRadius: '10px', height: '60px', fontFamily: 'Georgia' }}>USER PROFILE</h3>

              <div className="mb-3"style={{fontFamily: 'Georgia' }}>
                <strong>Username : </strong> {currentUser.username}
              </div>
              <div className="mb-3"style={{fontFamily: 'Georgia' }}>
                <strong>ID : </strong> {currentUser.id}
              </div>
              <div className="mb-3"style={{fontFamily: 'Georgia' }}>
                <strong>Authorities : </strong> {currentUser.role}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;
