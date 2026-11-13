import { Image, Pressable, StyleSheet, Text } from "react-native";
import { Place } from "../data/places";

type PlaceCardProps = {
	place: Place;
	onPress: () => void;
};

export default function PlaceCard({ place, onPress }: PlaceCardProps) {
	return (
		<Pressable
			accessibilityRole="button"
			accessibilityLabel={`Ver ${place.name}`}
			accessibilityHint="Abre el detalle del lugar"
			style={({ pressed }) => [styles.card, pressed && styles.pressed]}
			onPress={onPress}
		>
			<Text style={styles.name}>{place.name}</Text>
			<Text style={styles.description} numberOfLines={1}>
				{place.description}
			</Text>
			{place.image && (
				<Image
					source={{ uri: place.image }}
					style={styles.image}
					accessibilityLabel={`Foto de ${place.name}`}
				/>
			)}
		</Pressable>
	);
}

const styles = StyleSheet.create({
	card: { padding: 16, gap: 4, borderRadius: 12, backgroundColor: "#ffffff" },
	pressed: { opacity: 0.7 },
	name: { fontSize: 18, fontWeight: "600" },
	description: { color: "#666666" },
	image: { width: "100%", height: 140, borderRadius: 8 },
});
