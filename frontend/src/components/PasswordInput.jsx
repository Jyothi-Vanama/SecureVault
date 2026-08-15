import React, { useState } from "react";
import { Eye, EyeOff, Copy, Check } from "lucide-react";
import { estimateStrength } from "../utils/passwordStrength";

export default function PasswordInput({
  label,
  value,
  onChange,
  placeholder = "Enter password",
  error,
  showStrengthMeter = false,
  showCopyButton = false,
  autoComplete = "current-password",
  id = "password",
}) {
  const [visible, setVisible] = useState(false);
  const [copied, setCopied] = useState(false);

  const strength = showStrengthMeter
    ? estimateStrength(value)
    : null;

  async function handleCopy() {
    if (!value) return;

    await navigator.clipboard.writeText(value);

    setCopied(true);

    setTimeout(() => {
      setCopied(false);
    }, 1500);
  }

  return (
    <div>
      {/* Hide browser's built-in password reveal icon */}
      <style>
        {`
          input[type="password"]::-ms-reveal,
          input[type="password"]::-ms-clear {
            display: none;
          }
        `}
      </style>

      {label && (
        <label
          htmlFor={id}
          className="field-label"
        >
          {label}
        </label>
      )}

      <div className="relative">
        <input
          id={id}
          type={visible ? "text" : "password"}
          value={value}
          onChange={onChange}
          placeholder={placeholder}
          autoComplete={autoComplete}
          className={`field-input secret-text pr-20 ${
            error
              ? "border-danger-500 focus:border-danger-500"
              : ""
          }`}
        />

        <div className="absolute inset-y-0 right-2 flex items-center gap-1">
          {showCopyButton && (
            <button
              type="button"
              onClick={handleCopy}
              className="rounded-md p-1.5 text-ink-500 hover:bg-ink-50 hover:text-ink-900"
              aria-label="Copy password"
            >
              {copied ? (
                <Check className="h-4 w-4 text-success-500" />
              ) : (
                <Copy className="h-4 w-4" />
              )}
            </button>
          )}

          <button
            type="button"
            onClick={() => setVisible((v) => !v)}
            className="rounded-md p-1.5 text-ink-500 hover:bg-ink-50 hover:text-ink-900"
            aria-label={
              visible
                ? "Hide password"
                : "Show password"
            }
          >
            {visible ? (
              <EyeOff className="h-4 w-4" />
            ) : (
              <Eye className="h-4 w-4" />
            )}
          </button>
        </div>
      </div>

      {showStrengthMeter && value && (
        <div className="mt-2">
          <div className="flex gap-1">
            {[0, 1, 2, 3, 4].map((i) => (
              <span
                key={i}
                className={`h-1.5 flex-1 rounded-full ${
                  i < strength.score
                    ? strength.color
                    : "bg-ink-100"
                }`}
              />
            ))}
          </div>

          <p className="mt-1 text-xs capitalize text-ink-500">
            {strength.label}
          </p>
        </div>
      )}

      {error && (
        <p className="field-error">
          {error}
        </p>
      )}
    </div>
  );
}