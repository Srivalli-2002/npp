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
            <h3 className="card-title mb-4 text-center" style={{ backgroundColor: '#a21232', color: 'white', padding: '15px', borderRadius: '10px', height: '60px', fontFamily: 'Cambria' }}>USER PROFILE</h3>

              <div className="mb-3"style={{fontFamily: 'Cambria' }}>
                <strong>Username : </strong> {currentUser.username}
              </div>
              <div className="mb-3"style={{fontFamily: 'Cambria' }}>
                <strong>ID : </strong> {currentUser.id}
              </div>
              <div className="mb-3"style={{fontFamily: 'Cambria' }}>
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
