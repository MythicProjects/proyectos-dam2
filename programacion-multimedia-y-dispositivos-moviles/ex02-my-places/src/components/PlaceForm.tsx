import { Coords, useLocation } from "@/hooks/useLocation";
import { useState } from "react";
import { Pressable, StyleSheet, Text, TextInput, View } from "react-native";

export type PlaceFormData = {
	name: string;
	description: string;
	image: string;
};

type PlaceFormProps = {
	onSubmit: (data: PlaceFormData, coords: Coords | null) => void;
	error: string | null;
};

export default function PlaceForm({ onSubmit, error }: PlaceFormProps) {
	const [form, setForm] = useState<PlaceFormData>({
		name: "",
		description: "",
		image: "",
	});
	const { coords, message, loading, getLocation } = useLocation();

	const change = (key: keyof PlaceFormData, value: string) =>
		setForm({ ...form, [key]: value });

	return (
		<View style={styles.form}>
			<TextInput
				accessibilityLabel="Nombre del lugar"
				style={styles.input}
				placeholder="Nombre"
				value={form.name}
				onChangeText={(text) => change("name", text)}
			/>
			<TextInput
				accessibilityLabel="Descripción del lugar"
				style={[styles.input, styles.multiline]}
				placeholder="Description"
				value={form.description}
				onChangeText={(text) => change("description", text)}
				multiline
			/>
			<TextInput
				accessibilityLabel="URL de la imagen"
				style={styles.input}
				placeholder="Url de la imagen (opcional)"
				value={form.image}
				onChangeText={(text) => change("image", text)}
				keyboardType="url"
				autoCapitalize="none"
				autoCorrect={false}
			/>

			{error ? (
				<Text
					style={styles.error}
					accessibilityRole="alert"
					accessibilityLiveRegion="polite"
				>
					{error}
				</Text>
			) : null}

			<Pressable
				style={styles.locationButton}
				onPress={getLocation}
				disabled={loading}
				accessibilityRole="button"
				accessibilityLabel="Añadir mi ubicación al lugar"
			>
				<Text style={styles.buttonText}>
					{loading ? "Obteniendo ubicación…" : "Añadir mi ubicación (opcional)"}
				</Text>
			</Pressable>

			{coords ? (
				<Text accessibilityLiveRegion="polite">
					Ubicación: {coords.latitude.toFixed(5)}, {coords.longitude.toFixed(5)}
				</Text>
			) : null}
			{message ? <Text style={styles.error}>{message}</Text> : null}

			<Pressable
				accessibilityRole="button"
				accessibilityLabel="Guardar lugar"
				style={({ pressed }) => [styles.button, pressed && styles.pressed]}
				onPress={() => onSubmit(form, coords)}
			>
				<Text style={styles.buttonText}>Guardar</Text>
			</Pressable>
		</View>
	);
}

const styles = StyleSheet.create({
	form: { padding: 16, gap: 12 },
	input: {
		padding: 12,
		borderWidth: 1,
		borderColor: "#dddddd",
		borderRadius: 10,
		backgroundColor: "#ffffff",
	},
	multiline: { minHeight: 100, textAlignVertical: "top" },
	error: { color: "#c0392b" },
	button: {
		padding: 14,
		borderRadius: 10,
		alignItems: "center",
		backgroundColor: "#234c3b",
	},
	pressed: { opacity: 0.7 },
	buttonText: { color: "#ffffff", fontWeight: "600" },
	locationButton: {
		padding: 14,
		borderRadius: 10,
		alignItems: "center",
		backgroundColor: "#2c5d8a",
	},
});
