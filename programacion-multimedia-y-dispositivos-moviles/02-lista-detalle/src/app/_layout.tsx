import { Stack } from "expo-router";
import { AuthProvider, useAuth } from "@/auth";
import { colors } from "@/styles";

function RootStack() {
  const { isLoggedIn } = useAuth();

  return (
    <Stack
      screenOptions={{
        headerStyle: { backgroundColor: colors.primary },
        headerTintColor: "#ffffff",
        contentStyle: { backgroundColor: colors.background },
      }}
    >
      {/* Only reachable after login */}
      <Stack.Protected guard={isLoggedIn}>
        <Stack.Screen name="index" options={{ title: "Juegos" }} />
        <Stack.Screen name="game/[id]" options={{ title: "Detalle" }} />
      </Stack.Protected>

      {/* Only reachable before login */}
      <Stack.Protected guard={!isLoggedIn}>
        <Stack.Screen name="login" options={{ headerShown: false }} />
      </Stack.Protected>
    </Stack>
  );
}

export default function RootLayout() {
  return (
    <AuthProvider>
      <RootStack />
    </AuthProvider>
  );
}
