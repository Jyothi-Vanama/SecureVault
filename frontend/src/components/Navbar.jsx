import React, { useState } from "react";
import { Link } from "react-router-dom";
import { Menu, Plus, ChevronDown, LogOut, User, Settings } from "lucide-react";
import { useAuth } from "../context/AuthContext";

export default function Navbar({ onMenuClick }) {
  const { user, logout } = useAuth();
  const [menuOpen, setMenuOpen] = useState(false);

  const displayName = user?.name || user?.email || "Account";
  const initials = displayName.slice(0, 1).toUpperCase();

  return (
    <header className="sticky top-0 z-30 flex h-16 items-center justify-between border-b border-ink-100 bg-surface/95 px-4 backdrop-blur sm:px-6">
      <div className="flex items-center gap-3">
        <button
          onClick={onMenuClick}
          className="rounded-lg p-2 text-ink-500 hover:bg-ink-50 lg:hidden"
          aria-label="Open menu"
        >
          <Menu className="h-5 w-5" />
        </button>
      </div>

      <div className="flex items-center gap-2 sm:gap-3">
        <Link to="/vault/add" className="btn-primary hidden sm:inline-flex">
          <Plus className="h-4 w-4" />
          Add Credential
        </Link>
        <Link
          to="/vault/add"
          className="rounded-lg bg-brand-500 p-2.5 text-white hover:bg-brand-600 sm:hidden"
          aria-label="Add credential"
        >
          <Plus className="h-4 w-4" />
        </Link>

        <div className="relative">
          <button
            onClick={() => setMenuOpen((v) => !v)}
            className="flex items-center gap-2 rounded-lg border border-ink-100 py-1.5 pl-1.5 pr-2.5 hover:bg-ink-50"
          >
            <span className="flex h-7 w-7 items-center justify-center rounded-full bg-ink-900 text-xs font-semibold text-white">
              {initials}
            </span>
            <span className="hidden max-w-[9rem] truncate text-sm font-medium text-ink-700 sm:inline">
              {displayName}
            </span>
            <ChevronDown className="h-4 w-4 text-ink-300" />
          </button>

          {menuOpen && (
            <>
              <div className="fixed inset-0 z-10" onClick={() => setMenuOpen(false)} />
              <div className="absolute right-0 z-20 mt-2 w-48 rounded-xl border border-ink-100 bg-surface py-1.5 shadow-popover">
                <Link
                  to="/profile"
                  onClick={() => setMenuOpen(false)}
                  className="flex items-center gap-2.5 px-3.5 py-2 text-sm text-ink-700 hover:bg-ink-50"
                >
                  <User className="h-4 w-4" /> Profile
                </Link>
                <Link
                  to="/settings"
                  onClick={() => setMenuOpen(false)}
                  className="flex items-center gap-2.5 px-3.5 py-2 text-sm text-ink-700 hover:bg-ink-50"
                >
                  <Settings className="h-4 w-4" /> Settings
                </Link>
                <div className="my-1.5 border-t border-ink-100" />
                <button
                  onClick={logout}
                  className="flex w-full items-center gap-2.5 px-3.5 py-2 text-sm text-danger-500 hover:bg-danger-100"
                >
                  <LogOut className="h-4 w-4" /> Log out
                </button>
              </div>
            </>
          )}
        </div>
      </div>
    </header>
  );
}
