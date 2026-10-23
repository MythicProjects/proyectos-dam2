import { router, Stack, useLocalSearchParams } from "expo-router";
import { Pressable, Text, View } from "react-native";
import { games } from "@/data/games";
import { styles } from "@/styles";

// Detail screen: [id] in the file name = dynamic route (/game/1, /game/2...)
export default function GameDetailScreen() {
  const { id } = useLocalSearchParams<{ id: string }>();
  const game = games.find((game) => game.id === id);

  const goBack = () => {
    // Opened from a direct link there is no previous screen
    if (router.canGoBack()) router.back();
    else router.replace("/");
  };

  return (
    <View style={styles.screen}>
      {/* Extension: the header shows the game's name */}
      <Stack.Screen options={{ title: game ? game.title : "No encontrado" }} />

      <View style={styles.card}>
        <Text style={styles.title}>{game ? game.title : "Juego no encontrado"}</Text>
        {game && (
          <>
            <Text style={styles.label}>
              {game.platform} · {game.year}
            </Text>
            <Text style={styles.label}>{game.description}</Text>
          </>
        )}

        <Pressable
          style={({ pressed }) => [styles.button, pressed && styles.buttonPressed]}
          onPress={goBack}
        >
          <Text style={styles.buttonText}>Volver</Text>
        </Pressable>
      </View>
    </View>
  );
}
