import { usePlaces } from "@/context/PlacesContext";
import { router, Stack, useLocalSearchParams } from "expo-router";
import {
	Alert,
	Image,
	Platform,
	Pressable,
	StyleSheet,
	Text,
	View,
} from "react-native";

export default function DetailScreen() {
	const { id } = useLocalSearchParams<{ id: string }>();
	const { places, removePlace } = usePlaces();
	const place = places.find((place) => place.id === id);

	const goBack = () => {
		if (router.canGoBack()) router.back();
		else router.replace("/");
	};

	const confirmDelete = () => {
		if (!place) return;

		if (Platform.OS === "web") {
			if (window.confirm(`¿Eliminar «${place.name}»?`)) {
				removePlace(place.id);
				goBack();
			}
			return;
		}

		Alert.alert("Eliminar lugar", `¿Eliminar «${place.name}»?`, [
			{ text: "Cancelar", style: "cancel" },
			{
				text: "Eliminar",
				style: "destructive",
				onPress: () => {
					removePlace(place.id);
					goBack();
				},
			},
		]);
	};

	return (
		<View style={styles.screen}>
			{/* Extra: place name in the header */}
			<Stack.Screen options={{ title: place ? place.name : "No encontrado" }} />

			{place ? (
				<>
					{place.image && (
						<Image
							source={{ uri: place.image }}
							style={styles.image}
							accessibilityLabel={`Foto de ${place.name}`}
						/>
					)}
					<Text style={styles.title} accessibilityRole="header">
						{place.name}
					</Text>
					<Text>{place.description}</Text>
					{place.latitude !== undefined && place.longitude !== undefined ? (
						<Text>
							Ubicación: {place.latitude.toFixed(5)},{" "}
							{place.longitude.toFixed(5)}
						</Text>
					) : null}

					<Pressable
						accessibilityRole="button"
						accessibilityLabel={`Eliminar ${place.name}`}
						accessibilityHint="Pide confirmación antes de borrar"
						style={styles.deleteButton}
						onPress={confirmDelete}
					>
						<Text style={styles.buttonText}>Eliminar</Text>
					</Pressable>
				</>
			) : (
				<Text>No existe ningún lugar con este identificador.</Text>
			)}

			<Pressable
				accessibilityRole="button"
				accessibilityLabel="Volver a la lista"
				style={styles.button}
				onPress={goBack}
			>
				<Text style={styles.buttonText}>Volver</Text>
			</Pressable>
		</View>
	);
}

const styles = StyleSheet.create({
	screen: { flex: 1, padding: 16, gap: 12 },
	title: { fontSize: 24, fontWeight: "700" },
	button: {
		padding: 14,
		borderRadius: 10,
		alignItems: "center",
		backgroundColor: "#234c3b",
	},
	buttonText: { color: "#ffffff", fontWeight: "600" },
	image: { width: "100%", height: 220, borderRadius: 12 },
	deleteButton: {
		padding: 14,
		borderRadius: 10,
		alignItems: "center",
		backgroundColor: "#c0392b",
	},
});
