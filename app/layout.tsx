import type { Metadata } from "next";
import "./globals.css";
export const metadata: Metadata = { title: "Peachcar | Find a car worth loving", description: "A better way to buy, sell and finance quality cars in Kenya." };
export default function RootLayout({ children }: Readonly<{ children: React.ReactNode }>) { return <html lang="en"><body>{children}</body></html>; }
