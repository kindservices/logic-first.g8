import { defineConfig } from "vite";
import scalaJSPlugin from "@scala-js/vite-plugin-scalajs";

export default defineConfig({
  base: './',
  build: {
    outDir: 'dist'
  },
  plugins: [scalaJSPlugin()],
});