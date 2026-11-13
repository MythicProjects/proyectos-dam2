import PlaceForm, { PlaceFormData } from "@/components/PlaceForm";
import { usePlaces } from "@/context/PlacesContext";
import { Coords } from "@/hooks/useLocation";
import { router } from "expo-router";
import { useState } from "react";
import { Alert } from "react-native";

export default function AddPlacesScreen() {
	const { addPlace } = usePlaces();
	const [error, setError] = useState<string | null>(null);

	const save = (data: PlaceFormData, coords: Coords | null) => {
		const name = data.name.trim();
		const image = data.image.trim();

		if (name === "") {
			setError("El nombre es obligatorio");
			return;
		}

		if (image !== "" && !image.startsWith("https://")) {
			setError("La URL de la imagen debe empezar por https://");
			return;
		}

		addPlace({
			id: Date.now().toString(),
			name,
			description: data.description.trim(),
			image: image === "" ? undefined : image,
			latitude: coords?.latitude,
			longitude: coords?.longitude,
		});
		Alert.alert("Lugar guardado", `«${name}» se ha añadido a la lista`);
		router.back();
	};

	return <PlaceForm onSubmit={save} error={error} />;
}
