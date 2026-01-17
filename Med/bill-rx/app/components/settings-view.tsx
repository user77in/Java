import { GlassCard } from "./ui/glass-card";
import { Button } from "./ui/button";
import { User, Bell, Lock, CreditCard } from "lucide-react";

export function SettingsView() {
    return (
        <div className="space-y-6 animate-in fade-in duration-500 max-w-4xl mx-auto">
            <div className="flex items-center justify-between">
                <div>
                    <h2 className="text-2xl font-bold text-zinc-900">Settings</h2>
                    <p className="text-zinc-500">Manage your account preferences and subscription.</p>
                </div>
                <Button>Save Changes</Button>
            </div>

            <div className="grid gap-6">
                {/* Profile Section */}
                <GlassCard className="p-6">
                    <div className="flex items-center gap-4 mb-6">
                        <div className="p-2 bg-blue-50 text-blue-600 rounded-lg">
                            <User className="w-5 h-5" />
                        </div>
                        <h3 className="text-lg font-bold text-zinc-900">Profile Information</h3>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div className="space-y-2">
                            <label className="text-sm font-medium text-zinc-700">Full Name</label>
                            <input type="text" defaultValue="Dr. Administrator" className="w-full px-3 py-2 rounded-lg border border-zinc-200 focus:outline-none focus:ring-2 focus:ring-blue-500 bg-white/50" />
                        </div>
                        <div className="space-y-2">
                            <label className="text-sm font-medium text-zinc-700">Email Address</label>
                            <input type="email" defaultValue="admin@billrx.com" className="w-full px-3 py-2 rounded-lg border border-zinc-200 focus:outline-none focus:ring-2 focus:ring-blue-500 bg-white/50" />
                        </div>
                    </div>
                </GlassCard>

                {/* Notifications */}
                <GlassCard className="p-6">
                    <div className="flex items-center gap-4 mb-6">
                        <div className="p-2 bg-amber-50 text-amber-600 rounded-lg">
                            <Bell className="w-5 h-5" />
                        </div>
                        <h3 className="text-lg font-bold text-zinc-900">Notifications</h3>
                    </div>

                    <div className="space-y-4">
                        <div className="flex items-center justify-between py-2">
                            <div>
                                <p className="font-medium text-zinc-900">Email Alerts</p>
                                <p className="text-sm text-zinc-500">Receive analysis reports via email.</p>
                            </div>
                            <div className="w-10 h-6 bg-blue-600 rounded-full relative cursor-pointer">
                                <div className="absolute right-1 top-1 w-4 h-4 bg-white rounded-full shadow-sm" />
                            </div>
                        </div>
                        <div className="flex items-center justify-between py-2 border-t border-zinc-50">
                            <div>
                                <p className="font-medium text-zinc-900">Dispute Updates</p>
                                <p className="text-sm text-zinc-500">Get notified when a dispute status changes.</p>
                            </div>
                            <div className="w-10 h-6 bg-blue-600 rounded-full relative cursor-pointer">
                                <div className="absolute right-1 top-1 w-4 h-4 bg-white rounded-full shadow-sm" />
                            </div>
                        </div>
                    </div>
                </GlassCard>

                {/* Subscription */}
                <GlassCard className="p-6 border-blue-200 bg-blue-50/10">
                    <div className="flex items-center gap-4 mb-6">
                        <div className="p-2 bg-indigo-50 text-indigo-600 rounded-lg">
                            <CreditCard className="w-5 h-5" />
                        </div>
                        <h3 className="text-lg font-bold text-zinc-900">Subscription</h3>
                    </div>

                    <div className="flex items-center justify-between">
                        <div>
                            <p className="font-semibold text-zinc-900">BillRx Pro Plan</p>
                            <p className="text-sm text-zinc-500">Next billing date: Feb 15, 2026</p>
                        </div>
                        <Button variant="outline" className="text-blue-600 border-blue-200 hover:bg-blue-50">Manage Billing</Button>
                    </div>
                </GlassCard>
            </div>
        </div>
    );
}
