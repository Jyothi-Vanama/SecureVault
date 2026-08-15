import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import {
  KeyRound,
  ShieldCheck,
  ShieldAlert,
  Clock,
  Plus,
  Wand2,
  Gauge,
  Vault as VaultIcon,
  PlusCircle,
  Pencil,
  Trash2,
  LogIn,
} from "lucide-react";
import AppLayout from "../components/AppLayout";
import LoadingSpinner from "../components/LoadingSpinner";
import { mockCredentials, mockDashboardStats, mockActivity } from "../services/mockData";

const ACTIVITY_ICON = {
  created: PlusCircle,
  updated: Pencil,
  deleted: Trash2,
  login: LogIn,
};

function timeAgo(iso) {
  const diffMs = Date.now() - new Date(iso).getTime();
  const hours = Math.floor(diffMs / 3_600_000);
  if (hours < 1) return "just now";
  if (hours < 24) return `${hours}h ago`;
  return `${Math.floor(hours / 24)}d ago`;
}

function SecurityGauge({ score }) {
  const radius = 54;
  const circumference = 2 * Math.PI * radius;
  const offset = circumference * (1 - score / 100);
  const tone = score >= 75 ? "#1B8A5A" : score >= 45 ? "#B5740B" : "#C4321D";

  return (
    <div className="relative flex h-36 w-36 items-center justify-center">
      <svg viewBox="0 0 120 120" className="h-36 w-36 -rotate-90">
        <circle cx="60" cy="60" r={radius} fill="none" stroke="#E2E7F0" strokeWidth="10" />
        <circle
          cx="60"
          cy="60"
          r={radius}
          fill="none"
          stroke={tone}
          strokeWidth="10"
          strokeLinecap="round"
          strokeDasharray={circumference}
          strokeDashoffset={offset}
          style={{ transition: "stroke-dashoffset 0.6s ease" }}
        />
      </svg>
      <div className="absolute flex flex-col items-center">
        <span className="font-display text-3xl font-semibold text-ink-950">{score}</span>
        <span className="text-xs text-ink-500">out of 100</span>
      </div>
    </div>
  );
}

