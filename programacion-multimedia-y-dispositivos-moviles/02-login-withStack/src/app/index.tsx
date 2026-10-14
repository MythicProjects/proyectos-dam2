import { Pressable, Text, View } from "react-native";
import { useAuth } from "@/auth";
import { styles } from "@/styles";

export default function HomeScreen() {
  const { setIsLoggedIn } = useAuth();

  return (
    <View style={styles.screen}>
      <View style={styles.card}>
        <Text style={styles.title}>Bienvenido</Text>

        <Pressable
          style={({ pressed }) => [styles.button, pressed && styles.buttonPressed]}
          onPress={() => setIsLoggedIn(false)}
        >
          <Text style={styles.buttonText}>Salir</Text>
        </Pressable>
      </View>
    </View>
  );
}
