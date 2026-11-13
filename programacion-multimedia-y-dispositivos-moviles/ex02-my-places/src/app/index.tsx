import LocationButton from "@/components/LocationButton";
import PlaceCard from "@/components/PlaceCard";
import { usePlaces } from "@/context/PlacesContext";
import { router } from "expo-router";
import { FlatList, Pressable, StyleSheet, Text, View } from "react-native";

export default function Index() {
	const { places } = usePlaces();
	console.log("en la lista:", places.length);
	return (
		<FlatList
			data={places}
			keyExtractor={(place) => place.id}
			contentContainerStyle={styles.list}
			ListHeaderComponent={
				<View style={styles.header}>
					<Pressable
						accessibilityRole="button"
						accessibilityLabel="Añadir un lugar nuevo"
						style={({ pressed }) => [
							styles.addButton,
							pressed && styles.pressed,
						]}
						onPress={() => router.push("/add")}
					>
						<Text style={styles.addButtonText}>+ Añadir lugar</Text>
					</Pressable>

					<Pressable
						accessibilityRole="button"
						accessibilityLabel="Descubrir contenido de la API"
						style={({ pressed }) => [
							styles.addButton,
							pressed && styles.pressed,
						]}
						onPress={() => router.push("/discover")}
					>
						<Text style={styles.addButtonText}>Descubre</Text>
					</Pressable>
					<LocationButton />
					<Pressable
						style={({ pressed }) => [
							styles.addButton,
							pressed && styles.pressed,
						]}
						onPress={() => router.push("/sensors")}
						accessibilityRole="button"
						accessibilityLabel="Ver lecturas del acelerómetro"
					>
						<Text style={styles.addButtonText}>Sensores</Text>
					</Pressable>
				</View>
			}
			renderItem={({ item }) => (
				<PlaceCard
					place={item}
					onPress={() =>
						router.push({ pathname: "/detail/[id]", params: { id: item.id } })
					}
				/>
			)}
			ListEmptyComponent={
				<Text style={styles.empty}>Todavía no hay lugares guardados.</Text>
			}
		/>
	);
}

const styles = StyleSheet.create({
	list: { padding: 16, gap: 12 },
	header: { gap: 8 },
	addButton: {
		padding: 14,
		borderRadius: 10,
		alignItems: "center",
		backgroundColor: "#234c3b",
	},
	addButtonText: { color: "#ffffff", fontWeight: "600" },
	pressed: { opacity: 0.7 },
	empty: { textAlign: "center", color: "#888888", marginTop: 24 },
});
