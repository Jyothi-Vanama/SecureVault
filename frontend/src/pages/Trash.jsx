import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  ArrowLeft,
  RotateCcw,
  Trash2,
  KeyRound,
} from "lucide-react";

import AppLayout from "../components/AppLayout";
import ConfirmModal from "../components/ConfirmModal";

import {
  getTrash,
  restoreCredential,
  permanentlyDeleteCredential,
} from "../services/vaultService";

export default function Trash() {
  const navigate = useNavigate();

  const [credentials, setCredentials] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [actionLoading, setActionLoading] = useState(false);

  const [pendingPermanentDelete, setPendingPermanentDelete] =
    useState(null);

  async function loadTrash() {
    try {
      setLoading(true);
      setError("");

      const data = await getTrash();

      setCredentials(data ?? []);
    } catch (err) {
      console.error("Failed to load trash:", err);

      setError(
        err.response?.data?.message ||
          "Unable to load your Trash."
      );
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadTrash();
  }, []);

  async function handleRestore(id) {
    try {
      setActionLoading(true);
      setError("");

      await restoreCredential(id);

      await loadTrash();
    } catch (err) {
      console.error("Failed to restore credential:", err);

      setError(
        err.response?.data?.message ||
          "Unable to restore credential."
      );
    } finally {
      setActionLoading(false);
    }
  }

  async function handlePermanentDelete() {
    if (!pendingPermanentDelete) return;

    try {
      setActionLoading(true);
      setError("");

      await permanentlyDeleteCredential(
        pendingPermanentDelete.credentialId
      );

      setPendingPermanentDelete(null);

      await loadTrash();
    } catch (err) {
      console.error(
        "Failed to permanently delete credential:",
        err
      );

      setError(
        err.response?.data?.message ||
          "Unable to permanently delete credential."
      );
    } finally {
      setActionLoading(false);
    }
  }

  return (
    <AppLayout>
      <div className="mx-auto max-w-5xl">

        {/* Header */}
        <div className="mb-6 flex flex-col justify-between gap-4 sm:flex-row sm:items-center">
          <div>
            <button
              onClick={() => navigate("/vault")}
              className="mb-4 flex items-center gap-2 text-sm text-ink-500 hover:text-ink-900"
            >
              <ArrowLeft className="h-4 w-4" />
              Back to Vault
            </button>

            <div className="flex items-center gap-3">
              <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-danger-50 text-danger-600">
                <Trash2 className="h-5 w-5" />
              </div>

              <div>
                <h1 className="text-2xl font-semibold text-ink-950">
                  Trash
                </h1>

                <p className="mt-1 text-sm text-ink-500">
                  {credentials.length} deleted credential
                  {credentials.length === 1 ? "" : "s"}
                </p>
              </div>
            </div>
          </div>
        </div>

        {/* Error */}
        {error && (
          <div className="mb-5 rounded-lg border border-danger-200 bg-danger-50 px-4 py-3 text-sm text-danger-600">
            {error}
          </div>
        )}

        {/* Loading */}
        {loading ? (
          <div className="flex min-h-[300px] items-center justify-center">
            <p className="text-sm text-ink-500">
              Loading Trash…
            </p>
          </div>
        ) : credentials.length === 0 ? (
          /* Empty state */
          <div className="panel p-10 text-center">
            <div className="mx-auto mb-4 flex h-14 w-14 items-center justify-center rounded-xl bg-ink-50 text-ink-400">
              <KeyRound className="h-6 w-6" />
            </div>

            <h2 className="text-lg font-semibold text-ink-900">
              Trash is empty
            </h2>

            <p className="mx-auto mt-2 max-w-md text-sm text-ink-500">
              Credentials that you delete will appear here.
              You can restore them or permanently delete them.
            </p>

            <button
              onClick={() => navigate("/vault")}
              className="btn-primary mt-5"
            >
              Back to Vault
            </button>
          </div>
        ) : (
          /* Deleted credentials */
          <div className="space-y-3">
            {credentials.map((credential) => (
              <div
                key={credential.credentialId}
                className="panel flex flex-col gap-4 p-5 sm:flex-row sm:items-center sm:justify-between"
              >
                {/* Credential information */}
                <div className="flex min-w-0 items-center gap-3">
                  <div className="flex h-11 w-11 shrink-0 items-center justify-center rounded-xl bg-ink-50 text-sm font-semibold text-ink-500">
                    {(credential.title || "Credential")
                      .slice(0, 2)
                      .toUpperCase()}
                  </div>

                  <div className="min-w-0">
                    <h2 className="truncate text-sm font-semibold text-ink-900">
                      {credential.title || "Untitled Credential"}
                    </h2>

                    <p className="mt-1 truncate text-sm text-ink-500">
                      {credential.username || "No username"}
                    </p>

                    <div className="mt-1 flex flex-wrap items-center gap-2">
                      <span className="rounded-full bg-ink-50 px-2 py-0.5 text-[11px] font-medium capitalize text-ink-500">
                        {String(
                          credential.category || "Uncategorized"
                        ).toLowerCase()}
                      </span>

                      {credential.deletedAt && (
                        <span className="text-[11px] text-ink-400">
                          Deleted{" "}
                          {new Date(
                            credential.deletedAt
                          ).toLocaleString()}
                        </span>
                      )}
                    </div>
                  </div>
                </div>

                {/* Actions */}
                <div className="flex shrink-0 items-center gap-2">
                  <button
                    onClick={() =>
                      handleRestore(
                        credential.credentialId
                      )
                    }
                    disabled={actionLoading}
                    className="btn-secondary"
                  >
                    <RotateCcw className="h-4 w-4" />
                    Restore
                  </button>

                  <button
                    onClick={() =>
                      setPendingPermanentDelete(credential)
                    }
                    disabled={actionLoading}
                    className="btn-secondary text-danger-600 hover:bg-danger-50"
                  >
                    <Trash2 className="h-4 w-4" />
                    Delete permanently
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Permanent delete confirmation */}
      <ConfirmModal
        open={Boolean(pendingPermanentDelete)}
        title={`Delete "${pendingPermanentDelete?.title}" permanently?`}
        description="This action cannot be undone. The credential will be permanently removed from SecureVault."
        confirmLabel="Delete permanently"
        loading={actionLoading}
        onConfirm={handlePermanentDelete}
        onCancel={() => setPendingPermanentDelete(null)}
      />
    </AppLayout>
  );
}