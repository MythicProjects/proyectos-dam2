export type Place = {
	id: string;
	name: string;
	description: string;
	image?: string;
	latitude?: number;
	longitude?: number;
};
export const samplePlaces: Place[] = [
	{
		id: "1",
		name: "Great Wall of China",
		description: "A historic fortification in China.",
		image:
			"https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Great_Wall_of_China_July_2006.JPG/960px-Great_Wall_of_China_July_2006.JPG",
	},
	{
		id: "2",
		name: "Colosseum",
		description: "An ancient Roman amphitheatre in the centre of Rome.",
		image:
			"https://upload.wikimedia.org/wikipedia/commons/thumb/d/de/Colosseo_2020.jpg/960px-Colosseo_2020.jpg",
	},
	{
		id: "3",
		name: "Machu Picchu",
		description: "A 15th-century Inca citadel in the Andes of Peru.",
		image:
			"https://upload.wikimedia.org/wikipedia/commons/thumb/e/eb/Machu_Picchu,_Peru.jpg/960px-Machu_Picchu,_Peru.jpg",
	},
];
