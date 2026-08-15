import React from "react";
import { NavLink } from "react-router-dom";
import {
  LayoutDashboard,
  Vault,
  Wand2,
  ShieldCheck,
  Trash2,
  Users,
  ShieldHalf,
  X,
} from "lucide-react";

const NAV_ITEMS = [
  { to: "/dashboard", label: "Dashboard", icon: LayoutDashboard },
  { to: "/vault", label: "Vault", icon: Vault },
  { to: "/generator", label: "Password Generator", icon: Wand2 },
  { to: "/password-strength", label: "Password Strength", icon: ShieldCheck },
  { to: "/shared", label: "Shared with me", icon: Users },
  { to: "/trash", label: "Trash", icon: Trash2 },
];

function NavItems({ onNavigate }) {
  return (
    <nav className="flex flex-1 flex-col gap-1 px-3">
      {NAV_ITEMS.map(({ to, label, icon: Icon }) => (
        <NavLink
          key={to}
          to={to}
          onClick={onNavigate}
          className={({ isActive }) =>
            `flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-colors ${
              isActive
                ? "bg-brand-50 text-brand-700"
                : "text-ink-500 hover:bg-ink-50 hover:text-ink-900"
            }`
          }
        >
          <Icon className="h-[18px] w-[18px]" strokeWidth={1.9} />
          {label}
        </NavLink>
      ))}
    </nav>
  );
}

export default function Sidebar({ mobileOpen, onClose }) {
  return (
    <>
      {/* Desktop sidebar */}
      <aside className="hidden w-64 shrink-0 flex-col border-r border-ink-100 bg-surface py-6 lg:flex">
        <div className="mb-8 flex items-center gap-2 px-5">
          <ShieldHalf className="h-6 w-6 text-brand-500" strokeWidth={2} />
          <span className="font-display text-lg font-semibold text-ink-950">
            SecureVault
          </span>
        </div>
        <NavItems />
      </aside>

      {/* Mobile drawer */}
      {mobileOpen && (
        <div className="fixed inset-0 z-40 lg:hidden">
          <div className="absolute inset-0 bg-ink-950/50" onClick={onClose} />
          <aside className="relative flex h-full w-72 flex-col bg-surface py-6 shadow-popover">
            <div className="mb-8 flex items-center justify-between px-5">
              <div className="flex items-center gap-2">
                <ShieldHalf className="h-6 w-6 text-brand-500" strokeWidth={2} />
                <span className="font-display text-lg font-semibold text-ink-950">
                  SecureVault
                </span>
              </div>
              <button
                onClick={onClose}
                className="rounded-md p-1.5 text-ink-500 hover:bg-ink-50"
                aria-label="Close menu"
              >
                <X className="h-5 w-5" />
              </button>
            </div>
            <NavItems onNavigate={onClose} />
          </aside>
        </div>
      )}
    </>
  );
}
