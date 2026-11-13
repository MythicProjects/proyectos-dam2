import { useLocation } from "@/hooks/useLocation";
import {
    ActivityIndicator,
    Pressable,
    StyleSheet,
    Text,
    View,
} from "react-native";
type Coords = { latitude: number; longitude: number };

export default function LocationButton() {
	const { coords, message, loading, getLocation } = useLocation();
	return (
		<View style={styles.box}>
			<Pressable
				accessibilityRole="button"
				accessibilityLabel="Usar mi ubicación"
				accessibilityState={{ busy: loading, disabled: loading }}
				style={({ pressed }) => [styles.button, pressed && styles.pressed]}
				onPress={getLocation}
				disabled={loading}
			>
				<Text style={styles.buttonText}>Usa mi ubicación</Text>
			</Pressable>

			{loading ? <ActivityIndicator /> : null}
			{coords ? (
				<Text accessibilityLiveRegion="polite">
					Lat: {coords.latitude.toFixed(5)} · Lon: {coords.longitude.toFixed(5)}
				</Text>
			) : null}

			{message ? (
				<Text style={styles.message} accessibilityLiveRegion="polite">
					{message}
				</Text>
			) : null}
		</View>
	);
}

const styles = StyleSheet.create({
	box: { gap: 8 },
	button: {
		padding: 14,
		borderRadius: 10,
		alignItems: "center",
		backgroundColor: "#2c5d8a",
	},
	pressed: { opacity: 0.7 },
	buttonText: { color: "#ffffff", fontWeight: "600" },
	message: { color: "#c0392b" },
});
