export type WikiPlace = {
	pageid: number;
	title: string;
	extract?: string;
	thumbnail?: { source: string };
	coordinates?: { lat: number; lon: number }[];
};

const API_URL = "https://en.wikipedia.org/w/api.php";

// Default: Barcelona city centre
export async function fetchNearbyPlaces(
	lat = 41.3874,
	lon = 2.1686,
): Promise<WikiPlace[]> {
	const params = new URLSearchParams({
		action: "query",
		format: "json",
		formatversion: "2",
		origin: "*",
		generator: "geosearch",
		ggscoord: `${lat}|${lon}`,
		ggsradius: "10000",
		ggslimit: "20",
		prop: "coordinates|pageimages|extracts",
		piprop: "thumbnail",
		pithumbsize: "400",
		exintro: "1",
		explaintext: "1",
		exsentences: "2",
		exlimit: "20",
	});

	const response = await fetch(`${API_URL}?${params}`);
	if (!response.ok) throw new Error(`HTTP error ${response.status}`);

	const data = await response.json();
	return data.query?.pages ?? [];
}
