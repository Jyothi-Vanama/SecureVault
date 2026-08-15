import React, { useState } from "react";
import { Link } from "react-router-dom";
import { Eye, EyeOff, Copy, Check, Star, Pencil, Trash2 } from "lucide-react";

const STRENGTH_STYLES = {
  strong: "bg-success-100 text-success-600",
  medium: "bg-warning-100 text-warning-600",
  weak: "bg-danger-100 text-danger-600",
};

function initialsOf(title) {
  const safeTitle = title || "Credential";

  return safeTitle
    .split(" ")
    .map((w) => w[0])
    .join("")
    .slice(0, 2)
    .toUpperCase();
}

export default function CredentialCard({ credential, onToggleFavorite, onDelete }) {
  const [revealed, setRevealed] = useState(false);
  const [copiedField, setCopiedField] = useState(null);

  async function copy(field, text) {
    await navigator.clipboard.writeText(text);
    setCopiedField(field);
    setTimeout(() => setCopiedField(null), 1400);
  }

  return (
    <div className="panel group flex flex-col p-5 transition-shadow hover:shadow-popover">
      <div className="flex items-start justify-between">
        <div className="flex items-center gap-3">
          <div className="flex h-11 w-11 shrink-0 items-center justify-center rounded-xl bg-brand-50 text-sm font-semibold text-brand-600">
            {initialsOf(credential.title)}
          </div>
          <div className="min-w-0">
            <Link
              to={`/vault/${credential.id}`}
              className="block truncate font-display text-sm font-semibold text-ink-900 hover:text-brand-600"
            >
              {credential.title || "Untitled Credential"}
            </Link>
            <span className="mt-0.5 inline-block rounded-full bg-ink-50 px-2 py-0.5 text-[11px] font-medium capitalize text-ink-500">
              {String(credential.category || "Uncategorized").toLowerCase()}
            </span>
          </div>
        </div>
        <button
          onClick={() => onToggleFavorite?.(credential.id)}
          className="rounded-md p-1 text-ink-300 hover:text-warning-500"
          aria-label={credential.favorite ? "Remove from favorites" : "Add to favorites"}
        >
          <Star
            className="h-4 w-4"
            fill={credential.favorite ? "currentColor" : "none"}
            color={credential.favorite ? "#B5740B" : undefined}
          />
        </button>
      </div>

      <div className="mt-4 space-y-2.5 text-sm">
        <div className="flex items-center justify-between gap-2">
          <span className="truncate text-ink-500">{credential.username}</span>
          <button
            onClick={() => copy("username", credential.username)}
            className="shrink-0 rounded-md p-1 text-ink-300 hover:bg-ink-50 hover:text-ink-700"
            aria-label="Copy username"
          >
            {copiedField === "username" ? (
              <Check className="h-3.5 w-3.5 text-success-500" />
            ) : (
              <Copy className="h-3.5 w-3.5" />
            )}
          </button>
        </div>

        <div className="flex items-center justify-between gap-2">
          <span className="secret-text truncate text-sm">
            {revealed ? credential.password : "••••••••••••"}
          </span>
          <div className="flex shrink-0 items-center gap-0.5">
            <button
              onClick={() => setRevealed((v) => !v)}
              className="rounded-md p-1 text-ink-300 hover:bg-ink-50 hover:text-ink-700"
              aria-label={revealed ? "Hide password" : "Reveal password"}
            >
              {revealed ? <EyeOff className="h-3.5 w-3.5" /> : <Eye className="h-3.5 w-3.5" />}
            </button>
            <button
              onClick={() => copy("password", credential.password)}
              className="rounded-md p-1 text-ink-300 hover:bg-ink-50 hover:text-ink-700"
              aria-label="Copy password"
            >
              {copiedField === "password" ? (
                <Check className="h-3.5 w-3.5 text-success-500" />
              ) : (
                <Copy className="h-3.5 w-3.5" />
              )}
            </button>
          </div>
        </div>
      </div>

      <div className="mt-4 flex items-center justify-between border-t border-ink-100 pt-3.5">
        <span
  className={`badge ${
    STRENGTH_STYLES[String(credential.strength || "").toLowerCase()]
  }`}
>
  {credential.strength || "Unknown"}
</span>
        <div className="flex items-center gap-1">
          <Link
            to={`/vault/${credential.id}/edit`}
            className="rounded-md p-1.5 text-ink-500 hover:bg-ink-50 hover:text-ink-900"
            aria-label="Edit credential"
          >
            <Pencil className="h-4 w-4" />
          </Link>
          <button
            onClick={() => onDelete?.(credential)}
            className="rounded-md p-1.5 text-ink-500 hover:bg-danger-100 hover:text-danger-600"
            aria-label="Delete credential"
          >
            <Trash2 className="h-4 w-4" />
          </button>
        </div>
      </div>
    </div>
  );
}
