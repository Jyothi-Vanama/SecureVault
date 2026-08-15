import React, { useEffect, useState } from "react";
import { KeyRound, RefreshCw, Users } from "lucide-react";

import AppLayout from "../components/AppLayout";
import LoadingSpinner from "../components/LoadingSpinner";
import EmptyState from "../components/EmptyState";

import { getReceivedShares } from "../services/sharedService";

function initialsOf(title) {
  const safeTitle = title || "Credential";

  return safeTitle
    .split(" ")
    .map((word) => word[0])
    .join("")
    .slice(0, 2)
    .toUpperCase();
}

export default function SharedCredentials() {
  const [credentials, setCredentials] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  async function loadSharedCredentials() {
    try {
      setLoading(true);
      setError("");

      const data = await getReceivedShares();

      setCredentials(Array.isArray(data) ? data : []);
    } catch (err) {
      console.error(
        "Failed to load shared credentials:",
        err
      );

      setCredentials([]);

      setError(
        err.response?.data?.message ||
          "Unable to load shared credentials."
      );
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadSharedCredentials();
  }, []);

  return (
    <AppLayout>
      <div className="mx-auto max-w-5xl">

        {/* Header */}
        <div className="mb-6 flex flex-col justify-between gap-4 sm:flex-row sm:items-center">
          <div>
            <div className="mb-3 flex h-11 w-11 items-center justify-center rounded-xl bg-brand-50 text-brand-600">
              <Users className="h-5 w-5" />
            </div>

            <h1 className="text-2xl font-semibold text-ink-950">
              Shared with Me
            </h1>

            <p className="mt-1 text-sm text-ink-500">
              Credentials that other SecureVault users have shared
              with you.
            </p>
          </div>

          <button
            type="button"
            onClick={loadSharedCredentials}
            disabled={loading}
            className="btn-secondary self-start sm:self-auto"
          >
            <RefreshCw
              className={`h-4 w-4 ${
                loading ? "animate-spin" : ""
              }`}
            />
            Refresh
          </button>
        </div>

        {/* Error */}
        {error && (
          <div className="mb-5 rounded-lg border border-danger-200 bg-danger-50 px-4 py-3 text-sm text-danger-600">
            {error}
          </div>
        )}

        {/* Loading */}
        {loading ? (
          <LoadingSpinner
            fullPage
            label="Loading shared credentials…"
          />
        ) : credentials.length === 0 ? (
          <EmptyState
            icon={Users}
            title="No shared credentials"
            description="Credentials shared with you will appear here."
          />
        ) : (
          <div className="space-y-4">

            {credentials.map((credential) => (
              <div
                key={credential.credentialId}
                className="panel p-5 transition-shadow hover:shadow-popover"
              >
                <div className="flex flex-col gap-5 sm:flex-row sm:items-center sm:justify-between">

                  {/* Credential information */}
                  <div className="flex min-w-0 items-center gap-4">

                    <div className="flex h-12 w-12 shrink-0 items-center justify-center rounded-xl bg-brand-50 text-sm font-semibold text-brand-600">
                      {initialsOf(credential.title)}
                    </div>

                    <div className="min-w-0">
                      <h2 className="truncate font-display text-sm font-semibold text-ink-900">
                        {credential.title || "Untitled Credential"}
                      </h2>

                      <p className="mt-1 truncate text-sm text-ink-500">
                        {credential.username || "No username"}
                      </p>

                      <div className="mt-2 flex flex-wrap items-center gap-2">

                        <span className="rounded-full bg-ink-50 px-2 py-0.5 text-[11px] font-medium capitalize text-ink-500">
                          {String(
                            credential.category ||
                              "Uncategorized"
                          ).toLowerCase()}
                        </span>

                        {credential.website && (
                          <span className="truncate text-xs text-ink-400">
                            {credential.website}
                          </span>
                        )}

                      </div>
                    </div>
                  </div>

                  {/* Shared indicator */}
                  <div className="flex shrink-0 items-center gap-2 rounded-lg bg-brand-50 px-3 py-2 text-xs font-medium text-brand-700">
                    <KeyRound className="h-3.5 w-3.5" />
                    Shared credential
                  </div>

                </div>
              </div>
            ))}

          </div>
        )}
      </div>
    </AppLayout>
  );
}