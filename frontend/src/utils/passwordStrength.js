// Lightweight, client-side heuristic used purely for instant visual feedback
// while typing (e.g. in the credential form or register page). This is NOT
// a replacement for the backend's real strength analysis — that lives in
// passwordService.analyzePasswordStrength and is used on the dedicated
// Password Strength page.
export function estimateStrength(password = "") {
  if (!password) return { score: 0, label: "empty" };

  let score = 0;
  if (password.length >= 8) score += 1;
  if (password.length >= 12) score += 1;
  if (/[a-z]/.test(password) && /[A-Z]/.test(password)) score += 1;
  if (/\d/.test(password)) score += 1;
  if (/[^A-Za-z0-9]/.test(password)) score += 1;

  const labels = ["very weak", "weak", "fair", "good", "strong", "strong"];
  const colors = [
    "bg-danger-500",
    "bg-danger-500",
    "bg-warning-500",
    "bg-warning-500",
    "bg-success-500",
    "bg-success-500",
  ];

  return { score, label: labels[score], color: colors[score] };
}
