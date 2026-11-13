import { fetchNearbyPlaces, WikiPlace } from "@/api/discover";
import { usePlaces } from "@/context/PlacesContext";
import { Coords, useLocation } from "@/hooks/useLocation";
import { useNetInfo } from "@react-native-community/netinfo";
import { useEffect, useState } from "react";
import {
	ActivityIndicator,
	FlatList,
	Image,
	Pressable,
	StyleSheet,
	Text,
	View,
} from "react-native";

export default function DiscoverScreen() {
	const [results, setResults] = useState<WikiPlace[]>([]);
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState<string | null>(null);
	const { isConnected } = useNetInfo();
	const { places, addPlace } = usePlaces();
	const { coords, message, loading: locating, getLocation } = useLocation();

	const favoriteId = (item: WikiPlace) => `wiki-${item.pageid}`;

	const isSaved = (item: WikiPlace) =>
		places.some((place) => place.id === favoriteId(item));

	const saveFavorite = (item: WikiPlace) => {
		if (isSaved(item)) return; // avoid duplicates
		addPlace({
			id: favoriteId(item),
			name: item.title,
			description: item.extract ?? "",
			image: item.thumbnail?.source,
			latitude: item.coordinates?.[0]?.lat,
			longitude: item.coordinates?.[0]?.lon,
		});
	};

	const load = async (center?: Coords | null) => {
		if (isConnected === false) {
			setError("Sin conexión: no se puede consultar la API.");
			setLoading(false);
			return;
		}
		setLoading(true);
		setError(null);
		try {
			setResults(await fetchNearbyPlaces(center?.latitude, center?.longitude));
		} catch {
			setError("No se han podido cargar los resultados. Comprueba la conexión.");
		} finally {
			setLoading(false);
		}
	};

	useEffect(() => {
		if (isConnected !== null) load(coords);
	}, [isConnected, coords]);

	if (loading) {
		return (
			<View style={styles.center}>
				<ActivityIndicator size="large" accessibilityLabel="Cargando resultados" />
				<Text>Cargando...</Text>
			</View>
		);
	}

	if (error) {
		return (
			<View style={styles.center}>
				<Text accessibilityRole="alert" style={styles.error}>
					{error}
				</Text>
				<Pressable
					accessibilityRole="button"
					accessibilityLabel="Volver a intentar la carga"
					style={styles.button}
					onPress={() => load(coords)}
				>
					<Text style={styles.buttonText}>Volver a intentar</Text>
				</Pressable>
			</View>
		);
	}

	return (
		<FlatList
			data={results}
			keyExtractor={(item) => item.pageid.toString()}
			contentContainerStyle={styles.list}
			ListHeaderComponent={
				<View style={styles.header}>
					<Text>{coords ? "Lugares cerca de ti" : "Lugares cerca de Barcelona"}</Text>
					<Pressable
						style={styles.button}
						onPress={getLocation}
						disabled={locating}
						accessibilityRole="button"
						accessibilityLabel="Buscar lugares cerca de mi ubicación"
					>
						<Text style={styles.buttonText}>
							{locating ? "Obteniendo ubicación…" : "Buscar cerca de mí"}
						</Text>
					</Pressable>
					{message ? <Text style={styles.error}>{message}</Text> : null}
				</View>
			}
			renderItem={({ item }) => {
				const saved = isSaved(item);
				return (
					<View style={styles.card}>
						{item.thumbnail ? (
							<Image
								source={{ uri: item.thumbnail.source }}
								style={styles.image}
								accessibilityLabel={`Foto de ${item.title}`}
							/>
						) : null}

						<Text style={styles.title}>{item.title}</Text>
						{item.extract ? <Text numberOfLines={3}>{item.extract}</Text> : null}

						<Pressable
							style={[styles.favButton, saved && styles.favSaved]}
							onPress={() => saveFavorite(item)}
							disabled={saved}
							accessibilityRole="button"
							accessibilityLabel={
								saved ? "Ya guardado en favoritos" : `Guardar ${item.title} en favoritos`
							}
							accessibilityState={{ disabled: saved }}
						>
							<Text style={styles.buttonText}>{saved ? "✓ Guardado" : "★ Guardar"}</Text>
						</Pressable>
					</View>
				);
			}}
			ListEmptyComponent={<Text style={styles.empty}>No hay lugares cercanos.</Text>}
		/>
	);
}

const styles = StyleSheet.create({
	center: {
		flex: 1,
		justifyContent: "center",
		alignItems: "center",
		gap: 12,
		padding: 24,
	},
	list: { padding: 16, gap: 12 },
	card: { padding: 16, gap: 4, borderRadius: 12, backgroundColor: "#ffffff" },
	title: { fontSize: 16, fontWeight: "600" },
	error: { color: "#c0392b", textAlign: "center" },
	empty: { textAlign: "center", color: "#888888", marginTop: 24 },
	button: { padding: 14, borderRadius: 10, backgroundColor: "#234c3b" },
	buttonText: { color: "#ffffff", fontWeight: "600" },
	favButton: {
		marginTop: 8,
		padding: 10,
		borderRadius: 8,
		alignItems: "center",
		backgroundColor: "#2c5d8a",
	},
	favSaved: { backgroundColor: "#888888" },
	header: { gap: 8, marginBottom: 8 },
	image: { width: "100%", height: 160, borderRadius: 8 },
});
