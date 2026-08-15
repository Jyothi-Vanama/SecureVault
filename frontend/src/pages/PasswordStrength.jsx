import React, { useState } from "react";
import { ShieldCheck } from "lucide-react";

import AppLayout from "../components/AppLayout";
import PasswordInput from "../components/PasswordInput";
import { analyzePasswordStrength } from "../services/passwordService";

export default function PasswordStrength() {
  const [password, setPassword] = useState("");
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  async function handleAnalyze() {
    if (!password) {
      setError("Please enter a password.");
      setResult(null);
      return;
    }

    try {
      setLoading(true);
      setError("");

      const data = await analyzePasswordStrength(password);

      setResult(data);
    } catch (err) {
      console.error("Password strength analysis error:", err);

      setResult(null);

      setError(
        err.response?.data?.message ||
          err.message ||
          "Unable to analyze password. Please try again."
      );
    } finally {
      setLoading(false);
    }
  }

  function getStrengthStyle(strength) {
    switch (String(strength || "").toLowerCase()) {
      case "strong":
        return "bg-success-100 text-success-600";

      case "medium":
        return "bg-warning-100 text-warning-600";

      case "weak":
        return "bg-danger-100 text-danger-600";

      default:
        return "bg-ink-100 text-ink-500";
    }
  }

  return (
    <AppLayout>
      <div className="mx-auto max-w-3xl">

        {/* Header */}
        <div className="mb-6">
          <div className="mb-3 flex h-11 w-11 items-center justify-center rounded-xl bg-brand-50 text-brand-600">
            <ShieldCheck className="h-5 w-5" />
          </div>

          <h1 className="text-2xl font-semibold text-ink-950">
            Password Strength
          </h1>

          <p className="mt-1 text-sm text-ink-500">
            Analyze your password strength and get suggestions
            for improving security.
          </p>
        </div>

        {/* Main panel */}
        <div className="panel p-6 sm:p-8">

          <label className="field-label" htmlFor="strength-password">
            Password
          </label>

          <PasswordInput
            id="strength-password"
            value={password}
            onChange={(e) => {
              setPassword(e.target.value);
              setError("");
              setResult(null);
            }}
            placeholder="Enter a password to analyze"
            showCopyButton
            autoComplete="off"
          />

          {error && (
            <div className="mt-4 rounded-lg border border-danger-200 bg-danger-50 px-4 py-3 text-sm text-danger-600">
              {error}
            </div>
          )}

          <div className="mt-6 flex justify-end">
            <button
              type="button"
              onClick={handleAnalyze}
              disabled={loading || !password}
              className="btn-primary"
            >
              <ShieldCheck className="h-4 w-4" />

              {loading
                ? "Analyzing…"
                : "Analyze Password"}
            </button>
          </div>

          {/* Result */}
          {result && (
            <div className="mt-8 border-t border-ink-100 pt-6">

              <h2 className="text-lg font-semibold text-ink-900">
                Analysis Result
              </h2>

              {/* Score + Strength */}
              <div className="mt-4 grid gap-4 sm:grid-cols-2">

                <div className="rounded-xl border border-ink-100 bg-ink-50 p-5">
                  <p className="text-sm text-ink-500">
                    Security Score
                  </p>

                  <p className="mt-1 text-3xl font-semibold text-ink-950">
                    {result.score}
                    <span className="ml-1 text-sm font-normal text-ink-400">
                      / 5
                    </span>
                  </p>
                </div>

                <div className="rounded-xl border border-ink-100 bg-ink-50 p-5">
                  <p className="text-sm text-ink-500">
                    Strength
                  </p>

                  <span
                    className={`mt-2 inline-flex rounded-full px-3 py-1 text-sm font-medium ${getStrengthStyle(
                      result.strength
                    )}`}
                  >
                    {result.strength || "Unknown"}
                  </span>
                </div>

              </div>

              {/* Feedback */}
              {Array.isArray(result.feedback) &&
                result.feedback.length > 0 && (
                  <div className="mt-6">
                    <h3 className="text-sm font-semibold text-ink-900">
                      Feedback
                    </h3>

                    <ul className="mt-3 space-y-2">
                      {result.feedback.map(
                        (item, index) => (
                          <li
                            key={index}
                            className="rounded-lg border border-ink-100 bg-surface px-4 py-3 text-sm text-ink-600"
                          >
                            {item}
                          </li>
                        )
                      )}
                    </ul>
                  </div>
                )}

            </div>
          )}
        </div>
      </div>
    </AppLayout>
  );
}