import { Accelerometer } from "expo-sensors";
import { useEffect, useState } from "react";
import { StyleSheet, Text, View } from "react-native";

type Reading = { x: number; y: number; z: number };

export default function SensorsScreen() {
	const [available, setAvailable] = useState<boolean | null>(null);
	const [reading, setReading] = useState<Reading>({ x: 0, y: 0, z: 0 });

	useEffect(() => {
		let subscription: ReturnType<typeof Accelerometer.addListener> | null =
			null;
		let cancelled = false;

		Accelerometer.isAvailableAsync().then((isAvailable) => {
			if (cancelled) return;
			setAvailable(isAvailable);
			if (!isAvailable) return;

			Accelerometer.setUpdateInterval(200); // one reading every 200 ms
			subscription = Accelerometer.addListener(setReading);
		});

		// Cleanup: stop listening when leaving the screen
		return () => {
			cancelled = true;
			subscription?.remove();
		};
	}, []);

	if (available === null)
		return <Text style={styles.center}>Comprobando sensor…</Text>;
	if (!available) {
		return (
			<Text style={styles.center}>
				El acelerómetro no está disponible en este dispositivo.
			</Text>
		);
	}

	// Total force in g: about 1 at rest (gravity)
	const force = Math.sqrt(reading.x ** 2 + reading.y ** 2 + reading.z ** 2);

	return (
		<View style={styles.screen} accessibilityLiveRegion="polite">
			<Text style={styles.value}>x: {reading.x.toFixed(2)}</Text>
			<Text style={styles.value}>y: {reading.y.toFixed(2)}</Text>
			<Text style={styles.value}>z: {reading.z.toFixed(2)}</Text>
			<Text>Fuerza total: {force.toFixed(2)} g</Text>
			{force > 1.8 ? (
				<Text style={styles.alert}>¡Movimiento fuerte!</Text>
			) : null}
		</View>
	);
}

const styles = StyleSheet.create({
	screen: { flex: 1, padding: 24, gap: 8 },
	center: { padding: 24, textAlign: "center" },
	value: { fontSize: 24, fontVariant: ["tabular-nums"] },
	alert: { fontSize: 20, fontWeight: "700", color: "#c0392b" },
});
