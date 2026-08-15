import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import { useAuth } from "./context/AuthContext";
import ProtectedRoute from "./components/ProtectedRoute";

import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import Vault from "./pages/Vault";
import AddCredential from "./pages/AddCredential";
import EditCredential from "./pages/EditCredential";
import CredentialDetails from "./pages/CredentialDetails";
import Trash from "./pages/Trash";
import PasswordGenerator from "./pages/PasswordGenerator";
import PasswordStrength from "./pages/PasswordStrength";
import SharedCredentials from "./pages/SharedCredentials";

// Additional routes (AddCredential, EditCredential, CredentialDetails,
// PasswordGenerator, PasswordStrength, Trash, SharedCredentials, Profile,
// Settings) are built in the next pass and will be added here as:
//   /vault/add, /vault/:id, /vault/:id/edit, /generator,
//   /password-strength, /trash, /shared, /profile, /settings

export default function App() {
  const { isAuthenticated, initializing } = useAuth();

  return (
    <Routes>
      <Route
        path="/"
        element={
          initializing ? null : (
            <Navigate to={isAuthenticated ? "/dashboard" : "/login"} replace />
          )
        }
      />

      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />

      <Route
        path="/dashboard"
        element={
          <ProtectedRoute>
            <Dashboard />
          </ProtectedRoute>
        }
      />

      <Route
        path="/vault"
        element={
          <ProtectedRoute>
            <Vault />
          </ProtectedRoute>
        }
      />

      <Route
  path="/vault/add"
  element={
    <ProtectedRoute>
      <AddCredential />
    </ProtectedRoute>
  }
/>
<Route
  path="/vault/:id"
  element={
    <ProtectedRoute>
      <CredentialDetails />
    </ProtectedRoute>
  }
/>
<Route
  path="/vault/:id/edit"
  element={
    <ProtectedRoute>
      <EditCredential />
    </ProtectedRoute>
  }
/>
<Route
  path="/trash"
  element={
    <ProtectedRoute>
      <Trash />
    </ProtectedRoute>
  }
/>

<Route
  path="/generator"
  element={
    <ProtectedRoute>
      <PasswordGenerator />
    </ProtectedRoute>
  }
/>
<Route
  path="/password-strength"
  element={
    <ProtectedRoute>
      <PasswordStrength />
    </ProtectedRoute>
  }
/>
<Route
  path="/shared"
  element={
    <ProtectedRoute>
      <SharedCredentials />
    </ProtectedRoute>
  }
/>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
