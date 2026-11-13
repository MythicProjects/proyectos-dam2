import * as Location from "expo-location";
import { useState } from "react";

export type Coords = { latitude: number; longitude: number };

export function useLocation() {
	const [coords, setCoords] = useState<Coords | null>(null);
	const [message, setMessage] = useState<string | null>(null);
	const [loading, setLoading] = useState(false);

	const getLocation = async () => {
		setLoading(true);
		setMessage(null);
		try {
			const { status } = await Location.requestForegroundPermissionsAsync();
			if (status !== "granted") {
				setMessage(
					"Permiso de ubicación denegado. Puedes seguir usando la app con normalidad.",
				);
				return;
			}
			const position = await Location.getCurrentPositionAsync({
				accuracy: Location.Accuracy.Balanced,
			});
			setCoords({
				latitude: position.coords.latitude,
				longitude: position.coords.longitude,
			});
		} catch {
			setMessage("No se ha podido obtener la ubicación.");
		} finally {
			setLoading(false);
		}
	};

	return { coords, message, loading, getLocation };
}
