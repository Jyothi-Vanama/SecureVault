import React, { createContext, useContext, useEffect, useState } from "react";
import { loginUser, registerUser } from "../services/authService";

const AuthContext = createContext(null);

const TOKEN_KEY = "securevault_token";
const USER_KEY = "securevault_user";

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [initializing, setInitializing] = useState(true);

  // Restore session when the application starts.
  useEffect(() => {
    const storedToken = localStorage.getItem(TOKEN_KEY);
    const storedUser = localStorage.getItem(USER_KEY);

    if (storedToken) {
      try {
        setUser(
          storedUser
            ? JSON.parse(storedUser)
            : null
        );
      } catch {
        localStorage.removeItem(TOKEN_KEY);
        localStorage.removeItem(USER_KEY);
      }
    }

    setInitializing(false);
  }, []);

  async function login({ email, password }) {
    const data = await loginUser({ email, password });

    // Backend returns:
    // {
    //   token: "..."
    // }

    if (data?.token) {
      const loggedInUser = {
        email,
      };

      localStorage.setItem(TOKEN_KEY, data.token);
      localStorage.setItem(USER_KEY, JSON.stringify(loggedInUser));

      setUser(loggedInUser);
    }

    return data;
  }

  async function register({ name, email, password }) {
    const data = await registerUser({
      name,
      email,
      password,
    });

    // Registration does NOT return a JWT.
    // It only creates the account.
    // The user should login separately afterward.

    return data;
  }

  function logout() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);

    setUser(null);
  }

  const value = {
    user,
    isAuthenticated: Boolean(user),
    initializing,
    login,
    register,
    logout,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);

  if (!ctx) {
    throw new Error("useAuth must be used within an AuthProvider");
  }

  return ctx;
}