import { GlassCard } from "./ui/glass-card";
import { StatusBadge } from "./ui/status-badge";
import { MOCK_CLAIMS } from "@/app/lib/mock-data";
import { FileText, Filter, Download } from "lucide-react";
import { Button } from "./ui/button";

export function ClaimsView() {
    return (
        <div className="space-y-6 animate-in fade-in duration-500">
            <div className="flex items-center justify-between">
                <div>
                    <h2 className="text-2xl font-bold text-zinc-900">My Claims</h2>
                    <p className="text-zinc-500">Manage and track your medical bill disputes.</p>
                </div>
                <div className="flex gap-2">
                    <Button variant="outline" size="sm" className="gap-2"><Filter className="w-4 h-4" /> Filter</Button>
                    <Button variant="outline" size="sm" className="gap-2"><Download className="w-4 h-4" /> Export</Button>
                </div>
            </div>

            <GlassCard className="overflow-hidden p-0">
                <div className="overflow-x-auto">
                    <table className="w-full text-sm text-left">
                        <thead className="bg-zinc-50/50 text-zinc-500 font-medium border-b border-zinc-100">
                            <tr>
                                <th className="px-6 py-4">Claim ID</th>
                                <th className="px-6 py-4">Date</th>
                                <th className="px-6 py-4">Provider</th>
                                <th className="px-6 py-4">Amount</th>
                                <th className="px-6 py-4">Issues</th>
                                <th className="px-6 py-4">Status</th>
                                <th className="px-6 py-4 text-right">Action</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-zinc-100">
                            {MOCK_CLAIMS.map((claim) => (
                                <tr key={claim.id} className="hover:bg-zinc-50/50 transition-colors group">
                                    <td className="px-6 py-4 font-medium text-zinc-900 flex items-center gap-2">
                                        <div className="p-1.5 bg-blue-50 text-blue-600 rounded-lg">
                                            <FileText className="w-4 h-4" />
                                        </div>
                                        {claim.id}
                                    </td>
                                    <td className="px-6 py-4 text-zinc-500">{claim.date}</td>
                                    <td className="px-6 py-4 text-zinc-900">{claim.provider}</td>
                                    <td className="px-6 py-4 font-mono font-medium">${claim.amount.toLocaleString()}</td>
                                    <td className="px-6 py-4">
                                        {claim.issues > 0 ? (
                                            <span className="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-red-100 text-red-800">
                                                {claim.issues} Found
                                            </span>
                                        ) : (
                                            <span className="text-zinc-400">-</span>
                                        )}
                                    </td>
                                    <td className="px-6 py-4">
                                        <StatusBadge status={claim.status} />
                                    </td>
                                    <td className="px-6 py-4 text-right">
                                        <Button variant="ghost" size="sm" className="opacity-0 group-hover:opacity-100 transition-opacity text-blue-600">
                                            View Details
                                        </Button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </GlassCard>
        </div>
    );
}
