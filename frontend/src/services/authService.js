import api from "./api";

export async function loginUser(credentials) {
  const response = await api.post("/auth/login", credentials);

  // Backend response:
  // {
  //   success: true,
  //   message: "Login successful",
  //   data: {
  //     token: "..."
  //   }
  // }

  return response.data.data;
}

export async function registerUser(payload) {
  const response = await api.post("/auth/register", payload);

  // Backend response:
  // {
  //   success: true,
  //   message: "User registered successfully",
  //   data: {
  //     userId: 1,
  //     name: "...",
  //     email: "..."
  //   }
  // }

  return response.data.data;
}