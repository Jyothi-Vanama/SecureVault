import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { ShieldHalf, ShieldCheck } from "lucide-react";
import PasswordInput from "../components/PasswordInput";
import { useAuth } from "../context/AuthContext";

export default function Register() {
  const { register } = useAuth();
  const navigate = useNavigate();

  const [form, setForm] = useState({ name: "", email: "", password: "", confirmPassword: "" });
  const [errors, setErrors] = useState({});
  const [submitError, setSubmitError] = useState("");
  const [loading, setLoading] = useState(false);

  function update(field, value) {
    setForm((f) => ({ ...f, [field]: value }));
    if (errors[field]) setErrors((e) => ({ ...e, [field]: undefined }));
  }

  function validate() {
    const next = {};
    if (!form.name.trim()) next.name = "Name is required.";
    if (!form.email.trim()) next.email = "Email is required.";
    else if (!/\S+@\S+\.\S+/.test(form.email)) next.email = "Enter a valid email address.";
    if (!form.password) next.password = "Password is required.";
    else if (form.password.length < 8) next.password = "Use at least 8 characters.";
    if (form.confirmPassword !== form.password) next.confirmPassword = "Passwords don't match.";
    setErrors(next);
    return Object.keys(next).length === 0;
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setSubmitError("");
    if (!validate()) return;

    setLoading(true);
    try {
      await register({ name: form.name, email: form.email, password: form.password });
      navigate("/dashboard", { replace: true });
    } catch (err) {
      setSubmitError(
        err?.response?.data?.message ||
          "We couldn't create your account. That email may already be registered."
      );
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="flex min-h-screen">
      <div className="relative hidden w-1/2 flex-col justify-between overflow-hidden bg-ink-950 p-12 text-white lg:flex">
        <div className="flex items-center gap-2">
          <ShieldHalf className="h-7 w-7 text-brand-400" strokeWidth={2} />
          <span className="font-display text-xl font-semibold">SecureVault</span>
        </div>

        <div className="max-w-md">
          <ShieldCheck className="mb-6 h-10 w-10 text-brand-400" strokeWidth={1.5} />
          <h1 className="font-display text-3xl font-semibold leading-tight">
            Start building a vault you can actually trust.
          </h1>
          <p className="mt-4 text-sm leading-relaxed text-ink-300">
            Set a strong master password once. SecureVault handles the rest —
            generation, strength checks, and encrypted storage.
          </p>
        </div>

        <p className="text-xs text-ink-500">
          © {new Date().getFullYear()} SecureVault. All rights reserved.
        </p>
      </div>

      <div className="flex w-full flex-1 items-center justify-center bg-canvas px-6 py-12 lg:w-1/2">
        <div className="w-full max-w-sm">
          <div className="mb-8 flex items-center gap-2 lg:hidden">
            <ShieldHalf className="h-6 w-6 text-brand-500" strokeWidth={2} />
            <span className="font-display text-lg font-semibold text-ink-950">SecureVault</span>
          </div>

          <h2 className="text-2xl font-semibold text-ink-950">Create your account</h2>
          <p className="mt-1.5 text-sm text-ink-500">
            It only takes a minute.
          </p>

          {submitError && (
            <div className="mt-5 rounded-lg border border-danger-100 bg-danger-100/60 px-3.5 py-2.5 text-sm text-danger-600">
              {submitError}
            </div>
          )}

          <form onSubmit={handleSubmit} className="mt-6 space-y-5">
            <div>
              <label htmlFor="name" className="field-label">
                Name
              </label>
              <input
                id="name"
                value={form.name}
                onChange={(e) => update("name", e.target.value)}
                placeholder="Your full name"
                className={`field-input ${errors.name ? "border-danger-500" : ""}`}
              />
              {errors.name && <p className="field-error">{errors.name}</p>}
            </div>

            <div>
              <label htmlFor="reg-email" className="field-label">
                Email
              </label>
              <input
                id="reg-email"
                type="email"
                autoComplete="email"
                value={form.email}
                onChange={(e) => update("email", e.target.value)}
                placeholder="you@example.com"
                className={`field-input ${errors.email ? "border-danger-500" : ""}`}
              />
              {errors.email && <p className="field-error">{errors.email}</p>}
            </div>

            <PasswordInput
              id="reg-password"
              label="Password"
              value={form.password}
              onChange={(e) => update("password", e.target.value)}
              error={errors.password}
              showStrengthMeter
              autoComplete="new-password"
            />

            <PasswordInput
              id="reg-confirm-password"
              label="Confirm password"
              value={form.confirmPassword}
              onChange={(e) => update("confirmPassword", e.target.value)}
              error={errors.confirmPassword}
              autoComplete="new-password"
            />

            <button type="submit" className="btn-primary w-full" disabled={loading}>
              {loading ? "Creating account…" : "Create account"}
            </button>
          </form>

          <p className="mt-6 text-center text-sm text-ink-500">
            Already have an account?{" "}
            <Link to="/login" className="font-semibold text-brand-600 hover:text-brand-700">
              Sign in
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}
