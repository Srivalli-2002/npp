import React, { useState, useEffect } from "react";
import UserService from "../services/user.service";
import npp2 from "./npp2.jpg"; // Import the image
import "./Home.css";

const Home = () => {
  const [content, setContent] = useState("");

  useEffect(() => {
    UserService.getPublicContent().then(
      (response) => {
        setContent(response.data);
      },
      (error) => {
        const _content =
          (error.response && error.response.data) ||
          error.message ||
          error.toString();

        setContent(_content);
      }
    );
  }, []);

  return (
    <div className="imgcontainer">
      <header className="image">
        <img src={npp2} alt="My Desktop Image" /> {/* Display the image */}
      </header>
    </div>
  );
};

export default Home;