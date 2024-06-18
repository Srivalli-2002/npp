
import React from 'react';
 
const UserGuide2 = () => {
  return (
    <div className="container py-4">
      <h2 className="mb-4">USER GUIDE - 2: Troubleshooting Common Issues</h2>
      <h3>INTRODUCTION</h3>
      <p>This guide will help you troubleshoot and resolve common issues.</p>
     
      <div className="card mb-4">
        <div className="card-body">
          <h3 className="card-title">COMMON ISSUES AND SOLUTIONS</h3>
         
          <div className="mb-4">
            <h4>Unable to Log In:</h4>
            <p><strong>Issue:</strong> Forgot your password or username.</p>
            <p><strong>Solution:</strong> Click on the "Forgot Password" link on the login page. Enter your email to receive a password reset link.</p>
          </div>
         
          <div className="mb-4">
            <h4>Account Locked:</h4>
            <p><strong>Issue:</strong> Too many unsuccessful login attempts.</p>
            <p><strong>Solution:</strong> Wait 15 minutes and try again or contact customer support for assistance.</p>
          </div>
         
          <div className="mb-4">
            <h4>Payment Issues:</h4>
            <p><strong>Issue:</strong> Failed payment transactions.</p>
            <p><strong>Solution:</strong> Ensure your payment information is up-to-date. Contact your bank if the issue persists.</p>
          </div>
         
          <div className="mb-4">
            <h4>Slow Performance:</h4>
            <p><strong>Issue:</strong> Website or app is running slowly.</p>
            <p><strong>Solution:</strong> Clear your browser cache and cookies. Ensure you have a stable internet connection.</p>
          </div>
         
          <div className="mb-4">
            <h4>Email Notifications Not Received:</h4>
            <p><strong>Issue:</strong> Not receiving expected email notifications.</p>
            <p><strong>Solution:</strong> Check your spam/junk folder. Ensure your email address is correct in your account settings.</p>
          </div>
        </div>
      </div>
     
      <div className="card">
        <div className="card-body">
          <h3 className="card-title">ADDITIONAL RESOURCES</h3>
          <p><strong>Customer Support:</strong> Contact us via email or phone for further assistance.</p>
          <p><strong>Community Forum:</strong> Visit our community forum to see if others have encountered and resolved similar issues.</p>
        </div>
      </div>
    </div>
  );
};
 
export default UserGuide2;
 