import { Stack } from "expo-router";
import { AuthProvider, useAuth } from "../auth";

// The layout decides which routes can be visited
function RootStack() {
	const { isLoggedIn } = useAuth();

	return (
		<Stack screenOptions={{ headerShown: false }}>
			<Stack.Protected guard={isLoggedIn}>
				<Stack.Screen name="index" />
			</Stack.Protected>

			<Stack.Protected guard={!isLoggedIn}>
				<Stack.Screen name="login" />
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
