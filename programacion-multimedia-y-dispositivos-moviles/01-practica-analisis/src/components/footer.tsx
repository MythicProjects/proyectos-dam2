import { colors } from "@/constants/theme";
import { StyleSheet, Text, View } from "react-native";

export function Footer() {
	return (
		<View style={styles.container}>
			<Text style={styles.text}>This is a footer.</Text>
		</View>
	);
}

const styles = StyleSheet.create({
	container: {
		padding: 16,
		backgroundColor: colors.surface,
		alignItems: "center",
	},
	text: {
		color: colors.text,
		textAlign: "center",
	},
});
