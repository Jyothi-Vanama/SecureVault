import React, { useState } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import { ShieldHalf, LockKeyhole } from "lucide-react";
import PasswordInput from "../components/PasswordInput";
import { useAuth } from "../context/AuthContext";

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [rememberMe, setRememberMe] = useState(true);
  const [errors, setErrors] = useState({});
  const [submitError, setSubmitError] = useState("");
  const [loading, setLoading] = useState(false);

  function validate() {
    const next = {};
    if (!email.trim()) next.email = "Email is required.";
    else if (!/\S+@\S+\.\S+/.test(email)) next.email = "Enter a valid email address.";
    if (!password) next.password = "Password is required.";
    setErrors(next);
    return Object.keys(next).length === 0;
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setSubmitError("");
    if (!validate()) return;

    setLoading(true);
    try {
      await login({ email, password, rememberMe });
      const redirectTo = location.state?.from?.pathname || "/dashboard";
      navigate(redirectTo, { replace: true });
    } catch (err) {
      setSubmitError(
        err?.response?.data?.message || "We couldn't sign you in. Check your credentials and try again."
      );
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="flex min-h-screen">
      {/* Brand panel */}
      <div className="relative hidden w-1/2 flex-col justify-between overflow-hidden bg-ink-950 p-12 text-white lg:flex">
        <div className="flex items-center gap-2">
          <ShieldHalf className="h-7 w-7 text-brand-400" strokeWidth={2} />
          <span className="font-display text-xl font-semibold">SecureVault</span>
        </div>

        <div className="max-w-md">
          <LockKeyhole className="mb-6 h-10 w-10 text-brand-400" strokeWidth={1.5} />
          <h1 className="font-display text-3xl font-semibold leading-tight">
            Every credential, encrypted and in one place.
          </h1>
          <p className="mt-4 text-sm leading-relaxed text-ink-300">
            SecureVault keeps your passwords behind AES encryption and BCrypt-hashed
            authentication, so you only ever have to remember one.
          </p>
        </div>

        <p className="text-xs text-ink-500">
          © {new Date().getFullYear()} SecureVault. All rights reserved.
        </p>
      </div>

      {/* Form panel */}
      <div className="flex w-full flex-1 items-center justify-center bg-canvas px-6 py-12 lg:w-1/2">
        <div className="w-full max-w-sm">
          <div className="mb-8 flex items-center gap-2 lg:hidden">
            <ShieldHalf className="h-6 w-6 text-brand-500" strokeWidth={2} />
            <span className="font-display text-lg font-semibold text-ink-950">SecureVault</span>
          </div>

          <h2 className="text-2xl font-semibold text-ink-950">Welcome back</h2>
          <p className="mt-1.5 text-sm text-ink-500">
            Sign in to access your vault.
          </p>

          {submitError && (
            <div className="mt-5 rounded-lg border border-danger-100 bg-danger-100/60 px-3.5 py-2.5 text-sm text-danger-600">
              {submitError}
            </div>
          )}

          <form onSubmit={handleSubmit} className="mt-6 space-y-5">
            <div>
              <label htmlFor="email" className="field-label">
                Email
              </label>
              <input
                id="email"
                type="email"
                autoComplete="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="you@example.com"
                className={`field-input ${errors.email ? "border-danger-500" : ""}`}
              />
              {errors.email && <p className="field-error">{errors.email}</p>}
            </div>

            <PasswordInput
              id="login-password"
              label="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              error={errors.password}
              autoComplete="current-password"
            />

            <div className="flex items-center justify-between">
              <label className="flex items-center gap-2 text-sm text-ink-500">
                <input
                  type="checkbox"
                  checked={rememberMe}
                  onChange={(e) => setRememberMe(e.target.checked)}
                  className="h-4 w-4 rounded border-ink-300 text-brand-500 focus:ring-brand-500"
                />
                Remember me
              </label>
              <a href="#" className="text-sm font-medium text-brand-600 hover:text-brand-700">
                Forgot password?
              </a>
            </div>

            <button type="submit" className="btn-primary w-full" disabled={loading}>
              {loading ? "Signing in…" : "Sign in"}
            </button>
          </form>

          <p className="mt-6 text-center text-sm text-ink-500">
            Don't have an account?{" "}
            <Link to="/register" className="font-semibold text-brand-600 hover:text-brand-700">
              Create one
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}
