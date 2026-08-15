import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import {
  LayoutGrid,
  List,
  ChevronDown,
  Plus,
  KeyRound,
} from "lucide-react";

import AppLayout from "../components/AppLayout";
import SearchBar from "../components/SearchBar";
import CategoryFilter from "../components/CategoryFilter";
import CredentialCard from "../components/CredentialCard";
import LoadingSpinner from "../components/LoadingSpinner";
import EmptyState from "../components/EmptyState";
import ConfirmModal from "../components/ConfirmModal";

import {
  getCredentials,
  deleteCredential,
} from "../services/vaultService";

const PAGE_SIZE = 6;

const SORT_OPTIONS = [
  { value: "recent", label: "Recently added" },
  { value: "title-asc", label: "Title (A–Z)" },
  { value: "title-desc", label: "Title (Z–A)" },
];

export default function Vault() {
  const [loading, setLoading] = useState(true);
  const [credentials, setCredentials] = useState([]);

  const [search, setSearch] = useState("");
  const [category, setCategory] = useState("ALL");
  const [sort, setSort] = useState("recent");
  const [view, setView] = useState("grid");
  const [page, setPage] = useState(1);

  const [totalPages, setTotalPages] = useState(1);
  const [totalElements, setTotalElements] = useState(0);

  const [pendingDelete, setPendingDelete] = useState(null);
  const [deleting, setDeleting] = useState(false);
  const [error, setError] = useState("");

  async function loadCredentials() {
    try {
      setLoading(true);
      setError("");

      const data = await getCredentials({
        search,
        category,
        sort,
        page,
        size: PAGE_SIZE,
      });

      setCredentials(data?.content ?? []);
      setTotalPages(data?.totalPages ?? 1);
      setTotalElements(data?.totalElements ?? 0);
    } catch (err) {
      console.error("Failed to load credentials:", err);

      setCredentials([]);

      setError(
        err.response?.data?.message ||
          "Unable to load your vault."
      );
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadCredentials();
  }, [search, category, sort, page]);

  useEffect(() => {
    setPage(1);
  }, [search, category, sort]);

  async function handleDeleteConfirmed() {
    if (!pendingDelete) return;

    try {
      setDeleting(true);

      await deleteCredential(pendingDelete.id);

      setPendingDelete(null);

      await loadCredentials();
    } catch (err) {
      console.error("Failed to delete credential:", err);

      setError(
        err.response?.data?.message ||
          "Unable to move credential to Trash."
      );
    } finally {
      setDeleting(false);
    }
  }

  return (
    <AppLayout>
      <div className="mb-6 flex flex-col justify-between gap-4 sm:flex-row sm:items-center">
        <div>
          <h1 className="text-2xl font-semibold text-ink-950">
            Vault
          </h1>

          <p className="mt-1 text-sm text-ink-500">
            {totalElements} credential
            {totalElements === 1 ? "" : "s"}
          </p>
        </div>

        <Link
          to="/vault/add"
          className="btn-primary self-start sm:self-auto"
        >
          <Plus className="h-4 w-4" />
          Add Credential
        </Link>
      </div>

      <div className="mb-5 flex flex-col gap-3 sm:flex-row sm:items-center">
        <SearchBar
          value={search}
          onChange={setSearch}
        />

        <div className="flex gap-3">
          <CategoryFilter
            value={category}
            onChange={setCategory}
          />

          <div className="relative">
            <select
              value={sort}
              onChange={(e) => setSort(e.target.value)}
              className="field-input appearance-none pr-9"
            >
              {SORT_OPTIONS.map((option) => (
                <option
                  key={option.value}
                  value={option.value}
                >
                  {option.label}
                </option>
              ))}
            </select>

            <ChevronDown className="pointer-events-none absolute right-3 top-1/2 h-4 w-4 -translate-y-1/2 text-ink-300" />
          </div>

          <div className="flex overflow-hidden rounded-lg border border-ink-100">
            <button
              onClick={() => setView("grid")}
              className={`p-2.5 ${
                view === "grid"
                  ? "bg-brand-50 text-brand-600"
                  : "text-ink-400 hover:bg-ink-50"
              }`}
              aria-label="Grid view"
            >
              <LayoutGrid className="h-4 w-4" />
            </button>

            <button
              onClick={() => setView("list")}
              className={`p-2.5 ${
                view === "list"
                  ? "bg-brand-50 text-brand-600"
                  : "text-ink-400 hover:bg-ink-50"
              }`}
              aria-label="List view"
            >
              <List className="h-4 w-4" />
            </button>
          </div>
        </div>
      </div>

      {error && (
        <div className="mb-5 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-600">
          {error}
        </div>
      )}

      {loading ? (
        <LoadingSpinner
          fullPage
          label="Loading your vault…"
        />
      ) : credentials.length === 0 ? (
        <EmptyState
          icon={KeyRound}
          title={
            search || category !== "ALL"
              ? "No matching credentials"
              : "Your vault is empty"
          }
          description={
            search || category !== "ALL"
              ? "Try a different search term or clear your filters."
              : "Add your first credential to start building your vault."
          }
          action={
            !search && category === "ALL" ? (
              <Link
                to="/vault/add"
                className="btn-primary"
              >
                <Plus className="h-4 w-4" />
                Add Credential
              </Link>
            ) : undefined
          }
        />
      ) : (
        <>
          <div
            className={
              view === "grid"
                ? "grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-3"
                : "flex flex-col gap-3"
            }
          >
            {credentials.map((credential) => (
  <CredentialCard
    key={credential.credentialId}
    credential={{
  ...credential,
  id: credential.credentialId,
  password: credential.encryptedPassword,
}}
    onDelete={setPendingDelete}
  />
))}
          </div>

          {totalPages > 1 && (
            <div className="mt-6 flex items-center justify-center gap-1.5">
              {Array.from(
                { length: totalPages },
                (_, index) => index + 1
              ).map((pageNumber) => (
                <button
                  key={pageNumber}
                  onClick={() => setPage(pageNumber)}
                  className={`h-9 w-9 rounded-lg text-sm font-medium ${
                    pageNumber === page
                      ? "bg-brand-500 text-white"
                      : "text-ink-500 hover:bg-ink-50"
                  }`}
                >
                  {pageNumber}
                </button>
              ))}
            </div>
          )}
        </>
      )}

      <ConfirmModal
        open={Boolean(pendingDelete)}
        title={`Delete "${pendingDelete?.title}"?`}
        description="This moves the credential to Trash, where you can restore it or delete it permanently."
        confirmLabel="Move to Trash"
        loading={deleting}
        onConfirm={handleDeleteConfirmed}
        onCancel={() => setPendingDelete(null)}
      />
    </AppLayout>
  );
}