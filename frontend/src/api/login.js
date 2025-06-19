import axios from "axios";

export const loginUser = async (loginForm) => {
  try {
    const res = await axios.post("/api/auth/login", loginForm);
    return res.data;
  } catch (error) {
    console.error("Login API error:", error);
    throw error;
  }
};
