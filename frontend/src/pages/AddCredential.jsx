import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { ArrowLeft, KeyRound } from "lucide-react";

import AppLayout from "../components/AppLayout";
import CredentialForm from "../components/CredentialForm";

import { createCredential } from "../services/vaultService";
import { generatePassword } from "../services/passwordService";

export default function AddCredential() {
  const navigate = useNavigate();

  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  async function handleSubmit(form) {
    try {
      setSubmitting(true);
      setError("");

      const response = await createCredential(form);

      if (response?.success === false) {
        throw new Error(
          response.message || "Failed to create credential"
        );
      }

      navigate("/vault");
    } catch (err) {
      console.error("Create credential error:", err);

      setError(
        err.response?.data?.message ||
          err.message ||
          "Failed to create credential. Please try again."
      );
    } finally {
      setSubmitting(false);
    }
  }

  async function handleGeneratePassword(setGeneratedPassword) {
    try {
      setError("");

      const result = await generatePassword({
        length: 16,
        uppercase: true,
        lowercase: true,
        numbers: true,
        symbols: true,
      });

      if (!result?.password) {
        throw new Error(
          "Password generator did not return a password."
        );
      }

      setGeneratedPassword(result.password);
    } catch (err) {
      console.error("Generate password error:", err);

      setError(
        err.response?.data?.message ||
          err.message ||
          "Failed to generate password. Please try again."
      );
    }
  }

  return (
    <AppLayout>
      <div className="mx-auto max-w-3xl">

        <button
          onClick={() => navigate("/vault")}
          className="mb-5 flex items-center gap-2 text-sm text-ink-500 hover:text-ink-900"
        >
          <ArrowLeft className="h-4 w-4" />
          Back to Vault
        </button>

        <div className="mb-6">
          <div className="mb-3 flex h-11 w-11 items-center justify-center rounded-xl bg-brand-50 text-brand-600">
            <KeyRound className="h-5 w-5" />
          </div>

          <h1 className="text-2xl font-semibold text-ink-950">
            Add Credential
          </h1>

          <p className="mt-1 text-sm text-ink-500">
            Securely store a new credential in your vault.
          </p>
        </div>

        <div className="panel p-6 sm:p-8">

          {error && (
            <div className="mb-5 rounded-lg border border-danger-200 bg-danger-50 px-4 py-3 text-sm text-danger-600">
              {error}
            </div>
          )}

          <CredentialForm
            mode="add"
            submitting={submitting}
            onSubmit={handleSubmit}
            onCancel={() => navigate("/vault")}
            onGeneratePassword={handleGeneratePassword}
          />

        </div>
      </div>
    </AppLayout>
  );
}