import { useNetInfo } from "@react-native-community/netinfo";
import { StyleSheet, Text, View } from "react-native";

export default function ConnectionStatus() {
	const { isConnected, type } = useNetInfo();

	if (isConnected === null) return null;

	return (
		<View style={[styles.bar, isConnected ? styles.online : styles.offline]}>
			<Text style={styles.text}>
				{isConnected ? `Conectado (${type})` : "Sin conexión"}
			</Text>
		</View>
	);
}

const styles = StyleSheet.create({
	bar: { padding: 8, borderRadius: 8, alignItems: "center" },
	online: { backgroundColor: "#d4edda" },
	offline: { backgroundColor: "#f8d7da" },
	text: { fontWeight: "600" },
});
