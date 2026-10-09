import { colors } from "@/constants/theme";
import { useRef, useState } from "react";
import {
	Animated,
	Image,
	ImageSourcePropType,
	Pressable,
	StyleSheet,
	Text,
	View,
} from "react-native";

type GameCardProps = {
	title: string;
	platform: string;
	year: number;
	image: ImageSourcePropType;
};

// ImageSourcePropType -> tipo de TS, valores que acepta source de Image
// No funciona con string por la manera en que react-native gestiona las imágenes, necesita un objeto con la propiedad "uri" o un require() de un archivo local.

export function GameCard({ title, platform, year, image }: GameCardProps) {
	const [liked, setLiked] = useState(false);
	const scale = useRef(new Animated.Value(1)).current;
	const togleLike = () => {
		const willLike = !liked;
		setLiked(willLike);

		// Inicia secuencia -> timing(valor -> valor deseado en una duracion, useNativeDriver -> true para que la animación se ejecute en el hilo nativo)
		if (willLike) {
			Animated.sequence([
				Animated.timing(scale, { toValue: 1.35, duration: 150, useNativeDriver: true }),
				Animated.timing(scale, { toValue: 1, duration: 150, useNativeDriver: true }),
			]).start();
		} else {
			scale.setValue(1.35);
			Animated.sequence([
				Animated.timing(scale, { toValue: 0.75, duration: 150, useNativeDriver: true }),
				Animated.timing(scale, { toValue: 1, duration: 150, useNativeDriver: true }),
			]).start();
		}
	};

	return (
		<View style={styles.card}>
			<Image source={image} style={styles.cover} />

			<View style={styles.info}>
				<Text style={styles.title}>{title}</Text>
				<Text style={styles.text}>{platform}</Text>
				<Text style={styles.text}>{year}</Text>
			</View>

			<Pressable
				onPress={togleLike}
				style={({ pressed }) => [
					styles.likeButton,
					pressed && { transform: [{ scale: 1.1 }] },
				]}
			>
				<Animated.Image
					source={
						liked
							? require("../../assets/images/hearts/heart_full.png")
							: require("../../assets/images/hearts/heart_empty.png")
					}
					style={[styles.heart, { transform: [{ scale }] }]}
					resizeMode="contain"
				/>
			</Pressable>
		</View>
	);
}

const styles = StyleSheet.create({
	card: {
		flexDirection: "row",
		alignItems: "center",
		gap: 20,
		padding: 12,

		marginHorizontal: 30,
		marginVertical: 10,

		backgroundColor: colors.card,
		borderRadius: 25,
		boxShadow: `0 0px 8px ${colors.shadow}`,
	},
	cover: {
		height: 120,
		width: 120,
		borderRadius: 75,
		resizeMode: "cover",
	},
	info: {
		flex: 1,
	},
	title: {
		color: colors.text,
		fontSize: 20,
		fontWeight: 600,
	},
	text: {
		color: colors.muted,
	},
	likeButton: {
		padding: 10,
	},
	heart: {
		height: 45,
		width: 45,
		filter: "colorize(1, 1, 1, 1)",
	},
});
