import { PlacesProvider } from "@/context/PlacesContext";
import { Stack } from "expo-router";

export default function RootLayout() {
	return (
		<PlacesProvider>
			<Stack>
				<Stack.Screen name="index" options={{ title: "MyPlaces" }} />
				<Stack.Screen name="detail/[id]" options={{ title: "Detalles" }} />
				<Stack.Screen name="add" options={{ title: "Añadir lugar" }} />
				<Stack.Screen name="discover" options={{ title: "Descubre" }} />
				<Stack.Screen name="sensors" options={{ title: "Sensores" }} />
			</Stack>
		</PlacesProvider>
	);
}
