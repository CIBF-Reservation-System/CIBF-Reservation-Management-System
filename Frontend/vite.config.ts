import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";
import path from "path";
import AutoImport from "unplugin-auto-import/vite";

export default defineConfig(({ mode }) => ({
  server: {
    host: "::",
    port: 8080,
  },
  plugins: [
    react(),
    mode === "development" &&
      AutoImport({
        imports: [
          "react",
          {
            "react-router-dom": ["useNavigate", "useParams", "useLocation"],
          },
        ],
        dts: "src/auto-imports.d.ts",
      }),
  ].filter(Boolean),
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
}));
// ...existing code...