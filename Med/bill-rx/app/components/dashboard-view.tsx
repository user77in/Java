'use client';

import { useEffect, useState } from 'react';
import { Bill, Issue } from '@/app/types';
import { GlassCard } from './ui/glass-card';
import { Button } from './ui/button';
import { cn } from '@/app/lib/utils';
import { SavingsChart } from './analysis/savings-chart';
import { Trash2, FileText, TrendingUp, AlertCircle } from 'lucide-react';
import { DatabaseService } from '@/app/lib/db/database-service';

interface DashboardViewProps {
    onOpenBill: (billId: string) => void;
    onNewscan: () => void;
}

export function DashboardView({ onOpenBill, onNewscan }: DashboardViewProps) {
    const [history, setHistory] = useState<{ bill: Bill, issues: Issue[] }[]>([]);
    const [totalSaved, setTotalSaved] = useState(0);

    useEffect(() => {
        // Load from Database (or LocalStorage fallback)
        const db = new DatabaseService();
        db.getHistory().then(parsed => {
            if (!Array.isArray(parsed)) {
                console.error("Invalid history data:", parsed);
                setHistory([]);
                return;
            }
            setHistory(parsed);

            // Calculate potential savings
            const savings = parsed.reduce((acc: number, item: { issues: Issue[] }) => {
                return acc + item.issues.reduce((s: number, i) => s + (i.savingsPotential || 0), 0);
            }, 0);
            setTotalSaved(savings);
        });
    }, []);

    const clearHistory = () => {
        localStorage.removeItem('billrx_history'); // Force clear local
        setHistory([]);
        setTotalSaved(0);
    };

    return (
        <div className="space-y-8 animate-in fade-in duration-500">
            <div className="flex items-center justify-between">
                <h2 className="text-3xl font-bold bg-gradient-to-r from-zinc-900 to-zinc-600 bg-clip-text text-transparent">
                    Your Dashboard
                </h2>
                <Button onClick={onNewscan}>
                    New Scan
                </Button>
            </div>

            {/* KPI Grid */}
            <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-6">
                {/* Chart takes 2 columns */}
                <SavingsChart />

                {/* Stats Cards */}
                <GlassCard className="p-6 flex flex-col justify-between h-[180px]">
                    <div>
                        <div className="flex items-center justify-between mb-2">
                            <p className="text-sm font-medium text-zinc-500">Total Savings</p>
                            <span className="p-2 bg-green-100 text-green-700 rounded-full"><TrendingUp className="w-4 h-4" /></span>
                        </div>
                        <p className="text-4xl font-black text-zinc-900 tracking-tight">
                            {new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(totalSaved)}
                        </p>
                    </div>
                    <div className="flex items-center text-xs font-medium text-green-600 bg-green-50 w-fit px-2 py-1 rounded">
                        +12% from last month
                    </div>
                </GlassCard>

                <GlassCard className="p-6 flex flex-col justify-between h-[180px]">
                    <div>
                        <div className="flex items-center justify-between mb-2">
                            <p className="text-sm font-medium text-zinc-500">Bills Processed</p>
                            <span className="p-2 bg-blue-100 text-blue-700 rounded-full"><FileText className="w-4 h-4" /></span>
                        </div>
                        <p className="text-4xl font-black text-zinc-900 tracking-tight">{history.length}</p>
                    </div>
                    <div className="flex items-center text-xs font-medium text-blue-600 bg-blue-50 w-fit px-2 py-1 rounded">
                        1 pending review
                    </div>
                </GlassCard>
            </div>

            <div className="space-y-4">
                <div className="flex items-center justify-between border-b pb-2">
                    <h3 className="text-xl font-semibold text-zinc-800">Recent Uploads</h3>
                    {history.length > 0 && (
                        <button onClick={clearHistory} className="text-xs text-red-500 hover:text-red-600 flex items-center">
                            <Trash2 className="w-3 h-3 mr-1" /> Clear History
                        </button>
                    )}
                </div>

                {history.length === 0 ? (
                    <div className="text-center py-12 text-zinc-400">
                        <p>No bills analyzed yet.</p>
                        <Button variant="link" onClick={onNewscan}>Start your first scan</Button>
                    </div>
                ) : (
                    <div className="space-y-3">
                        {history.map((item) => (
                            <GlassCard
                                key={item.bill.id}
                                className="p-4 flex flex-col md:flex-row items-center justify-between gap-4 hover:border-primary/30 transition-colors cursor-pointer"
                                onClick={() => onOpenBill(item.bill.id)}
                            >
                                <div className="flex items-center gap-4 w-full md:w-auto">
                                    <div className="p-2 bg-zinc-100 rounded-lg">
                                        <FileText className="w-6 h-6 text-zinc-500" />
                                    </div>
                                    <div className="text-left">
                                        <p className="font-semibold text-zinc-900">{item.bill.providerName || 'Unknown Provider'}</p>
                                        <p className="text-xs text-zinc-500">{new Date(item.bill.uploadDate).toLocaleDateString()} • {item.bill.fileName}</p>
                                    </div>
                                </div>

                                <div className="flex items-center gap-6 w-full md:w-auto justify-between md:justify-end">
                                    <div className="flex items-center gap-2">
                                        <AlertCircle className={cn("w-4 h-4", item.issues.length > 0 ? "text-red-500" : "text-green-500")} />
                                        <span className="text-sm font-medium">{item.issues.length} Issues</span>
                                    </div>
                                    <div className="text-right">
                                        <p className="font-bold text-zinc-900">
                                            {item.bill.totalBilled ? `$${item.bill.totalBilled.toFixed(2)}` : '--'}
                                        </p>
                                    </div>
                                </div>
                            </GlassCard>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}
