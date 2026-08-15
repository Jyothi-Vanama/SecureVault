import React from "react";
import { AlertTriangle, X } from "lucide-react";

export default function ConfirmModal({
  open,
  title = "Are you sure?",
  description,
  confirmLabel = "Confirm",
  cancelLabel = "Cancel",
  tone = "danger", // "danger" | "brand"
  loading = false,
  onConfirm,
  onCancel,
}) {
  if (!open) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div
        className="absolute inset-0 bg-ink-950/50 backdrop-blur-[2px]"
        onClick={onCancel}
      />
      <div className="relative w-full max-w-sm panel p-6">
        <button
          onClick={onCancel}
          className="absolute right-4 top-4 rounded-md p-1 text-ink-300 hover:bg-ink-50 hover:text-ink-700"
          aria-label="Close"
        >
          <X className="h-4 w-4" />
        </button>

        <div
          className={`mb-4 flex h-11 w-11 items-center justify-center rounded-full ${
            tone === "danger" ? "bg-danger-100" : "bg-brand-50"
          }`}
        >
          <AlertTriangle
            className={`h-5 w-5 ${tone === "danger" ? "text-danger-500" : "text-brand-500"}`}
          />
        </div>

        <h3 className="text-base font-semibold text-ink-900">{title}</h3>
        {description && (
          <p className="mt-1.5 text-sm text-ink-500">{description}</p>
        )}

        <div className="mt-6 flex justify-end gap-2">
          <button className="btn-secondary" onClick={onCancel} disabled={loading}>
            {cancelLabel}
          </button>
          <button
            className={tone === "danger" ? "btn-danger" : "btn-primary"}
            onClick={onConfirm}
            disabled={loading}
          >
            {loading ? "Please wait…" : confirmLabel}
          </button>
        </div>
      </div>
    </div>
  );
}
