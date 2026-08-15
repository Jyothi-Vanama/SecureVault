import React from "react";
import { ChevronDown } from "lucide-react";
import { mockCategories } from "../services/mockData";

export default function CategoryFilter({ value, onChange }) {
  return (
    <div className="relative">
      <select
        value={value}
        onChange={(e) => onChange(e.target.value)}
        className="field-input appearance-none pr-9 capitalize"
      >
        <option value="ALL">All categories</option>
        {mockCategories.map((cat) => (
          <option key={cat} value={cat} className="capitalize">
            {cat.charAt(0) + cat.slice(1).toLowerCase()}
          </option>
        ))}
      </select>
      <ChevronDown className="pointer-events-none absolute right-3 top-1/2 h-4 w-4 -translate-y-1/2 text-ink-300" />
    </div>
  );
}
