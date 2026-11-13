import { Place } from "@/data/places";
import AsyncStorage from "@react-native-async-storage/async-storage";
import {
	createContext,
	ReactNode,
	useContext,
	useEffect,
	useState,
} from "react";

const STORAGE_KEY = "places";

type PlacesContextValue = {
	places: Place[];
	addPlace: (place: Place) => void;
	removePlace: (id: string) => void;
};

const PlacesContext = createContext<PlacesContextValue | null>(null);

async function loadPlaces(): Promise<Place[]> {
	try {
		const text = await AsyncStorage.getItem(STORAGE_KEY);
		return text ? JSON.parse(text) : [];
	} catch {
		return [];
	}
}

async function savePlaces(places: Place[]) {
	try {
		await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(places));
	} catch (error) {
		console.warn("Could not save places", error);
	}
}

export function PlacesProvider({ children }: { children: ReactNode }) {
	const [places, setPlaces] = useState<Place[]>([]);

	useEffect(() => {
		loadPlaces().then(setPlaces);
	}, []);

	const update = (update: Place[]) => {
		setPlaces(update);
		savePlaces(update);
	};

	const addPlace = (place: Place) => update([...places, place]);
	const removePlace = (id: String) => {
		update(places.filter((places) => places.id !== id));
	};

	return (
		<PlacesContext.Provider value={{ places, addPlace, removePlace }}>
			{children}
		</PlacesContext.Provider>
	);
}

export function usePlaces() {
	const context = useContext(PlacesContext);
	if (!context) throw new Error("usePlaces must be inside PlacesProvider");
	return context;
}
