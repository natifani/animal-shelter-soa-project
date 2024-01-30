import React, { useEffect, useState } from "react";
import { Route, Routes } from "react-router-dom";
import NavBar from "./components/NavBar";

import "./index.css";
import Login from "./components/Login";
import Register from "./components/Register";
import { getCurrentUser } from "./services/AuthService";

const RemoteAnimalApp = React.lazy(() => import("animal/AnimalApp"));
const RemoteAdoptionApp = React.lazy(() => import("adoption/AdoptionApp"));

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
      <NavBar user={userData} />
      <React.Suspense fallback={() => 'loading...'}>
        <Routes>
          {userData.isUser === true && (<Route path="/animals/*" element={<RemoteAnimalApp />} />)}
          {userData.isAdmin === true && <Route path="/adoptions/*" element={<RemoteAdoptionApp />} />}
          {!userData.user && (<Route path="/login" element={<Login />} />)}
          {!userData.user && (<Route path="/signup" element={<Register />} />)}
        </Routes>
      </React.Suspense>
    </div>
  );
};

export default App;
