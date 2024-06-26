import React, { useState, useEffect } from "react";
import UserService from "../services/user.service";
import npp1 from "./npp1.jpg"; // Import the image
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
        <img src={npp1} alt="My Desktop Image" /> {/* Display the image */}
      </header>
    </div>
  );
};

export default Home;
