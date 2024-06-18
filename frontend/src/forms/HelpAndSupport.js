import React from 'react';
import {Link} from 'react-router-dom';
import './HelpAndSupport.css';

const HelpAndSupport = () => {
    return (
        <div className='guide-container mt-5 pt-5'>
            <h1 className='heading'>NUMBER PORTABILITY PORTAL HELP & SUPPORT GUIDE</h1>
 
            <section>
                <h2 className='subheading'>INTRODUCTION</h2>
                <p>Welcome to the Number Portability Portal Help and Support Guide. This document is designed to assist users in navigating and troubleshooting the Number Portability Portal effectively.</p>
            </section>
 
            <section>
                <h2 className='subheading'>ACCESSING THE PORTAL</h2>
                <p>To access the Number Portability Portal:</p>
                <ul>
                    <li>Visit the official website: <a href="http://localhost:3000/">NumberPortabilityPortal</a></li>
                    <li>Enter your username and password to log in.</li>
                    <li>If you do not have an account, follow the registration process by clicking on "Sign Up" and providing the necessary information.</li>
                </ul>
            </section>
 
            <section>
                <h2 className='subheading'>COMMON ISSUES AND SOLUTIONS</h2>
                <div>
                    <h3>Unable to Log In</h3>
                    <ul>
                        <li>Ensure your username and password are correct.</li>
                        <li>Check your internet connection.</li>
                        <li>Clear your browser cache and cookies.</li>
                    </ul>
                </div>
                <div>
                    <h3>Porting Request Not Processing</h3>
                    <ul>
                        <li>Ensure all required fields in the porting request form are filled out correctly.</li>
                        <li>Verify that the information provided matches the records of your current service provider.</li>
                        <li>Check for any system maintenance announcements that might affect processing times.</li>
                    </ul>
                </div>
                <div>
                    <h3>Receiving Error Messages</h3>
                    <ul>
                        <li>Take note of the specific error message.</li>
                        <li>Refer to the error message section in the User Guide for detailed explanations and solutions.</li>
                        <li>Contact support if the issue persists.</li>
                    </ul>
                </div>
            </section>
 
            <section>
                <h2 className='subheading'>USER GUIDES</h2>
                <ul>
                    <li>
                <button button className="btn btn"><Link to={"/support&assistance/userguide1"}>
                User Guide 1 : How to Submit a Porting Request
              </Link></button>
              </li>
              <li>
              <button button className="btn btn"><Link to={"/support&assistance/userguide2"}>
              User Guide 2 : Troubleshooting Common Issues
              </Link></button>
              </li>
                </ul>
            </section>
 
            <section>
                <h2 className='subheading'>FREQUENTLY ASKED QUESTIONS (FAQs)</h2>
                <div>
                    <p><strong>Q1: What is number portability?</strong></p>
                    <p>A: Number portability allows you to switch from one service provider to another without changing your phone number.</p>
                </div>
                <div>
                    <p><strong>Q2: How long does the porting process take?</strong></p>
                    <p>A: The porting process typically takes between 1-7 business days, depending on the service providers involved.</p>
                </div>
                <div>
                    <p><strong>Q3: Are there any charges for number portability?</strong></p>
                    <p>A: Some service providers may charge a fee for porting your number. Check with your current and new providers for details.</p>
                </div>
            </section>
 
            <section>
                <h2 className='subheading'>CONTACT SUPPORT</h2>
                <p>If you need further assistance, please contact our support team:</p>
                <ul>
                    <li><strong>Email</strong>: <a href="mailto:support@numberportabilityportal.com">support@numberportabilityportal.com</a></li>
                    <li><strong>Phone</strong>: 1-800-123-4567</li>
                    <li><strong>Live Chat</strong>: Available on the portal from 9 AM to 5 PM (EST), Monday to Friday.</li>
                </ul>
            </section>
        </div>
    );
};
 
export default HelpAndSupport;
 