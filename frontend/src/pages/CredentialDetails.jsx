import React, { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import {
  ArrowLeft,
  Eye,
  EyeOff,
  Copy,
  Check,
  Pencil,
  Trash2,
  ExternalLink,
  KeyRound,
} from "lucide-react";

import AppLayout from "../components/AppLayout";
import ConfirmModal from "../components/ConfirmModal";

import {
  getCredentialById,
  deleteCredential,
} from "../services/vaultService";

const STRENGTH_STYLES = {
  strong: "bg-success-100 text-success-600",
  medium: "bg-warning-100 text-warning-600",
  weak: "bg-danger-100 text-danger-600",
};

function initialsOf(title) {
  const safeTitle = title || "Credential";

  return safeTitle
    .split(" ")
    .map((word) => word[0])
    .join("")
    .slice(0, 2)
    .toUpperCase();
}

export default function CredentialDetails() {
  const navigate = useNavigate();
  const { id } = useParams();

  const [credential, setCredential] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [revealed, setRevealed] = useState(false);
  const [copiedField, setCopiedField] = useState(null);

  const [pendingDelete, setPendingDelete] = useState(false);
  const [deleting, setDeleting] = useState(false);

  useEffect(() => {
    async function loadCredential() {
      try {
        setLoading(true);
        setError("");

        const data = await getCredentialById(id);

        setCredential(data);
      } catch (err) {
        console.error("Failed to load credential:", err);

        setError(
          err.response?.data?.message ||
            "Unable to load this credential."
        );
      } finally {
        setLoading(false);
      }
    }

    loadCredential();
  }, [id]);

  async function copyToClipboard(field, value) {
    if (!value) return;

    try {
      await navigator.clipboard.writeText(value);

      setCopiedField(field);

      setTimeout(() => {
        setCopiedField(null);
      }, 1400);
    } catch (err) {
      console.error("Copy failed:", err);
    }
  }

  async function handleDeleteConfirmed() {
    try {
      setDeleting(true);

      await deleteCredential(id);

      setPendingDelete(false);

      navigate("/vault");
    } catch (err) {
      console.error("Failed to delete credential:", err);

      setError(
        err.response?.data?.message ||
          "Unable to move credential to Trash."
      );

      setPendingDelete(false);
    } finally {
      setDeleting(false);
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
            <div className="mx-auto mb-4 flex h-12 w-12 items-center justify-center rounded-xl bg-danger-50 text-danger-600">
              <KeyRound className="h-6 w-6" />
            </div>

            <h1 className="text-lg font-semibold text-ink-900">
              Credential not found
            </h1>

            <p className="mt-2 text-sm text-danger-600">
              {error || "The requested credential could not be found."}
            </p>
          </div>
        </div>
      </AppLayout>
    );
  }

  const strength = String(
    credential.strength || ""
  ).toLowerCase();

  return (
    <AppLayout>
      <div className="mx-auto max-w-3xl">

        {/* Back button */}
        <button
          onClick={() => navigate("/vault")}
          className="mb-5 flex items-center gap-2 text-sm text-ink-500 hover:text-ink-900"
        >
          <ArrowLeft className="h-4 w-4" />
          Back to Vault
        </button>

        {/* Error */}
        {error && (
          <div className="mb-5 rounded-lg border border-danger-200 bg-danger-50 px-4 py-3 text-sm text-danger-600">
            {error}
          </div>
        )}

        {/* Header */}
        <div className="mb-6 flex flex-col justify-between gap-4 sm:flex-row sm:items-center">
          <div className="flex items-center gap-4">
            <div className="flex h-14 w-14 shrink-0 items-center justify-center rounded-xl bg-brand-50 text-lg font-semibold text-brand-600">
              {initialsOf(credential.title)}
            </div>

            <div>
              <h1 className="text-2xl font-semibold text-ink-950">
                {credential.title || "Untitled Credential"}
              </h1>

              <div className="mt-1 flex items-center gap-2">
                <span className="rounded-full bg-ink-50 px-2.5 py-1 text-xs font-medium capitalize text-ink-500">
                  {String(
                    credential.category || "Uncategorized"
                  ).toLowerCase()}
                </span>

                {credential.strength && (
                  <span
                    className={`badge ${
                      STRENGTH_STYLES[strength] || ""
                    }`}
                  >
                    {credential.strength}
                  </span>
                )}
              </div>
            </div>
          </div>

          {/* Actions */}
          <div className="flex items-center gap-2">
            <Link
              to={`/vault/${id}/edit`}
              className="btn-secondary"
            >
              <Pencil className="h-4 w-4" />
              Edit
            </Link>

            <button
              onClick={() => setPendingDelete(true)}
              className="btn-secondary text-danger-600 hover:bg-danger-50"
            >
              <Trash2 className="h-4 w-4" />
              Delete
            </button>
          </div>
        </div>

        {/* Credential details */}
        <div className="panel p-6 sm:p-8">

          {/* Username */}
          <div className="border-b border-ink-100 pb-5">
            <label className="field-label">
              Username
            </label>

            <div className="mt-1 flex items-center justify-between gap-3">
              <p className="break-all text-sm text-ink-900">
                {credential.username || "Not provided"}
              </p>

              {credential.username && (
                <button
                  onClick={() =>
                    copyToClipboard(
                      "username",
                      credential.username
                    )
                  }
                  className="shrink-0 rounded-md p-2 text-ink-400 hover:bg-ink-50 hover:text-ink-800"
                  aria-label="Copy username"
                >
                  {copiedField === "username" ? (
                    <Check className="h-4 w-4 text-success-500" />
                  ) : (
                    <Copy className="h-4 w-4" />
                  )}
                </button>
              )}
            </div>
          </div>

          {/* Password */}
          <div className="border-b border-ink-100 py-5">
            <label className="field-label">
              Password
            </label>

            <div className="mt-1 flex items-center justify-between gap-3">
              <p className="secret-text break-all text-sm text-ink-900">
                {revealed
                  ? credential.encryptedPassword
                  : "••••••••••••"}
              </p>

              <div className="flex shrink-0 items-center gap-1">
                <button
                  onClick={() =>
                    setRevealed((value) => !value)
                  }
                  className="rounded-md p-2 text-ink-400 hover:bg-ink-50 hover:text-ink-800"
                  aria-label={
                    revealed
                      ? "Hide password"
                      : "Reveal password"
                  }
                >
                  {revealed ? (
                    <EyeOff className="h-4 w-4" />
                  ) : (
                    <Eye className="h-4 w-4" />
                  )}
                </button>

                <button
                  onClick={() =>
                    copyToClipboard(
                      "password",
                      credential.encryptedPassword
                    )
                  }
                  className="rounded-md p-2 text-ink-400 hover:bg-ink-50 hover:text-ink-800"
                  aria-label="Copy password"
                >
                  {copiedField === "password" ? (
                    <Check className="h-4 w-4 text-success-500" />
                  ) : (
                    <Copy className="h-4 w-4" />
                  )}
                </button>
              </div>
            </div>
          </div>

          {/* Website */}
          <div className="border-b border-ink-100 py-5">
            <label className="field-label">
              Website
            </label>

            {credential.website ? (
              <a
                href={
                  credential.website.startsWith("http://") ||
                  credential.website.startsWith("https://")
                    ? credential.website
                    : `https://${credential.website}`
                }
                target="_blank"
                rel="noopener noreferrer"
                className="mt-1 flex items-center gap-2 break-all text-sm text-brand-600 hover:text-brand-700"
              >
                {credential.website}

                <ExternalLink className="h-3.5 w-3.5 shrink-0" />
              </a>
            ) : (
              <p className="mt-1 text-sm text-ink-400">
                Not provided
              </p>
            )}
          </div>

          {/* Category */}
          <div className="border-b border-ink-100 py-5">
            <label className="field-label">
              Category
            </label>

            <p className="mt-1 text-sm capitalize text-ink-900">
              {String(
                credential.category || "Uncategorized"
              ).toLowerCase()}
            </p>
          </div>

          {/* Notes */}
          <div className="py-5">
            <label className="field-label">
              Notes
            </label>

            <p className="mt-1 whitespace-pre-wrap text-sm text-ink-700">
              {credential.notes || "No notes added."}
            </p>
          </div>

          {/* Metadata */}
          {(credential.createdAt || credential.updatedAt) && (
            <div className="border-t border-ink-100 pt-5">
              <div className="grid gap-4 text-xs text-ink-400 sm:grid-cols-2">
                {credential.createdAt && (
                  <div>
                    <span className="font-medium text-ink-500">
                      Created
                    </span>
                    <p className="mt-1">
                      {new Date(
                        credential.createdAt
                      ).toLocaleString()}
                    </p>
                  </div>
                )}

                {credential.updatedAt && (
                  <div>
                    <span className="font-medium text-ink-500">
                      Last updated
                    </span>
                    <p className="mt-1">
                      {new Date(
                        credential.updatedAt
                      ).toLocaleString()}
                    </p>
                  </div>
                )}
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Delete confirmation */}
      <ConfirmModal
        open={pendingDelete}
        title={`Delete "${credential.title}"?`}
        description="This moves the credential to Trash, where you can restore it or delete it permanently."
        confirmLabel="Move to Trash"
        loading={deleting}
        onConfirm={handleDeleteConfirmed}
        onCancel={() => setPendingDelete(false)}
      />
    </AppLayout>
  );
}