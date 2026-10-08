import { colors } from "@/constants/theme";
import { Image, StyleSheet, View } from "react-native";

export function Header() {
	return (
		<View style={styles.container}>
			<Image style={styles.icon} source={require("../../assets/images/icon.png")} />
		</View>
	);
}

const styles = StyleSheet.create({
	container: {
		paddingVertical: 12,
		backgroundColor: colors.surface,
		alignItems: "center",
	},
	icon: {
		height: 70,
		width: 70,
		borderRadius: 35,
	},
});
