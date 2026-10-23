import { Link } from "expo-router";
import { FlatList, Pressable, Text } from "react-native";
import { useAuth } from "@/auth";
import { games } from "@/data/games";
import { styles } from "@/styles";

// List screen: one card per game, each one opens its detail
export default function GameListScreen() {
  const { setIsLoggedIn } = useAuth();

  return (
    <FlatList
      data={games}
      keyExtractor={(game) => game.id}
      contentContainerStyle={styles.list}
      renderItem={({ item: game }) => (
        // asChild: the Pressable card itself becomes the link
        <Link href={{ pathname: "/game/[id]", params: { id: game.id } }} asChild>
          <Pressable style={({ pressed }) => [styles.card, pressed && styles.buttonPressed]}>
            <Text style={styles.title}>{game.title}</Text>
            <Text style={styles.label}>
              {game.platform} · {game.year}
            </Text>
          </Pressable>
        </Link>
      )}
      ListFooterComponent={
        <Pressable
          style={({ pressed }) => [styles.button, pressed && styles.buttonPressed]}
          onPress={() => setIsLoggedIn(false)}
        >
          <Text style={styles.buttonText}>Salir</Text>
        </Pressable>
      }
    />
  );
}
