import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import { ShieldCheck } from 'lucide-react';

const inter = Inter({ subsets: ["latin"], variable: "--font-sans" });

export const metadata: Metadata = {
  title: "BillRx | Medical Bill Auditor",
  description: "Stop overpaying for medical care. Detect errors and generate disputes automatically.",
};

import { Sidebar } from "./components/ui/sidebar";

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body
        className={`${inter.variable} antialiased h-screen flex font-sans bg-slate-50 text-slate-900 overflow-hidden`}
      >
        <Sidebar />

        <main className="flex-1 flex flex-col h-full overflow-hidden relative">
          {/* Context Header for Mobile/Desktop */}
          <header className="h-16 border-b border-zinc-200 bg-white/80 backdrop-blur-sm flex items-center justify-between px-6 shrink-0 z-30">
            <h1 className="font-bold text-xl text-zinc-800">Overview</h1>
            <div className="flex items-center gap-4">
              <button className="text-sm font-medium text-blue-600 hover:bg-blue-50 px-3 py-1.5 rounded-full transition-colors">
                + New Claim
              </button>
              <div className="w-8 h-8 rounded-full bg-zinc-100 border border-zinc-200" />
            </div>
          </header>

          <div className="flex-1 overflow-auto p-6 md:p-8">
            {children}
          </div>
        </main>
      </body>
    </html>
  );
}
