import type { NextConfig } from "next";

const apiBase = process.env.API_BASE_URL ?? "http://localhost:8080";

const nextConfig: NextConfig = {
  async rewrites() {
    return [{ source: "/api/:path*", destination: apiBase + "/api/:path*" }];
  },
};

export default nextConfig;
