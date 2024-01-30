import React, { useState, useEffect } from "react";
import { Route, Routes } from "react-router-dom";
import AdoptionList from "./components/AdoptionList";
import { getCurrentUser } from "./services/AuthService";

import "./index.css";

const RemoteLoginApp = React.lazy(() => (import("login/Login")));

const App = () => {
  const [userData, setUserData] = useState({
    user: null,
    isUser: false,
    isAdmin: false
  });

  useEffect(() => {
    const currentUser = getCurrentUser();
    if (currentUser) {
      setUserData({
        user: currentUser,
        isUser: currentUser.roles.includes("ROLE_USER"),
        isAdmin: currentUser.roles.includes("ROLE_ADMIN")
      });
    }
  }, []);

  return (
    <div>
      <Routes>
        {userData.isAdmin && (<Route path="/" element={<AdoptionList />} />)}
        {!userData.user && (<Route path="/login" element={<RemoteLoginApp />} />)}
      </Routes>
    </div>
  );
};

export default App;
