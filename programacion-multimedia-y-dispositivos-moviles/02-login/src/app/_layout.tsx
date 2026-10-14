import { Stack } from "expo-router";

// Default layout: a single screen (index) without header
export default function RootLayout() {
  return <Stack screenOptions={{ headerShown: false }} />;
}
