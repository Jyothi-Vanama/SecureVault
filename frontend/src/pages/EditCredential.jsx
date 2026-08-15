import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ArrowLeft, KeyRound } from "lucide-react";
import AppLayout from "../components/AppLayout";
import CredentialForm from "../components/CredentialForm";
import {
  getCredentialById,
  updateCredential,
} from "../services/vaultService";

export default function EditCredential() {
  const navigate = useNavigate();
  const { id } = useParams();

  const [credential, setCredential] = useState(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    async function loadCredential() {
      try {
        setLoading(true);
        setError("");

        const data = await getCredentialById(id);

        setCredential({
          title: data.title || "",
          username: data.username || "",
          password: data.encryptedPassword || "",
          website: data.website || "",
          category: data.category || "PERSONAL",
          notes: data.notes || "",
        });
      } catch (err) {
        console.error("Fetch credential error:", err);

        setError(
          err.response?.data?.message ||
            "Failed to load credential. Please try again."
        );
      } finally {
        setLoading(false);
      }
    }

    loadCredential();
  }, [id]);

  async function handleSubmit(form) {
    try {
      setSubmitting(true);
      setError("");

      await updateCredential(id, form);

      navigate("/vault");
    } catch (err) {
      console.error("Update credential error:", err);

      setError(
        err.response?.data?.message ||
          err.message ||
          "Failed to update credential. Please try again."
      );
    } finally {
      setSubmitting(false);
    }
  }

  if (loading) {
    return (
      <AppLayout>
        <div className="flex min-h-[400px] items-center justify-center">
          <p className="text-sm text-ink-500">
            Loading credential…
          </p>
        </div>
      </AppLayout>
    );
  }

  if (!credential) {
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

          <div className="panel p-8 text-center">
            <p className="text-sm text-danger-600">
              {error || "Credential not found."}
            </p>
          </div>
        </div>
      </AppLayout>
    );
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
            Edit Credential
          </h1>

          <p className="mt-1 text-sm text-ink-500">
            Update the details of your saved credential.
          </p>
        </div>

        <div className="panel p-6 sm:p-8">
          {error && (
            <div className="mb-5 rounded-lg border border-danger-200 bg-danger-50 px-4 py-3 text-sm text-danger-600">
              {error}
            </div>
          )}

          <CredentialForm
            mode="edit"
            initialValues={credential}
            submitting={submitting}
            onSubmit={handleSubmit}
            onCancel={() => navigate("/vault")}
          />
        </div>
      </div>
    </AppLayout>
  );
}