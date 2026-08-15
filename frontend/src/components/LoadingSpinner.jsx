import React from "react";
import { ShieldEllipsis } from "lucide-react";

const SIZES = {
  sm: "h-4 w-4",
  md: "h-6 w-6",
  lg: "h-10 w-10",
};

export default function LoadingSpinner({ size = "md", label, fullPage = false }) {
  const spinner = (
    <div className="flex flex-col items-center justify-center gap-3">
      <ShieldEllipsis className={`${SIZES[size]} animate-spin text-brand-500`} strokeWidth={1.75} />
      {label && <p className="text-sm text-ink-500">{label}</p>}
    </div>
  );

  if (fullPage) {
    return (
      <div className="flex min-h-[60vh] w-full items-center justify-center">
        {spinner}
      </div>
    );
  }

  return spinner;
}
