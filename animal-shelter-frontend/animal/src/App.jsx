import React, { useState, useEffect } from "react";
import { Route, Routes } from "react-router-dom";
import AnimalList from "./components/AnimalList";
import AddAnimal from "./components/AddAnimal";
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
      <React.Suspense fallback={() => 'loading...'}></React.Suspense>
      <Routes>
        {userData.isUser && (<Route path="/" element={<AnimalList />} />)}
        {userData.isAdmin && (<Route path="/add/:id" element={<AddAnimal />} />)}
        {!userData.user && (<Route path="/login" element={<RemoteLoginApp />} />)}
      </Routes>
    </div>
  );

};

export default App;
