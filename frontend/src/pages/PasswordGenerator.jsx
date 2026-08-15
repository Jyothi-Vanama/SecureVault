import React, { useState } from "react";
import {
  Copy,
  Check,
  RefreshCw,
  KeyRound,
} from "lucide-react";

import AppLayout from "../components/AppLayout";
import { generatePassword } from "../services/passwordService";

const DEFAULT_OPTIONS = {
  length: 16,
  uppercase: true,
  lowercase: true,
  numbers: true,
  symbols: true,
};

export default function PasswordGenerator() {
  const [options, setOptions] = useState(DEFAULT_OPTIONS);
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [copied, setCopied] = useState(false);
  const [error, setError] = useState("");

  function updateOption(field, value) {
    setOptions((current) => ({
      ...current,
      [field]: value,
    }));
  }

  async function handleGenerate() {
    try {
      setLoading(true);
      setError("");
      setCopied(false);

      const response = await generatePassword(options);

      setPassword(response?.password || "");
    } catch (err) {
      console.error("Password generation error:", err);

      setError(
        err.response?.data?.message ||
          "Unable to generate password. Please try again."
      );
    } finally {
      setLoading(false);
    }
  }

  async function handleCopy() {
    if (!password) return;

    try {
      await navigator.clipboard.writeText(password);

      setCopied(true);

      setTimeout(() => {
        setCopied(false);
      }, 1400);
    } catch (err) {
      console.error("Copy password error:", err);
    }
  }

  return (
    <AppLayout>
      <div className="mx-auto max-w-3xl">

        {/* Header */}
        <div className="mb-6">
          <div className="mb-3 flex h-11 w-11 items-center justify-center rounded-xl bg-brand-50 text-brand-600">
            <KeyRound className="h-5 w-5" />
          </div>

          <h1 className="text-2xl font-semibold text-ink-950">
            Password Generator
          </h1>

          <p className="mt-1 text-sm text-ink-500">
            Generate a strong and secure password using your preferred
            settings.
          </p>
        </div>

        {/* Main panel */}
        <div className="panel p-6 sm:p-8">

          {/* Generated password */}
          <div>
            <label className="field-label">
              Generated Password
            </label>

            <div className="mt-1 flex items-center gap-2">
              <div className="flex min-h-[46px] flex-1 items-center rounded-lg border border-ink-100 bg-ink-50 px-4">
                <span className="break-all font-mono text-sm text-ink-900">
                  {password || "Click Generate Password"}
                </span>
              </div>

              <button
                type="button"
                onClick={handleCopy}
                disabled={!password}
                className="rounded-lg border border-ink-100 p-3 text-ink-500 hover:bg-ink-50 hover:text-ink-900 disabled:cursor-not-allowed disabled:opacity-40"
                aria-label="Copy generated password"
              >
                {copied ? (
                  <Check className="h-4 w-4 text-success-500" />
                ) : (
                  <Copy className="h-4 w-4" />
                )}
              </button>
            </div>
          </div>

          {/* Error */}
          {error && (
            <div className="mt-5 rounded-lg border border-danger-200 bg-danger-50 px-4 py-3 text-sm text-danger-600">
              {error}
            </div>
          )}

          {/* Length */}
          <div className="mt-6">
            <div className="flex items-center justify-between">
              <label
                htmlFor="password-length"
                className="field-label mb-0"
              >
                Password Length
              </label>

              <span className="text-sm font-semibold text-brand-600">
                {options.length}
              </span>
            </div>

            <input
              id="password-length"
              type="range"
              min="8"
              max="64"
              value={options.length}
              onChange={(e) =>
                updateOption(
                  "length",
                  Number(e.target.value)
                )
              }
              className="mt-4 w-full"
            />

            <div className="mt-1 flex justify-between text-xs text-ink-400">
              <span>8</span>
              <span>64</span>
            </div>
          </div>

          {/* Character options */}
          <div className="mt-6">
            <p className="field-label">
              Character Types
            </p>

            <div className="mt-3 grid gap-3 sm:grid-cols-2">

              <label className="flex cursor-pointer items-center gap-3 rounded-lg border border-ink-100 p-4 hover:bg-ink-50">
                <input
                  type="checkbox"
                  checked={options.uppercase}
                  onChange={(e) =>
                    updateOption(
                      "uppercase",
                      e.target.checked
                    )
                  }
                  className="h-4 w-4"
                />

                <div>
                  <p className="text-sm font-medium text-ink-900">
                    Uppercase
                  </p>
                  <p className="text-xs text-ink-400">
                    A–Z
                  </p>
                </div>
              </label>

              <label className="flex cursor-pointer items-center gap-3 rounded-lg border border-ink-100 p-4 hover:bg-ink-50">
                <input
                  type="checkbox"
                  checked={options.lowercase}
                  onChange={(e) =>
                    updateOption(
                      "lowercase",
                      e.target.checked
                    )
                  }
                  className="h-4 w-4"
                />

                <div>
                  <p className="text-sm font-medium text-ink-900">
                    Lowercase
                  </p>
                  <p className="text-xs text-ink-400">
                    a–z
                  </p>
                </div>
              </label>

              <label className="flex cursor-pointer items-center gap-3 rounded-lg border border-ink-100 p-4 hover:bg-ink-50">
                <input
                  type="checkbox"
                  checked={options.numbers}
                  onChange={(e) =>
                    updateOption(
                      "numbers",
                      e.target.checked
                    )
                  }
                  className="h-4 w-4"
                />

                <div>
                  <p className="text-sm font-medium text-ink-900">
                    Numbers
                  </p>
                  <p className="text-xs text-ink-400">
                    0–9
                  </p>
                </div>
              </label>

              <label className="flex cursor-pointer items-center gap-3 rounded-lg border border-ink-100 p-4 hover:bg-ink-50">
                <input
                  type="checkbox"
                  checked={options.symbols}
                  onChange={(e) =>
                    updateOption(
                      "symbols",
                      e.target.checked
                    )
                  }
                  className="h-4 w-4"
                />

                <div>
                  <p className="text-sm font-medium text-ink-900">
                    Symbols
                  </p>
                  <p className="text-xs text-ink-400">
                    ! @ # $ %
                  </p>
                </div>
              </label>

            </div>
          </div>

          {/* Generate button */}
          <div className="mt-7 flex justify-end">
            <button
              type="button"
              onClick={handleGenerate}
              disabled={loading}
              className="btn-primary"
            >
              <RefreshCw
                className={`h-4 w-4 ${
                  loading ? "animate-spin" : ""
                }`}
              />

              {loading
                ? "Generating…"
                : "Generate Password"}
            </button>
          </div>
        </div>
      </div>
    </AppLayout>
  );
}