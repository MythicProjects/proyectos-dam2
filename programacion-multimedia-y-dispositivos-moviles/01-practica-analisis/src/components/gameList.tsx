import { colors } from "@/constants/theme";
import { ScrollView, StyleSheet } from "react-native";
import { GameCard } from "./gameCard";

const games = [
	{
		id: "1",
		title: "Juego uno",
		platform: "PC",
		year: 2024,
		image: require("../../assets/images/artibot.jpg"),
	},
	{
		id: "2",
		title: "Juego dos",
		platform: "Switch",
		year: 2022,
		image: require("../../assets/images/golden.jpg"),
	},
	{
		id: "3",
		title: "Juego tres",
		platform: "PS5",
		year: 2023,
		image: require("../../assets/images/kepler.jpg"),
	},
	{
		id: "4",
		title: "Juego cuatro",
		platform: "Xbox",
		year: 2021,
		image: require("../../assets/images/kori.jpg"),
	},
	{
		id: "5",
		title: "Juego cinco",
		platform: "PC",
		year: 2020,
		image: require("../../assets/images/sky.jpg"),
	},
	{
		id: "6",
		title: "Juego seis",
		platform: "GBA",
		year: 2004,
		image: require("../../assets/images/character.jpg"),
	},
	{
		id: "7",
		title: "Juego siete",
		platform: "SNES",
		year: 1992,
		image: require("../../assets/images/atlas.jpg"),
	},
	{
		id: "8",
		title: "Juego ocho",
		platform: "PS1",
		year: 1998,
		image: require("../../assets/images/church.jpg"),
	},
];

export function GameList() {
	return (
		<ScrollView style={styles.list} contentContainerStyle={styles.content}>
			{games.map(({ id, ...game }) => (
				<GameCard key={id} {...game} />
			))}
		</ScrollView>
	);
}
// 1. games.map() -> recorre el array
// 2. extrae id en una nueva variable y el resto de propiedades del objeto se agrupan en un nuevo objeto llamado game
// 3. Es un return de lo que sigue
// 4. React necesita key, "id"
// 5. operador "spread" {...game} -> antes ha recogido todas las propiedades, ahora las aplica en GameCard.

const styles = StyleSheet.create({
	list: {
		flex: 1,
		backgroundColor: colors.background,
	},
	content: {
		paddingVertical: 16,
	},
});
