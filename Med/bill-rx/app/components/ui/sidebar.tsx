'use client';

import { useState, useEffect } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { Home, FileText, Settings, Users, ShieldCheck, ChevronLeft, ChevronRight, PieChart, Menu } from 'lucide-react';
import { cn } from '@/app/lib/utils';

export function Sidebar() {
    const [collapsed, setCollapsed] = useState(false);
    const [mobileOpen, setMobileOpen] = useState(false);
    const router = useRouter();
    const searchParams = useSearchParams();
    const currentView = searchParams.get('view') || 'dashboard';

    const items = [
        { icon: Home, label: 'Dashboard', id: 'dashboard' },
        { icon: FileText, label: 'My Claims', id: 'claims' },
        { icon: Users, label: 'Patients', id: 'patients' },
        { icon: PieChart, label: 'Reports', id: 'reports' },
        { icon: Settings, label: 'Settings', id: 'settings' },
    ];

    const handleNav = (viewId: string) => {
        const params = new URLSearchParams(window.location.search);
        params.set('view', viewId);
        router.push(`/?${params.toString()}`);
        setMobileOpen(false);
    };

    return (
        <>
            {/* Mobile Trigger */}
            <button
                className="md:hidden fixed top-4 left-4 z-50 p-2 bg-white rounded-lg shadow-lg border border-zinc-200"
                onClick={() => setMobileOpen(!mobileOpen)}
            >
                <Menu className="w-5 h-5 text-zinc-600" />
            </button>

            {/* Sidebar Overlay (Mobile) */}
            {mobileOpen && (
                <div
                    className="fixed inset-0 bg-black/50 z-40 md:hidden backdrop-blur-sm"
                    onClick={() => setMobileOpen(false)}
                />
            )}

            <aside className={cn(
                "fixed md:static inset-y-0 left-0 bg-white border-r border-zinc-200 transition-all duration-300 z-50 flex flex-col shadow-2xl md:shadow-none",
                collapsed ? "w-20" : "w-64",
                mobileOpen ? "translate-x-0" : "-translate-x-full md:translate-x-0"
            )}>
                {/* Brand */}
                <div className="h-16 flex items-center justify-center border-b border-zinc-50 shrink-0">
                    <div className="flex items-center gap-2 font-bold text-xl text-zinc-900">
                        <div className="bg-gradient-to-br from-blue-600 to-indigo-600 text-white p-2 rounded-xl shadow-lg shadow-blue-500/30">
                            <ShieldCheck className="w-5 h-5" />
                        </div>
                        {!collapsed && <span className="tracking-tight bg-gradient-to-r from-zinc-900 to-zinc-600 bg-clip-text text-transparent">BillRx</span>}
                    </div>
                </div>

                {/* Navigation */}
                <div className="flex-1 py-6 px-3 space-y-1 overflow-y-auto">
                    {items.map((item) => {
                        const isActive = currentView === item.id;
                        return (
                            <button
                                key={item.id}
                                onClick={() => handleNav(item.id)}
                                className={cn(
                                    "w-full flex items-center gap-3 px-3 py-3 rounded-xl transition-all duration-200 group relative overflow-hidden",
                                    isActive
                                        ? "bg-blue-50/50 text-blue-700 font-semibold shadow-sm ring-1 ring-blue-100"
                                        : "text-zinc-500 hover:bg-zinc-50 hover:text-zinc-900"
                                )}
                            >
                                <item.icon className={cn("w-5 h-5 shrink-0 transition-colors", isActive ? "text-blue-600" : "text-zinc-400 group-hover:text-zinc-600")} />
                                {!collapsed && (
                                    <span className="whitespace-nowrap font-medium text-sm">
                                        {item.label}
                                    </span>
                                )}
                                {isActive && <div className="absolute right-0 top-1/2 -translate-y-1/2 w-1 h-8 bg-blue-600 rounded-l-full" />}
                            </button>
                        );
                    })}
                </div>

                {/* User */}
                <div className="p-4 border-t border-zinc-50 bg-zinc-50/50">
                    <div className={cn("flex items-center gap-3", collapsed ? "justify-center" : "")}>
                        <div className="w-10 h-10 rounded-full bg-gradient-to-tr from-blue-500 to-cyan-400 shrink-0 border-2 border-white shadow-sm" />
                        {!collapsed && (
                            <div className="overflow-hidden text-left">
                                <p className="text-sm font-semibold text-zinc-900 truncate">Dr. Administrator</p>
                                <p className="text-xs text-zinc-500 truncate">Pro License</p>
                            </div>
                        )}
                    </div>
                </div>

                {/* Desktop Collapse Toggle */}
                <button
                    onClick={() => setCollapsed(!collapsed)}
                    className="absolute -right-3 top-20 bg-white border border-zinc-200 p-1.5 rounded-full shadow-md text-zinc-400 hover:text-blue-600 transition-all hidden md:flex hover:scale-110"
                >
                    {collapsed ? <ChevronRight className="w-3 h-3" /> : <ChevronLeft className="w-3 h-3" />}
                </button>
            </aside>
        </>
    );
}

