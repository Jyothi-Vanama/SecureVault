import api from "./api";

/**
 * Analyze password strength using the backend API.
 *
 * @param {string} password
 * @returns {Promise<{ score: number, strength: string, feedback: string[] }>}
 */
export async function analyzePasswordStrength(password) {
  const response = await api.post("/password/strength", {
    password,
  });

  return response.data.data;
}

/**
 * Generate a secure password using the backend API.
 *
 * @param {{
 *   length: number,
 *   uppercase: boolean,
 *   lowercase: boolean,
 *   numbers: boolean,
 *   symbols: boolean
 * }} options
 *
 * @returns {Promise<{ password: string }>}
 */
export async function generatePassword(options) {
  const response = await api.post(
    "/password/generate",
    options
  );

  return response.data.data;
}