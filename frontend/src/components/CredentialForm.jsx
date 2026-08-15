import React, { useState } from "react";
import { Wand2 } from "lucide-react";
import PasswordInput from "./PasswordInput";
const CATEGORIES = [
  "PERSONAL",
  "WORK",
  "DEVELOPMENT",
  "SOCIAL",
  "BANKING",
  "ENTERTAINMENT",
  "OTHER",
];

const EMPTY_FORM = {
  title: "",
  username: "",
  password: "",
  website: "",
  category: "PERSONAL",
  notes: "",
};

/**
 * Reusable credential form used by both the Add and Edit flows.
 * Pass `initialValues` for edit mode; omit it (or pass nothing) for add mode.
 */
export default function CredentialForm({
  initialValues,
  mode = "add", // "add" | "edit"
  submitting = false,
  onSubmit,
  onCancel,
  onGeneratePassword,
}) {
  const [form, setForm] = useState({ ...EMPTY_FORM, ...initialValues });
  const [errors, setErrors] = useState({});

  function update(field, value) {
    setForm((f) => ({ ...f, [field]: value }));
    if (errors[field]) setErrors((e) => ({ ...e, [field]: undefined }));
  }

  function validate() {
    const next = {};
    if (!form.title.trim()) next.title = "Title is required.";
    if (!form.username.trim()) next.username = "Username is required.";
    if (!form.password) next.password = "Password is required.";
    setErrors(next);
    return Object.keys(next).length === 0;
  }

  function handleSubmit(e) {
    e.preventDefault();
    if (!validate()) return;
    onSubmit?.(form);
  }

  function handleGenerate() {
    if (onGeneratePassword) {
      onGeneratePassword((generated) => update("password", generated));
    }
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-5">
      <div>
        <label className="field-label" htmlFor="cred-title">
          Title
        </label>
        <input
          id="cred-title"
          className={`field-input ${errors.title ? "border-danger-500" : ""}`}
          placeholder="e.g. Google Account"
          value={form.title}
          onChange={(e) => update("title", e.target.value)}
        />
        {errors.title && <p className="field-error">{errors.title}</p>}
      </div>

      <div className="grid gap-5 sm:grid-cols-2">
        <div>
          <label className="field-label" htmlFor="cred-username">
            Username
          </label>
          <input
            id="cred-username"
            className={`field-input ${errors.username ? "border-danger-500" : ""}`}
            placeholder="email or handle"
            value={form.username}
            onChange={(e) => update("username", e.target.value)}
          />
          {errors.username && <p className="field-error">{errors.username}</p>}
        </div>

        <div>
          <label className="field-label" htmlFor="cred-category">
            Category
          </label>
          <select
  id="cred-category"
  className="field-input"
  value={form.category}
  onChange={(e) => update("category", e.target.value)}
>
  {CATEGORIES.map((cat) => (
    <option key={cat} value={cat}>
      {cat.charAt(0) + cat.slice(1).toLowerCase()}
    </option>
  ))}
</select>
        </div>
      </div>

      <div>
        <div className="mb-1.5 flex items-center justify-between">
          <label className="field-label mb-0" htmlFor="cred-password">
            Password
          </label>
          <button
            type="button"
            onClick={handleGenerate}
            className="flex items-center gap-1 text-xs font-semibold text-brand-600 hover:text-brand-700"
          >
            <Wand2 className="h-3.5 w-3.5" />
            Generate
          </button>
        </div>
        <PasswordInput
          id="cred-password"
          value={form.password}
          onChange={(e) => update("password", e.target.value)}
          error={errors.password}
          showStrengthMeter
          showCopyButton
          autoComplete="new-password"
        />
      </div>

      <div>
  <label className="field-label" htmlFor="cred-website">
    Website URL
  </label>

  <input
    id="cred-website"
    className="field-input"
    placeholder="https://example.com"
    value={form.website}
    onChange={(e) => update("website", e.target.value)}
  />
</div>

      <div>
        <label className="field-label" htmlFor="cred-notes">
          Notes
        </label>
        <textarea
          id="cred-notes"
          rows={3}
          className="field-input resize-none"
          placeholder="Optional notes…"
          value={form.notes}
          onChange={(e) => update("notes", e.target.value)}
        />
      </div>

      <div className="flex justify-end gap-3 pt-2">
        <button type="button" className="btn-secondary" onClick={onCancel} disabled={submitting}>
          Cancel
        </button>
        <button type="submit" className="btn-primary" disabled={submitting}>
          {submitting ? "Saving…" : mode === "edit" ? "Save changes" : "Add credential"}
        </button>
      </div>
    </form>
  );
}