export default function Dashboard() {
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const t = setTimeout(() => setLoading(false), 500);
    return () => clearTimeout(t);
  }, []);

  const stats = mockDashboardStats;
  const recent = [...mockCredentials]
    .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
    .slice(0, 4);

  const summaryCards = [
    { label: "Total Credentials", value: stats.totalCredentials, icon: KeyRound, tone: "text-brand-600 bg-brand-50" },
    { label: "Strong Passwords", value: stats.strongPasswords, icon: ShieldCheck, tone: "text-success-600 bg-success-100" },
    { label: "Weak Passwords", value: stats.weakPasswords, icon: ShieldAlert, tone: "text-danger-600 bg-danger-100" },
    { label: "Recently Added", value: stats.recentlyAdded, icon: Clock, tone: "text-ink-700 bg-ink-100" },
  ];

  const quickActions = [
    { label: "Add Credential", icon: Plus, to: "/vault/add" },
    { label: "Generate Password", icon: Wand2, to: "/generator" },
    { label: "Check Password Strength", icon: Gauge, to: "/password-strength" },
    { label: "View Vault", icon: VaultIcon, to: "/vault" },
  ];

  return (
    <AppLayout>
      <div className="mb-6">
        <h1 className="text-2xl font-semibold text-ink-950">Dashboard</h1>
        <p className="mt-1 text-sm text-ink-500">
          A snapshot of your vault's health and recent activity.
        </p>
      </div>

      {loading ? (
        <LoadingSpinner fullPage label="Loading your dashboard…" />
      ) : (
        <div className="space-y-6">
          {/* Summary cards */}
          <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
            {summaryCards.map(({ label, value, icon: Icon, tone }) => (
              <div key={label} className="panel p-5">
                <div className={`mb-3 flex h-10 w-10 items-center justify-center rounded-lg ${tone}`}>
                  <Icon className="h-5 w-5" strokeWidth={1.9} />
                </div>
                <p className="text-2xl font-semibold text-ink-950">{value}</p>
                <p className="mt-0.5 text-sm text-ink-500">{label}</p>
              </div>
            ))}
          </div>

          <div className="grid grid-cols-1 gap-6 lg:grid-cols-3">
            {/* Password health */}
            <div className="panel flex flex-col items-center p-6 text-center">
              <h2 className="self-start text-base font-semibold text-ink-900">Password Health</h2>
              <div className="mt-4">
                <SecurityGauge score={stats.securityScore} />
              </div>
              <p className="mt-3 text-sm text-ink-500">
                {stats.weakPasswords} weak password{stats.weakPasswords === 1 ? "" : "s"} could use an update.
              </p>
              <Link to="/vault" className="btn-secondary mt-4 w-full">
                Review vault
              </Link>
            </div>

            {/* Recent credentials */}
            <div className="panel p-6 lg:col-span-2">
              <div className="mb-4 flex items-center justify-between">
                <h2 className="text-base font-semibold text-ink-900">Recent Credentials</h2>
                <Link to="/vault" className="text-sm font-medium text-brand-600 hover:text-brand-700">
                  View all
                </Link>
              </div>
              <ul className="divide-y divide-ink-100">
                {recent.map((c) => (
                  <li key={c.id} className="flex items-center justify-between py-3">
                    <div className="flex items-center gap-3">
                      <div className="flex h-9 w-9 items-center justify-center rounded-lg bg-brand-50 text-xs font-semibold text-brand-600">
                        {c.title.slice(0, 2).toUpperCase()}
                      </div>
                      <div>
                        <Link
                          to={`/vault/${c.id}`}
                          className="text-sm font-medium text-ink-900 hover:text-brand-600"
                        >
                          {c.title}
                        </Link>
                        <p className="text-xs text-ink-500">{c.username}</p>
                      </div>
                    </div>
                    <span className="text-xs text-ink-300">{timeAgo(c.createdAt)}</span>
                  </li>
                ))}
              </ul>
            </div>
          </div>

          <div className="grid grid-cols-1 gap-6 lg:grid-cols-3">
            {/* Security activity */}
            <div className="panel p-6 lg:col-span-2">
              <h2 className="mb-4 text-base font-semibold text-ink-900">Security Activity</h2>
              <ul className="space-y-4">
                {mockActivity.map((a) => {
                  const Icon = ACTIVITY_ICON[a.type] ?? Clock;
                  return (
                    <li key={a.id} className="flex items-start gap-3">
                      <div className="mt-0.5 flex h-8 w-8 shrink-0 items-center justify-center rounded-full bg-ink-50">
                        <Icon className="h-4 w-4 text-ink-500" strokeWidth={1.9} />
                      </div>
                      <div className="min-w-0">
                        <p className="text-sm text-ink-900">{a.message}</p>
                        <p className="text-xs text-ink-300">{timeAgo(a.timestamp)}</p>
                      </div>
                    </li>
                  );
                })}
              </ul>
            </div>

            {/* Quick actions */}
            <div className="panel p-6">
              <h2 className="mb-4 text-base font-semibold text-ink-900">Quick Actions</h2>
              <div className="space-y-2">
                {quickActions.map(({ label, icon: Icon, to }) => (
                  <Link
                    key={label}
                    to={to}
                    className="flex items-center gap-3 rounded-lg border border-ink-100 px-3.5 py-2.5 text-sm font-medium text-ink-700 transition-colors hover:border-brand-200 hover:bg-brand-50 hover:text-brand-700"
                  >
                    <Icon className="h-4 w-4" strokeWidth={1.9} />
                    {label}
                  </Link>
                ))}
              </div>
            </div>
          </div>
        </div>
      )}
    </AppLayout>
  );
}
