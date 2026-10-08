import { Footer } from "@/components/footer";
import { GameList } from "@/components/gameList";
import { colors } from "@/constants/theme";
import { StyleSheet } from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import { Header } from "../components/header";

export default function HomeScreen() {
	return (
		<SafeAreaView style={styles.screen}>
			<Header />
			<GameList />
			<Footer />
		</SafeAreaView>
	);
}

const styles = StyleSheet.create({
	screen: {
		flex: 1,
		backgroundColor: colors.surface,
	},
});
